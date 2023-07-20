package io.github.xiaoyureed.raincloud.core.starter.security;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.boot.web.reactive.context.ReactiveWebApplicationContext;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.mvc.condition.PathPatternsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;
import io.github.xiaoyureed.raincloud.core.starter.common.util.BeanUtils;
import io.github.xiaoyureed.raincloud.core.starter.security.auth.AnonymousAccessAllowed;
import io.github.xiaoyureed.raincloud.core.starter.security.auth.jwt.JwtAuthenticationFilter;
import io.github.xiaoyureed.raincloud.core.starter.security.auth.jwt.JwtAuthenticationProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Configuration
@Slf4j
/**
 * // 可以去掉, security仍然生效
 * 若是引入的 spring-boot-starter-security, 则可去掉
 * 若使用的是单独的 spring-security-config、web、core（三个），就需要使用 @EnableWebSecurity注解。
 */
//@EnableWebSecurity
//@EnableMethodSecurity(
//    /**
//     * 默认开启
//     *
//     * 会激活@PreAuthorize和@PostAuthorize，这两个注解都是方法或类级别的注解。
//     * @PreAuthorize表示访问方法或类在执行之前先判断权限，大多情况下都是使用这个注解，注解的参数和access()方法参数值相同，都是权限表达式。
//     *      如@PreAuthorize(“hasRole(‘ROLE_abc’)”) 表示拥有abc角色可以访问，但是也可以在写成@PreAuthorize(“hasRole(‘abc’)”)
//     *      (但是如果是在 configure方法中使用access表达式配置，角色前面不能以ROLE_开头)
//     * @PostAuthorize 表示方法或类执行结束后判断权限，此注解很少被使用。
//     * */
//    prePostEnabled = true,
//    //会激活@Secured，开启基于角色注解的访问控制，将注解@Secured(“ROLE_角色”) 标注在Controller的方法上，那么只有有相应角色的用户才能够访问该方法。
//    securedEnabled = true,
//    jsr250Enabled = true
//)
public class SecurityConfiguration {

    /**
     * SHA-256+随机盐+密钥 对密码进行加密
     *
     * encode() 通过 hash 进行加密 不可逆
     * matches(原始明文{即需要验证的密码}, 密文{一般来自数据库})
     */
    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder();
    }

    @Value("${spring.profiles.active:}")
    private String activeProfiles;

    private static final String[] swagger_paths = {
        // -- Swagger UI v2
        "/v2/api-docs",
        "/swagger-resources",
        "/swagger-resources/**",
        "/configuration/ui",
        "/configuration/security",
        "/swagger-ui.html",
        "/webjars/**",
        "/error", // 忽略 /error 页面

        // -- Swagger UI v3 (OpenAPI)
        "/v3/api-docs/**",
        "/swagger-ui/**",
        // swagger-ui shortcut path
        "/",

        // others
        "/css/**","/js/**","/index.html","/img/**","/fonts/**","/favicon.ico"
    };

    private String[] getWhiteList() {
        List<String> list = new ArrayList<>();

        if (!activeProfiles.contains("prod")) {
            log.debug("!!! Current env is not prod, will add swagger resources to the whitelist");

            list.add("/swagger-ui.html");
            list.add("/**");

            list.add("/swagger-ui.html/**");
            list.add("/swagger-ui/**");
            list.add("/swagger-resources/**");
            list.add("/webjars/**");
            list.add("/v3/**");
            list.add("/v2/**");
            list.add("/*/api-docs");
            list.add("/doc.html");
            list.add("/css/**");
        }

//        list.add("/userAccount/login");
//        list.add("/login");
//        list.add("/home");

        return list.toArray(new String[0]);
    }

