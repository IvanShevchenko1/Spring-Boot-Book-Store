package org.store.springbootbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.store.springbootbookstore.dto.order.CreateOrderRequestDto;
import org.store.springbootbookstore.dto.order.OrderResponseDto;
import org.store.springbootbookstore.service.OrderService;

@Tag(name = "Orders", description = "EndPoint for managing Orders and OrderItems")
@RequiredArgsConstructor
@RestController
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all Orders",
            description = "Retrieve a paginated list of all Orders for current User")
    public Page<OrderResponseDto> getAllForCurrentUser(Pageable pageable) {
        return orderService.getAllForCurrentUser(pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new book",
            description = "Add a new book to the store")
    public OrderResponseDto createOrder(@RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.createOrder(requestDto);
    }
}
