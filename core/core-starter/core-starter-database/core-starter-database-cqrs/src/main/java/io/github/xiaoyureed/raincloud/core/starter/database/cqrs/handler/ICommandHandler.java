package io.github.xiaoyureed.raincloud.core.starter.database.cqrs.handler;

import io.github.xiaoyureed.raincloud.core.starter.database.cqrs.model.AbstractCommand;

/**
 * 类似 dao 层, 和数据库交互, 会注入 repository/mapper
 *
 * xiaoyureed@gmail.com
 */
public interface ICommandHandler<C extends AbstractCommand> {
    void handle(C command);
}
