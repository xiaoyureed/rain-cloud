package io.github.xiaoyureed.raincloud.service.support.gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

/**
 * xiaoyureed@gmail.com
 */
@RestController
public class indexController {

    @GetMapping("hello")
    public Mono<String> hello() {
        return Mono.just("hello from gateway");
    }
}
