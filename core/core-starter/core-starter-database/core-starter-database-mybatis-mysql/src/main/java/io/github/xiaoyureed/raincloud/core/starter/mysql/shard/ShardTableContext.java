package io.github.xiaoyureed.raincloud.core.starter.mysql.shard;

import java.util.Optional;

import lombok.Getter;
import lombok.Setter;

/**
 */
public class ShardTableContext {

    private ShardTableContext(){}

    private static final ThreadLocal<ShardTable> SHARD_TABLE_THREAD_LOCAL = new ThreadLocal<>();

    public static void set(Class<?> tableClass, Object id) {
        ShardTable shardTable = new ShardTable();
        shardTable.setTableClass(tableClass);
        shardTable.setId(id);
        SHARD_TABLE_THREAD_LOCAL.set(shardTable);
    }

    public static ShardTable get() {
        ShardTable shardTable = SHARD_TABLE_THREAD_LOCAL.get();
        SHARD_TABLE_THREAD_LOCAL.remove();
        return shardTable;
    }

    public static Optional<ShardTable> getOptional() {
        ShardTable shardTable = SHARD_TABLE_THREAD_LOCAL.get();
        SHARD_TABLE_THREAD_LOCAL.remove();
        return Optional.ofNullable(shardTable);
    }


    @Getter
    @Setter
    public static class ShardTable {
        private Class<?> tableClass;
        private Object id;
    }
}
