package com.subkore.back.menu.servise.impl;

import com.subkore.back.menu.controller.MenuController;
import com.subkore.back.menu.dto.CreateMenuRequestDto;
import com.subkore.back.menu.dto.MenuResponseDto;
import com.subkore.back.menu.entity.Menu;
import com.subkore.back.menu.mapper.MenuMapper;
import com.subkore.back.menu.repository.MenuRepository;
import com.subkore.back.menu.servise.MenuService;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Menu 관련 Controller 레이어의 메소드가 호출 될 때 실제로 실행되는 메소드들
 * 실제로 서버에서 동작하는 로직이나 행동이 여기서 실행된다
 * @author peppone-choi (peppone.choi@gmail.com)
 * @version 1.0
 */
@Service
@RequiredArgsConstructor
public class MenuServiceImpl implements MenuService {
    private final MenuRepository menuRepository;
    private final MenuMapper menuMapper = MenuMapper.INSTANCE;

    /**
     * 모든 메뉴를 반환한다
     * @return 모든 메뉴의 리스트
     * @see MenuController#getMenuList() 
     */
    @Override
    public List<MenuResponseDto> getMenuList() {
        List<Menu> menus = menuRepository.findAll();
        menus.sort(Comparator.comparingInt(Menu::getOrder));
        return menus.stream().map(menuMapper::menuToMenuResponseDto).toList();
    }

    /**
     * DTO를 통하여 데이터를 받고 이 데이터를 통해 메뉴를 만들고 이 메뉴를 DB에 저장하고 저장 된 메뉴를 DTO를 통하여 반환한다
     * @param createMenuRequestDto 메뉴 저장 데이터
     * @return 저장 된 메뉴 데이터
     * @see MenuController#createMenu(CreateMenuRequestDto) 
     */
    @Override
    public MenuResponseDto createMenu(CreateMenuRequestDto createMenuRequestDto) {
        Menu createdMenu = menuMapper.createMenuRequestDtoToMenu(createMenuRequestDto);
        Menu savedMenu = menuRepository.save(createdMenu);
        return menuMapper.menuToMenuResponseDto(savedMenu);
    }
}
