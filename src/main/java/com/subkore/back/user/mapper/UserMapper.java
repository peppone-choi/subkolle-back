package com.subkore.back.user.mapper;

import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {
    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    UserResponseDto userToUserResponseDto(User user);
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userUUID", ignore = true)
    @Mapping(target = "isDeleted", ignore = true)
    User createUserRequestDtoToUser(CreateUserRequestDto createUserRequestDto);
}
