package io.github.xiaoyureed.raincloud.core.starter.mq.kafka.config;

import java.util.Map;

import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 * 需要配置 spring.kafka.producer.properties.partitioner.class: 本类名
 */
@Slf4j
public class CustomizedPartitionConfiguration implements Partitioner {
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {
        // 自定义分区规则(这里假设全部发到0号分区)
        log.info("自定义分区策略 topic:{} key:{} value:{}", topic, key, value.toString());
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> map) {

    }
}
