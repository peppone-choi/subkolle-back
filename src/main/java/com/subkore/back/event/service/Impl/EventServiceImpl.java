package com.subkore.back.event.service.Impl;

import com.subkore.back.event.dto.CreateEventRequestDto;
import com.subkore.back.event.dto.EventResponseDto;
import com.subkore.back.event.dto.UpdateEventRequestDto;
import com.subkore.back.event.entity.Event;
import com.subkore.back.event.enumerate.EventState;
import com.subkore.back.event.mapper.EventMapper;
import com.subkore.back.event.repository.EventRepository;
import com.subkore.back.event.service.EventService;
import com.subkore.back.exception.EventException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper = EventMapper.INSTANCE;

    @Override
    public List<EventResponseDto> getEventListByStateContains(List<EventState> state) {
        List<Event> eventList = eventRepository.findAllByStateIn(state);
        if (eventList.isEmpty()) {
            throw new EventException("해당하는 이벤트가 없습니다.");
        }
        return eventList.stream().map(eventMapper::eventToEventResponseDto).toList();
    }

    @Override
    public EventResponseDto createEvent(CreateEventRequestDto createEventRequestDto) {
        Event event = eventMapper.createEventRequestDtoToEvent(createEventRequestDto);
        Event savedEvent = eventRepository.save(event);
        return eventMapper.eventToEventResponseDto(savedEvent);
    }

    @Override
    public List<EventResponseDto> getEventListAll() {
        List<Event> eventList = eventRepository.findAll();
        if (eventList.isEmpty()) {
            throw new EventException("해당하는 이벤트가 없습니다.");
        }
        return eventList.stream().map(eventMapper::eventToEventResponseDto).toList();
    }

    @Override
    public EventResponseDto updateEvent(Long id, UpdateEventRequestDto updateEventRequestDto) {
        if (eventRepository.existsById(id)) {
            Event event = eventRepository.findById(id).get();
            eventMapper.updateEventFromDto(updateEventRequestDto, event);
            Event savedEvent = eventRepository.save(event);
            return eventMapper.eventToEventResponseDto(savedEvent);
        } else {
            throw new EventException("해당하는 이벤트가 없습니다.");
        }
    }
}
