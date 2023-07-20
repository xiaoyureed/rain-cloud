package io.github.xiaoyureed.raincloud.core.starter.mq.kafka.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

import io.github.xiaoyureed.raincloud.core.starter.mq.kafka.properties.KafkaCustomizedProperties;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
@EnableConfigurationProperties({
    KafkaCustomizedProperties.class
})
@Slf4j
public class TopicInitConfiguration {
    @Autowired
    private KafkaCustomizedProperties kafkaCustomizedProperties;

    @Autowired
    private DefaultListableBeanFactory defaultListableBeanFactory;

    @PostConstruct
    public void init() {
        initTopics();
    }

    private void initTopics() {

        kafkaCustomizedProperties.getTopics().forEach(topic -> {
            defaultListableBeanFactory.registerSingleton(topic.getName(),
                TopicBuilder.name(topic.getName())
                    .partitions(topic.getPartitions())
                    .replicas(topic.getReplicas())
                    .build()
            );

            log.debug("!!! kafka topic [{}] registered", topic.getName());
        });
    }
}
