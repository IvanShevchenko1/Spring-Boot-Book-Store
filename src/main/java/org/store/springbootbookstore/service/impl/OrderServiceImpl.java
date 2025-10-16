package org.store.springbootbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.store.springbootbookstore.dto.order.CreateOrderRequestDto;
import org.store.springbootbookstore.dto.order.OrderResponseDto;
import org.store.springbootbookstore.exception.EntityNotFoundException;
import org.store.springbootbookstore.mapper.OrderItemMapper;
import org.store.springbootbookstore.mapper.OrderMapper;
import org.store.springbootbookstore.model.*;
import org.store.springbootbookstore.repository.BookRepository;
import org.store.springbootbookstore.repository.OrderRepository;
import org.store.springbootbookstore.repository.ShoppingCartRepository;
import org.store.springbootbookstore.service.OrderService;
import org.store.springbootbookstore.service.ShoppingCartService;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final BookRepository bookRepository;

    @Override
    public Page<OrderResponseDto> getAllForCurrentUser(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return orderRepository.findAllByUserId(user.getId(), pageable).map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartService.getCartForCurrentUser();
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("No shopping cart items found");
        }

        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);
        order.setShippingAddress(requestDto.shippingAddress());

        Set<OrderItem> orderItems = shoppingCart.getCartItems().stream()
                .map(cartItem -> {
                    OrderItem orderItem = orderItemMapper.fromCartItemToOrderItem(cartItem);

                    Long bookId = cartItem.getBook().getId();
                    orderItem.setBook(new Book(bookId));

                    orderItem.setOrder(order);

                    BigDecimal totalPrice = cartItem.getBook().getPrice()
                            .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
                    orderItem.setPrice(totalPrice);

                    return orderItem;
                })
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        order.setTotal(getTotalPrice(orderItems));

        Order saved = orderRepository.save(order);

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(saved);
    }


    private BigDecimal getTotalPrice(Set<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
