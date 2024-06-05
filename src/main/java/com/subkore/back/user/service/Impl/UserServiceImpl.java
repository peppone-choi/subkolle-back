package com.subkore.back.user.service.Impl;

import com.subkore.back.exception.UserException;
import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.entity.User;
import com.subkore.back.user.mapper.UserMapper;
import com.subkore.back.user.repository.UserRepository;
import com.subkore.back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Override
    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        if (userRepository.existsUserByEmailAndIsDeletedFalse(createUserRequestDto.email())) {
            throw new UserException("이미 등록된 이메일입니다.");
        }
        User user = userMapper.createUserRequestDtoToUser(createUserRequestDto);
        user.passwordEncode(bCryptPasswordEncoder.encode(createUserRequestDto.password()));
        User savedUser = userRepository.save(user);
        return userMapper.userToUserResponseDto(savedUser);
    }

    @Override
    public UserResponseDto getUserByEmail(String email) {
        if (!userRepository.existsUserByEmailAndIsDeletedFalse(email)) {
            throw new UserException("등록되지 않은 이메일입니다.");
        }
        User user = userRepository.findByEmailAndIsDeletedFalse(email);
        return userMapper.userToUserResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByUserUUID(String userUUID) {
        if (!userRepository.existsUserByUserUUIDAndIsDeletedFalse(userUUID)) {
            throw new UserException("등록되지 않은 유저입니다.");
        }
        User user = userRepository.findByUserUUIDAndIsDeletedFalse(userUUID);
        return userMapper.userToUserResponseDto(user);
    }

    @Override
    public UserResponseDto getUserByNickname(String nickname) {
        if (!userRepository.existsUserByNicknameAndIsDeletedFalse(nickname)) {
            throw new UserException("등록되지 않은 닉네임입니다.");
        }
        User user = userRepository.findByNicknameAndIsDeletedFalse(nickname);
        return userMapper.userToUserResponseDto(user);
    }
}
