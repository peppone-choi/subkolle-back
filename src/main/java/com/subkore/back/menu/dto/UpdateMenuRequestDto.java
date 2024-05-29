package com.subkore.back.menu.dto;

import lombok.Builder;

@Builder
public record UpdateMenuRequestDto(
    Integer menuOrder,
    String iconType,
    String icon,
    String text,
    String linkTo
) {

}
