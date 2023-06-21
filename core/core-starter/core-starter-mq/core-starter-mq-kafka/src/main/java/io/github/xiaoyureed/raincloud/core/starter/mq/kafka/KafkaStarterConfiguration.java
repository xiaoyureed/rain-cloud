package io.github.xiaoyureed.raincloud.core.starter.mq.kafka;

import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@ComponentScan
public class KafkaStarterConfiguration {
    {
        log.debug("!!! kafka starter ok");
    }
}
