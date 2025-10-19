package org.store.springbootbookstore.service.impl;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.store.springbootbookstore.dto.order.CreateOrderRequestDto;
import org.store.springbootbookstore.dto.order.OrderResponseDto;
import org.store.springbootbookstore.dto.order.PatchOrderRequestDto;
import org.store.springbootbookstore.dto.orderitem.OrderItemResponseDto;
import org.store.springbootbookstore.exception.EntityNotFoundException;
import org.store.springbootbookstore.mapper.OrderItemMapper;
import org.store.springbootbookstore.mapper.OrderMapper;
import org.store.springbootbookstore.model.Order;
import org.store.springbootbookstore.model.OrderItem;
import org.store.springbootbookstore.model.ShoppingCart;
import org.store.springbootbookstore.model.User;
import org.store.springbootbookstore.repository.OrderItemRepository;
import org.store.springbootbookstore.repository.OrderRepository;
import org.store.springbootbookstore.repository.ShoppingCartRepository;
import org.store.springbootbookstore.service.OrderService;
import org.store.springbootbookstore.service.ShoppingCartService;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final ShoppingCartService shoppingCartService;
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderItemRepository orderItemRepository;

    @Override
    public Page<OrderResponseDto> getAllForCurrentUser(Pageable pageable) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        User user = (User) auth.getPrincipal();
        return orderRepository.findAllByUserId(user.getId(), pageable)
                .map(orderMapper::toDto);
    }

    @Override
    @Transactional
    public OrderResponseDto createOrder(CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartService.getCartForCurrentUser();
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new EntityNotFoundException("No shopping cart items found");
        }

        Order order = orderMapper.toModel(requestDto);

        order.setUser(shoppingCart.getUser());
        order.setOrderDate(LocalDateTime.now());
        order.setStatus(Order.Status.PENDING);

        Set<OrderItem> orderItems = shoppingCart.getCartItems()
                .stream()
                .map(orderItemMapper::fromCartItemToOrderItem)
                .peek(orderItem -> orderItem.setOrder(order))
                .collect(Collectors.toSet());

        order.setOrderItems(orderItems);
        order.setTotal(getTotalPrice(orderItems));

        shoppingCart.getCartItems().clear();
        shoppingCartRepository.save(shoppingCart);

        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    @Transactional
    public OrderResponseDto patchOrder(Long id, PatchOrderRequestDto requestDto) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Can't find order by id: " + id));
        orderMapper.updateEntity(requestDto, order);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public Page<OrderItemResponseDto> getAllOrderItemsByOrderId(Long id, Pageable pageable) {
        return orderItemRepository.findByOrderId(id, pageable)
                .map(orderItemMapper::toDto);
    }

    @Override
    public OrderItemResponseDto getAnOrderItemByItsId(Long orderId, Long itemId) {
        return orderItemMapper.toDto(
                orderItemRepository.findByOrderIdAndId(orderId, itemId));
    }

    private BigDecimal getTotalPrice(Set<OrderItem> items) {
        return items.stream()
                .map(OrderItem::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
