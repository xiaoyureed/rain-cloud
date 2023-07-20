package io.github.xiaoyureed.raincloud.core.starter.database.cqrs.model;

import java.util.Map;

import lombok.Data;

/**
 * xiaoyureed@gmail.com
 */
@Data
public class Event {
    private String id;
    private String type;
    private Map<String, Object> payload;
}
