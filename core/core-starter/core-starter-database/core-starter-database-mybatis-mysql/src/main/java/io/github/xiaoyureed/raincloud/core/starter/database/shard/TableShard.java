package io.github.xiaoyureed.raincloud.core.starter.database.shard;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 */
@Target({TYPE})
@Retention(RUNTIME)
public @interface TableShard {
    Class<? extends TableShardStrategy> shardStrategy();

    int hashShard() default 0;
}
