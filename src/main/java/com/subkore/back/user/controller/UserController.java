package com.subkore.back.user.controller;

import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.service.UserService;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/v1/users")
    public ResponseEntity<UserResponseDto> createUser(CreateUserRequestDto createUserRequestDto) {
        UserResponseDto user = userService.createUser(createUserRequestDto);
        return ResponseEntity.created(URI.create("/users/" + user.id())).body(user);
    }
}
