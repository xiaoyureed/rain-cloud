package io.github.xiaoyureed.raincloud.core.starter.redis;

import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@ComponentScan
public class RedisStarterConfiguration {

    {
        log.debug("!!! redis starter ok");
    }
}
