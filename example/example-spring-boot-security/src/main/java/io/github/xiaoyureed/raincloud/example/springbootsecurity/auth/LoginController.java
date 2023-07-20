package io.github.xiaoyureed.raincloud.example.springbootsecurity.auth;

import java.util.Objects;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;

import io.github.xiaoyureed.raincloud.core.common.exception.SystemException;
import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import io.github.xiaoyureed.raincloud.core.starter.database.x.query.LambdaQueryWrapperX;
import io.github.xiaoyureed.raincloud.core.starter.security.auth.AnonymousAccessAllowed;
import io.github.xiaoyureed.raincloud.core.starter.security.util.JwtUtils;
import io.github.xiaoyureed.raincloud.example.springbootsecurity.account.Account;
import io.github.xiaoyureed.raincloud.example.springbootsecurity.account.AccountMapper;
import io.github.xiaoyureed.raincloud.example.springbootsecurity.account.AccountService;
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

    @AnonymousAccessAllowed
    @PostMapping
    public ResponseEntity<ResponseWrapper<LoginResp>> login(@RequestBody LoginReq req) throws Throwable {
        // 根据 username/password 查询
        UserDetails userDetails = userDetailsService.loadUserByUsername(req.getUsername());

        // 密码校验


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
