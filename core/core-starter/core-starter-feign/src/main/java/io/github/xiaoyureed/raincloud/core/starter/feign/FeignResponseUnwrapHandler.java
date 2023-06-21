package io.github.xiaoyureed.raincloud.core.starter.feign;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import io.github.xiaoyureed.raincloud.core.common.consts.Consts;
import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@RestControllerAdvice
@Slf4j
public class FeignResponseUnwrapHandler implements ResponseBodyAdvice<Object> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        log.debug("!!! " + this.getClass().getSimpleName() + "is supported");
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        boolean feign = StringUtils.isNotBlank(ServletUtils.getRequestHeader(Consts.Web.HeaderNames.REQUEST_HEADER_FEIGN_FLAG));
        if (feign) {
            return body;
        }

        return body;

    }
}
