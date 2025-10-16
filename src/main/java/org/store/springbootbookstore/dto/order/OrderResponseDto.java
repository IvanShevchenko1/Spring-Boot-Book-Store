package org.store.springbootbookstore.dto.order;

import org.store.springbootbookstore.dto.orderItem.OrderItemResponseDto;
import org.store.springbootbookstore.model.Order;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

public record OrderResponseDto(
        Long id,
        int userId,
        Order.Status status,
        BigDecimal total,
        LocalDateTime orderDate,
        Set<OrderItemResponseDto> orderItems
) {
}