//    @Autowired
//    private UserDetailsService userDetailsService;

    /**
     * @lazy 解决循环依赖问题
     */
    @Autowired
    @Lazy
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private JwtAuthenticationProvider jwtAuthenticationProvider;

    /**
     * 配置路由
     *
     * ant 表达式语法:
     *      ?: 匹配某一个字符
     *      *: 匹配0个or一个 or多个字符
     *      **: 匹配0个or一个 or多个目录
     *
     *      /** — 特殊字符串，匹配所有路径
     *      ** — 特殊字符串，匹配所有路径
     *      /bla/**  — 匹配所有以/bla/开头的路径
     *
     *      最长匹配原则:
     *      /project/upload/avatar.jpg 对于两个模式会匹配较为长/详细的那个
     *          /project/upload/*.jpg       匹配
     *          /** /*.jpg                  不匹配
     *
     *      match("/root/*", "/root/aaa/")          //false, 结束符不一致
     *      match("/root/aaa/*", "/root/aaa/");     // true
     *      match("/root/aaa/**", "/root/aaa/");    // true
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            // 不需要 session, 因为是基于 token 进行校验的
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .csrf(cus -> cus.disable()) // 基于 token, 无需 csrf

//            .formLogin(formLoginCustomizer -> formLoginCustomizer
//                .successHandler()
//                .failureHandler()
//                .failureForwardUrl()
//            )
            .formLogin(cus -> cus.disable())

//            .logout(logoutConfigurer -> logoutConfigurer.logoutSuccessHandler())

            .httpBasic(cus -> cus.disable())

            .authorizeHttpRequests(auth -> {
//                auth.requestMatchers(getWhiteList()).permitAll()
                auth
                    /**
                     * anonymous() :匿名访问，仅允许匿名用户访问, 如果登录认证后，带有token信息再去请求，这个anonymous()关联的资源就不能被访问
                     * permitAll() 登录能访问,不登录也能访问
                     */
                    .requestMatchers(anonymousAccessAllowedPaths()).permitAll()
                    //.auth.requestMatchers().anonymous();

                    .requestMatchers("/public/**").permitAll()
                    .requestMatchers("/admin/**").hasRole("ADMIN")

                    // 全局权限校验, 配置之后, 方法上就无需@hasPermission 这种注解了
//                    .anyRequest().access(new AuthorizationManager<RequestAuthorizationContext>() {
//                        @Override
//                        public AuthorizationDecision check(Supplier<Authentication> authentication, RequestAuthorizationContext authorizationContext) {
//                            HttpServletRequest request = authorizationContext.getRequest();
//                        }
//                    })

                    // .requestMatchers().authenticated()

                    // anyRequest 必须在最后设置
                    .anyRequest().authenticated()

                ;

            })

            /**
             * 过滤器顺序问题:
             *FilterComparator 比较器中初始化了Spring Security 自带的Filter 的顺序，即在创建时已经确定了默认Filter的顺序。并将所有过滤器保存在一个 filterToOrder Map中。key值是Filter的类名，value是过滤器的顺序号。
             *
             * addFilterAt(A, B.class) A 添加在 B 之前 (b 一般是自带的 filter), A 序号会比 B 小
             * addFilter(Filter filter) 添加在所有默认 filter 之前,
             *      添加的 filter 必须是已经注册到 security 的 filter(意味着, filter 可能是已经注册的内置 filter, 也可能是先前 add 了的 filter)
             *
             * 若两个 filter 需要相等, 则谁先被 add 进 security, 则谁在前面
             */
            // 添加 jwt filter
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)

            // 可选
            //只需要自定义了 userDetailsService 并注册进了容器, 这行可以注释掉
            //若没有注册进 spring, 则需要在这里显式指定
            //
            //添加自定义provider 之后，security 配置就不会自动注入 DaoAuthenticationProvider 了，如果还想使用，就调用下方代码指定自定义 userDetailsService
//            .userDetailsService(userDetailsService)

//            .authenticationProvider(jwtAuthenticationProvider)

