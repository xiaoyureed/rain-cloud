package io.github.xiaoyureed.raincloud.core.starter.extension.email;

import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@ComponentScan
public class EmailStarterConfiguration {
    {
        log.debug("!!! email starter is in use");
    }
}
