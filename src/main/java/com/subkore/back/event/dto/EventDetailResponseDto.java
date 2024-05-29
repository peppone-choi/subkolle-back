package com.subkore.back.event.dto;

import java.util.List;
import lombok.Builder;

@Builder
public record EventDetailResponseDto(
    List<PriceResponseDto> price,
    String link,
    String description
) {

}
