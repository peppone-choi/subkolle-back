package com.subkore.back.event.controller;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.subkore.back.event.dto.CreateEventRequestDto;
import com.subkore.back.event.dto.EventResponseDto;
import com.subkore.back.event.dto.UpdateEventRequestDto;
import com.subkore.back.event.entity.Event;
import com.subkore.back.event.enumerate.EventState;
import com.subkore.back.event.enumerate.EventTag;
import com.subkore.back.event.mapper.EventMapper;
import com.subkore.back.event.repository.EventRepository;
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
    @Autowired
    private EventController eventController;
    @MockBean
    private EventRepository eventRepository;
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

    @Test
    @WithMockUser
    void 이벤트_수정이_가능하다() throws Exception {
        // given
        Long id = 0L;
        Event event = Event.builder()
            .id(0L)
            .title("test")
            .state(EventState.WILL_UPDATE)
            .build();
        UpdateEventRequestDto updateEventRequestDto = UpdateEventRequestDto.builder()
            .title("test")
            .state("PROCEEDING")
            .build();
        eventMapper.updateEventFromDto(updateEventRequestDto, event);
        EventResponseDto eventResponseDto = eventMapper.eventToEventResponseDto(event);
        // when
        when(eventService.updateEvent(id, new ObjectMapper().registerModule(new JavaTimeModule())
            .readValue(new ObjectMapper().registerModule(new JavaTimeModule())
                .writeValueAsString(updateEventRequestDto), UpdateEventRequestDto.class)))
            .thenReturn(eventResponseDto);
        // then
        mockMvc.perform(put("/api/v1/events/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(updateEventRequestDto)))
            .andDo(print())
            .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser
    void 이벤트_수정_시_해당_이벤트가_존재하지_않을_경우_예외가_발생한다() throws Exception {
        // given
        Long id = 0L;
        UpdateEventRequestDto updateEventRequestDto = UpdateEventRequestDto.builder()
            .title("test")
            .state("PROCEEDING")
            .build();
        // when
        when(eventRepository.findById(id)).thenReturn(java.util.Optional.empty());
        when(eventService.updateEvent(id, updateEventRequestDto)).thenThrow(new EventException("해당하는 이벤트가 없습니다."));
        // then
        mockMvc.perform(put("/api/v1/events/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().registerModule(new JavaTimeModule())
                    .writeValueAsString(updateEventRequestDto)))
            .andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 이벤트_삭제가_가능하다() throws Exception {
        // given
        Long id = 0L;
        // when
        doNothing().when(eventService).deleteEvent(id);
        // then
        mockMvc.perform(delete("/api/v1/events/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser
    void 이벤트_삭제_시_해당_이벤트가_존재하지_않을_경우_예외가_발생한다() throws Exception {
        // given
        Long id = 0L;
        // when
        when(eventController.deleteEvent(id)).thenThrow(new EventException("해당하는 이벤트가 없습니다."));
        // then
        mockMvc.perform(delete("/api/v1/events/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 이벤트_삭제_시_이미_삭제되어있는_이벤트를_다시_삭제할_수_없다() throws Exception {
        // given
        Long id = 0L;
        // when
        when(eventController.deleteEvent(id)).thenThrow(new EventException("이미 삭제된 이벤트입니다."));
        // then
        mockMvc.perform(delete("/api/v1/events/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 삭제된_이벤트를_되살릴_수_있다() throws Exception {
        // given
        Long id = 0L;
        Event event = Event.builder()
            .id(0L)
            .title("test")
            .state(EventState.WILL_UPDATE)
            .isDeleted(true)
            .build();
        event.recover();
        EventResponseDto eventResponseDto = eventMapper.eventToEventResponseDto(event);
        // when
        when(eventService.recoverEvent(id)).thenReturn(eventResponseDto);
        // then
        mockMvc.perform(put("/api/v1/events/{id}/recover", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isCreated());
    }
    @Test
    @WithMockUser
    void 이미_삭제되지_않은_이벤트를_되살릴_시_예외가_던져진다() throws Exception {
        // given
        Long id = 0L;
        Event event = Event.builder()
            .id(0L)
            .title("test")
            .state(EventState.WILL_UPDATE)
            .isDeleted(false)
            .build();
        // when
        when(eventRepository.findById(id)).thenReturn(java.util.Optional.of(event));
        when(eventService.recoverEvent(id)).thenThrow(new EventException("이미 삭제되지 않은 이벤트입니다."));

        // then
        mockMvc.perform(put("/api/v1/events/{id}/recover", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().is4xxClientError());
    }

    @Test
    @WithMockUser
    void 특정한_이벤트의_정보를_확인_할_수_있다() throws Exception {
        // given
        Long id = 0L;
        Event event = Event.builder()
            .id(0L)
            .title("test")
            .state(EventState.WILL_UPDATE)
            .build();
        EventResponseDto eventResponseDto = eventMapper.eventToEventResponseDto(event);
        // when
        when(eventService.getEvent(id)).thenReturn(eventResponseDto);
        // then
        mockMvc.perform(get("/api/v1/events/{id}", id)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 이벤트의_정보를_확인할_때_이벤트가_없으면_오류를_반환한다() {
        // given
        Long id = 0L;
        when(eventService.getEvent(id)).thenThrow(new EventException("해당하는 이벤트가 없습니다."));
        // then
        assertThrows(EventException.class,
            () -> eventService.getEvent(id));
    }

    @Test
    @WithMockUser
    void 이벤트를_태그별로_조회할_수_있다() throws Exception {
        // given
        List<Event> eventList = List.of(Event.builder()
            .id(0L)
            .title("test")
                .tag(List.of(EventTag.EXHIBITION_AND_SALE))
            .build());
        List<EventResponseDto> eventResponseDto = eventList.stream()
            .map(eventMapper::eventToEventResponseDto).toList();
        // when
        when(eventService.getEventListByTag(EventTag.EXHIBITION_AND_SALE)).thenReturn(eventResponseDto);
        // then
        mockMvc.perform(get("/api/v1/events/tag/{tag}", EventTag.EXHIBITION_AND_SALE)
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
            ).andDo(print())
            .andExpect(status().isOk());
    }

    @Test
    @WithMockUser
    void 이벤트_태그에_해당하는_이벤트가_없을_경우_예외를_반환한다() {
        // given
        when(eventService.getEventListByTag(EventTag.EXHIBITION_AND_SALE)).thenThrow(new EventException("해당하는 이벤트가 없습니다."));
        // then
        assertThrows(EventException.class,
            () -> eventService.getEventListByTag(EventTag.EXHIBITION_AND_SALE));
    }
}