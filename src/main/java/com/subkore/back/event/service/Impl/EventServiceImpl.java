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
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper = EventMapper.INSTANCE;

    @Override
    public List<EventResponseDto> getEventListByStateContains(List<EventState> state) {
        List<Event> eventList = eventRepository.findAllByStateInAndIsDeletedFalseAndIsShowTrue(state);
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
    public EventResponseDto getEvent(Long id) {
        if (eventRepository.existsById(id)) {
            Event event = eventRepository.findById(id).get();
            return eventMapper.eventToEventResponseDto(event);
        } else {
            throw new EventException("해당하는 이벤트가 없습니다.");
        }
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

    @Override
    public void deleteEvent(Long id) {
        if (eventRepository.existsById(id) && !eventRepository.findById(id).get().getIsDeleted()) {
            Event event = eventRepository.findById(id).get();
            event.setIsDeleted(true);
            eventRepository.save(event);
        } else if (!eventRepository.existsById(id)) {
            throw new EventException("해당하는 이벤트가 없습니다.");
        } else {
            throw new EventException("이미 삭제된 이벤트입니다.");
        }
    }

    @Override
    public EventResponseDto recoverEvent(Long id) {
        if (eventRepository.existsById(id) && eventRepository.findById(id).get().getIsDeleted()) {
            Event event = eventRepository.findById(id).get();
            event.setIsDeleted(false);
            Event savedEvent = eventRepository.save(event);
            return eventMapper.eventToEventResponseDto(savedEvent);
        } else if (!eventRepository.existsById(id)) {
            throw new EventException("해당하는 이벤트가 없습니다.");
        } else {
            throw new EventException("이미 복구된 이벤트입니다.");
        }
    }
}
