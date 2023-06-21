package io.github.xiaoyureed.raincloud.core.starter.webstatic;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * xiaoyureed@gmail.com
 */
@Data
@ConfigurationProperties("core.starter.webstatic")
public class WebStaticProperties {
    private Boolean enabled = true;
}
