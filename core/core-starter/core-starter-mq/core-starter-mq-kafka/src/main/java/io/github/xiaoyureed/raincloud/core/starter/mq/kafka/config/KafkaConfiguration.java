package io.github.xiaoyureed.raincloud.core.starter.mq.kafka.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.config.KafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.listener.CommonErrorHandler;
import org.springframework.kafka.listener.ConsumerAwareListenerErrorHandler;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.listener.ListenerExecutionFailedException;
import org.springframework.messaging.Message;
import org.springframework.util.backoff.BackOff;
import org.springframework.util.backoff.FixedBackOff;

/**
 * xiaoyureed@gmail.com
 * https://www.cnblogs.com/lxw180/articles/14870898.html
 * https://github.com/pg-liudong/kafka-spring-boot-starter/tree/main 多数据源配置
 */
@Configuration
public class KafkaConfiguration {

    @Value("${spring.kafka.bootstrap-servers}")
    private String bootstrapServers;

    @Value("${spring.kafka.consumer.enable-auto-commit}")
    private Boolean autoCommit;

    @Value("${spring.kafka.consumer.auto-commit-interval}")
    private Integer autoCommitInterval;

    @Value("${spring.kafka.consumer.properties.group.id}")
    private String groupId;

    @Value("${spring.kafka.consumer.max-poll-records}")
    private Integer maxPollRecords;

    @Value("${spring.kafka.consumer.auto-offset-reset}")
    private String autoOffsetReset;

    @Value("${spring.kafka.producer.retries}")
    private Integer retries;

    @Value("${spring.kafka.producer.batch-size}")
    private Integer batchSize;

    @Value("${spring.kafka.producer.buffer-memory}")
    private Integer bufferMemory;

    /**
     * 生产者配置信息
     */
    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ProducerConfig.ACKS_CONFIG, "0");
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.RETRIES_CONFIG, retries);
        props.put(ProducerConfig.BATCH_SIZE_CONFIG, batchSize);
        props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
        props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, bufferMemory);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        return props;
    }

    /**
     * 生产者工厂
     */
    @Bean
    public ProducerFactory<String, Object> producerFactory() {
        return new DefaultKafkaProducerFactory<>(producerConfigs());
    }

    /**
     * 生产者模板
     */
    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        return new KafkaTemplate<>(producerFactory());
        // or
