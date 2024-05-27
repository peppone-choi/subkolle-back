package com.subkore.back.carousel.dto;

import lombok.Builder;

@Builder
public record CreateCarouselRequestDto(
    Integer order,
    String title,
    String description,
    String imageUrl,
    String linkTo
) {

}
