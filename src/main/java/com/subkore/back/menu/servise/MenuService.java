package com.subkore.back.menu.servise;

import com.subkore.back.menu.dto.CreateMenuRequestDto;
import com.subkore.back.menu.dto.MenuResponseDto;
import java.util.List;

public interface MenuService {
    List<MenuResponseDto> getMenuList();
    MenuResponseDto createMenu(CreateMenuRequestDto createMenuRequestDto);
}
