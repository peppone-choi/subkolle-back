package com.subkore.back.menu.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.subkore.back.exception.MenuException;
import com.subkore.back.menu.dto.CreateMenuRequestDto;
import com.subkore.back.menu.dto.MenuResponseDto;
import com.subkore.back.menu.entity.Menu;
import com.subkore.back.menu.mapper.MenuMapper;
import com.subkore.back.menu.service.MenuService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = MenuController.class)
@AutoConfigureWebMvc
class MenuControllerTest {

    @MockBean
    private MenuService menuService;
    @Autowired
    private MockMvc mockMvc;
    private MenuMapper menuMapper = Mappers.getMapper(MenuMapper.class).INSTANCE;
    @Test
    @WithMockUser(username = "테스트")
    void 메뉴를_등록_할_수_있다() throws Exception {
        CreateMenuRequestDto createMenuRequestDto = CreateMenuRequestDto.builder()
                .icon("test")
                .text("test")
                .linkTo("/").build();
        MenuResponseDto menuResponseDto = menuMapper.menuToMenuResponseDto(
                menuMapper.createMenuRequestDtoToMenu(createMenuRequestDto));
        when(menuService.createMenu(createMenuRequestDto)).thenReturn(menuResponseDto);
        mockMvc.perform(post("/api/v1/menus")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(createMenuRequestDto)))
                .andDo(print())
                .andExpect(status().isCreated());

    }
    @Test
    @WithMockUser(username = "테스트")
    void 메뉴가_있을_경우_메뉴_리스트를_반환한다() throws Exception {
        List<Menu> menuList = List.of(Menu.builder()
                        .id(0L)
                        .menuOrder(0)
                        .icon("test")
                        .text("test")
                        .linkTo("test")
                        .build(),
                Menu.builder()
                        .id(1L)
                        .menuOrder(1)
                        .icon("test2")
                        .text("test2")
                        .linkTo("test2")
                        .build());

        List<MenuResponseDto> dtoList = menuList.stream()
                .map(menu -> menuMapper.menuToMenuResponseDto(menu)).toList();
        when(menuService.getMenuList()).thenReturn(dtoList);
        mockMvc.perform(get("/api/v1/menus")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print()).andExpect(status().isOk());
    }
    @Test
    @WithMockUser
    void 메뉴가_없을_경우_예외가_발생한다() throws Exception {
        when(menuService.getMenuList()).thenThrow(new MenuException("메뉴가 없습니다."));
        mockMvc.perform(get("/api/v1/menus")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 메뉴_등록_시_항목이_없을_경우_예외가_던져진다() throws Exception {
        CreateMenuRequestDto createMenuRequestDto = CreateMenuRequestDto.builder().build();
        mockMvc.perform(post("/api/v1/menus")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().registerModule(new JavaTimeModule()).writeValueAsString(createMenuRequestDto)))
                .andDo(print())
                .andExpect(status().is4xxClientError());
    }
}