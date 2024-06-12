package com.subkore.back.user.controller;

import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.JwtDto;
import com.subkore.back.user.dto.LoginRequestDto;
import com.subkore.back.user.dto.LoginResponseDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping("/api/v1/users")
    public ResponseEntity<UserResponseDto> createUser(@RequestBody CreateUserRequestDto createUserRequestDto) {
        UserResponseDto user = userService.createUser(createUserRequestDto);
        return ResponseEntity.created(URI.create("/users/" + user.id())).body(user);
    }

    @GetMapping("/api/v1/users/email")
    public ResponseEntity<UserResponseDto> getUserByEmail(@RequestParam(name = "email") String email) {
        UserResponseDto user = userService.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/v1/users/UUID")
    public ResponseEntity<UserResponseDto> getUserByUserUUID(@RequestParam(name = "uuid") String userUUID) {
        UserResponseDto user = userService.getUserByUserUUID(userUUID);
        return ResponseEntity.ok(user);
    }

    @GetMapping("/api/v1/users/nickname")
    public ResponseEntity<UserResponseDto> getUserByNickname(@RequestParam(name = "nickname") String nickname) {
        UserResponseDto user = userService.getUserByNickname(nickname);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/api/v1/users/login")
    public ResponseEntity<LoginResponseDto> login(@RequestBody LoginRequestDto loginRequestDto) {
        LoginResponseDto dto = userService.login(loginRequestDto);
        return ResponseEntity.created(URI.create("/api/v1/users/login/" + dto.id()))
            .body(dto);
    }

    @GetMapping("/api/v1/users/logout")
    public ResponseEntity<Void> logout(@RequestHeader("Authorization") String token,
        HttpServletResponse response) {
        userService.logout(token);
        Cookie tokenCookie = new Cookie("Authorization", null);
        tokenCookie.setMaxAge(0);
        response.addCookie(tokenCookie);
        return ResponseEntity.noContent().build();
    }
}
