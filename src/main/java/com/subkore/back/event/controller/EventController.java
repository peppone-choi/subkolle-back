package com.subkore.back.event.controller;

import com.subkore.back.event.dto.CreateEventRequestDto;
import com.subkore.back.event.dto.EventResponseDto;
import com.subkore.back.event.dto.UpdateEventRequestDto;
import com.subkore.back.event.enumerate.EventState;
import com.subkore.back.event.service.EventService;
import java.net.URI;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class EventController {

    private final EventService eventService;

    @GetMapping("/api/v1/events")
    public ResponseEntity<List<EventResponseDto>> getEventListByState(@RequestParam(value =
        "state", required = false, defaultValue = "") List<String> state) {
        List<EventState> stateList = state.stream().map(EventState::valueOf).toList();
        List<EventResponseDto> result = eventService.getEventListByStateContains(stateList);
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/api/v1/events/all")
    public ResponseEntity<List<EventResponseDto>> getEventListAll() {
        List<EventResponseDto> result = eventService.getEventListAll();
        return ResponseEntity.ok().body(result);
    }

    @GetMapping("/api/v1/events/{id}")
    public ResponseEntity<EventResponseDto> getEvent(@PathVariable("id") Long id) {
        EventResponseDto result = eventService.getEvent(id);
        return ResponseEntity.ok().body(result);
    }

    @PostMapping("/api/v1/events")
    public ResponseEntity<EventResponseDto> createEvent(
        @RequestBody CreateEventRequestDto createEventRequestDto) {
        EventResponseDto result = eventService.createEvent(createEventRequestDto);
        return ResponseEntity.created(URI.create("/api/v1/events/" + result.id())).body(result);
    }

    @PutMapping("/api/v1/events/{id}")
    public ResponseEntity<EventResponseDto> updateEvent(@PathVariable("id") Long id,
        @RequestBody UpdateEventRequestDto updateEventRequestDto) {
        EventResponseDto result = eventService.updateEvent(id, updateEventRequestDto);
        return ResponseEntity.created(URI.create("/api/v1/events/" + result.id())).body(result);
    }

    @DeleteMapping("/api/v1/events/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable("id") Long id) {
        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/api/v1/events/{id}/recover")
    public ResponseEntity<EventResponseDto> recoverEvent(@PathVariable("id") Long id) {
        EventResponseDto result = eventService.recoverEvent(id);
        return ResponseEntity.created(URI.create("/api/v1/events/" + result.id())).body(result);
    }
}
