package com.subkore.back.event.dto;

import com.subkore.back.event.entity.EventDetail;
import java.util.List;
import lombok.Builder;

@Builder
public record UpdateEventRequestDto(
    String title,
    String headerImage,
    Boolean isLongTimeEvent,
    String transport,
    String startTime,
    String endTime,
    List<String> tag,
    Boolean isOverNight,
    String state,
    String location,
    String detailLocation,
    List<String> genreAndKeyword,
    EventDetail detail,
    Boolean isShow
) {

}
