package com.subkore.back.user.dto;

import lombok.Builder;

@Builder
public record JwtDto(
    String accessToken,
    String refreshToken
) {

}
