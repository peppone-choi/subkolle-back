package com.subkore.back.menu.service.impl;

import com.subkore.back.exception.MenuException;
import com.subkore.back.menu.controller.MenuController;
import com.subkore.back.menu.dto.CreateMenuRequestDto;
import com.subkore.back.menu.dto.MenuResponseDto;
import com.subkore.back.menu.entity.Menu;
import com.subkore.back.menu.mapper.MenuMapper;
import com.subkore.back.menu.repository.MenuRepository;
import com.subkore.back.menu.service.MenuService;
import java.util.Comparator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

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
     * 모든 메뉴를 반환한다. 메뉴가 아예 없을 경우 예외를 던진다.
     * @return 모든 메뉴의 리스트
     * @see MenuController#getMenuList() 
     */
    @Override
    public List<MenuResponseDto> getMenuList() {
        List<Menu> sortedMenus = menuRepository.findAllByIsDeletedFalseOrderByMenuOrder();
        if (sortedMenus.isEmpty()) {
            throw new MenuException("메뉴가 없습니다.");
        }
        return sortedMenus.stream().map(menuMapper::menuToMenuResponseDto).toList();
    }

    /**
     * DTO를 통하여 데이터를 받고 이 데이터를 통해 메뉴를 만들고 이 메뉴를 DB에 저장하고 저장 된 메뉴를 DTO를 통하여 반환한다
     * @param createMenuRequestDto 메뉴 저장 데이터
     * @return 저장 된 메뉴 데이터
     * @see MenuController#createMenu(CreateMenuRequestDto) 
     */
    @Override
    public MenuResponseDto createMenu(CreateMenuRequestDto createMenuRequestDto) {
        if (createMenuRequestDto.icon() == null ||  createMenuRequestDto.text() == null || createMenuRequestDto.linkTo() == null) {
            throw new MenuException("메뉴의 항목은 null 일 수 없습니다.");
        }
        if (createMenuRequestDto.icon().isEmpty()
                || createMenuRequestDto.text().isEmpty() || createMenuRequestDto.linkTo().isEmpty()) {
            throw new MenuException("메뉴의 항목은 비워둘 수 없습니다.");
        }
        Menu createdMenu = menuMapper.createMenuRequestDtoToMenu(createMenuRequestDto);
        Integer count = menuRepository.countByIsDeletedFalse();
        createdMenu.setMenuOrder(count);
        System.out.println(createdMenu.getMenuOrder());
        Menu savedMenu = menuRepository.save(createdMenu);
        return menuMapper.menuToMenuResponseDto(savedMenu);
    }
}
