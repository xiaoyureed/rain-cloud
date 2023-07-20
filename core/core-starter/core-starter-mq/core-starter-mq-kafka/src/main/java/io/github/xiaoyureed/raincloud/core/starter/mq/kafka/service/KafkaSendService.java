package io.github.xiaoyureed.raincloud.core.starter.mq.kafka.service;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

/**
 * xiaoyureed@gmail.com
 */
@Component
public class KafkaSendService<K, V> {
    @Autowired
    private KafkaTemplate<K, V> kafkaTemplate;

    /**
     * 发送消息
     */
    public ResultWrapper<K, V> send(String topic, V message, ProducerListener<K, V> producerListener, boolean async) {
        if (topic == null || topic.trim().length() == 0) {
            throw new IllegalArgumentException("Send message to kafka topic cannot be empty.");
        }

        if (message == null) {
            throw new IllegalArgumentException("Send message to kafka data cannot be empty.\"");
        }

        if (producerListener != null) {
            kafkaTemplate.setProducerListener(producerListener);
        }

        CompletableFuture<SendResult<K, V>> future = kafkaTemplate.send(topic, message);

        ResultWrapper<K, V> result = new ResultWrapper<>();

        if (async) {
            return result.setFuture(future);
        }

        SendResult<K, V> sendResult;
        try {
            sendResult = future.get();
        } catch (InterruptedException | ExecutionException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Send message to Kafka failed.", e);
        }

        result.setResult(sendResult);
        return result;
    }

    @Getter
    @Setter
    @Accessors
    public static class ResultWrapper<K, V> {
        private CompletableFuture<SendResult<K, V>> future;
        private SendResult<K, V> result;
    }

    /**
     * 异步发送
     */
    public CompletableFuture<SendResult<K, V>> sendAsync(String topic, V message, ProducerListener<K, V> producerListener) {
        return send(topic, message, producerListener, true).getFuture();
    }

    /**
     * 异步发送
     */
    public CompletableFuture<SendResult<K, V>> sendAsync(String topic, V message) {
        return send(topic, message, null, true).getFuture();
    }

    /**
     * 同步发送消息
     */
    public SendResult<K, V> sendSync(String topic, V message) {
        return send(topic, message, null, false).getResult();
    }

    /**
     * 同步发送数据
     */
    public SendResult<K, V> sendSync(String topic, V message, ProducerListener<K, V> producerListener) {
        return send(topic, message, producerListener, false).getResult();
    }
}
