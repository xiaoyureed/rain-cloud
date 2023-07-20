package io.github.xiaoyureed.raincloud.example.springbootsecurity.auth;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.stereotype.Service;
import org.springframework.util.AntPathMatcher;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
//@Service
@Slf4j
public class PermissionService {

    /**
     * new AntPathRequestMatcher("/login", HttpMethod.POST.name()) 会匹配 POST 请求方式的 /login 接口
     *
     * 前缀匹配
     * antPathMatcher.matchStart("/user/*","/user/001"); // 返回 true
     * antPathMatcher.matchStart("/user/*","/user"); // 返回 true
     * antPathMatcher.matchStart("/user/*","/user001"); // 返回 false
     *
     * 去掉路径开头的静态部分，得到匹配到的动态路径
     * 例如：myroot/*.html 匹配 myroot/myfile.html 路径，结果为 myfile.html
     * antPathMatcher.extractPathWithinPattern("uc/profile*","uc/profile.html"); // 返回 profile.html
     *
     * 匹配路径中的变量
     * 例如：/hotels/{hotel} 匹配 /hotels/1 路径，结果为 hotel -> 1
     * 例如: /com/{filename:\\w+}.jsp will match /com/test.jsp and assign the value test to the filename variable
     * Map<String, String> extractUriTemplateVariables(String pattern, String path);
     *
     * 合并两个模式
     * antPathMatcher.combine("uc/*.html","uc/profile.html"); // uc/profile.html
     */
    private static final AntPathMatcher PATH_MATCHER = new AntPathMatcher("/");

    /**
     * 已经认证之后, 可以拿到 userDetails 了, 才会调用这个方法鉴权
     */
    public boolean hasPermission(HttpServletRequest httpServletRequest, Authentication authentication) {

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof UserDetails)) {
            return false;
        }

        String username = ((UserDetails) principal).getUsername();
        Set<String> allowedUrls = allowedUrls(username);

        return allowedUrls.stream().anyMatch(url ->
            PATH_MATCHER.match(url, httpServletRequest.getRequestURI())
        );
    }

    public Set<String> allowedUrls(String username) {
        //todo
        HashSet<String> result = new HashSet<>();
        result.add("/hello");

        return result;
    }
}
