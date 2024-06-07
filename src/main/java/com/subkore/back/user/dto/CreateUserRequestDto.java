package com.subkore.back.user.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record CreateUserRequestDto(
    String email,
    String nickname,
    String password,
    String profileImage,
    List<String> role
) {

}
