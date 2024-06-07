package com.subkore.back.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.subkore.back.exception.UserException;
import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.mapper.UserMapper;
import com.subkore.back.user.service.UserService;
import java.util.List;
import java.util.Objects;
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
    private final UserMapper userMapper = UserMapper.INSTANCE;
    @Test
    @WithMockUser
    void 유저를_등록할_수_있다() throws Exception {
        // given
        CreateUserRequestDto userDto = CreateUserRequestDto.builder()
            .email("test@test.com")
            .nickname("test")
            .password("test")
            .profileImage("test")
            .role(List.of("USER"))
            .build();
        given(userService.createUser(any()))
            .willReturn(userMapper.userToUserResponseDto(userMapper.createUserRequestDtoToUser(userDto)));
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
            .andExpect(jsonPath("$.role").value("USER"));

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
            .role(List.of("USER"))
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

    @Test
    @WithMockUser
    void 이메일로_유저를_찾을_수_있다() throws Exception {
        // given
        String email = "tset@test.com";
        given(userService.getUserByEmail(email))
            .willReturn(UserResponseDto.builder()
                .email(email)
                .nickname("test")
                .profileImage("test")
                .role(List.of("USER"))
                .build());
        // when
        // then
        mockMvc.perform(get("/api/v1/users/email")
                .param("email", email)
                .with(csrf())
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.email").value(email))
            .andExpect(jsonPath("$.nickname").value("test"))
            .andExpect(jsonPath("$.profileImage").value("test"))
            .andExpect(jsonPath("$.role").value("USER"));
    }

    @Test
    @WithMockUser
    void 만약_등록되지_않거나_삭제된_유저의_이메일이라면_예외가_반환된다() throws Exception {
        // given
        String email = "tset@test.com";
        given(userService.getUserByEmail(email)).willThrow(new UserException("등록되지 않은 이메일입니다."));
        // when
        // then
        mockMvc.perform(get("/api/v1/users/email")
                .param("email", email)
                .with(csrf())
            ).andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertInstanceOf(UserException.class,
                result.getResolvedException()))
            .andExpect(result -> assertEquals("등록되지 않은 이메일입니다.",
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }

    @Test
    @WithMockUser
    void UUID로_유저를_찾을_수_있다() throws Exception {
        // given
        String userUUID = "test";
        given(userService.getUserByUserUUID(userUUID))
            .willReturn(UserResponseDto.builder()
                .userUUID(userUUID)
                .email("test@test.com")
                .build());
        // when
        // then
        mockMvc.perform(get("/api/v1/users/UUID")
                .param("uuid", userUUID)
                .with(csrf())
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.userUUID").value(userUUID))
            .andExpect(jsonPath("$.email").value("test@test.com"));
    }

    @Test
    @WithMockUser
    void 만약_등록되지_않거나_삭제된_UUID라면_예외가_반환된다() throws Exception {
        // given
        String userUUID = "test";
        given(userService.getUserByUserUUID(userUUID)).willThrow(new UserException("등록되지 않은 UUID입니다."));
        // when
        // then
        mockMvc.perform(get("/api/v1/users/UUID")
                .param("uuid", userUUID)
                .with(csrf())
            ).andDo(print())
            .andExpect(status().is4xxClientError())
            .andExpect(result -> assertInstanceOf(UserException.class,
                result.getResolvedException()))
            .andExpect(result -> assertEquals("등록되지 않은 UUID입니다.",
                Objects.requireNonNull(result.getResolvedException()).getMessage()));
    }
    @Test
    @WithMockUser
    void 닉네임으로_유저를_찾을_수_있다() throws Exception {
        // given
        String nickname = "test";
        given(userService.getUserByNickname(nickname))
            .willReturn(UserResponseDto.builder()
                .nickname(nickname)
                .email("test@test.com")
                .build());
        // when
        // then
        mockMvc.perform(get("/api/v1/users/nickname")
                .param("nickname", nickname)
                .with(csrf())
            ).andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.nickname").value(nickname))
            .andExpect(jsonPath("$.email").value("test@test.com"));
    }
}
