package org.store.springbootbookstore.dto.cartitem;

import jakarta.validation.constraints.Positive;

public record UpdateQuantityCartItemDto(
        @Positive
        int quantity
) {
}
