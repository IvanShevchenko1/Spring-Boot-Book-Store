package org.store.springbootbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.store.springbootbookstore.dto.user.UserRegistrationRequestDto;
import org.store.springbootbookstore.dto.user.UserResponseDto;
import org.store.springbootbookstore.exception.RegistrationException;
import org.store.springbootbookstore.mapper.UserMapper;
import org.store.springbootbookstore.repository.UserRepository;
import org.store.springbootbookstore.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Override
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {
        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException("Provided Email is already registered: "
                    + requestDto.getEmail());
        }
        return userMapper.toDto(userRepository
                .save(userMapper
                        .toModel(requestDto)));
    }
}
