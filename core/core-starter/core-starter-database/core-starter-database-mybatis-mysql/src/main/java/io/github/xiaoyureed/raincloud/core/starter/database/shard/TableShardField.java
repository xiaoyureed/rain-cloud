package io.github.xiaoyureed.raincloud.core.starter.database.shard;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
public @interface TableShardField {
}
