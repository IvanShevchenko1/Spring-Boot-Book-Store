package org.store.springbootbookstore.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.store.springbootbookstore.dto.user.UserRegistrationRequestDto;
import org.store.springbootbookstore.dto.user.UserResponseDto;
import org.store.springbootbookstore.exception.RegistrationException;
import org.store.springbootbookstore.mapper.UserMapper;
import org.store.springbootbookstore.model.Role;
import org.store.springbootbookstore.model.User;
import org.store.springbootbookstore.repository.RoleRepository;
import org.store.springbootbookstore.repository.UserRepository;
import org.store.springbootbookstore.service.UserService;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponseDto register(UserRegistrationRequestDto requestDto)
            throws RegistrationException {

        if (userRepository.existsByEmail(requestDto.getEmail())) {
            throw new RegistrationException(
                    "Provided Email is already registered: " + requestDto.getEmail());
        }

        User user = userMapper.toModel(requestDto);

        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        Role userRole = roleRepository.findByRole(Role.RoleName.USER)
                .orElseThrow(() -> new IllegalStateException(
                        "Default role USER is missing. Add roles first."));
        user.getRoles().add(userRole);

        User saved = userRepository.save(user);
        return userMapper.toDto(saved);
    }
}
