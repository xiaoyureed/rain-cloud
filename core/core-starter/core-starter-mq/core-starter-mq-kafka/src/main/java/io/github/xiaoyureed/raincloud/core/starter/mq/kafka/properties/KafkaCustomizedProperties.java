package io.github.xiaoyureed.raincloud.core.starter.mq.kafka.properties;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

/**
 * xiaoyureed@gmail.com
 */
@Data
@ConfigurationProperties(prefix = "raincloud.kafka")
public class KafkaCustomizedProperties {

    private String bootstrapServers;

    private List<Topic> topics;

    /**
     * kafka 类型, 默认作为消费者使用
     */
    private Type consumerOrProducer = Type.consumer;

    @Data
    public static class Topic {
        private String name;
        private Integer partitions = 1;
        private Integer replicas = 1;
    }

    enum Type {
        consumer,
        producer,
    }
}
