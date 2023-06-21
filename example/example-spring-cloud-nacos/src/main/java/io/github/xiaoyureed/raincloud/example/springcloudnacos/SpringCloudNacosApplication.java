package io.github.xiaoyureed.raincloud.example.springcloudnacos;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xiaoyureed@gmail.com
 */
@SpringBootApplication
@RestController
public class SpringCloudNacosApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringCloudNacosApplication.class, args);
    }

    @Autowired
    private NacosConfiguration nacosConfiguration;

    @GetMapping("/aa")
    public String aa() {
        return nacosConfiguration.getAa();
    }
}
