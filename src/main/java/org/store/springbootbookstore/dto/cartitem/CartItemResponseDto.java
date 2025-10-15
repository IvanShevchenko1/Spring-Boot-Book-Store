package org.store.springbootbookstore.dto.cartitem;

public record CartItemResponseDto(
        Long id,
        Long bookId,
        int quantity
) {
}
