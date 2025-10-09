package org.store.springbootbookstore.dto.shoppingcart;

import java.util.Set;
import org.store.springbootbookstore.dto.cartitem.CartItemResponseDto;

public record ShoppingCartResponserDto(
        Long id,
        Set<CartItemResponseDto> cartItems
) {
}
