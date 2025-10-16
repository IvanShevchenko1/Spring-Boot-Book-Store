package org.store.springbootbookstore.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.store.springbootbookstore.dto.order.CreateOrderRequestDto;
import org.store.springbootbookstore.dto.order.OrderResponseDto;
import org.store.springbootbookstore.dto.order.PatchOrderRequestDto;
import org.store.springbootbookstore.dto.orderitem.OrderItemResponseDto;

public interface OrderService {
    Page<OrderResponseDto> getAllForCurrentUser(Pageable pageable);

    OrderResponseDto createOrder(CreateOrderRequestDto requestDto);

    OrderResponseDto patchOrder(Long id, PatchOrderRequestDto requestDto);

    Page<OrderItemResponseDto> getAllOrderItemsByOrderId(Long id, Pageable pageable);

    OrderItemResponseDto getAnOrderItemByItsId(Long orderId, Long itemId);
}
