package io.github.xiaoyureed.raincloud.example.springbootkafka.controller;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import io.github.xiaoyureed.raincloud.core.starter.common.util.BeanUtils;
import io.github.xiaoyureed.raincloud.core.starter.mq.kafka.service.KafkaSendService;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@RestController
@Slf4j
public class HelloController {

//    @Autowired
//    private KafkaSendService<String, byte[]> kafkaSendService;
//
//    @GetMapping("{topic}/{msg}")
//    public void send(@PathVariable("msg") String msg, @PathVariable("topic") String topic) {
//        log.info("!!! send {} to topic: {}", msg, topic);
//        kafkaSendService.sendSync(topic, msg.getBytes(StandardCharsets.UTF_8));
//    }

    @Getter
    @Setter
    @Accessors
    public static class HelloMesage {
        private String id;
        private String content;
    }

    @Autowired
    private KafkaSendService<String, String> sender;

    @GetMapping("{topic}/{msg}/1")
    public void send1(@PathVariable("msg") String msg, @PathVariable("topic") String topic) {
        log.info("!!! send1 {} to topic: {}", msg, topic);
        sender.sendSync(topic, BeanUtils.toJson(new HelloMesage().setContent(msg)));
    }


    //发送带回调的消息
    @GetMapping("/kafka/callback/{topic}/{message}")
    public void sendCallbackMessage(@PathVariable("message") String msg, @PathVariable("topic") String topic) {
        sender.sendSync(topic, msg, new ProducerListener<String, String>() {
            @Override
            public void onSuccess(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata) {
                ProducerListener.super.onSuccess(producerRecord, recordMetadata);
            }

            @Override
            public void onError(ProducerRecord<String, String> producerRecord, RecordMetadata recordMetadata, Exception exception) {
                ProducerListener.super.onError(producerRecord, recordMetadata, exception);
            }
        });
    }
}
