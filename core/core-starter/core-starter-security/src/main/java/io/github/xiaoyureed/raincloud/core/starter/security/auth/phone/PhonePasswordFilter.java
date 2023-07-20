package io.github.xiaoyureed.raincloud.core.starter.security.auth.phone;

import java.io.IOException;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * 通过继承 AbstractAuthenticationProcessingFilter 创建的 filter 本身没有校验逻辑, 是交给自定义的 Authentication Provider 去校验的
 *
 *
 * xiaoyureed@gmail.com
 */
//@Component
public class PhonePasswordFilter extends AbstractAuthenticationProcessingFilter {

    private static final String form_key_phone = "phone";
    private static final String form_key_password = "password";

    private static final boolean post_only = true;

    private static String defaultFilterProcessesUrl = "/phoneUrl";

    /**
     * 指定那个 URL 会进入登录认证 (只有登录认证才会进入这个 filter, 别的请求（如查询列表新增等）是不会进行登录认证的，因为这时候判断的就是是否登录是否有权限，而不是登录认证)
     *
     * authenticationManager 必须传入, 否则报错
     */
    protected PhonePasswordFilter(AuthenticationManager authenticationManager) {
//        super(defaultFilterProcessesUrl);
//        super(new AntPathRequestMatcher(defaultFilterProcessesUrl, HttpMethod.POST.name()), authenticationManager);
        super(new AntPathRequestMatcher(defaultFilterProcessesUrl), authenticationManager);
    }

    /**
     * 准备用来认证的 token
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (post_only && !HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Authentication method not supported:" + request.getMethod());
        }

        String phone = Optional.ofNullable(request.getParameter(form_key_phone)).map(String::trim).orElse("");
        String password = Optional.ofNullable(request.getParameter(form_key_password)).map(String::trim).orElse("");

        // 构建未认证的 Token
        PhonePasswordAuthenticationToken unAuthenticatedToken = new PhonePasswordAuthenticationToken(phone, password);
        // set up details
        unAuthenticatedToken.setDetails(this.authenticationDetailsSource.buildDetails(request));

        // 交给 providerManager 进行认证
        return this.getAuthenticationManager().authenticate(unAuthenticatedToken);
    }
}
