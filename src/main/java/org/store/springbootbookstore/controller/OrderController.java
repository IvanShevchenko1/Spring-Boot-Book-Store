package org.store.springbootbookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.store.springbootbookstore.dto.order.CreateOrderRequestDto;
import org.store.springbootbookstore.dto.order.OrderResponseDto;
import org.store.springbootbookstore.dto.order.PatchOrderRequestDto;
import org.store.springbootbookstore.dto.orderitem.OrderItemResponseDto;
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
    @Operation(summary = "Create a new Order",
            description = "Create Order from cart items in the shopping cart")
    public OrderResponseDto createOrder(
            @RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.createOrder(requestDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @PatchMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public OrderResponseDto updateOrder(
            @RequestBody @Valid PatchOrderRequestDto requestDto,
            @PathVariable Long id) {
        return orderService.patchOrder(id, requestDto);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/{id}/items")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get all items in the Order",
            description = "Retrieve a paginated list of all items in the Order by id")
    public Page<OrderItemResponseDto> getAllOrderItemsByOrderId(
            Pageable pageable, @PathVariable Long id) {
        return orderService.getAllOrderItemsByOrderId(id, pageable);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get an item in the Order",
            description = "Get an item by its id in the Order")
    public OrderItemResponseDto getAnOrderItemByItsId(
            @PathVariable Long orderId,
            @PathVariable Long itemId) {
        return orderService.getAnOrderItemByItsId(orderId, itemId);
    }
}
