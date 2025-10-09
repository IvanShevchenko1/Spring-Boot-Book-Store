package org.store.springbootbookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record UpdateQuantityCartItemDto(
        @NotNull
        @Positive
        int quantity
) {
}
