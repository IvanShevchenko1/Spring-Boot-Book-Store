package org.store.springbootbookstore.service;

import org.store.springbootbookstore.dto.user.UserRegistrationRequestDto;
import org.store.springbootbookstore.dto.user.UserResponseDto;
import org.store.springbootbookstore.exception.RegistrationException;

public interface UserService {
    UserResponseDto register(UserRegistrationRequestDto requestDto) throws RegistrationException;
}
