package io.github.xiaoyureed.raincloud.core.starter.redis.cache;

import java.time.Duration;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.xiaoyureed.raincloud.core.starter.redis.cache.expire.ExpireRedisCacheWriter;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
@EnableCaching
public class CacheConfiguration extends CachingConfigurerSupport {

    @Value("${spring.application.name:unknown}")
    private String appName;

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * 注入RedisTemplate
     * tips：泛型可以适用于各种类型的注入
     */
    @Bean
    public <T> RedisTemplate<String, T> redisTemplate(RedisConnectionFactory factory, RedisSerializer<?> redisSerializer) {
        // 指定序列化方式
        RedisTemplate<String, T> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(RedisSerializer.string());
        redisTemplate.setHashKeySerializer(RedisSerializer.string());
        redisTemplate.setValueSerializer(redisSerializer);
        redisTemplate.setHashValueSerializer(redisSerializer);
        redisTemplate.setConnectionFactory(factory);
        return redisTemplate;
    }

    /**
     * 注入RedisCacheManager
     */
    @Bean
    public CacheManager cacheManager(RedisConnectionFactory factory, RedisSerializer<Object> redisSerializer) {

        org.springframework.data.redis.cache.RedisCacheConfiguration config = org.springframework.data.redis.cache.RedisCacheConfiguration
            // 注意默认配置允许缓存null
            .defaultCacheConfig()
            // 接口缓存上指定了key的时候统一加服务名前缀
            .computePrefixWith(cacheName -> appName + ":" + cacheName + ":")
            // 可以根据业务需要设置统一过期时间，这里是为了强制使用@CacheExpire手动设置过期时间所以设置很短
            .entryTtl(Duration.ofMinutes(1))
            // 配置序列化和反序列化方式
            .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(RedisSerializer.string()))
            .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer));
        return RedisCacheManager.builder(new ExpireRedisCacheWriter(factory)).cacheDefaults(config).build();
    }

    /**
     * 使用@Cachable生成的key的默认规则
     * 如果手动指定了key则不生效
     * <p>
     * 格式：入参1,入参2
     * 示例：1,2
     * 加上前缀后完整key格式：order_service:order:1,2
     * <p>
     * 建议使用@Cacheable的时候都指定key
     */
    @Bean
    @Override
    public KeyGenerator keyGenerator() {
        return (target, method, params) -> Stream.of(params).map(String::valueOf).collect(Collectors.joining(","));
    }

    /**
     * 初始化Redis序列器，使用jackson
     */
    @Bean
    public RedisSerializer<Object> redisSerializer() {
        return new Jackson2JsonRedisSerializer<>(objectMapper, Object.class);
    }

}
