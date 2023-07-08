package io.github.xiaoyureed.raincloud.service.support.gateway.properties;

import java.io.Serializable;
import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Getter;
import lombok.Setter;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
@ConfigurationProperties(prefix = "raincloud.gateway")
@Setter
@Getter
public class GatewayAuthProperties {
    private List<String> whiteList;

    private Auth auth = new Auth();

    private Trace trace = new Trace();

    @Getter
    @Setter
    public static class Trace implements Serializable {
        /**
         * Trace key 在Redis中有效时间，单位秒, 默认5分钟
         */
        private long expired = 60 * 5;

        /**
         * refresh threshold
         *
         * Trace key 在Redis中有效时间还是剩余多长时间，就需要进行更新，单位秒, 默认1分钟
         * 即，如果Trace key在Redis中过期时间小于60秒，那么就重新创建Trace key
         */
        private long threshold = 60;
    }

    @Getter
    @Setter
    public static class Auth {
        /**
         * If enable the gateway authorization
         */
        private boolean enabled = true;
    }
}
