package org.store.springbootbookstore.dto.orderitem;

public record OrderItemResponseDto(
        Long id,
        int bookId,
        int quantity
) {
}
