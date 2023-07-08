package io.github.xiaoyureed.raincloud.service.support.gateway.filter;

import java.util.List;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.github.xiaoyureed.raincloud.core.common.consts.Consts;
import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import io.github.xiaoyureed.raincloud.service.support.gateway.properties.GatewayAuthProperties;
import io.github.xiaoyureed.raincloud.service.support.gateway.util.WebFluxUtils;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * xiaoyureed@gmail.com
 */
@Slf4j
@Component
public class GlobalAuthFilter implements GlobalFilter, Ordered {

    private static final String BEARER = "bearer";

    @Resource
    private GatewayAuthProperties gatewaySecurityProperties;

    @Resource
    private RedisTemplate<Object, Object> redisTemplate;

    /**
     * 说明：目前所有请求均是通过Gateway进行访问。
     * /oauth/check_token，是比较特殊的地址，不是使用token的方式进行鉴权。
     * 虽然目前使用的是“permitAll”的方式，不够安全。但是不管什么情况，在Gateway这一端，不应该进行拦截。
     * 后续可以根据IP，以及OAuth2鉴权的方式进行安全控制。
     *
     */
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        log.debug("!!! GlobalAuthFilter in use!");

        String url = exchange.getRequest().getURI().getPath();

        if (!gatewaySecurityProperties.getAuth().isEnabled()) {
            return chain.filter(exchange);
        }

        List<String> whiteList = gatewaySecurityProperties.getWhiteList();
        // If the request url exists in white list,
        //then allow this request directly
        if (WebFluxUtils.isPathMatch(whiteList, url)) {
            return chain.filter(exchange);
        }

        // 外部进入的请求，如果包含 x-feign 请求头，认为是非法请求，直接拦截。x-feign 只能用于内部 Feign 间忽略权限使用
        // If the request contains x-feign header, we assume that it's an illegal request
        //(since the x-feign header only appears in internal request)
        String fromIn = exchange.getRequest().getHeaders()
            .getFirst(Consts.Web.HeaderNames.REQUEST_HEADER_FEIGN_FLAG);
        if (ObjectUtils.isNotEmpty(fromIn)) {
            log.warn("!!! Illegal request to disable access!");
            ResponseWrapper<?> response = ResponseWrapper.error(CodeEnum.ILLEGAL_REQUEST, HttpStatus.FORBIDDEN);

            return WebFluxUtils.writeJsonResponse(exchange.getResponse(), response);
        }

//        allow the websocket request.
//
//        String webSocketToken =  exchange.getRequest().getHeaders().getFirst(com.google.common.net.HttpHeaders.SEC_WEBSOCKET_PROTOCOL);
//        if (StringUtils.isNotBlank(webSocketToken) && StringUtils.endsWith(webSocketToken,".stomp")) {
//            return chain.filter(exchange);
//        }

        // 3.非免登陆地址，获取token 检查token，如果为空，或者不是 Bearer XXX形式，则认为未授权。
        String token = exchange.getRequest().getHeaders()
            .getFirst(Consts.Web.HeaderNames.REQUEST_HEADER_TOKEN);
        if (tokenNotWellFormed(token)) {
            log.warn("!!! Token is not Well Formed!");
            ResponseWrapper<?> error = ResponseWrapper.error(CodeEnum.TOKEN_FAIL, HttpStatus.UNAUTHORIZED);

            return WebFluxUtils.writeJsonResponse(exchange.getResponse(), error);
        }

        // 非免登陆接口，同时也有格式正确的Token，那么就验证Token是否过期
        //    就看Redis中，是否有这个Token
        String redisTokenKey = StringUtils.replace(token, BEARER, "auth:");
        if (StringUtils.isNotBlank(redisTokenKey) && Boolean.TRUE.equals(redisTemplate.hasKey(redisTokenKey))) {
            log.debug("!!! Token is OK！");
            return chain.filter(exchange);
        }

        log.debug("!!! Token is Expired！");
        ResponseWrapper<?> error = ResponseWrapper.error(CodeEnum.TOKEN_EXPIRE, HttpStatus.UNAUTHORIZED);
        return WebFluxUtils.writeJsonResponse(exchange.getResponse(), error);
    }

    @Override
    public int getOrder() {
        return FilterOrderConsts.GLOBAL_AUTH_FILTER_ORDER;
    }

    private boolean tokenWellFormed(String token) {
        return StringUtils.isNotBlank(token) && StringUtils.containsOnly(token, "bearer");
    }

    private boolean tokenNotWellFormed(String token){
        return StringUtils.isBlank(token) || StringUtils.containsOnly(token, BEARER);
    }
}
