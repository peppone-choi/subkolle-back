package com.subkore.back.event.enumerate;

import lombok.Getter;

@Getter
public enum EventTag {
    EXHIBITION_AND_SALE("전시 / 판매"),
    DJING("디제잉"),
    LIVE("라이브"),
    COLLABORATION("콜라보"),
    POPUP_STORE("팝업스토어"),
    ONLINE("온라인"),
    ETC("기타");
    private final String name;

    EventTag(String name) {
        this.name = name;
    }

}
