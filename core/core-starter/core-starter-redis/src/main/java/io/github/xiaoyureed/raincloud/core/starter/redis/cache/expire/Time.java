package io.github.xiaoyureed.raincloud.core.starter.redis.cache.expire;

/**
 * 常用时间段（对应秒数）
 */
public final class Time {

    private Time() {}

    public final int SECOND = 1;

    public final int MINUTE = 60;
    public final int HOUR = 60 * MINUTE;
    public final int DAY = 24 * HOUR;
    public final int MONTH = 30 * DAY;
    public final int YEAR = 12 * MONTH;
    /**
     * forever = 50 years
     */
    public final int FOREVER = 50 * YEAR;


}