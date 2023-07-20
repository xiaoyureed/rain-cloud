package io.github.xiaoyureed.raincloud.example.springbootkafka.consumer;

import java.nio.charset.StandardCharsets;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Component
@Slf4j
public class HelloConsumer {

    {
        log.info("!!! hello consumer is in use.");
    }

    /**
     * 如果都采用默认配置, 可以这样写 (默认配置都是采用的 byteArray 序列化反序列化)
     */
//    @KafkaListener(topics = "topic-hello", groupId = "group-hello")
//    public void onMsg(ConsumerRecord<String, byte[]> record) {
//        System.out.println("enter consumer!!!");
//        log.info("!!! topic: {}, partition: {}, value: {}", record.topic(), record.partition(),
//            new String(record.value(), StandardCharsets.UTF_8));
//    }

    @KafkaListener(topics = "topic-hello", containerFactory = "simpleFactory")
    public void onStringMsg(String record) {
        log.debug("!!! enter string consumer");
        log.info("!!! value: {}", record);
    }
}
