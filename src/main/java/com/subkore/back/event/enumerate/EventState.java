package com.subkore.back.event.enumerate;

import lombok.Getter;

@Getter
public enum EventState {
    WILL_UPDATE("추가 예정"),
    BEFORE_PROCEEDING("진행 전"),
    PROCEEDING("진행중"),
    END("종료"),
    CANCEL("취소");

    private final String name;

    EventState(String name) {
        this.name = name;
    }

}
