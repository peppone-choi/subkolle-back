package com.subkore.back.user.dto;

import lombok.Builder;

@Builder
public record UserResponseDto(
    Long id,
    String userUUID,
    String email,
    String nickname,
    String profileImage,
    String role
) {

}
