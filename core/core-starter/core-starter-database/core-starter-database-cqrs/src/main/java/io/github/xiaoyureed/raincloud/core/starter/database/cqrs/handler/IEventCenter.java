package io.github.xiaoyureed.raincloud.core.starter.database.cqrs.handler;

import java.util.List;

import io.github.xiaoyureed.raincloud.core.starter.database.cqrs.model.Event;

/**
 * xiaoyureed@gmail.com
 */
public interface IEventCenter {
    /**
     *
     * save event into db
     */
    void save(Event event);

    /**
     * query event from db
     */
    List<Event> getEventById(String id);
}
