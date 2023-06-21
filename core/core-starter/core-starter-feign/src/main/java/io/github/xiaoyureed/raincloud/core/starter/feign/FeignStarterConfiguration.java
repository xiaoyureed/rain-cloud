package io.github.xiaoyureed.raincloud.core.starter.feign;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;

import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@ComponentScan
@EnableFeignClients({
    "io.github.xiaoyureed.raincloud.**.feign"
})
public class FeignStarterConfiguration {
    {
        log.debug("!!! feign starter ok");
    }

//    @Bean
//    public ErrorDecoder feignErrorDecoder(ObjectProvider<HttpMessageConverters> messageConverters) {
//        return new FeignExceptionDecoder(new SpringDecoder(messageConverters));
//    }
}
