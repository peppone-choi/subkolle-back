package com.subkore.back.menu.repository;

import static org.junit.jupiter.api.Assertions.*;

import com.subkore.back.menu.entity.Menu;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@ActiveProfiles("test")
class MenuRepositoryTest {
    @Autowired
    private MenuRepository menuRepository;

    @Test
    void 저장_테스트() {
        // given
        String menuText = "test";
        Menu menu = Menu.builder()
            .icon(menuText)
            .text(menuText)
            .linkTo("/")
            .build();
        // when
        final Menu savedMenu = menuRepository.save(menu);
        // then
        assertEquals(menu, savedMenu);
    }
}