package org.store.springbootbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.springbootbookstore.dto.shoppingcart.ShoppingCartResponserDto;
import org.store.springbootbookstore.mapper.ShoppingCartMapper;
import org.store.springbootbookstore.service.ShoppingCartService;
import org.store.springbootbookstore.service.UserService;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartMapper shoppingCartMapper;
    private final UserService userService;

    @Override
    public ShoppingCartResponserDto getShoppingCart() {
        return shoppingCartMapper.toDto(
                userService.getShoppingCartOfCurrentUser());
    }
}
