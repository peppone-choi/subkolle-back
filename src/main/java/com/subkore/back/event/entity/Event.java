package com.subkore.back.event.entity;

import com.subkore.back.event.enumerate.EventState;
import com.subkore.back.event.enumerate.EventTag;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String headerImage;
    private Boolean isLongTimeEvent;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @Enumerated(value = EnumType.STRING)
    private EventTag tag;
    private Boolean isOverNight;
    @Enumerated(value = EnumType.STRING)
    @Default
    private EventState state = EventState.WILL_UPDATE;
    private String location;
    @ElementCollection
    @CollectionTable(name = "event_genre_and_keyword")
    @Default
    private List<String> genreAndKeyword = new ArrayList<>();
    @Embedded
    private EventDetail detail;
    @Default
    private Boolean isDeleted = false;
}