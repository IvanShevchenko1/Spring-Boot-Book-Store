package org.store.springbootbookstore.service;

import org.store.springbootbookstore.dto.user.UserRegistrationRequestDto;
import org.store.springbootbookstore.dto.user.UserResponseDto;
import org.store.springbootbookstore.exception.RegistrationException;
import org.store.springbootbookstore.model.ShoppingCart;
import org.store.springbootbookstore.model.User;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;

    ShoppingCart getShoppingCartOfCurrentUser();

    void createCartForUser(User user);
}