//        return new KafkaTemplate<>(producerFactory(), props);
    }

    /**
     * 自动确认消费者配置信息
     */
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, autoCommit);
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 120000);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 180000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    /**
     * 简单消费者工厂
     */
    @Bean("simpleFactory")
    public KafkaListenerContainerFactory<?> simpleFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
        factory.setBatchListener(false);//设置关闭批量消费（默认就是关闭）

        // 如果要使用@SendTo转发消息，需要配置replyTemplate
        // (在消费者Listener上添加SendTo注解，可以将消息转发到SendTo指定的Topic中, 方法return 值就是要转发的消息)
        factory.setReplyTemplate(kafkaTemplate());

        return factory;
    }

    /**
     * 批量消费者工厂
     * (在 listener 中接收时就是 List<xxx> msgs)
     */
    @Bean("batchFactory")
    public KafkaListenerContainerFactory<?> batchFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
        //设置为批量消费，每个批次数量在Kafka配置参数中设置ConsumerConfig.MAX_POLL_RECORDS_CONFIG
        factory.setBatchListener(true);
        return factory;
    }

    //////// 手动确认消费相关配置 ////////////

    /**
     * 手动确认消费者配置信息
     */
    @Bean
    public Map<String, Object> manualConsumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.GROUP_ID_CONFIG, groupId);
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, autoOffsetReset);
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, maxPollRecords);
        props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, false); //在这里配置，关闭自动提交
        props.put(ConsumerConfig.SESSION_TIMEOUT_MS_CONFIG, 120000);
        props.put(ConsumerConfig.REQUEST_TIMEOUT_MS_CONFIG, 180000);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        return props;
    }

    /**
     * 手动确认消费者工厂
     * <p>
     * 消费监听里需要加方法参数 Acknowledgment ack, 通过 ack.acknowledge() 控制手动提交
     */
    @Bean("manualFactory")
    public KafkaListenerContainerFactory<?> manualFactory() {
        ConcurrentKafkaListenerContainerFactory<Integer, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(manualConsumerConfigs()));
        factory.setBatchListener(false);
        //设置确认模式
        //RECORD  每处理一条commit一次
        //BATCH(默认) 每次poll的时候批量提交一次，频率取决于每次poll的调用频率
        //TIME  每次间隔ackTime的时间去commit
        //COUNT 累积达到ackCount次的ack去commit
        //COUNT_TIME ackTime或ackCount哪个条件先满足，就commit
        //MANUAL listener负责ack，但是背后也是批量上去
        //MANUAL_IMMEDIATE listner负责ack，每调用一次，就立即commit
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        return factory;
    }

    //////// 手动确认消费相关配置 ////////////


    /////////// 事务消息 /////////////////

    // 生产者配置里添加 props.put(ProducerConfig.TRANSACTIONAL_ID_CONFIG, "tx");
    /*
    * //发送事务消息 (可以用kafkaTemplate.executeInTransaction或者使用@Transactional注解)
@GetMapping("/kafka/transaction/{message}")
public void sendTransactionMessage(@PathVariable("message") String transactionMessage) {
    // 声明事务：后面报错消息不会发出去
    kafkaTemplate.executeInTransaction(operations -> {
        operations.send("testTopic",transactionMessage);
        operations.flush();
        //throw new RuntimeException("fail");
        return "commit";
    });
}
    * */

    /////////// 事务消息 /////////////////

    ///////// 异常处理 ////////////

    /**
     * 需要在 在listener上配置errorHandler属性 (errorHandler = "consumerAwareErrorHandler")
     */
    @Bean
    public ConsumerAwareListenerErrorHandler consumerAwareListenerErrorHandler() {
        return new ConsumerAwareListenerErrorHandler() {
            @Override
            public Object handleError(Message<?> message, ListenerExecutionFailedException e, Consumer<?, ?> consumer) {
                System.out.println("consumer error: " + message.getPayload());
                return "exception";
            }
        };
    }

    ///////// 异常处理 ////////////

    //////// 消费重试 ////////

    @Bean
    public CommonErrorHandler kafkaErrorHandler() {
        ConsumerRecordRecoverer recoverer = new DeadLetterPublishingRecoverer(kafkaTemplate());
        // 间隔3秒，重试2次，共三次
        BackOff backOff = new FixedBackOff(3000, 2);
        return new DefaultErrorHandler(recoverer, backOff);
    }

    /**
     *
     * 需要消费 listener 中配置containerFactory, 方法中抛出异常后会触发重试逻辑
     * 超出重试次数后，消息会被发往死信队列(topicName + .DLT)，可以根据业务需要做进一步处理。
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, String> retryKafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, String> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL_IMMEDIATE); // 手动提交
        factory.setCommonErrorHandler(kafkaErrorHandler());
        return factory;
    }

    //////// 消费重试 ////////

    /////// 消息过滤器 (在消息抵达consumer之前被拦截, 筛选出需要的信息再交由KafkaListener处理) ////////

    @Autowired
    ConsumerFactory consumerFactory;

    /**
     * 在消费者 listener 中配置 containerFactory = "filterContainerFactory"
     */
    @Bean
    public ConcurrentKafkaListenerContainerFactory filterContainerFactory() {
        ConcurrentKafkaListenerContainerFactory factory = new ConcurrentKafkaListenerContainerFactory();
        factory.setConsumerFactory(consumerFactory);
        //or
//        factory.setConsumerFactory(new DefaultKafkaConsumerFactory<>(consumerConfigs()));

        // 被过滤的消息将被丢弃
        factory.setAckDiscarded(true);
        // 消息过滤策略
        factory.setRecordFilterStrategy(consumerRecord -> {
            if (Integer.parseInt(consumerRecord.value().toString()) % 2 == 0) {
                return false;
            }
            //返回true消息则被过滤
            return true;
        });
        return factory;
    }

    /////// 消息过滤器 ////////

}
