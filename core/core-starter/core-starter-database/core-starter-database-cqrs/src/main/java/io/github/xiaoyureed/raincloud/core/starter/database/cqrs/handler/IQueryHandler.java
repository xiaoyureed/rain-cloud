package io.github.xiaoyureed.raincloud.core.starter.database.cqrs.handler;

import io.github.xiaoyureed.raincloud.core.starter.database.cqrs.model.AbstractQuery;

/**
 * xiaoyureed@gmail.com
 */
public interface IQueryHandler<Q extends AbstractQuery, R> {
    R handle(Q query);
}
