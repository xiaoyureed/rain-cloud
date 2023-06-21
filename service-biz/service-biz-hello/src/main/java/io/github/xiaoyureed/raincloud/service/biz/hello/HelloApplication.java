package io.github.xiaoyureed.raincloud.service.biz.hello;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * xiaoyureed@gmail.com
 */
@SpringBootApplication
@EnableDiscoveryClient
public class HelloApplication {
    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

}
