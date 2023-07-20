package io.github.xiaoyureed.raincloud.example.springbootsecurity.auth;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;

import io.github.xiaoyureed.raincloud.core.common.model.LoginUserInfo;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * xiaoyureed@gmail.com
 */
@Data
@Accessors
public class LoginUserDetails implements UserDetails, LoginUserInfo<LoginUserDetails> {

    private String id;
    private String username;
    private String password;

    private String roles;
    private Collection<? extends GrantedAuthority> authorities;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> grantedAuthorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
        this.authorities = grantedAuthorities;

        return grantedAuthorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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
    public LoginUserDetails user() {
        return this;
    }

    @Override
    public String identifier() {
        return username;
    }
}
