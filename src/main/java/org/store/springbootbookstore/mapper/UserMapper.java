package org.store.springbootbookstore.mapper;

import org.mapstruct.Mapper;
import org.store.springbootbookstore.config.MapperConfig;
import org.store.springbootbookstore.dto.user.UserRegistrationRequestDto;
import org.store.springbootbookstore.dto.user.UserResponseDto;
import org.store.springbootbookstore.model.User;

@Mapper(config = MapperConfig.class)
public interface UserMapper {
    UserResponseDto toDto(User user);

    User toModel(UserRegistrationRequestDto requestDto);
}
