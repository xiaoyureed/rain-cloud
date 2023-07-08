package io.github.xiaoyureed.raincloud.service.support.gateway.config;

import java.net.Inet4Address;

import org.springdoc.core.properties.SwaggerUiConfigParameters;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.xiaoyureed.raincloud.service.support.gateway.listener.RefreshRoutesListener;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@Configuration(proxyBeanMethods = false)
@OpenAPIDefinition(info = @Info(
    title = "${spring.application.name}",
    version = "1",
    description = "${spring.application.name} 网关聚合 APIs"
))
@Slf4j
public class OpenAPIConfiguration {

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Value("${server.servlet.context-path:}")
    private String contextPath;

    @Value("${server.port:8080}")
    private String port;

    @Value("${spring.profiles.active:}")
    private String profile;

    @Value("${springdoc.swagger-ui.path:/swagger-ui.html}")
    private String swaggerUiPath;

    @Bean
    public ApplicationRunner openAPIConsole() {
        return args -> {
            String host = Inet4Address.getLocalHost().getHostAddress();
            log.info("\n----------------------------------------------------------\n" +
                    "\t Application: '{}' is running! \n" +
                    "\t Environment:  {} \n" +
                    "\t Spring Doc (local network):  http://{}:{}{}{} \n" +
                    "\t Spring Doc (local):  http://localhost:{}{}{} \n" +
                    "----------------------------------------------------------",
                applicationName,
                profile,
                host, port, contextPath, swaggerUiPath,
                port, contextPath, swaggerUiPath);
        };
    }


}
