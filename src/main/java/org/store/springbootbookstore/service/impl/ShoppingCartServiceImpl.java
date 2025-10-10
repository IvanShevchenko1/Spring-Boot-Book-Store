package org.store.springbootbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.store.springbootbookstore.dto.cartitem.CreateCartItemRequestDto;
import org.store.springbootbookstore.dto.cartitem.UpdateQuantityCartItemDto;
import org.store.springbootbookstore.dto.shoppingcart.ShoppingCartResponserDto;
import org.store.springbootbookstore.exception.EntityNotFoundException;
import org.store.springbootbookstore.mapper.CartItemMapper;
import org.store.springbootbookstore.mapper.ShoppingCartMapper;
import org.store.springbootbookstore.model.CartItem;
import org.store.springbootbookstore.repository.CartItemRepository;
import org.store.springbootbookstore.service.ShoppingCartService;
import org.store.springbootbookstore.service.UserService;

@Service
@Transactional
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserService userService;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public ShoppingCartResponserDto getShoppingCart() {
        return shoppingCartMapper.toDto(
                userService.getShoppingCartOfCurrentUser());
    }

    @Override
    public ShoppingCartResponserDto createNewItem(CreateCartItemRequestDto requestDto) {
        CartItem cartItem = cartItemMapper.toModel(requestDto);
        cartItem.setShoppingCart(
                userService.getShoppingCartOfCurrentUser());
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(
                userService.getShoppingCartOfCurrentUser());
    }

    @Override
    public ShoppingCartResponserDto updateById(Long id, UpdateQuantityCartItemDto requestDto) {
        CartItem cartItem = cartItemRepository.findById(id)
                .orElseThrow(
                        () -> new EntityNotFoundException("Can't find cart item by id: " + id)
                );
        cartItemMapper.updateEntity(requestDto, cartItem);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(
                userService.getShoppingCartOfCurrentUser());
    }

    @Override
    public void deleteById(Long id) {
        cartItemRepository.deleteById(id);
    }
}
