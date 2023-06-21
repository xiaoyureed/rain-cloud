package io.github.xiaoyureed.raincloud.example.springcloudnacos;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
@RefreshScope // auto refresh
@Data
public class NacosConfiguration {

    @Value("${aa:defaultValue}")
    private String aa;
}
