package io.github.xiaoyureed.raincloud.core.starter.mq.kafka;

import org.springframework.context.annotation.ComponentScan;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@ComponentScan
public class KafkaStarterConfiguration {
    {
        log.debug("!!! kafka starter ok");
    }



//    @Bean
//    @ConditionalOnProperty(prefix = "raincloud.kafka", name = "consumer-or-producer", value = "producer")
//    public KafkaProducer<String, byte[]> byteArrayProducer(Properties kafkaProperties) {
//        return new KafkaProducer<>(kafkaProperties, new StringSerializer(), new ByteArraySerializer());
//    }
//
//    @Bean
//    public Properties kafkaProperties() {
//        Properties result = new Properties();
//        result.setProperty("bootstrap.servers", kafkaProperties.getBootstrapServers());
////        result.put("acks", "all");
//
//        return result;
//    }

}
