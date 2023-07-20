package io.github.xiaoyureed.raincloud.core.starter.database.cqrs;

import java.util.List;

import io.github.xiaoyureed.raincloud.core.starter.database.cqrs.model.Event;

/**
 *  聚合根对象(就是业务对象)需要实现这个接口
 *
 * xiaoyureed@gmail.com
 */
public interface IAggregateRoot<E extends Event> {
    void apply(E event);
    List<Event> getUncommittedChanges();
    void markChangesAsCommitted();
}
