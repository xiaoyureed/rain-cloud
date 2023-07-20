package io.github.xiaoyureed.raincloud.core.starter.security.util;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Objects;
import javax.crypto.spec.SecretKeySpec;

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

    private static final String tokenSalt = "$@lt__12121213efesfsefe@@sefsefsfefejiojoijofsefsefjiefijfeijioiliiaddfd";

    private static final SecretKeySpec secretKeySpec =
        new SecretKeySpec(tokenSalt.getBytes(StandardCharsets.UTF_8), SignatureAlgorithm.HS256.getJcaName());

    /**
     * 有效时间 3m (unit: s)
     */
    private static final Integer expireInSeconds = 3 * 60 * 60;

    private JwtUtils() {
    }

    public static String createToken(UserDetails userDetails) {

        HashMap<String, Object> clims = new HashMap<>();
        clims.put("username", userDetails.getUsername());

        ZonedDateTime nowZoned = LocalDateTime.now().atZone(ZoneId.systemDefault());
        Date currentDate = Date.from(nowZoned.toInstant());
        Date expireDate = Date.from(nowZoned.plusSeconds(expireInSeconds).toInstant());


        return Jwts.builder()
            .setHeaderParam("type", "JWT") // optional
            .setClaims(clims) // 设置自定义参数信息
            .setSubject(userDetails.getUsername()) // optional
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

    public static ParseResult parseToken(String token) {
        ParseResult result = new ParseResult();

        try {
            Claims claims = Jwts.parserBuilder()
                .setSigningKey(secretKeySpec)
                .build()
                .parseClaimsJws(token)
                .getBody();

            result.setClaims(claims);
            result.setExpired(false);

        } catch (ExpiredJwtException e) {
            log.warn("!!! jwt parse: has already expired");
            result.setClaims(e.getClaims());
            result.setExpired(true);
        } catch (Exception e) {
            log.error("!!! jwt parse: {}", e.getMessage(), e);
            result.setParseException(e);
        }

        return result;
    }

    @Data
    public static class ParseResult {
        private Claims claims;
        private Boolean expired;
        private Exception parseException;
    }
}
