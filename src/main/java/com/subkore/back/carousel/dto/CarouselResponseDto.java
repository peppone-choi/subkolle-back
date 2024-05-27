package com.subkore.back.carousel.dto;

import lombok.Builder;

@Builder
public record CarouselResponseDto(
        Long id,
        String title,
        String description,
        String imageUrl,
        String linkTo,
        Integer order
) {

}
