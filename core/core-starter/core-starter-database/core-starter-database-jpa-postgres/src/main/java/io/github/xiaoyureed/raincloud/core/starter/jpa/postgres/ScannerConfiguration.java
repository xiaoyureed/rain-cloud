package io.github.xiaoyureed.raincloud.core.starter.jpa.postgres;

import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@ComponentScan
public class ScannerConfiguration {
    {
        log.info("!!! database starter ok");
    }
}
