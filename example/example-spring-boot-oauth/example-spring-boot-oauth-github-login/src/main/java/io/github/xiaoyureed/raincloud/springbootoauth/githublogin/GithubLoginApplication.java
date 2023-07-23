package io.github.xiaoyureed.raincloud.springbootoauth.githublogin;

import java.util.Collections;
import java.util.Map;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.config.oauth2.client.CommonOAuth2Provider;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * https://spring.io/guides/tutorials/spring-boot-oauth2/
 * <p>
 * xiaoyureed@gmail.com
 */
@SpringBootApplication
@RestController
public class GithubLoginApplication {

    public static void main(String[] args) {
        SpringApplication.run(GithubLoginApplication.class, args);
    }

    @GetMapping("free")
    public String free() {
        return "free page";
    }

    @GetMapping("secured")
    public String index() {
        return "secured page";
    }

    //    @GetMapping("user")
//    public Map<String, Object> user(Principal user) {
//        return Collections.singletonMap("name", user.getName());
//    }
    @GetMapping("user")
    public Map<String, Object> user(@AuthenticationPrincipal OAuth2User user) {
//        return Collections.singletonMap("name", user.getName());
        return Collections.singletonMap("name", user.getAttribute("name"));
    }

    @Configuration
    @EnableWebSecurity(debug = true)
    public static class SecurityConfig {
        @Bean
        public WebSecurityCustomizer webSecurityCustomizer() {
            return web -> {
                web.debug(true)
                    // "/error" should be allowed, checking https://github.com/spring-projects/spring-security/issues/12586
                    .ignoring().requestMatchers("/error", "/css/**", "/js/**", "/index.html", "/img/**", "/fonts/**", "/favicon.ico");

            };
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
            return httpSecurity

                .authorizeHttpRequests(cus -> cus
                    .requestMatchers("/free").permitAll()
                    .anyRequest().authenticated()
                )
                // comment out this line to allow auto redirect to the login page
//                .exceptionHandling(cus -> cus
//                    .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
//                )

                .formLogin(Customizer.withDefaults())
                .oauth2Login(Customizer.withDefaults())

                // the session management has no business about the oauth login
//                .sessionManagement(cus -> cus.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .csrf(cus -> cus
//                    .disable()
//                )
//            .csrf(cus -> cus
//                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//            )

                .build();
        }
    }


