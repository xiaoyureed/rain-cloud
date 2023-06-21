package io.github.xiaoyureed.raincloud.example.springboot;

import org.springframework.boot.SpringApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xiaoyureed@gmail.com
 */
@org.springframework.boot.autoconfigure.SpringBootApplication
@RestController
public class SpringBootApplication {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApplication.class, args);
    }

    @GetMapping({"/i"})
    public String index() {
        return "index";
    }
}