//            .authenticationManager()

            // 校验异常处理
            .exceptionHandling(cusExceptionHandling -> cusExceptionHandling
                //如果是授权过程中出现的异常会被封装成AccessDeniedException然后调用AccessDeniedHandler对象的方法去进行异常处理。
                // 会在全局异常之后捕获
                // 可以用来解决认证过的用户访问无权限资源时的异常
                    /**
                     * 也可以在JwtAuthenticationFilter里面抛出异常的地方, 用自动注入的 HandlerExceptionResolver (name="handlerExceptionResolver") 来 resolve 异常:
                     *      resolver.resolveException(request, response, null, new RuntimeException("无效的会话，或者会话已过期，请重新登录。")), 将异常抛给了全局，就可以在全局中处理我们的异常了
                     */
                .accessDeniedHandler(new AccessDeniedHandler() {
                    @Override
                    public void handle(HttpServletRequest request, HttpServletResponse response,
                                       AccessDeniedException accessDeniedException) throws IOException, ServletException {
                        //当用户在没有授权的情况下访问受保护的REST资源时，将调用此方法发送403 Forbidden响应
//                        response.sendError(HttpServletResponse.SC_FORBIDDEN, accessDeniedException == null ? "UnAuthorized" : accessDeniedException.getMessage());
                        ServletUtils.sendResponseContent(response, BeanUtils.toJson(ResponseWrapper.error(CodeEnum.UNAUTHORIZED_ACCESS_DENIED_ERROR)));
                    }
                })

                //如果是未认证请求过程中出现的异常会被封装成AuthenticationException然后调用AuthenticationEntryPoint对象的方法去进行异常处理。
                    // 会在全局异常捕获前捕获异常；
                    //可以用来解决匿名(未登录)用户访问无权限资源时的异常
                    //当用户身份验证失败时，将会触发自定义的身份验证入口点的commence()方法，可以在其中编写自己的逻辑，
                    //  例如返回适当的错误响应、重定向到登录页面等。
                    .authenticationEntryPoint(new AuthenticationEntryPoint() {
                        @Override
                        public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
                            //当用户尝试访问安全的REST资源而不提供任何凭据时，将调用此方法发送401 响应
//                            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException == null ? "Unauthenticated" : authException.getMessage());

                            ServletUtils.sendResponseContent(response, BeanUtils.toJson(ResponseWrapper.error(CodeEnum.UNAUTHENTICATED_ERROR)));

                            //也可以抛出
                            // 这里抛出的异常可在 filter 中对 "filterChain.doFilter(request, response);" 进行 catch 来捕获, 然后 HandlerExceptionResolver.resolve(..) 通过全局异常来进行处理
//                            throw authException;
                        }
                    })
            )

            .headers(cusHeader -> cusHeader
                .cacheControl(cusCache -> cusCache
                    // 禁用缓存
                    .disable()

                )
                //防止iframe 造成跨域
                .frameOptions(cusFrame -> cusFrame
                    .disable()
                )
            )
//            .cors(cusCors -> cusCors.disable())
            // 默认会使用名字为 corsConfigurationSource 的 bean 来配置 cors
            .cors(cusCors -> cusCors.configurationSource(corsConfigurationSource()))

//            .authenticationManager()

            // 记住我
            // 用户在登录页勾选记住我之后, 登录信息被保存起来(内存/数据库), 一段时间内无需登录
//            .rememberMe(cusRememberme -> cusRememberme
//                // 自定义登录逻辑
//                .userDetailsService()
//                .rememberMeParameter("hah") // 自定义表单中参数名字, 默认是remember-me
//                // 持久层对象
//                .tokenRepository(persistentTokenRepository())
//                .tokenValiditySeconds(60 * 5) // 有效时间, 默认是两周时间
//            )

            .build();


    }

    private String[] anonymousAccessAllowedPaths() {
        HashSet<String> result = new HashSet<>();

        // 指定名字, 否则会报错(有多个同类型的实例)
        RequestMappingHandlerMapping requestMappingHandlerMapping = applicationContext.getBean("requestMappingHandlerMapping", RequestMappingHandlerMapping.class);
        Map<RequestMappingInfo, HandlerMethod> mappings = requestMappingHandlerMapping.getHandlerMethods();
        mappings.entrySet().forEach(entry -> {
            HandlerMethod method = entry.getValue();
            AnonymousAccessAllowed anno = method.getMethodAnnotation(AnonymousAccessAllowed.class);

            if (anno != null) {
                // 不要用这种方式, 否则controller 类上的 path 不会被拼上来
//                PatternsRequestCondition patternsCondition = entry.getKey().getPatternsCondition();
//                if (patternsCondition != null) {
//                    result.addAll(patternsCondition.getPatterns());
//                }

                PathPatternsRequestCondition pathPatternsCondition = entry.getKey().getPathPatternsCondition();
                if (pathPatternsCondition != null) {
                    List<String> collect = pathPatternsCondition.getPatterns().stream()
                        .map(p -> p.getPatternString())
                        .collect(Collectors.toList());
                    result.addAll(collect);
                }

            }
        });

        log.debug("!!! AnonymousAccessAllowed paths resolved: {}", result);

        return result.toArray(new String[0]);
    }

