package com.subkore.back.event.controller;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.subkore.back.event.dto.CreateEventRequestDto;
import com.subkore.back.event.dto.EventResponseDto;
import com.subkore.back.event.entity.Event;
import com.subkore.back.event.enumerate.EventState;
import com.subkore.back.event.mapper.EventMapper;
import com.subkore.back.event.service.EventService;
import com.subkore.back.exception.EventException;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = EventController.class)
@AutoConfigureWebMvc
class EventControllerTest {

    @MockBean
    private EventService eventService;
    @Autowired
    private MockMvc mockMvc;
    private EventMapper eventMapper = Mappers.getMapper(EventMapper.class).INSTANCE;

    @Test
    @WithMockUser
    void 상태별로_이벤트_리스트를_반환한다() throws Exception {
        // given
        List<Event> eventList = List.of(Event.builder()
            .id(0L)
            .title("test")
            .state(EventState.WILL_UPDATE)
            .build());
        List<EventState> state = List.of(EventState.WILL_UPDATE);
        List<EventResponseDto> eventResponseDto = eventList.stream()
            .map(eventMapper::eventToEventResponseDto).toList();
        // when
        when(eventService.getEventListByStateContains(state)).thenReturn(eventResponseDto);

        // then
        mockMvc.perform(get("/api/v1/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .param("state", "WILL_UPDATE")
            ).andDo(print())
            .andExpect(status().isOk());

    }

    @Test
    @WithMockUser
    void 해당_상태의_이벤트_리스트가_없을_경우_예외가_발생한다() throws Exception {
        // given
        List<EventState> state = List.of(EventState.WILL_UPDATE);
        // when
        when(eventService.getEventListByStateContains(state)).thenThrow(new EventException("이벤트 "
            + "리스트가 존재하지 않습니다. 상태를 확인해 주십시오."));
        // then
        mockMvc.perform(get("/api/v1/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .param("state", "WILL_UPDATE")
            ).andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 모든_이벤트_리스트를_반환한다() throws Exception {
        // given
        List<Event> eventList = List.of(Event.builder()
            .id(0L)
            .title("test")
            .state(EventState.WILL_UPDATE)
            .build());
        List<EventResponseDto> eventResponseDto = eventList.stream()
            .map(eventMapper::eventToEventResponseDto).toList();
        // when
        when(eventService.getEventListAll()).thenReturn(eventResponseDto);
        // then
        mockMvc.perform(get("/api/v1/events/all")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 모든_이벤트_리스트가_없을_경우_예외가_발생한다() throws Exception {
        // given
        // when
        when(eventService.getEventListAll()).thenThrow(new EventException("이벤트 리스트가 존재하지 않습니다."));
        // then
        mockMvc.perform(get("/api/v1/events/all")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 이벤트를_등록할_수_있다() throws Exception {
        // given
        CreateEventRequestDto createEventRequestDto = CreateEventRequestDto.builder()
            .title("test")
            .state("WILL_UPDATE")
            .build();
        Event event = Event.builder()
            .id(0L)
            .title("test")
            .state(EventState.WILL_UPDATE)
            .build();
        EventResponseDto eventResponseDto = eventMapper.eventToEventResponseDto(event);
        // when
        when(eventService.createEvent(createEventRequestDto)).thenReturn(eventResponseDto);
        // then
        mockMvc.perform(post("/api/v1/events")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(createEventRequestDto)))
            .andDo(print())
            .andExpect(status().isCreated());
    }
}