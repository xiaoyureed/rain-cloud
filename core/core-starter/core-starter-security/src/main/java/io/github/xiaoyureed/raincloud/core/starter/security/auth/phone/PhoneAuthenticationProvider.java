package io.github.xiaoyureed.raincloud.core.starter.security.auth.phone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.AbstractUserDetailsAuthenticationProvider;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * 自定义 provider:
 * - 方法 1: 注册一个 AuthenticationManager类型 bean, 返回 new ProviderManager(Arrays.asList(new AuthenticationManager 接口)) 即可
 *      AuthenticationManager 接口里面有 两个方法
 *          supports() 配置provider 支持的 authentication token 类型
 *          authenticate() 自定义校验逻辑
 * - 方法 2: 继承 AbstractUserDetailsAuthenticationProvider
 *
 * xiaoyureed@gmail.com
 */
//@Component
@Slf4j
public class PhoneAuthenticationProvider extends AbstractUserDetailsAuthenticationProvider {

    /**
     * phone user details service, 这里注入的是自定义的 PhoneUserDetailsServiceImpl
     */
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * 指定本 provider 是对哪个 authenticationToken 生效的
     */
    @Override
    public boolean supports(Class<?> authentication) {
        // PhonePasswordAuthenticationToken 是否等于 authentication 或者是 authentication 父类
        return PhonePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }

    /**
     * 额外的校验逻辑
     *
     * check the password to see if it is matched
     */
    @Override
    protected void additionalAuthenticationChecks(
        UserDetails userDetails, // 来自数据库的数据
        UsernamePasswordAuthenticationToken authentication // 来自请求的数据
    ) throws AuthenticationException {
        Object credentials = authentication.getCredentials();
        if (credentials == null) {
            log.error("!!! Failed to authenticate since no credentials provided");
            throw new BadCredentialsException("No credentials provided");
        }

        String password = credentials.toString();
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            log.error("!!! Failed to match password, password from request: {}", password);
            throw new BadCredentialsException("Bad credentials");
        }
    }

    /**
     * 执行校验逻辑
     *
     * 去数据源中拿取用户信息，是由 父类 中的 authenticate() 进行调用
     */
    @Override
    protected UserDetails retrieveUser(String phone, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        UserDetails userDetails = userDetailsService.loadUserByUsername(phone);

        if (userDetails == null) {
            throw new InternalAuthenticationServiceException("UserDetailsService returned null, which is an interface contract violation");
        }

        return userDetails;
    }
}
