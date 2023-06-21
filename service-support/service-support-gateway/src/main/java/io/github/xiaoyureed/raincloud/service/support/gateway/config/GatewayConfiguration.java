package io.github.xiaoyureed.raincloud.service.support.gateway.config;

import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.cloud.client.discovery.ReactiveDiscoveryClient;
import org.springframework.cloud.gateway.discovery.DiscoveryClientRouteDefinitionLocator;
import org.springframework.cloud.gateway.discovery.DiscoveryLocatorProperties;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * xiaoyureed@gmail.com
 */
@Configuration
public class GatewayConfiguration {
//    @Bean
//    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
//        return builder.routes()
//            .route("r1", r -> r.host("**.baeldung.com")
//                .and()
//                .path("/baeldung")
//                .uri("http://baeldung.com"))
//            .route(r -> r.host("**.baeldung.com")
//                .and()
//                .path("/myOtherRouting")
//                .filters(f -> f.prefixPath("/myPrefix"))
//                .uri("http://othersite.com")
//                .id("myOtherID"))
//            .build();
//    }

//    @Bean
//    public DiscoveryClientRouteDefinitionLocator discoveryClientRouteLocator(
//        ReactiveDiscoveryClient discoveryClient, DiscoveryLocatorProperties properties) {
//
//        return new DiscoveryClientRouteDefinitionLocator(discoveryClient, properties);
//    }
}
