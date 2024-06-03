package com.subkore.back.user.service.Impl;

import com.subkore.back.exception.UserException;
import com.subkore.back.user.dto.CreateUserRequestDto;
import com.subkore.back.user.dto.UserResponseDto;
import com.subkore.back.user.entity.User;
import com.subkore.back.user.mapper.UserMapper;
import com.subkore.back.user.repository.UserRepository;
import com.subkore.back.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper = UserMapper.INSTANCE;

    @Override
    public UserResponseDto createUser(CreateUserRequestDto createUserRequestDto) {
        if (userRepository.existsUserByEmail(createUserRequestDto.email())) {
            throw new UserException("이미 등록된 이메일입니다.");
        }
        User user = userMapper.createUserRequestDtoToUser(createUserRequestDto);
        User savedUser = userRepository.save(user);
        return userMapper.userToUserResponseDto(savedUser);
    }
}
