package io.github.xiaoyureed.raincloud.core.starter.springdoc.swagger;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * xiaoyureed@gmail.com
 */
@ConfigurationProperties("raincloud.openapi")
@Data
public class OpenAPIProperties {
    private String version;
    private String url;
}
