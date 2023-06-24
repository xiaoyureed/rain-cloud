package io.github.xiaoyureed.raincloud.core.starter.extension.webstatic;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@ComponentScan
//@Configuration
@EnableConfigurationProperties({WebStaticProperties.class})
@Slf4j
public class WebStaticStarterConfiguration {
    {
        log.debug("!!! web static starter ok");
    }
}
