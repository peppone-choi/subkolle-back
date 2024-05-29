package com.subkore.back.carousel.dto;

import lombok.Builder;

@Builder
public record UpdateCarouselRequestDto(
    String title,
    String description,
    String imageUrl,
    String linkTo,
    Integer order,
    Boolean isShow
) {

}
