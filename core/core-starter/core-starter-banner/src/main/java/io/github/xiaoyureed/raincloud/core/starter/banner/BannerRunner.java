package io.github.xiaoyureed.raincloud.core.starter.banner;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * xiaoyureed@gmail.com
 */
//@Component
public class BannerRunner implements ApplicationRunner {
    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("banner runner...");
    }
}
