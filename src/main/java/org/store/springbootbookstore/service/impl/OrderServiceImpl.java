package org.store.springbootbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.store.springbootbookstore.dto.order.CreateOrderRequestDto;
import org.store.springbootbookstore.dto.order.OrderResponseDto;
import org.store.springbootbookstore.exception.EmptyShopCartException;
import org.store.springbootbookstore.mapper.OrderItemMapper;
import org.store.springbootbookstore.mapper.OrderMapper;
import org.store.springbootbookstore.model.Order;
import org.store.springbootbookstore.model.OrderItem;
import org.store.springbootbookstore.model.ShoppingCart;
import org.store.springbootbookstore.model.User;
import org.store.springbootbookstore.repository.OrderRepository;
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

    @Override
    public Page<OrderResponseDto> getAllForCurrentUser(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return orderRepository.findAllByUserId(user.getId(), pageable).map(orderMapper::toDto);
    }

    @Override
    public OrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
        Order order = new Order();
        ShoppingCart shoppingCart = shoppingCartService.getCartForCurrentUser();
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EmptyShopCartException("Shopping cart is empty");
        }
        order.setOrderItems((shoppingCart.getCartItems()
                .stream()
                .map(orderItemMapper::fromCartItemToOrderItem)
                .peek(orderItem -> orderItem.setOrder(order))
                .collect(Collectors.toSet())));
        order.setShippingAddress(requestDto.shippingAddress());
        order.setOrderDate(LocalDateTime.now());
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        order.setUser((User) auth.getPrincipal());
        order.setStatus(Order.Status.PENDING);
        order.setTotal(getTotalPrice(order.getOrderItems()));
        shoppingCartService.emptyCartForCurrentUser();
        return orderMapper.toDto(orderRepository.save(order));
    }


    private BigDecimal getTotalPrice(Set<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
