package io.github.xiaoyureed.raincloud.core.starter.security.util;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
public final class JwtUtils {

    /**
     * 需要达到一定的长度
     * need to reach a certain length
     */
    private static final String tokenSalt = "$@lt__12121213efesfsefe@@sefsefsfefejiojoijofsefsefjiefijfeijioiliiaddfd";

    private static final SecretKeySpec secretKeySpec =
        new SecretKeySpec(tokenSalt.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

    /**
     * 有效时间 3m (unit: s)
     */
    private static final Integer expireInSeconds = 3 * 60 * 60;

    private static final boolean check_expire = false;

    private JwtUtils() {
    }

    public static String createToken(String username) {
        return createToken(new HashMap<>(0), username);
    }

    public static String createToken(Map<String, Object> claims, String username) {
        //系统默认时区: ZoneId.systemDefault()
        ZonedDateTime nowZoned = LocalDateTime.now().atZone(ZoneId.systemDefault());
        Date currentDate = Date.from(nowZoned.toInstant());
        Date expireDate = Date.from(nowZoned.plusSeconds(expireInSeconds).toInstant());

        claims.put("username", username);

        return Jwts.builder()
            .setHeaderParam("type", "JWT") // optional
            .setClaims(claims) // 设置自定义参数信息
            .setSubject(username) // optional
//            .setId() // optional, 设置JWT唯一标识(根据业务需要，这个可以设置为一个不重复的值，主要用来作为一次性token,从而回避重放攻击)
            // 设置签发时间
            //系统默认时区: ZoneId.systemDefault()
            .setIssuedAt(currentDate)
//            .setIssuer() // 设置签发者
//            .setNotBefore() // 定义在什么时间之前，该jwt都是不可用的.
            .setExpiration(expireDate)
            // 设置签名算法和使用的秘钥(盐)
            .signWith(secretKeySpec)

            .compact();

    }

    public static String createToken(UserDetails userDetails) {
        HashMap<String, Object> claims = new HashMap<>();
        claims.put("username", userDetails.getUsername());

        return createToken(claims, userDetails.getUsername());
    }

    public static ParseResult parseToken(String token) {
        ParseResult result = new ParseResult();

        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeySpec)
                .build()
                .parseClaimsJws(token)
                .getBody();

            result.setClaims(claims);
//            result.setExpired(false); // 没过期

        } catch (ExpiredJwtException e) {
            log.warn("!!! jwt parse: has already expired");

            // 即使过期, 也构造 result
            result.setClaims(e.getClaims());
            // 已经过期, 综合 check_expire 进行判断
//            result.setExpired(check_expire);
        } catch (Exception e) {
            log.error("!!! jwt parse error: {}", e.getMessage(), e);
            throw new AuthenticationServiceException("parse jwt token error", e);
        }

        return result;
    }

    @Data
    public static class ParseResult {
        private Claims claims;

        /**
         * 是否过期
         */
//        private Boolean expired;
//        private Boolean expired = claims.getExpiration().before(new Date());

        public <T> T getClaimsProperty(String claimsKey, Class<T> type) {
            return this.claims.get(claimsKey, type);
        }

        public <T> T getClaimsProperty(Function<Claims, T> extractor) {
            return extractor.apply(this.claims);
        }

        public Boolean getExpired() {
            return this.claims.getExpiration().before(new Date());
        }

        public String getUsername() {
            return getClaimsProperty("username", String.class);
        }

        /**
         * @param userDetails 根据 jwt 解析出的 username 从数据库查出来的
         */
        public Boolean valid(UserDetails userDetails) {
            return userDetails != null
                && StringUtils.equals(this.getUsername(), userDetails.getUsername()) // 这个肯定为 true
                && !this.getExpired();
        }

        /**
         * @param username 从数据库查出来的 username
         */
        public Boolean valid(String username) {
            return StringUtils.equals(this.getUsername(), username)
                && !this.getExpired();
        }
    }
}
