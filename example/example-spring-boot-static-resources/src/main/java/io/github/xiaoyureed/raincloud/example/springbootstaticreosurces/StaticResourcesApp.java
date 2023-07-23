package io.github.xiaoyureed.raincloud.example.springbootstaticreosurces;

import java.nio.file.FileSystem;
import java.nio.file.FileSystems;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * 默认静态资源目录
 *     /static
 *     /public
 *     /resources
 *     /META-INF/resources
 * 动态资源目录：/templates
 *
 *
 * xiaoyureed@gmail.com
 */
@SpringBootApplication
public class StaticResourcesApp {
    public static void main(String[] args) {
        SpringApplication.run(StaticResourcesApp.class, args);
    }

    @Configuration
    public static class WebMvcConfiguration implements WebMvcConfigurer {
        @Override
        public void addResourceHandlers(ResourceHandlerRegistry registry) {
            // 映射类路径, 亦可  addResourceLocations("classpath:/xxx")
//            registry.addResourceHandler("/webjars/**").addResourceLocations(new ClassPathResource("/META-INF/resources/webjars/"));
            // 映射文件系统路径, 亦可 "file:/xxx"
//            registry.addResourceHandler("/file/**").addResourceLocations(new FileSystemResource("/Users/xiaoyu/Downloads/"));
        }
    }
}
