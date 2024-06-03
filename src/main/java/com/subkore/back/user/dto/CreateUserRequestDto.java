package com.subkore.back.user.dto;

import lombok.Builder;

@Builder
public record CreateUserRequestDto(
    String email,
    String nickname,
    String password,
    String profileImage,
    String role
) {

}
