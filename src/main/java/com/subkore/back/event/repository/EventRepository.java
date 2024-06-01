package com.subkore.back.event.repository;

import com.subkore.back.event.entity.Event;
import com.subkore.back.event.enumerate.EventState;
import com.subkore.back.event.enumerate.EventTag;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findAllByStateInAndIsDeletedFalseAndIsShowTrue(List<EventState> stateList);
    List<Event> findAllByIsDeletedFalse();
    List<Event> findAllByIsDeletedFalseAndIsShowTrue();
    List<Event> findAllByTagContainsAndIsDeletedFalseAndIsShowTrue(EventTag eventTag);
    boolean existsByTagContainsAndIsDeletedFalseAndIsShowTrue(EventTag eventTag);
    Event findEventByShortcutAndIsDeletedFalseAndIsShowTrue(String shortcut);
    boolean existsByShortcutAndIsDeletedFalseAndIsShowTrue(String shortcut);
}
