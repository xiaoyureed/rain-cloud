package io.github.xiaoyureed.raincloud.core.starter.security.auth;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * OncePerRequestFilter 对于转发/重定向 这种内部请求, 不会拦截, 只会拦截外部进来的请求, 也就是只会拦截一次
 *
 * xiaoyureed@gmail.com
 */
@Component
@Slf4j
@Order(-101) // 保证请求最先进入, 最后出来
public class GlobalSecurityExceptionCatchFilter extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        try {
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            log.error("!!! global filter catching a exception, ", e);
            // 这里不能再抛出了, 否则 全局异常处理就捕获不到了
//                throw e;

            // 对于 controller 里面抛出的异常, security 有自己内置的 filter 进行捕获, 如果没有捕获到才会再在这里被捕获, 这里相当于是进行一个兜底操作
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
