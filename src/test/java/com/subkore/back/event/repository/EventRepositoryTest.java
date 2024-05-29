package com.subkore.back.event.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.subkore.back.event.entity.Event;
import com.subkore.back.event.entity.EventDetail;
import com.subkore.back.event.entity.Price;
import com.subkore.back.event.enumerate.EventState;
import com.subkore.back.event.enumerate.EventTag;
import java.time.LocalDateTime;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@ExtendWith(SpringExtension.class)
@DataJpaTest
@Transactional
@ActiveProfiles("test")
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void 저장_테스트() {
        // given
        Event event = Event.builder()
            .title("test")
            .headerImage("test")
            .isLongTimeEvent(false)
            .startTime(LocalDateTime.parse("2021-01-01T00:00:00"))
            .endTime(LocalDateTime.parse("2021-01-02T00:00:00"))
            .tag(EventTag.ETC)
            .isOverNight(false)
            .state(EventState.WILL_UPDATE)
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
        final Event savedEvent = eventRepository.save(event);
        // then
        assertEquals(event, savedEvent);
    }
}