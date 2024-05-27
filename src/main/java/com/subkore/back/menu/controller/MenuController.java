package com.subkore.back.menu.controller;

import com.subkore.back.menu.dto.CreateMenuRequestDto;
import com.subkore.back.menu.dto.MenuResponseDto;
import com.subkore.back.menu.service.MenuService;
import com.subkore.back.menu.service.impl.MenuServiceImpl;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * 서버와 통신을 하기 위한 Restful Controller 클래스
 * @author peppone-choi (peppone.choi@gmail.com)
 * @version 1.0
 */
@RestController
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    /**
     * 메뉴 전체를 불러오는 컨테이너 메소드.
     * 각 메뉴를 불러오는 메소드는 아직 만들지 않음.
     * @return 전체 메뉴
     * @see MenuServiceImpl#getMenuList()
     */
    @GetMapping("/api/v1/menus")
    public ResponseEntity<List<MenuResponseDto>> getMenuList() {
        List<MenuResponseDto> result = menuService.getMenuList();
        return ResponseEntity.ok().body(result);
    }


    /**
     * 데이터를 받아 DB에 메뉴를 등록하는 메소드
     * @param createMenuRequestDto 데이터가 들어있는 DTO
     * @return HTTP 응답 상태코드와 등록한 메뉴 반환
     * @see MenuServiceImpl#createMenu(CreateMenuRequestDto)
     */
    @PostMapping("/api/v1/menus")
    public ResponseEntity<MenuResponseDto> createMenu(@Valid @RequestBody CreateMenuRequestDto createMenuRequestDto) {
        MenuResponseDto result = menuService.createMenu(createMenuRequestDto);
        return ResponseEntity.created(URI.create("/menus/" + result.id())).body(result);
    }
}
