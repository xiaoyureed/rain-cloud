package io.github.xiaoyureed.raincloud.example.springbootkafka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.env.OriginTrackedMapPropertySource;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@SpringBootApplication
@Slf4j
public class SpringBootKafkaApplication {
    public static void main(String[] args) {
        ConfigurableApplicationContext run = SpringApplication.run(SpringBootKafkaApplication.class, args);

        ConfigurableEnvironment environment = run.getEnvironment();
        environment.getPropertySources().stream().filter(s -> s instanceof OriginTrackedMapPropertySource)
            .map(raw -> (OriginTrackedMapPropertySource) raw)
            .findFirst()
            .ifPresent(source -> {
                log.info("!!! source");
                for (String name : source.getPropertyNames()) {
                    Object value = source.getProperty(name);
                    log.info("!!! {} = {}", name, value);
                }
            });

        log.info("!!! {}", environment.getProperty("spring.kafka.producer.valueSerializer"));
    }
}
