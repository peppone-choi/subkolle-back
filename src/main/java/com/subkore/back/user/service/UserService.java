package com.subkore.back.user.service;

import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.LoginRequestDto;
import com.subkore.back.user.dto.LoginResponseDto;
import com.subkore.back.user.dto.UserResponseDto;

public interface UserService {

    UserResponseDto createUser(CreateUserRequestDto createUserRequestDto);

    UserResponseDto getUserByEmail(String email);

    UserResponseDto getUserByUserUUID(String userUUID);

    UserResponseDto getUserByNickname(String nickname);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    void logout(String token);
}
