package io.github.xiaoyureed.raincloud.core.starter.mysql.shard;

import java.util.List;

/**
 */
public interface TableShardStrategy {

    /**
     * 通过主键id 和 原始表命进行分表
     * @param tableName 表名
     * @param id 主键id
     * @return 目标表名
     */
    String getTableShardName(Class<?> tableClass, String tableName, Object id);


    List<String> listTableShardName(Class<?> tableClass, String tableName);
}
