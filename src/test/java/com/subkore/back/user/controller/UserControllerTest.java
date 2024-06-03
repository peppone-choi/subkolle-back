package com.subkore.back.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.subkore.back.exception.UserException;
import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(UserController.class)
@AutoConfigureWebMvc
public class UserControllerTest {

    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;

    @Test
    @WithMockUser
    void 유저를_등록할_수_있다() throws Exception {
        // given
        CreateUserRequestDto userDto = CreateUserRequestDto.builder()
            .email("test@test.com")
            .nickname("test")
            .password("test")
            .profileImage("test")
            .role("test")
            .build();
        given(userService.createUser(any()))
            .willReturn(UserResponseDto.builder()
                .email(userDto.email())
                .nickname(userDto.nickname())
                .profileImage(userDto.profileImage())
                .role(userDto.role())
                .build());
        // when
        // then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(userDto))
                .with(csrf())
            ).andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.email").value(userDto.email()))
            .andExpect(jsonPath("$.nickname").value(userDto.nickname()))
            .andExpect(jsonPath("$.profileImage").value(userDto.profileImage()))
            .andExpect(jsonPath("$.role").value(userDto.role()));

    }

    @Test
    @WithMockUser
    void 만약_등록된_유저라면_예외가_반환된다() throws Exception {
        // given
        CreateUserRequestDto userDto = CreateUserRequestDto.builder()
            .email("test@test.com")
            .password("test")
            .nickname("test")
            .profileImage("test")
            .role("test")
            .build();
        given(userService.createUser(any())).willThrow(new UserException("이미 등록된 이메일입니다."));
        // when

        // then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(userDto))
                .with(csrf())
            ).andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertInstanceOf(UserException.class,
                result.getResolvedException()))
            .andExpect(result -> assertEquals("이미 등록된 이메일입니다.",
                result.getResolvedException().getMessage()));
    }

}
