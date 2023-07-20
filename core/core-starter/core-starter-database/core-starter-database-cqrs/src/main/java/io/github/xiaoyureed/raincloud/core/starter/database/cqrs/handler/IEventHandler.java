package io.github.xiaoyureed.raincloud.core.starter.database.cqrs.handler;

import io.github.xiaoyureed.raincloud.core.starter.database.cqrs.model.Event;

/**
 * xiaoyureed@gmail.com
 */
public interface IEventHandler<E extends Event> {
    void handle(E event);
}
