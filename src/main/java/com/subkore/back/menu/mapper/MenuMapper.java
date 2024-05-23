package com.subkore.back.menu.mapper;

import com.subkore.back.menu.dto.CreateMenuRequestDto;
import com.subkore.back.menu.dto.MenuResponseDto;
import com.subkore.back.menu.entity.Menu;
import lombok.experimental.UtilityClass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * MenuMapper 클래스
 * Menu 타입의 객체와 그 DTO를 서로 변환하는 일을 한다
 * @author peppone-choi (peppone.choi@gmail.com)
 * @version 1.0
 * */
@Mapper
public interface MenuMapper {
    MenuMapper INSTANCE = Mappers.getMapper(MenuMapper.class);

    /**
     * 메뉴 Type 객체를 DTO 타입 객체로 변환
     * @param menu Menu Type 객체
     * @return MenuResponseDto
     */
    @Mapping(target = "id", ignore = true)
    MenuResponseDto menuToMenuResponseDto(Menu menu);

    /**
     * 메뉴 생성 시 DTO 객체를 Menu 객체로 변환
     * @param createMenuRequestDto DTO 객체
     * @return Menu
     */
    @Mapping(target = "id", ignore = true)
    Menu createMenuRequestDtoToMenu(CreateMenuRequestDto createMenuRequestDto);
}
