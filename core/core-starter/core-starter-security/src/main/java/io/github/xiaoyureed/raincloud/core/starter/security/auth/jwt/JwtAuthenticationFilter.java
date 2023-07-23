package io.github.xiaoyureed.raincloud.core.starter.security.auth.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;
import io.github.xiaoyureed.raincloud.core.starter.security.SecurityConfiguration;
import io.github.xiaoyureed.raincloud.core.starter.security.util.JwtUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

/**
 * Check the jwt token , if everything is ok, update the security context holder, or let the current request go to the next filter.
 * <p>
 * xiaoyureed@gmail.com
 */
//@Component
//public class JwtAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
//
//    private static final boolean post_only = false;
////    private static final String url_pattern = "/login";
//    private static final String url_pattern = "/**";
//
//    protected JwtAuthenticationFilter(AuthenticationManager authenticationManager) {
////        super(new AntPathRequestMatcher(url_pattern, HttpMethod.POST.name()), authenticationManager);
//        super(new AntPathRequestMatcher(url_pattern), authenticationManager);
//    }
//
//    /**
//     * construct unauthenticated token and passed it to authenticationManager to perform the auth
//     */
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException, IOException, ServletException {
//        if (post_only && !HttpMethod.POST.matches(request.getMethod())) {
//            throw new AuthenticationServiceException("Jwt Authentication http method not supported: " + request.getMethod());
//        }
//
//        String tokenWithHead = ServletUtils.getRequestHeader("Authentication");
//        if (StringUtils.isBlank(tokenWithHead)) {
//            throw new AuthenticationServiceException("Jwt Authentication failed, token missed in request");
//        }
//
//        String token = tokenWithHead.substring("bearer ".length());
//
//        JwtUtils.ParseResult parseResult = JwtUtils.parseToken(token);
//        if (parseResult.getExpired()) {
//            throw new AuthenticationServiceException("Jwt authentication failed, token expired, current expire: ");
//        }
//
//        JwtAuthenticationToken unAuthenticatedToken = new JwtAuthenticationToken(token);
//        unAuthenticatedToken.setDetails(this.authenticationDetailsSource.buildDetails(request));
//
//        return this.getAuthenticationManager().authenticate(unAuthenticatedToken);
//    }
//}

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final String auth_header_name = "Authorization";

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private HandlerExceptionResolver handlerExceptionResolver;

    private Set<String> whiteList = new HashSet<>();

    {
        whiteList.addAll(Arrays.asList(SecurityConfiguration.swagger_paths));
        whiteList.add("/login/**");
    }

    /**
     * 指定不过滤的 URi (返回 true 即不过滤)
     */
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return whiteList.stream().anyMatch(p -> new AntPathRequestMatcher(p).matches(request));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = ServletUtils.getAuthenticationToken(auth_header_name, true).orElse("");
        if (StringUtils.isBlank(token)) {
            AuthenticationServiceException e = new AuthenticationServiceException("Fetch token failed, target header name: " + auth_header_name);
            handlerExceptionResolver.resolveException(request, response, null, e);
            return;
        }

        JwtUtils.ParseResult parseResult = JwtUtils.parseToken(token);
        String usernameJwt = parseResult.getUsername();

        // 通过 username 构建 authentication, 填充进 security context (填充后, 后续的内置 filter 就可以通过了)
        if (StringUtils.isNotBlank(usernameJwt)
            && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = userDetailsService.loadUserByUsername(usernameJwt);

            if (parseResult.valid(userDetails.getUsername())) {
                UsernamePasswordAuthenticationToken authenticatedToken = new UsernamePasswordAuthenticationToken(
                    userDetails, null, userDetails.getAuthorities());
                authenticatedToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authenticatedToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}

