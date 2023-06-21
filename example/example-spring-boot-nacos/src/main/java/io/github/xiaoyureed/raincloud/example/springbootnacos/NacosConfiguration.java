package io.github.xiaoyureed.raincloud.example.springbootnacos;

import java.util.stream.IntStream;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.nacos.api.config.annotation.NacosValue;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
public class NacosConfiguration {
    @NacosValue(value = "aa", autoRefreshed = true)
    private String aa;

    @Bean
    public ApplicationRunner runner() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                IntStream.range(0, 50).forEach(v -> {
                    System.out.println("!!! aa = " + aa);

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
            }
        };
    }
}
