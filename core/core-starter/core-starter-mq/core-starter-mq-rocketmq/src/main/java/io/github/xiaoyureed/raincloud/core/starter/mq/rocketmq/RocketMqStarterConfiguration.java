package io.github.xiaoyureed.raincloud.core.starter.mq.rocketmq;

import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@ComponentScan
public class RocketMqStarterConfiguration {
    {
        log.debug("!!! rocketmq starter ok");
    }
}
