package com.subkore.back.event.dto;

import com.subkore.back.event.enumerate.EventState;
import com.subkore.back.event.enumerate.EventTag;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import lombok.Builder;

@Builder
public record EventResponseDto(
    Long id,
    String shortcut,
    String title,
    String headerImage,
    String transport,
    Boolean isLongTimeEvent,
    LocalDateTime startTime,
    LocalDateTime endTime,
    List<EventTag> tag,
    Boolean isOverNight,
    EventState state,
    String location,
    String detailLocation,
    List<String> genreAndKeyword,
    EventDetailResponseDto detail,
    Boolean isShow,
    Boolean isDeleted
) {

}
