package com.subkore.back.menu.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Builder;

/**
 * @param order 몇번째 메뉴인지
 * @param icon 아이콘
 * @param text 해당 메뉴의 이름
 * @param linkTo 해당 메뉴가 어디로 이동시키는지
 * @see com.subkore.back.menu.entity.Menu
 */
@Builder
public record CreateMenuRequestDto(
    Integer order,
    @NotEmpty
    String icon,
    @NotEmpty
    String text,
    @NotEmpty
    String linkTo
) {

}
