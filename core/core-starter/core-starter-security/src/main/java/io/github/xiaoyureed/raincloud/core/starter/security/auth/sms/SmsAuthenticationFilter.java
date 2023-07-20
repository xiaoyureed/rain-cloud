package io.github.xiaoyureed.raincloud.core.starter.security.auth.sms;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 这里可以定义所有的校验逻辑, 但是又自定义了 provider (基本的 username 校验已经在 自定义 provider 有了), 所以这里仅仅做一下路由判断和额外的校验逻辑
 *
 * xiaoyureed@gmail.com
 */
//@Component
public class SmsAuthenticationFilter extends OncePerRequestFilter {
    private static final String url_pattern = "/auth/sms";
    private static final boolean post_only = true;

    private AntPathMatcher pathMatcher = new AntPathMatcher();
//    private AntPathMatcher pathMatcher = new AntPathRequestMatcher("");

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    /**
     * 认证成功后, setAuthenticated(true),然后Authentication存储到SecurityContext中
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (supported(request)) {
            // todo
            String codeInBackend = "9527";

            String codeInRequest = Optional.ofNullable(request.getParameter("smsCode")).orElse("");
            if (!StringUtils.equals(codeInRequest, codeInRequest)) {
                AuthenticationServiceException e = new AuthenticationServiceException("Sms code Authentication failed");

//                throw e;
                handlerExceptionResolver.resolveException(request, response, null, e);

            }

            // 使用完codeInBackend 后在 redis删除

        }

        filterChain.doFilter(request, response);
    }

    private boolean supported(HttpServletRequest request) {
        if (post_only) {
            return HttpMethod.POST.matches(request.getMethod()) && pathMatcher.match(url_pattern, request.getRequestURI());
        } else {
            return pathMatcher.match(url_pattern, request.getRequestURI());
        }
    }
}
