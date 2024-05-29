package com.subkore.back.event.dto;

import com.subkore.back.event.enumerate.EventState;
import com.subkore.back.event.enumerate.EventTag;
import java.time.LocalDateTime;
import java.util.List;
import lombok.Builder;

@Builder
public record EventResponseDto(
    Long id,
    String title,
    String headerImage,
    Boolean isLongTimeEvent,
    LocalDateTime startTime,
    LocalDateTime endTime,
    EventTag tag,
    Boolean isOverNight,
    EventState state,
    String location,
    List<String> genreAndKeyword,
    EventDetailResponseDto detail
) {

}
