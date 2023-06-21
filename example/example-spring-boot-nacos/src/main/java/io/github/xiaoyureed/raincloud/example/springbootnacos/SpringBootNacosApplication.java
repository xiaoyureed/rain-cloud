package io.github.xiaoyureed.raincloud.example.springbootnacos;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.alibaba.nacos.spring.context.annotation.config.NacosPropertySource;
import com.alibaba.nacos.spring.context.annotation.discovery.EnableNacosDiscovery;

/**
 * xiaoyureed@gmail.com
 */
@SpringBootApplication
@EnableNacosDiscovery
@NacosPropertySource(dataId = "example.yml", autoRefreshed = true)
public class SpringBootNacosApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootNacosApplication.class, args);
    }
}
