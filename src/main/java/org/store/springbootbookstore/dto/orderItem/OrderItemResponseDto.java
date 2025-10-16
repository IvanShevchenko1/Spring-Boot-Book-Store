package org.store.springbootbookstore.dto.orderItem;

public record OrderItemResponseDto(
        Long id,
        int bookId,
        int quantity
) {
}
