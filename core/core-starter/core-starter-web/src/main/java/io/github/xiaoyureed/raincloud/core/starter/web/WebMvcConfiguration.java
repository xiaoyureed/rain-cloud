package io.github.xiaoyureed.raincloud.core.starter.web;

import java.nio.charset.StandardCharsets;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.fasterxml.jackson.databind.ObjectMapper;

import io.github.xiaoyureed.raincloud.core.common.util.ListUtil;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
public class WebMvcConfiguration implements WebMvcConfigurer {

    @Autowired
    private ObjectMapper objectMapper;

    /**
     * swagger 相关的静态资源配置
     * 可选, xxx-xxx-ui 依赖里包含了这部分配置了
     */
//    @Override
//    public void addResourceHandlers(ResourceHandlerRegistry registry) {
//        registry.addResourceHandler("swagger-ui.html")
//            .addResourceLocations("classpath:/META-INF/resources/");
//
//        registry.addResourceHandler("/webjars/**")
//            .addResourceLocations("classpath:/META-INF/resources/webjars/");
//    }

    /**
     * cors configuration
     * 必须配置, 否则 gateway 的 swagger 会在页面报 cors 异常
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
            .maxAge(18000L)          // 预检请求的缓存时间（秒），即在这个时间段里，对于相同的跨域请求不会再预检了
            .allowedOrigins("*")                 // 设置允许跨域请求的域名
//            .allowedOriginPatterns("*")    // 同上
            .allowedHeaders("*")           // 设置允许的header
            .allowedMethods("*");       //// 设置允许的请求方式
//            .allowCredentials(true);  // 是否允许cookie跨域

    }

    /**
     * 1. 执行时机不同:
     * - extendMessageConverters()是在Spring Boot自动配置的HttpMessageConverters初始化后执行。
     * - configureMessageConverters()是在Spring Boot自动配置的HttpMessageConverters初始化前执行。
     * 2. 配置方式不同:
     * - extendMessageConverters()是追加或者修改默认HttpMessageConverters。
     * - configureMessageConverters()是完全自定义HttpMessageConverters,会覆盖默认的配置。
     * 3. 使用场景不同:
     * - 如果只需要添加或者修改部分消息转换器,推荐使用extendMessageConverters()。
     * - 如果要完全控制HttpMessageConverters的配置,不使用Spring Boot的自动配置,则需要使用configureMessageConverters()。
     */
    @Override
    public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
        // this line gets the swagger ui rendering issue, do not do this way
//        converters.add(0, mappingJackson2HttpMessageConverter());
        converters.add(mappingJackson2HttpMessageConverter());
    }

    /**
     *
     * mvc 配置 json 格式化
     *
     * 如果仅仅是为了配置 object mapper, 可以省略, 因为mvc 因为 mvc默认就是使用的用户自定义的 Object mapper (如果有自定义 mapper的话)
     */
    @Bean
    public MappingJackson2HttpMessageConverter mappingJackson2HttpMessageConverter() {
        MappingJackson2HttpMessageConverter result = new MappingJackson2HttpMessageConverter();

        result.setObjectMapper(objectMapper);
        result.setDefaultCharset(StandardCharsets.UTF_8);
        result.setSupportedMediaTypes(List.of(
            MediaType.APPLICATION_JSON,
            MediaType.TEXT_PLAIN
        ));

        return result;
    }
}
