package io.github.xiaoyureed.raincloud.core.starter.security.auth.jwt;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import io.github.xiaoyureed.raincloud.core.starter.security.util.JwtUtils;

/**
 * xiaoyureed@gmail.com
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername(String token) throws UsernameNotFoundException {
        JwtUtils.ParseResult parseResult = JwtUtils.parseToken(token);

        String username = parseResult.getClaims().get("username", String.class);

        return new JwtUserDetails().setUsername(username);
    }
}
