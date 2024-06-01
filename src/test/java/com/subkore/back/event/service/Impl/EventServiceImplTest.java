package com.subkore.back.event.service.Impl;

import static com.subkore.back.event.enumerate.EventState.BEFORE_PROCEEDING;
import static com.subkore.back.event.enumerate.EventState.PROCEEDING;
import static com.subkore.back.event.enumerate.EventState.WILL_UPDATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.subkore.back.event.dto.CreateEventRequestDto;
import com.subkore.back.event.dto.EventResponseDto;
import com.subkore.back.event.dto.UpdateEventRequestDto;
import com.subkore.back.event.entity.Event;
import com.subkore.back.event.entity.EventDetail;
import com.subkore.back.event.entity.Price;
import com.subkore.back.event.enumerate.EventTag;
import com.subkore.back.event.mapper.EventMapper;
import com.subkore.back.event.repository.EventRepository;
import com.subkore.back.exception.EventException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mapstruct.factory.Mappers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class).INSTANCE;
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private EventServiceImpl eventService;

    @Test
    void 현재_준비중이거나_열린_전체_행사_리스트를_확인할_수_있다() {
        // given
        List<Event> eventList = List.of(Event.builder()
                .id(1L)
                .title("test")
                .headerImage("test")
                .isLongTimeEvent(false)
                .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
                .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
                .tag(EventTag.ETC)
                .isOverNight(false)
                .state(BEFORE_PROCEEDING)
                .location("test")
                .genreAndKeyword(List.of("test", "test2"))
                .detail(EventDetail.builder()
                    .price(List.of(
                        Price.builder()
                            .option("성인")
                            .price(10000)
                            .build(),
                        Price.builder()
                            .option("청소년")
                            .price(5000)
                            .build()))
                    .link("test")
                    .description("test")
                    .build())
                .build(),
            Event.builder()
                .id(2L)
                .title("test2")
                .headerImage("test2")
                .isLongTimeEvent(true)
                .startTime(LocalDateTime.parse("2021-01-04T00:00:00"))
                .endTime(LocalDateTime.parse("2021-01-08T00:00:00"))
                .tag(EventTag.ETC)
                .isOverNight(false)
                .state(BEFORE_PROCEEDING)
                .location("test")
                .genreAndKeyword(List.of("test", "test2"))
                .detail(EventDetail.builder()
                    .price(List.of(
                        Price.builder()
                            .option("성인")
                            .price(10000)
                            .build(),
                        Price.builder()
                            .option("청소년")
                            .price(5000)
                            .build()))
                    .link("test")
                    .description("test")
                    .build())
                .build());
        when(eventRepository.findAllByStateInAndIsDeletedFalseAndIsShowTrue(
            List.of(BEFORE_PROCEEDING, PROCEEDING))).thenReturn(
            eventList);
        // when
        List<EventResponseDto> responseList =
            eventService.getEventListByStateContains(List.of(BEFORE_PROCEEDING, PROCEEDING));
        // then
        assertEquals(responseList.size(), 2);
        assertEquals(responseList.get(0).id(), 1L);
    }

    @Test
    void 준비중이거나_열린_행사_리스트가_없다면_예외를_반환한다() {
        // given
        when(eventRepository.findAllByStateInAndIsDeletedFalseAndIsShowTrue(
            List.of(BEFORE_PROCEEDING, PROCEEDING))).thenReturn(
            List.of());
        // when + then
        assertThrows(EventException.class,
            () -> eventService.getEventListByStateContains(List.of(BEFORE_PROCEEDING,
                PROCEEDING)));
    }

    @Test
    void 전체_행사_리스트를_확인할_수_있다() {
        // given
        List<Event> eventList = List.of(Event.builder()
                .id(1L)
                .title("test")
                .headerImage("test")
                .isLongTimeEvent(false)
                .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
                .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
                .tag(EventTag.ETC)
                .isOverNight(false)
                .state(BEFORE_PROCEEDING)
                .location("test")
                .genreAndKeyword(List.of("test", "test2"))
                .detail(EventDetail.builder()
                    .price(List.of(
                        Price.builder()
                            .option("성인")
                            .price(10000)
                            .build(),
                        Price.builder()
                            .option("청소년")
                            .price(5000)
                            .build()))
                    .link("test")
                    .description("test")
                    .build())
                .build(),
            Event.builder()
                .id(2L)
                .title("test2")
                .headerImage("test2")
                .isLongTimeEvent(true)
                .startTime(LocalDateTime.parse("2021-01-04T00:00:00"))
                .endTime(LocalDateTime.parse("2021-01-08T00:00:00"))
                .tag(EventTag.ETC)
                .isOverNight(false)
                .state(BEFORE_PROCEEDING)
                .location("test")
                .genreAndKeyword(List.of("test", "test2"))
                .detail(EventDetail.builder()
                    .price(List.of(
                        Price.builder()
                            .option("성인")
                            .price(10000)
                            .build(),
                        Price.builder()
                            .option("청소년")
                            .price(5000)
                            .build()))
                    .link("test")
                    .description("test")
                    .build())
                .build());
        when(eventRepository.findAll()).thenReturn(eventList);
        // when
        List<EventResponseDto> responseList =
            eventService.getEventListAll();
        // then
        assertEquals(responseList.size(), 2);
        assertEquals(responseList.get(0).id(), 1L);
    }

    @Test
    void 등록된_행사_리스트가_없다면_예외를_반환한다() {
        // given
        when(eventRepository.findAll()).thenReturn(List.of());
        // when + then
        assertThrows(EventException.class,
            () -> eventService.getEventListAll());
    }

    @Test
    void 이벤트가_등록된다() {
        // given
        Event event = Event.builder()
            .id(1L)
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
            .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
            .tag(EventTag.ETC)
            .isOverNight(false)
            .state(WILL_UPDATE)
            .location("test")
            .genreAndKeyword(List.of("test", "test2"))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .build();
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        CreateEventRequestDto createEventRequestDto = CreateEventRequestDto.builder()
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime("2021-01-01T00:00:00")
            .endTime("2021-01-02T00:00:00")
            .tag("ETC")
            .isOverNight(false)
            .state("WILL_UPDATE")
            .location("test")
            .genreAndKeyword(List.of("test", "test2"))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .build();
        // when
        EventResponseDto response = eventService.createEvent(createEventRequestDto);
        // then
        assertEquals(response.id(), 1L);
    }

    @Test
    void 이벤트_상태이상으로_등록되지_않는다() {
        // given
        CreateEventRequestDto createEventRequestDto = CreateEventRequestDto.builder()
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime("2021-01-01T00:00:00")
            .endTime("2021-01-02T00:00:00")
            .tag("ETC")
            .isOverNight(false)
            .state("test")
            .location("test")
            .genreAndKeyword(List.of("test", "test2"))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .build();
        // when + then
        assertThrows(IllegalArgumentException.class,
            () -> eventService.createEvent(createEventRequestDto));
    }

    @Test
    void 이벤트가_성공적으로_수정된다() {
        // given
        Event event = Event.builder()
            .id(1L)
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
            .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
            .tag(EventTag.ETC)
            .isOverNight(false)
            .state(WILL_UPDATE)
            .location("test")
            .genreAndKeyword(new ArrayList<>(List.of("test", "test2")))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .isShow(true)
            .build();
        UpdateEventRequestDto updateEventRequestDto = UpdateEventRequestDto.builder()
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime("2021-01-01T00:00:00")
            .endTime("2021-01-02T00:00:00")
            .tag("EXHIBITION_AND_SALE")
            .isOverNight(false)
            .state("WILL_UPDATE")
            .location("test")
            .genreAndKeyword(new ArrayList<>(List.of("test", "test2", "test3")))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .isShow(true)
            .build();
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(event));
        eventMapper.updateEventFromDto(updateEventRequestDto, event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        // when
        EventResponseDto response = eventService.updateEvent(1L, updateEventRequestDto);
        // then
        assertEquals(response.tag(), EventTag.EXHIBITION_AND_SALE);
        assertEquals(response.genreAndKeyword(), Arrays.asList("test", "test2", "test3"));
    }

    @Test
    void 이벤트가_존재하지_않을때_수정을_시도하면_예외를_반환한다() {
        // given
        UpdateEventRequestDto updateEventRequestDto = UpdateEventRequestDto.builder()
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime("2021-01-01T00:00:00")
            .endTime("2021-01-02T00:00:00")
            .tag("EXHIBITION_AND_SALE")
            .isOverNight(false)
            .state("WILL_UPDATE")
            .location("test")
            .genreAndKeyword(new ArrayList<>(List.of("test", "test2", "test3")))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .build();
        // when + then
        assertThrows(EventException.class,
            () -> eventService.updateEvent(1L, updateEventRequestDto));
    }

    @Test
    void 이벤트를_삭제할_수_있다() {
        Event event = Event.builder()
            .id(1L)
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
            .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
            .tag(EventTag.ETC)
            .isOverNight(false)
            .state(WILL_UPDATE)
            .location("test")
            .genreAndKeyword(new ArrayList<>(List.of("test", "test2")))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .isShow(true)
            .build();
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        // when
        eventService.deleteEvent(1L);
        // then
        assertEquals(event.getIsDeleted(), true);
    }

    @Test
    void 삭제할_이벤트가_존재하지_않으면_예외를_반환한다() {
        // given
        // when + then
        assertThrows(EventException.class,
            () -> eventService.deleteEvent(1L));
    }

    @Test
    void 삭제된_이벤트를_복구할_수_있다() {
        Event event = Event.builder()
            .id(1L)
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
            .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
            .tag(EventTag.ETC)
            .isOverNight(false)
            .state(WILL_UPDATE)
            .location("test")
            .genreAndKeyword(new ArrayList<>(List.of("test", "test2")))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .isShow(true)
            .isDeleted(true)
            .build();
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        // when
        EventResponseDto response = eventService.recoverEvent(1L);
        // then
        assertEquals(response.isDeleted(), false);
    }

    @Test
    void 복구할_이벤트가_존재하지_않으면_예외를_반환한다() {
        // given
        // when + then
        assertThrows(EventException.class,
            () -> eventService.recoverEvent(1L));
    }

    @Test
    void 이미_복구된_이벤트를_복구하려_하면_예외를_반환한다() {
        // given
        Event event = Event.builder()
            .id(1L)
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
            .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
            .tag(EventTag.ETC)
            .isOverNight(false)
            .state(WILL_UPDATE)
            .location("test")
            .genreAndKeyword(new ArrayList<>(List.of("test", "test2")))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .isShow(true)
            .isDeleted(false)
            .build();
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(event));
        // when + then
        assertThrows(EventException.class,
            () -> eventService.recoverEvent(1L));
    }

    @Test
    void 특정한_이벤트의_정보를_확인할_수_있다() {
        // given
        Event event = Event.builder()
            .id(1L)
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
            .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
            .tag(EventTag.ETC)
            .isOverNight(false)
            .state(WILL_UPDATE)
            .location("test")
            .genreAndKeyword(new ArrayList<>(List.of("test", "test2")))
            .detail(EventDetail.builder()
                .price(List.of(
                    Price.builder()
                        .option("성인")
                        .price(10000)
                        .build(),
                    Price.builder()
                        .option("청소년")
                        .price(5000)
                        .build()))
                .link("test")
                .description("test")
                .build())
            .isShow(true)
            .isDeleted(false)
            .build();
        when(eventRepository.existsById(1L)).thenReturn(true);
        when(eventRepository.findById(1L)).thenReturn(java.util.Optional.of(event));
        // when
        EventResponseDto response = eventService.getEvent(1L);
        // then
        assertEquals(response.id(), 1L);
    }

    @Test
    void 특정한_이벤트가_존재하지_않으면_예외를_반환한다() {
        // given
        when(eventRepository.existsById(1L)).thenReturn(false);
        // when + then
        assertThrows(EventException.class,
            () -> eventService.getEvent(1L));
    }

    @Test
    void 태그에_따라_이벤트_리스트를_반환한다() {
        // given
        List<Event> eventList = List.of(Event.builder()
                .id(1L)
                .title("test")
                .headerImage("test")
                .isLongTimeEvent(false)
                .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
                .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
                .tag(EventTag.EXHIBITION_AND_SALE)
                .isOverNight(false)
                .state(BEFORE_PROCEEDING)
                .location("test")
                .genreAndKeyword(List.of("test", "test2"))
                .detail(EventDetail.builder()
                    .price(List.of(
                        Price.builder()
                            .option("성인")
                            .price(10000)
                            .build(),
                        Price.builder()
                            .option("청소년")
                            .price(5000)
                            .build()))
                    .link("test")
                    .description("test")
                    .build())
                .build(),
            Event.builder()
                .id(2L)
                .title("test2")
                .headerImage("test2")
                .isLongTimeEvent(true)
                .startTime(LocalDateTime.parse("2021-01-04T00:00:00"))
                .endTime(LocalDateTime.parse("2021-01-08T00:00:00"))
                .tag(EventTag.EXHIBITION_AND_SALE)
                .isOverNight(false)
                .state(BEFORE_PROCEEDING)
                .location("test")
                .genreAndKeyword(List.of("test", "test2"))
                .detail(EventDetail.builder()
                    .price(List.of(
                        Price.builder()
                            .option("성인")
                            .price(10000)
                            .build(),
                        Price.builder()
                            .option("청소년")
                            .price(5000)
                            .build()))
                    .link("test")
                    .description("test")
                    .build())
                .build());
        when(eventRepository.existsByTagContainsAndIsDeletedFalseAndIsShowTrue(
            EventTag.EXHIBITION_AND_SALE)).thenReturn(true);
        when(eventRepository.findAllByTagContainsAndIsDeletedFalseAndIsShowTrue(
            EventTag.EXHIBITION_AND_SALE)).thenReturn(
            eventList);

        // when
        List<EventResponseDto> responseList =
            eventService.getEventListByTag(EventTag.EXHIBITION_AND_SALE);

        // then
        assertEquals(responseList.size(), 2);
        assertEquals(responseList.get(0).id(), 1L);
        assertEquals(responseList.get(1).tag(), EventTag.EXHIBITION_AND_SALE);
    }

    @Test
    void 태그에_따른_이벤트가_없다면_예외를_반환한다() {
        // given
        when(eventRepository.existsByTagContainsAndIsDeletedFalseAndIsShowTrue(
            EventTag.EXHIBITION_AND_SALE)).thenReturn(false);
        // when + then
        assertThrows(EventException.class,
            () -> eventService.getEventListByTag(EventTag.EXHIBITION_AND_SALE));
    }
}