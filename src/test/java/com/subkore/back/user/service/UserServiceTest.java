package com.subkore.back.user.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

import com.subkore.back.exception.UserException;
import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.entity.User;
import com.subkore.back.user.enumerate.Role;
import com.subkore.back.user.repository.UserRepository;
import com.subkore.back.user.service.Impl.UserServiceImpl;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(MockitoExtension.class)
@Transactional
public class UserServiceTest {
    @Mock
    UserRepository userRepository;
    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;
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
            .role(new ArrayList<>(List.of("USER"))).build();
        when(userRepository.save(any(User.class))).then(AdditionalAnswers.returnsFirstArg());
        // when
        UserResponseDto responseDto = userServiceImpl.createUser(createUserRequestDto);
        // then
        assertEquals(responseDto.email(), "test@test.com");
        assertEquals(responseDto.nickname(), "test");
        assertEquals(responseDto.profileImage(), "test");
        assertEquals(responseDto.role(), List.of("USER"));
    }
    @Test
    void 만약_등록된_유저라면_예외가_반환된다() {
        // given
        CreateUserRequestDto createUserRequestDto = CreateUserRequestDto.builder()
            .email("test@test.com")
            .password("test")
            .nickname("test")
            .profileImage("test")
            .role(List.of("USER")).build();
        when(userRepository.existsUserByEmailAndIsDeletedFalse(createUserRequestDto.email())).thenReturn(true);
        // then
        assertThrows(UserException.class, () -> userServiceImpl.createUser(createUserRequestDto));
    }

    @Test
    void 유저_이메일로_유저를_찾을_수_있다() {
        // given
        String email = "test@test.com";
        User user = User.builder()
            .email(email)
            .password("test")
            .nickname("test")
            .profileImage("test")
            .role(List.of(Role.USER))
            .isDeleted(false)
            .build();
        when(userRepository.existsUserByEmailAndIsDeletedFalse(email)).thenReturn(true);
        when(userRepository.findByEmailAndIsDeletedFalse(email)).thenReturn(user);
        // when
        UserResponseDto responseDto = userServiceImpl.getUserByEmail(email);
        // then
        assertEquals(responseDto.email(), email);
        assertEquals(responseDto.nickname(), "test");
        assertEquals(responseDto.profileImage(), "test");
    }

    @Test
    void 만약_등록되지_않거나_삭제된_유저라면_예외가_반환된다() {
        // given
        String email = "test@test.com";
        given(userRepository.existsUserByEmailAndIsDeletedFalse(email)).willReturn(false);
        // then
        assertThrows(UserException.class, () -> userServiceImpl.getUserByEmail(email));
    }

    @Test
    void 유저_UUID로_유저를_찾을_수_있다() {
        // given
        String userUUID = "test";
        User user = User.builder()
            .userUUID(userUUID)
            .email("test@test.com")
            .build();
        given(userRepository.existsUserByUserUUIDAndIsDeletedFalse(userUUID)).willReturn(true);
        given(userRepository.findByUserUUIDAndIsDeletedFalse(userUUID)).willReturn(user);
        // when
        UserResponseDto responseDto = userServiceImpl.getUserByUserUUID(userUUID);
        // then
        assertEquals(responseDto.userUUID(), userUUID);
        assertEquals(responseDto.email(), "test@test.com");
    }

    @Test
    void 만약_등록되지_않거나_삭제된_유저_UUID라면_예외가_반환된다() {
        // given
        String userUUID = "test";
        given(userRepository.existsUserByUserUUIDAndIsDeletedFalse(userUUID)).willReturn(false);
        // then
        assertThrows(UserException.class, () -> userServiceImpl.getUserByUserUUID(userUUID));
    }

    @Test
    void 유저_닉네임으로_유저를_찾을_수_있다() {
        // given
        String nickname = "test";
        String email = "test@test.com";
        User user = User.builder()
            .nickname(nickname)
            .email(email)
            .build();
        given(userRepository.existsUserByNicknameAndIsDeletedFalse(nickname)).willReturn(true);
        given(userRepository.findByNicknameAndIsDeletedFalse(nickname)).willReturn(user);
        // when
        UserResponseDto responseDto = userServiceImpl.getUserByNickname(nickname);
        // then
        assertEquals(responseDto.nickname(), nickname);
        assertEquals(responseDto.email(), email);
    }

    @Test
    void 만약_등록되지_않거나_삭제된_유저_닉네임이라면_예외가_반환된다() {
        // given
        String nickname = "test";
        given(userRepository.existsUserByNicknameAndIsDeletedFalse(nickname)).willReturn(false);
        // then
        assertThrows(UserException.class, () -> userServiceImpl.getUserByNickname(nickname));
    }
}
