package io.github.xiaoyureed.raincloud.core.starter.security.auth.sms;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

/**
 * 校验逻辑
 *
 * 从未经认证的 authentication 构建经过认证的 authentication
 *
 * 使用时可以这样:
 * 拿到自动注入 AuthenticationManager am, 调用 am.authenticate(自己构造的unAuthToken), 判断返回值是否为空, 底层调用的就是这里的 authenticate()
 *
 * xiaoyureed@gmail.com
 */
//@Component
public class SmsAuthenticationProvider implements AuthenticationProvider {
    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * 这里可以完全控制校验逻辑
     */
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SmsAuthenticationToken smsToken = (SmsAuthenticationToken) authentication;
        String phone = (String) smsToken.getPrincipal();

        UserDetails userDetails = userDetailsService.loadUserByUsername(phone); // 可能抛出 user not found 异常

        //todo 利用 passwordEncode校验密码
        //校验验证码

        // 构建经过认证的 token
        SmsAuthenticationToken smsAuthenticationToken = new SmsAuthenticationToken(userDetails.getUsername(), userDetails.getAuthorities());
        // 需要把未认证中的一些信息copy到已认证的token中
        smsAuthenticationToken.setDetails(smsToken);

        return smsAuthenticationToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SmsAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
