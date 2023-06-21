package io.github.xiaoyureed.raincloud.core.starter.database;

import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@ComponentScan
public class DatabaseStarterConfiguration {
    {
        log.debug("!!! database starter ok");
    }
}
