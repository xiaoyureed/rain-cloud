package io.github.xiaoyureed.raincloud.core.starter.security.auth.sms;

import java.util.Collection;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

/**
 * xiaoyureed@gmail.com
 */
public class SmsAuthenticationToken extends AbstractAuthenticationToken {

    /**
     * 手机号
     */
    private Object principal;

    /**
     * 创建未经认证的 token
     */
    public SmsAuthenticationToken(String phone) {
        super(null);
        this.principal = phone;
    }

    /**
     * 创建认证的 token
     */
    public SmsAuthenticationToken(Object principal, Collection<? extends GrantedAuthority> authorities) {
        super(authorities);
        this.principal = principal;
        super.setAuthenticated(true);
    }

    @Override
    public Object getCredentials() {
        return principal;
    }

    @Override
    public Object getPrincipal() {
        return principal;
    }
}
