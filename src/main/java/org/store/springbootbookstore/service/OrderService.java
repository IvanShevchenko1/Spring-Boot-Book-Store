package org.store.springbootbookstore.service;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.store.springbootbookstore.dto.order.CreateOrderRequestDto;
import org.store.springbootbookstore.dto.order.OrderResponseDto;

public interface OrderService {
    Page<OrderResponseDto> getAllForCurrentUser(Pageable pageable);

    OrderResponseDto createOrder(@Valid CreateOrderRequestDto requestDto);
}
