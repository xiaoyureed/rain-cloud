package io.github.xiaoyureed.raincloud.core.starter.mysql.shard;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

/**
 */
public class ShardLambdaQueryWrapper<T> extends LambdaQueryWrapper<T> {

    /**
     * 分表查询
     */
    public ShardLambdaQueryWrapper<T> eqShardId(Class<T> t, SFunction<T, ?> column, Object val) {
        super.eq(column, val);
        ShardTableContext.set(t, val);
        return this;
    }
}
