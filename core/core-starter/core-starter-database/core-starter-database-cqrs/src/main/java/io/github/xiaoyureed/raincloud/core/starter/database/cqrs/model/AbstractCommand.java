package io.github.xiaoyureed.raincloud.core.starter.database.cqrs.model;

import java.util.Map;

import lombok.Data;

/**
 * 不同动作会新建不同的 command 类 (如: CreateOrderCommand)
 *
 * xiaoyureed@gmail.com
 */
@Data
public abstract class AbstractCommand {
    private String id;
    private String type;
    private Map<String, Object> payload;
}
