package io.github.xiaoyureed.raincloud.service.biz.hello.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * xiaoyureed@gmail.com
 */
@FeignClient(name = "service-biz-account")
public interface AccountFeignClient {

    @GetMapping("/index")
    String index();
}
