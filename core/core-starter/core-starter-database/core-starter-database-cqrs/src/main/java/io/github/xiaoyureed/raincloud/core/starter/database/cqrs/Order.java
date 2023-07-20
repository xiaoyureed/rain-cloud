package io.github.xiaoyureed.raincloud.core.starter.database.cqrs;

import java.util.ArrayList;
import java.util.List;

import io.github.xiaoyureed.raincloud.core.starter.database.cqrs.model.Event;

/**
 * xiaoyureed@gmail.com
 */
public class Order implements IAggregateRoot<Event> {
    private String id;
    private String status;
    private final List<Event> uncommittedChanges = new ArrayList<>();

    @Override
    public void apply(Event event) {
        // 根据事件更新聚合根对象的状态
    }

    @Override
    public List<Event> getUncommittedChanges() {
        return uncommittedChanges;
    }

    @Override
    public void markChangesAsCommitted() {
        uncommittedChanges.clear();
    }
}