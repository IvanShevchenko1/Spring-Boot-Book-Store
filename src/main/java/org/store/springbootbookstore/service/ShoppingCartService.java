package org.store.springbootbookstore.service;

import org.store.springbootbookstore.dto.cartitem.CreateCartItemRequestDto;
import org.store.springbootbookstore.dto.cartitem.UpdateQuantityCartItemDto;
import org.store.springbootbookstore.dto.shoppingcart.ShoppingCartResponserDto;
import org.store.springbootbookstore.model.ShoppingCart;
import org.store.springbootbookstore.model.User;

public interface ShoppingCartService {
    ShoppingCartResponserDto getShoppingCart();

    ShoppingCartResponserDto createNewItem(CreateCartItemRequestDto requestDto);

    ShoppingCartResponserDto updateById(Long id, UpdateQuantityCartItemDto requestDto);

    void deleteById(Long id);

    void createCartForUser(User user);

    ShoppingCart getCartForCurrentUser();
}
