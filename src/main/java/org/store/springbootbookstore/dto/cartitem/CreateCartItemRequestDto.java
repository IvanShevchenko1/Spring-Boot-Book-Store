package org.store.springbootbookstore.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record CreateCartItemRequestDto(
        @NotNull
        Long bookId,
        @NotNull
        @Positive
        int quantity
) {
}
