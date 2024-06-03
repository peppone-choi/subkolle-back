package com.subkore.back.menu.controller;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.subkore.back.exception.MenuException;
import com.subkore.back.menu.dto.CreateMenuRequestDto;
import com.subkore.back.menu.dto.MenuResponseDto;
import com.subkore.back.menu.entity.Menu;
import com.subkore.back.menu.mapper.MenuMapper;
import com.subkore.back.menu.repository.MenuRepository;
import com.subkore.back.menu.service.MenuService;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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


    @Test
    @WithMockUser(username = "테스트")
    void 메뉴를_등록_할_수_있다() throws Exception {
        CreateMenuRequestDto createMenuRequestDto = CreateMenuRequestDto.builder()
            .iconType("test")
            .icon("test")
            .text("test")
            .linkTo("/").build();
        given(menuService.createMenu(createMenuRequestDto)).willReturn(MenuResponseDto.builder()
            .iconType(createMenuRequestDto.iconType())
            .icon(createMenuRequestDto.icon())
            .text(createMenuRequestDto.text())
            .linkTo(createMenuRequestDto.linkTo())
            .build());
        mockMvc.perform(post("/api/v1/menus")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(createMenuRequestDto)))
            .andDo(print())
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.iconType").value(createMenuRequestDto.iconType()))
            .andExpect(jsonPath("$.icon").value(createMenuRequestDto.icon()))
            .andExpect(jsonPath("$.text").value(createMenuRequestDto.text()))
            .andExpect(jsonPath("$.linkTo").value(createMenuRequestDto.linkTo()));
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

        given(menuService.getMenuList()).willReturn(List.of(
            MenuResponseDto.builder()
                .id(0L)
                .menuOrder(0)
                .icon("test")
                .text("test")
                .linkTo("test")
                .build(),
            MenuResponseDto.builder()
                .id(1L)
                .menuOrder(1)
                .icon("test2")
                .text("test2")
                .linkTo("test2")
                .build()
        ));

        mockMvc.perform(get("/api/v1/menus")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print()).andExpect(status().isOk())
            .andExpect(jsonPath("$[0].id").value(0L))
            .andExpect(jsonPath("$[0].menuOrder").value(0))
            .andExpect(jsonPath("$[0].icon").value("test"))
            .andExpect(jsonPath("$[0].text").value("test"))
            .andExpect(jsonPath("$[0].linkTo").value("test"))
            .andExpect(jsonPath("$[1].id").value(1L))
            .andExpect(jsonPath("$[1].menuOrder").value(1))
            .andExpect(jsonPath("$[1].icon").value("test2"))
            .andExpect(jsonPath("$[1].text").value("test2"))
            .andExpect(jsonPath("$[1].linkTo").value("test2"));

    }

    @Test
    @WithMockUser
    void 메뉴가_없을_경우_예외가_발생한다() throws Exception {
        given(menuService.getMenuList()).willThrow(new MenuException("메뉴가 없습니다."));
        mockMvc.perform(get("/api/v1/menus")
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 메뉴_등록_시_항목이_없을_경우_예외가_던져진다() throws Exception {
        given(menuService.createMenu(null)).willThrow(new MenuException("메뉴의 항목은 null 일 수 없습니다."));
        mockMvc.perform(post("/api/v1/menus")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(null)))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }
    @Test
    @WithMockUser
    void 메뉴가_삭제된다() throws Exception {
        // given
        Long id = 1L;
        // when
        doNothing().when(menuService).deleteMenu(id);
        // then
        mockMvc.perform(delete("/api/v1/menus/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void 삭제_시_메뉴가_존재하지_않을_경우_예외가_던져진다() throws Exception {
        // given
        Long id = 1L;
        // when
        doThrow(new MenuException("존재하지 않는 메뉴입니다.")).when(menuService).deleteMenu(id);

        // then
        mockMvc.perform(delete("/api/v1/menus/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 이미_삭제된_메뉴를_삭제할_시_예외가_발생한다() throws Exception {
        // given
        Long id = 1L;
        // when
        doThrow(new MenuException("존재하지 않는 메뉴입니다.")).when(menuService).deleteMenu(id);
        // then
        mockMvc.perform(delete("/api/v1/menus/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 삭제된_메뉴를_되살릴_수_있다() throws Exception {
        // given
        Long id = 1L;
        // when

        given(menuService.recoverMenu(id)).willReturn(MenuResponseDto.builder()
            .id(id)
            .menuOrder(0)
            .icon("test")
            .text("test")
            .linkTo("test")
            .build());
        // then
        mockMvc.perform(put("/api/v1/menus/{id}/recover", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void 삭제되지_않은_메뉴를_되살릴_경우_예외가_발생한다() throws Exception {
        // given
        Long id = 1L;
        Menu menu = Menu.builder().id(id).isDeleted(false).build();
        given(menuService.recoverMenu(id)).willThrow(new MenuException("삭제 되지 않은 메뉴입니다."));
        // when
        // then
        mockMvc.perform(put("/api/v1/menus/{id}/recover", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }
}