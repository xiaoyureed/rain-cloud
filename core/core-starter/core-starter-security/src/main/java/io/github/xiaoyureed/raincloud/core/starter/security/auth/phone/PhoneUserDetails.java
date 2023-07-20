package io.github.xiaoyureed.raincloud.core.starter.security.auth.phone;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.xiaoyureed.raincloud.core.common.model.LoginUserInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * xiaoyureed@gmail.com
 */
@Data
@Accessors
public class PhoneUserDetails implements UserDetails, LoginUserInfo<String> {

    private String phone;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return phone;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String user() {
        return phone;
    }

    @Override
    public String identifier() {
        return phone;
    }
}
