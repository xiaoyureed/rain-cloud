package io.github.xiaoyureed.raincloud.service.support.gateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.github.xiaoyureed.raincloud.core.common.consts.Consts;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * xiaoyureed@gmail.com
 *
 * ip, logging ...
 */
@Slf4j
@Component
public class GlobalCommonFilter implements GlobalFilter, Ordered {
    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String url = exchange.getRequest().getURI().getPath();
        String path = exchange.getRequest().getPath().toString();
        String ip = exchange.getRequest().getRemoteAddress().getHostString();

        log.debug("!!! request path: {}, {}, client ip: {}", url, path, ip);

        ServerHttpRequest request = exchange.getRequest().mutate()
            //将获取的真实ip存入header微服务方便获取
            .header(Consts.Web.HeaderNames.REQUEST_HEADER_IP, ip)
            .build();
        return chain.filter(exchange.mutate().request(request).build());
    }

    @Override
    public int getOrder() {
        return FilterOrderConsts.GLOBAL_COMMON_FILTER_ORDER;
    }
}
