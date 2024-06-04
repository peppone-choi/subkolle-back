package com.subkore.back.user.service;

import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    UserResponseDto getUserByEmail(String email);

    UserResponseDto getUserByUserUUID(String userUUID);

    UserResponseDto getUserByNickname(String nickname);
}
