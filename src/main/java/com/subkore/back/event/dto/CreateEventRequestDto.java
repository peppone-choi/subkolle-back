package com.subkore.back.event.dto;

import com.subkore.back.event.entity.EventDetail;
import com.subkore.back.event.enumerate.EventTag;
import java.util.List;
import lombok.Builder;

@Builder
public record CreateEventRequestDto(
    String title,
    String headerImage,
    Boolean isLongTimeEvent,
    String startTime,
    String endTime,
    List<EventTag> tag,
    Boolean isOverNight,
    String state,
    String location,
    List<String> genreAndKeyword,
    EventDetail detail
) {

}
