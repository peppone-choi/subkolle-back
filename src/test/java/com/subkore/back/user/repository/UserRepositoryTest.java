package com.subkore.back.user.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subkore.back.user.entity.User;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@ActiveProfiles("test")
public class UserRepositoryTest {
    @Autowired
    UserRepository userRepository;

    @Test
    void 저장_테스트() {
        // given
        User user = User.builder().build();
        // when
        final User savedUser = userRepository.save(user);
        // then
        assertEquals(savedUser, user);
    }
}
