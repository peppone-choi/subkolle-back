package com.subkore.back.user.dto;

public record LoginRequestDto(
    String email,
    String password
) {

}
