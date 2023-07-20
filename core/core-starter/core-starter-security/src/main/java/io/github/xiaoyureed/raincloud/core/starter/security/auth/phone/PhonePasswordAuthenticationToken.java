package io.github.xiaoyureed.raincloud.core.starter.security.auth.phone;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * 自定义 authentication token
 * - 方式 1: 继承 AbstractAuthenticationToken
 *          需要自己添加构造方法, 创建未认证/已认证的 token
 * - 方式 2: 更简单 - 继承 UsernamePasswordAuthenticationToken, 新疆两个 constructor
 *      constructor(Object principal, Object credentials), /调用父类的构造函数，创建一个未认证的 Token
 *      constructor(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities), /调用父类的构造函数，创建一个已认证的 Token
 *
 *
 * xiaoyureed@gmail.com
 */
public class PhonePasswordAuthenticationToken extends UsernamePasswordAuthenticationToken {

    // 未经认证的 token
    public PhonePasswordAuthenticationToken(Object principal, Object credentials) {
        super(principal, credentials);
    }

    // 已经认证的 token
    public PhonePasswordAuthenticationToken(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities) {
        super(principal, credentials, authorities);
    }
}