    /**
     * 四种授权方式:
     * <p>
     * Authentication code mode: 第三方登录常用这种
     *      1. 用户选择通过微信账号登录掘金, 点击按钮向微信发起授权请求
     *          https://wx.com/oauth/authorize?
     *              response_type=code&
     *              client_id=CLIENT_ID&
     *              redirect_uri=http://juejin.im/callback&
     *              scope=read，
     *          - reponse_type为code要求返回授权码。scope参数表示本地授权范围为只读权限，redirect_rui重定向地址。
     *          - 客户端 ID（client ID）和客户端密钥（client secret)是第三方 Client 预先在微信系统里申请的
     *          会弹出一个微信的页面 ,要求用户登录微信, 并询问用户是否同意授权(一个弹窗)。
     * <p>
     *     2. 用户同意授权后，微信根据redirect_uri重定向请求并带上授权码
     *          http://juejin.im/callback?code={AUTHORIZATION_CODE}
     * <p>
     *     3. 当掘金拿到授权码(code)时，带授权码和密钥等参数向微信申请令牌。
     *          https://wx.com/oauth/token?
     *              client_id=CLIENT_ID&
     *              client_secret=CLIENT_SECRET&
     *              grant_type=authorization_code&
     *              code={AUTHORIZATION_CODE}&
     *              redirect_uri=http://juejin.im/callback
     *          - grant_type表示本次授权为授权码方式, client_id, client_secret 是第三方应用(掘金)预先在微信系统申请的
     *          - code 即上一步得到的授权码
     *          - redirect_uir 是微信系统生成token 后向这个回调地址发送一段包含 token 的 json
     * <p>
     *          3.1. 令牌过期处理: 一般在颁发令牌的时候会一次发两个令牌，一个令牌(access_token)用来请求API，另一个令牌(refresh_token)负责获取新的Access token。
     *              Access token 过期前, 第三方 app 发送微信请求:
     *                  https://wx.com/oauth/token?
     *                      grant_type=refresh_token&
     *                      client_id=CLIENT_ID&
     *                      client_secret=CLIENT_SECRET&
     *                      refresh_token=REFRESH_TOKEN
     *                      grant_type为refresh_token表示请求要更新令牌, refresh_token 为更新令牌的令牌
     * <p>
     * <p>
     *     4. 最后微信收到请求后向redirect_uri地址发送JSON数据，其中的ACCESS_TOKEN就是令牌。
     * {
     * "access_token":"ACCESS_TOKEN",
     * "token_type":"bearer",
     * "expires_in":2592000,
     * "refresh_token":"REFRESH_TOKEN",
     * "scope":"read",
     * ....
     * }
     *
     * <p>
     * <p>
     *
     * Implicit mode: 前者的简化版, 直接在浏览器中向授权服务器申请令牌 (有一些应用时没有后端的，纯前端应用，就无法用到授权码模式。令牌的申请和存储都需要在前端完成，跳过了授权码这一步)
     *      这种方式把令牌直接传给前端，是很不安全的。因此，只能用于一些安全要求不高的场景，并且令牌的有效期必须非常短
     *     1. 前端应用直接获取token，response_type设置为token，要求直接返回令牌，跳过授权码。微信授权通过后重定向到指定的redirect_uri
     *          https://wx.com/oauth/authorize?
     *              response_type=token&
     *              client_id=CLIENT_ID&
     *              redirect_uri=http://juejin.im/callback&
     *              scope=read
     *     2. 用户登录微信, 并且同意授权, 微信会跳转到 redirect_uri, 同时token 作为 URL 参数返回给掘金
     *          类似这样: https://a.com/callback#token=ACCESS_TOKEN
     * <p>
     * <p>
     * 密码模式: 密码模式是用户把用户名，密码直接告诉客户端，客户端使用这些信息向授权服务器申请令牌。这需要用户对客户端高度信任。
     *     1. 用户直接输入自己的用户名，密码。直接去申请令牌，
     *          https://oauth.b.com/token?
     *              grant_type=password&
     *              username=USERNAME&
     *              password=PASSWORD&
     *              client_id=CLIENT_ID
     *          请求响应的json结果中返回token。grant_type为password表示密码式授权。
     * <p>
     *
     * 凭证模式模式: 凭证式和密码式很相似，主要使用与那些没有前端的命令行应用。可以用最简单的方式获取令牌，
     *     1. 第三方应用在命令行向微信发送请求
     *          https://wx.com/token?
     *              grant_type=client_credentials&
     *              client_id=CLIENT_ID&
     *              client_secret=CLIENT_SECRET
     *              grant_type为client_credentials表示凭证式授权，client_id和client_secret用来识别身份。
     *      微信验证通过后, 在请求响应json结果中返回token。
     */
//    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.githubClientRegistration());
//        return new InMemoryClientRegistrationRepository(this.githubClientRegistration1());
    }

    private ClientRegistration githubClientRegistration() {
        return ClientRegistration.withRegistrationId("github")
            .clientName("Github")
            .clientId("ad6fa4354390a68c2a5d")
            .clientSecret("35xyb1ae1c4d8a44cfad3d5a5c23b05ae2e116856a")
            .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
            .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
            .authorizationUri("https://github.com/login/oauth/authorize")
            .tokenUri("https://github.com/login/oauth/access_token")
//            .scope("read")
            .userInfoUri("https://api.github.com/user")
//            .userNameAttributeName(IdTokenClaimNames.SUB)
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}") // 占位符会自动填充


            .build();
    }

    private ClientRegistration githubClientRegistration1() {
        return CommonOAuth2Provider.GITHUB.getBuilder("github")
            .clientId("ad6fa4354390a68c2a5d")
            .clientSecret("35b1ae1c4d8a44cfad3d5a5c23b05ae2e116856a")
            .redirectUri("{baseUrl}/login/oauth2/code/{registrationId}") // 在 Github 预申请 Client id 时会要求填一个 callback url, 此处占位符会去匹配那个 callback url
            .build();
    }


}
