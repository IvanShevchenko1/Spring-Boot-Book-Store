package org.store.springbootbookstore.service;

import org.store.springbootbookstore.dto.cartitem.CartItemResponseDto;
import org.store.springbootbookstore.dto.cartitem.CreateCartItemRequestDto;
import org.store.springbootbookstore.dto.cartitem.UpdateQuantityCartItemDto;

public interface CartItemService {
    CartItemResponseDto createNewItem(CreateCartItemRequestDto requestDto);

    CartItemResponseDto updateById(Long id, UpdateQuantityCartItemDto requestDto);

    void deleteById(Long id);
}
