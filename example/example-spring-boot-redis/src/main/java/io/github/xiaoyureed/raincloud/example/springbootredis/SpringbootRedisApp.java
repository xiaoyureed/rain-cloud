package io.github.xiaoyureed.raincloud.example.springbootredis;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * xiaoyureed@gmail.com
 * @Cacheable ：触发将数据保存到缓存的操作；
 * @CacheEvict : 触发将数据从缓存删除的操作;(清除模式)
 * @CachePut ：不影响方法执行更新缓存；(双写模式:在修改后返回要存在缓存的数据,再次更新缓存)
 * @Cacheing：组合以上多个操作；(清除多个缓存)
 * @CacheConfig：在类级别共享缓存的相同配置；
 */
@SpringBootApplication
public class SpringbootRedisApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringbootRedisApp.class, args);
    }
}
