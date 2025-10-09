package org.store.springbootbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.springbootbookstore.dto.cartitem.CartItemResponseDto;
import org.store.springbootbookstore.dto.cartitem.CreateCartItemRequestDto;
import org.store.springbootbookstore.dto.cartitem.UpdateQuantityCartItemDto;
import org.store.springbootbookstore.exception.EntityNotFoundException;
import org.store.springbootbookstore.mapper.CartItemMapper;
import org.store.springbootbookstore.model.CartItem;
import org.store.springbootbookstore.repository.CartItemRepository;
import org.store.springbootbookstore.service.CartItemService;
import org.store.springbootbookstore.service.UserService;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final UserService userService;
    private final CartItemRepository cartItemRepository;
    private final CartItemMapper cartItemMapper;

    @Override
    public CartItemResponseDto createNewItem(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(
                userService.getShoppingCartOfCurrentUser());
        return cartItemMapper.toDto(
                cartItemRepository.save(cartItem));
    }

    @Override
    public CartItemResponseDto updateById(Long id, UpdateQuantityCartItemDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item by id: " + id)
                );
        cartItemMapper.updateEntity(requestDto, cartItem);
        return cartItemMapper.toDto(cartItemRepository.save(cartItem));
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
