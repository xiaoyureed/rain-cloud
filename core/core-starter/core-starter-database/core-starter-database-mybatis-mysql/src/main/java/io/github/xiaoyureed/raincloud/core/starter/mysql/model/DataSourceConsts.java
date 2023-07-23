package io.github.xiaoyureed.raincloud.core.starter.mysql.model;

import com.baomidou.mybatisplus.annotation.DbType;

/**
 *
 * 对应于多数据源中不同数据源配置
 *
 * 通过在方法上，使用 {@link com.baomidou.dynamic.datasource.annotation.DS} 注解，设置使用的数据源。
 * 注意，默认是 {@link #MASTER} 数据源
 *
 * 对应官方文档为 http://dynamic-datasource.com/guide/customize/Annotation.html
 *
 * xiaoyureed@gmail.com
 */
public final class DataSourceConsts {
    /**
     * 主库，推荐使用 {@link com.baomidou.dynamic.datasource.annotation.Master} 注解
     */
    public static final String MASTER = "master";
    /**
     * 从库，推荐使用 {@link com.baomidou.dynamic.datasource.annotation.Slave} 注解
     */
    public static final String SLAVE = "slave";

    /**
     * 在 @{link {@link io.github.xiaoyureed.raincloud.core.starter.database.IdTypeEnvironmentPostProcessor} 中初始化
     */
    public static DbType DB_TYPE;
}
