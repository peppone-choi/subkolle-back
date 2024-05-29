package com.subkore.back.event.entity;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class EventDetail {

    @ElementCollection
    @CollectionTable(name = "event_price", joinColumns = @JoinColumn(name = "event_id"))
    private List<Price> price;
    private String link;
    private String description;
}
