package com.subkore.back.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.subkore.back.exception.UserException;
import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.entity.User;
import com.subkore.back.user.repository.UserRepository;
import com.subkore.back.user.service.Impl.UserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    void 생성_된_유저를_DB에_저장할_수_있다() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
            .email("test@test.com")
            .password("test")
            .nickname("test")
            .profileImage("test")
            .role("test").build();
        User user = User.builder()
                    .userUUID("test")
                    .email("test@test.com")
                    .password("test")
                    .nickname("test")
                    .profileImage("test")
                    .role("test")
            .isDeleted(false)
            .build();
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());
        // when
        UserResponseDto responseDto = userServiceImpl.createUser(createUserRequestDto);
        // then
        assertEquals(responseDto.email(), "test@test.com");
        assertEquals(responseDto.nickname(), "test");
        assertEquals(responseDto.password(), "test");
        assertEquals(responseDto.profileImage(), "test");
        assertEquals(responseDto.role(), "test");
    }
    @Test
    void 만약_등록된_유저라면_예외가_반환된다() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
            .email("test@test.com")
            .password("test")
            .nickname("test")
            .profileImage("test")
            .role("test").build();
        when(userRepository.existsUserByEmail(createUserRequestDto.email())).thenReturn(true);
        // then
        assertThrows(UserException.class, () -> userServiceImpl.createUser(createUserRequestDto));
    }
}
