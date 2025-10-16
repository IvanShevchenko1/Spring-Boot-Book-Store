package org.store.springbootbookstore.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import org.store.springbootbookstore.dto.orderitem.OrderItemResponseDto;
import org.store.springbootbookstore.model.Order;

public record OrderResponseDto(
        Long id,
        int userId,
        Order.Status status,
        BigDecimal total,
        LocalDateTime orderDate,
        Set<OrderItemResponseDto> orderItems
) {
}
