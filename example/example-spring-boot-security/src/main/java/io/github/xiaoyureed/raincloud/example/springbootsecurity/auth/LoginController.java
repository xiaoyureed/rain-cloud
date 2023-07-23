package io.github.xiaoyureed.raincloud.example.springbootsecurity.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import io.github.xiaoyureed.raincloud.core.starter.security.auth.AnonymousAccess;
import io.github.xiaoyureed.raincloud.core.starter.security.auth.PermitAccess;
import io.github.xiaoyureed.raincloud.core.starter.security.util.JwtUtils;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@RestController
@RequestMapping("login")
public class LoginController {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    /**
     *
     */
    @AnonymousAccess
    @PostMapping("standard")
    public ResponseEntity<ResponseWrapper<LoginResp>> loginStandard(@RequestBody LoginReq req) throws Throwable {
        // 内部会调用 userDetailsService 验证 name, 同时查出真是密码, 调用 password encode 验证密码
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(req.getUsername(), req.getPassword()));

        if (authenticate.isAuthenticated()) {
            String token = JwtUtils.createToken(req.getUsername());
            return ResponseEntity.ok(ResponseWrapper.ok(new LoginResp().setToken(token)));
        }

        throw new AuthenticationServiceException("user or password wrong");
    }

//    @PermitAccess
    @PostMapping
    public ResponseEntity<ResponseWrapper<LoginResp>> login(@RequestBody LoginReq req) throws Throwable {
        // 根据 username/password 查询
        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());

        // 密码校验
        if (!passwordEncoder.matches(req.getPassword(), userDetails.getPassword())) {
            throw new BadCredentialsException("wrong password");
        }

        UsernamePasswordAuthenticationToken authenticatedToken = new UsernamePasswordAuthenticationToken(req.getUsername(), null, null);
        SecurityContextHolder.getContext().setAuthentication(authenticatedToken);

        String token = JwtUtils.createToken(userDetails);
        // 根据业务需要, 决定是否保存 redis, key 为 token, value 为 login user 信息

        return ResponseEntity.ok(ResponseWrapper.ok(new LoginResp().setToken(token)));
    }

    @Data
    static class LoginResp {
        private String token;
    }

    @Data
    static class LoginReq {
        private String username;
        private String password;
    }
}
