package io.github.xiaoyureed.raincloud.example.springbootredis;

import java.util.concurrent.TimeUnit;

import org.springframework.aop.framework.AopContext;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * xiaoyureed@gmail.com
 */
@RestController
@RequestMapping("index")
//@CacheConfig(keyGenerator = "keyGenerator", cacheManager = "cacheManager", cacheNames = "index") // 通过 实现 CachingConfigurer 配置好全局后, 这个可省略
public class indexController {
    @GetMapping("/{index}")
    public String index(@PathVariable("index") String index) {
//        String result = calc(index);
        String result = ((indexController) AopContext.currentProxy()).calc(index);
        return result;
    }

    @Cacheable(cacheNames = "index")
    public String calc(String index) {
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        return index;
    }
}
