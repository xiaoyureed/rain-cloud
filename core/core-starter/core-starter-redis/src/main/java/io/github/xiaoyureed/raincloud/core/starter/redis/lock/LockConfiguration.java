package io.github.xiaoyureed.raincloud.core.starter.redis.lock;

import org.redisson.api.RedissonClient;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.xiaoyureed.raincloud.core.starter.redis.lock.locker.Locker;
import io.github.xiaoyureed.raincloud.core.starter.redis.lock.locker.RedissonLocker;

/**
 * xiaoyureed@gmail.com
 */
@Configuration(proxyBeanMethods = false) // this means the instance get from container is always new one
@EnableConfigurationProperties({
    LockProperties.class
})
public class LockConfiguration {
    /**
     * 注入分布式锁，业务框架有需要也可以自己实现
     */
    @Bean
    @ConditionalOnMissingBean(Locker.class)
    public Locker locker(LockProperties lockProperties, RedissonClient redissonClient) {
        return new RedissonLocker(lockProperties.getType(), redissonClient);
    }
}
