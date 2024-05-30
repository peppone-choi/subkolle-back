package com.subkore.back.event.service;

import com.subkore.back.event.dto.CreateEventRequestDto;
import com.subkore.back.event.dto.EventResponseDto;
import com.subkore.back.event.dto.UpdateEventRequestDto;
import com.subkore.back.event.enumerate.EventState;
import java.util.List;

public interface EventService {

    List<EventResponseDto> getEventListByStateContains(List<EventState> state);

    EventResponseDto createEvent(CreateEventRequestDto createEventRequestDto);

    List<EventResponseDto> getEventListAll();

    EventResponseDto updateEvent(Long id, UpdateEventRequestDto updateEventRequestDto);

    void deleteEvent(Long id);

    EventResponseDto recoverEvent(Long id);

    EventResponseDto getEvent(Long id);
}
