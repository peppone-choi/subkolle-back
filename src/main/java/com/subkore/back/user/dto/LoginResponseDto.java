package com.subkore.back.user.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record LoginResponseDto(
    Long id,
    String userUUID,
    String email,
    String nickname,
    String profileImage,
    List<String> role,
    String accessToken,
    String refreshToken
) {

}
