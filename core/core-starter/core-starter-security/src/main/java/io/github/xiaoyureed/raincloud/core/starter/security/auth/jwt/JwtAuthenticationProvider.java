package io.github.xiaoyureed.raincloud.core.starter.security.auth.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
//@Component
@Slf4j
public class JwtAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    private JwtUserDetailsServiceImpl userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        JwtAuthenticationToken unAuthorizedToken = (JwtAuthenticationToken) authentication;
        String token = (String) unAuthorizedToken.getPrincipal();

        //todo
        log.warn("!!! todo");

        JwtAuthenticationToken authenticatedToken = new JwtAuthenticationToken(token, null);

        UserDetails userDetails = userDetailsService.loadUserByUsername(token);
        authenticatedToken.setDetails(userDetails);

        SecurityContext context = SecurityContextHolder.getContext();
        context.setAuthentication(authenticatedToken);

        return authenticatedToken;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return JwtAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