//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl result = new JdbcTokenRepositoryImpl();
////        InMemoryTokenRepositoryImpl result = new InMemoryTokenRepositoryImpl();
//
//        // 这行代码在第一次启动时存在。其他时候需要注释掉，否则会报错
//        result.setCreateTableOnStartup(true);
//
//    }


    /**
     * 认证管理器，登录的时候参数会传给 authenticationManager
     *
     * 自定义:
     *  return new AuthenticationManager() {
     *             @Override
     *             public Authentication authenticate(Authentication authentication) throws AuthenticationException {
     *             }
     *         };
     *   一般不会直接自定义, 而是通过自定义 authenticationProvider 的方式间接实现
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        return new ProviderManager(Arrays.asList(jwtAuthenticationProvider));
    }

    /**
     * 配置静态资源
     * https://www.fengnayun.com/news/content/287262.html
     * 这种方式是不走过滤器链的, 跟 Spring Security 无关
     *      例如, 放行前端页面的静态资源
     * 而通过 SecurityFilterChain 这种方式是走 Spring Security 过滤器链的，
     *      在过滤器链中，给请求放行, 在请求通过的时候可以获取 security 相关的数据, 例如 登录接口 就应该放在此处放行 (因为在这个过程中，还有其他事情要做。)
     *
     * - 放行 swagger ui 相关的资源
     */
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return new WebSecurityCustomizer() {
            @Override
            public void customize(WebSecurity web) {
                if (applicationContext instanceof ReactiveWebApplicationContext) {
                    log.debug("!!! detected that this is a [reactive] application");
                    web.ignoring().requestMatchers(swagger_paths)
                        // todo 忽略reactive application 常见的静态资源路径
                        //此处 PathRequest 需要分情况引入 (servlet, reactive 两种包)
//                        .requestMatchers(org.springframework.boot.autoconfigure.security.reactive.PathRequest.toStaticResources().atCommonLocations())
                        ;

                } else {
                    log.debug("!!! detected that this is a [servlet] application");
                    web.ignoring().requestMatchers(swagger_paths)// // 忽略 swagger 接口路径
                        // 忽略常见的静态资源路径
                        //此处 PathRequest 需要分情况引入 (servlet, reactive 两种包)
                        .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
                }


            }
        };
    }

    @Autowired
    private ApplicationContext applicationContext;

    /**
     * use global cross replace @CrossOrigin on Controller level
     */
    private CorsConfigurationSource corsConfigurationSource() {
        if (applicationContext instanceof ReactiveWebApplicationContext) {
            log.debug("!!! detected that this is a [reactive] application");
            throw new RuntimeException("unsupported");
        }
        else {
            log.debug("!!! detected that this is a [servlet] application");
            CorsConfiguration configuration = new CorsConfiguration();

//            configuration.setAllowedOrigins(Arrays.asList("http://localhost:3030","http://localhost:3010","http://localhost:4173"));
            configuration.setAllowedOrigins(Arrays.asList("*"));

//            configuration.setAllowedMethods(Arrays.asList("GET", "POST","PUT"));
            configuration.setAllowedMethods(Arrays.asList("*"));

            configuration.setAllowedHeaders(Arrays.asList("*"));
            configuration.setMaxAge(Duration.ofMinutes(10));

            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }


    }

    /**
     * 基于默认内存模型的用户模式
     */
//    @Bean
    public UserDetailsService memoryUserDeTailsService() {
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        /**
         * 在初始化的时候，创建了一个HashMap，所有的用户信息以username作为“主键”都放在了HashMap中
         *         createUser(UserDetails user)
         *         updateUser(UserDetails user)
         *         deleteUser(String username)
         *         changePassword(String oldPassword, String newPassword)
         *         userExists(String username)
         */
        manager.createUser(User.withUsername("admin")
            .password("{noop}123") // 密码不加密, 明文存储
//            .password("{bcrypt}$2a$10$GRLdNijSQMUvl/au9ofL.eDwmoohzzS7.rmNSJZ.0FxO/BTk76klW") // 加密存储
//            .passwordEncoder(pwd -> encoder().encode(pwd)) // optional
            .roles("ADMIN")
            .build());


        return manager;
    }

//    基于默认数据库模型的用户模式
//    @Bean
//    public UserDetailsService jdbcUserDetailsService(DataSource dataSource){
//        JdbcUserDetailsManager manager = new JdbcUserDetailsManager(dataSource);
//
//        if(!manager.userExists("admin")){
//            //下面的创建用户就相当于通过 jdbc 调用 mysql 的 insert
//            manager.createUser(User.withUsername("admin")
//                .password("{noop}xxx")
//                .roles("ADMIN")
//                .build());
//        }
//
//        return  manager;
//    }

}
