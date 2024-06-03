package com.subkore.back.user.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.subkore.back.exception.UserException;
import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.entity.User;
import com.subkore.back.user.mapper.UserMapper;
import com.subkore.back.user.repository.UserRepository;
import com.subkore.back.user.service.UserService;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
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
    @MockBean
    private UserRepository userRepository;
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private UserController userController;
    private final UserMapper userMapper = Mappers.getMapper(UserMapper.class).INSTANCE;

    @Test
    @WithMockUser
    void 유저를_등록할_수_있다() throws Exception {
        // given
        CreateUserRequestDto userDto = CreateUserRequestDto.builder().build();
        User user = userMapper.createUserRequestDtoToUser(userDto);
        // when
        UserResponseDto responseDto = userMapper.userToUserResponseDto(user);
        when(userService.createUser(userDto)).thenReturn(responseDto);
        // then
        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(
                    new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(userDto))
                .with(csrf())
            ).andDo(print())
            .andExpect(status().isCreated());
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

        // when
        when(userRepository.existsUserByEmail(userDto.email())).thenReturn(true);
        when(userService.createUser(any(CreateUserRequestDto.class))).thenThrow(new UserException("이미 등록된 이메일입니다."));

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
            .andExpect(result -> assertEquals("이미 등록된 이메일입니다.", result.getResolvedException().getMessage()));
    }

}
