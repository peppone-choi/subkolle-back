package com.subkore.back.menu.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.subkore.back.exception.MenuException;
import com.subkore.back.menu.dto.CreateMenuRequestDto;
import com.subkore.back.menu.dto.MenuResponseDto;
import com.subkore.back.menu.dto.UpdateMenuRequestDto;
import com.subkore.back.menu.entity.Menu;
import com.subkore.back.menu.mapper.MenuMapper;
import com.subkore.back.menu.repository.MenuRepository;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuServiceImplTest {

    @Mock
    MenuRepository repository;
    @InjectMocks
    MenuServiceImpl menuService;
    MenuMapper menuMapper = Mappers.getMapper(MenuMapper.class).INSTANCE;

    @Test
    void 메뉴의_리스트가_반환된다() {
        // given
        List<Menu> menuList = List.of(Menu.builder()
                .id(1L)
                .menuOrder(0)
                .icon("test")
                .text("test")
                .linkTo("test")
                .isDeleted(false)
                .build(),
            Menu.builder()
                .id(2L)
                .menuOrder(1)
                .icon("test2")
                .text("test2")
                .linkTo("test2")
                .isDeleted(false)
                .build());
        when(repository.findAllByIsDeletedFalseOrderByMenuOrder()).thenReturn(menuList);
        // when
        List<MenuResponseDto> responseList = menuService.getMenuList();
        // then
        assertEquals(responseList.size(), 2);
    }

    @Test
    void 메뉴가_없을_때엔_리스트가_반환되지_않는다() {
        // given
        when(repository.findAllByIsDeletedFalseOrderByMenuOrder()).thenReturn(List.of());
        // when
        MenuException e = assertThrows(MenuException.class, (() -> menuService.getMenuList()));
        // then
        assertEquals(e.getMessage(), "메뉴가 없습니다.");
    }

    @Test
    void 메뉴가_만들어_진다() {
        // given
        CreateMenuRequestDto createMenuRequestDto = CreateMenuRequestDto.builder()
            .text("test")
            .linkTo("/")
            .icon("test")
            .build();
        when(repository.save(any(Menu.class))).then(AdditionalAnswers.returnsFirstArg());
        // when
        MenuResponseDto response = menuService.createMenu(createMenuRequestDto);
        // then
        assertEquals(response.icon(), "test");
        assertEquals(response.text(), "test");
        assertEquals(response.linkTo(), "/");
    }

    @Test
    void 메뉴의_입력_항목이_null_일_경우_예외가_던져진다() {
        // given
        CreateMenuRequestDto createMenuRequestDto = CreateMenuRequestDto.builder()
            .build();
        // when
        // then
        assertThrows(MenuException.class, () -> menuService.createMenu(createMenuRequestDto));
    }

    @Test
    void 메뉴의_입력_항목이_공백_일_경우_예외가_던져진다() {
        // given
        CreateMenuRequestDto createMenuRequestDto = CreateMenuRequestDto.builder()
            .text("")
            .linkTo("")
            .icon("")
            .build();
        // when
        // then
        assertThrows(MenuException.class, () -> menuService.createMenu(createMenuRequestDto));
    }

    @Test
    void 메뉴가_수정된다() {
        // given
        Menu menu = Menu.builder()
            .id(1L)
            .menuOrder(0)
            .icon("test")
            .text("test")
            .linkTo("test")
            .isDeleted(false)
            .build();
        when(repository.existsById(1L)).thenReturn(true);
        when(repository.findById(1L)).thenReturn(java.util.Optional.of(menu));
        UpdateMenuRequestDto updateMenuRequestDto = UpdateMenuRequestDto.builder()
            .text("test2")
            .linkTo("test2")
            .icon("test2")
            .build();
        when(repository.save(any(Menu.class))).then(AdditionalAnswers.returnsFirstArg());
        // when
        MenuResponseDto response = menuService.updateMenu(1L, updateMenuRequestDto);
        // then
        assertEquals(response.icon(), "test2");
        assertEquals(response.text(), "test2");
        assertEquals(response.linkTo(), "test2");
    }

    @Test
    void 메뉴가_없을_때_수정_시_예외가_던져진다() {
        // given
        when(repository.existsById(1L)).thenReturn(false);
        // when
        // then
        assertThrows(MenuException.class,
            () -> menuService.updateMenu(1L, UpdateMenuRequestDto.builder().build()));
    }
}