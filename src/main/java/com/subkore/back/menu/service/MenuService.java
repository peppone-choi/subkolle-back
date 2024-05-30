package com.subkore.back.menu.service;

import com.subkore.back.menu.dto.CreateMenuRequestDto;
import com.subkore.back.menu.dto.MenuResponseDto;
import com.subkore.back.menu.dto.UpdateMenuRequestDto;
import java.util.List;

public interface MenuService {

    List<MenuResponseDto> getMenuList();

    MenuResponseDto createMenu(CreateMenuRequestDto createMenuRequestDto);

    MenuResponseDto updateMenu(Long id, UpdateMenuRequestDto updateMenuRequestDto);

    void deleteMenu(long id);

    MenuResponseDto recoverMenu(long id);
}
