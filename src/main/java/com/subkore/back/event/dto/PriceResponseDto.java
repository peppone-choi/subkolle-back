package com.subkore.back.event.dto;

import lombok.Builder;

@Builder
public record PriceResponseDto(
    String price,
    String option
) {

}
