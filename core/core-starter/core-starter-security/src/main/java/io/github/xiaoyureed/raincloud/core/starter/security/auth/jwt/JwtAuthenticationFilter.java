package io.github.xiaoyureed.raincloud.core.starter.security.auth.jwt;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;

import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;
import io.github.xiaoyureed.raincloud.core.starter.security.util.JwtUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * xiaoyureed@gmail.com
 */
@Component
public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final boolean post_only = false;
//    private static final String url_pattern = "/login";
    private static final String url_pattern = "/**";

    protected JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
//        super(new AntPathRequestMatcher(url_pattern, HttpMethod.POST.name()), authenticationManager);
        super(new AntPathRequestMatcher(url_pattern), authenticationManager);
    }

    /**
     *
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
        if (post_only && !HttpMethod.POST.matches(request.getMethod())) {
            throw new AuthenticationServiceException("Jwt Authentication http method not supported: " + request.getMethod());
        }

        String tokenWithHead = ServletUtils.getRequestHeader("Authentication");
        if (StringUtils.isBlank(tokenWithHead)) {
            throw new AuthenticationServiceException("Jwt Authentication failed, token missed in request");
        }

        String token = tokenWithHead.substring("bearer".length() + 1);

        JwtUtils.ParseResult parseResult = JwtUtils.parseToken(token);
        if (parseResult.getParseException() != null) {
            throw new AuthenticationServiceException("Jwt authentication failed, error occurred when parsing token, further error message: " + parseResult.getParseException().getMessage());
        }
        if (parseResult.getExpired() != null && parseResult.getExpired()) {
            throw new AuthenticationServiceException("Jwt authentication failed, token expired, current expire: ");
        }

        JwtAuthenticationToken unAuthenticatedToken = new JwtAuthenticationToken(token);
        unAuthenticatedToken.setDetails(this.authenticationDetailsSource.buildDetails(request));

        return this.getAuthenticationManager().authenticate(unAuthenticatedToken);
    }
}
