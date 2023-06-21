package io.github.xiaoyureed.raincloud.core.starter.springdoc.swagger;

import java.net.InetAddress;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.github.xiaoyureed.raincloud.core.starter.common.util.SpringContextUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 * To see how to use springdoc please check :
 * https://juejin.cn/post/7080328458206707720
 * https://blog.lanweihong.com/posts/1527/
 *
 * https://blog.csdn.net/shijizhe1/article/details/130495081
 * https://www.baeldung.com/spring-rest-openapi-documentation
 */
@Configuration
@OpenAPIDefinition
@Slf4j
@EnableConfigurationProperties({
    OpenAPIProperties.class
})
public class OpenAPIConfiguration {

    @Autowired
    private OpenAPIProperties openAPIProperties;

    @Value("${spring.application.name:unknown}")
    private String applicationName;


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
            .servers(List.of(new Server().url("xxx")))
            .externalDocs(externalDocumentation())
            .info(info());

    }

    @Bean
    public ApplicationRunner openAPIConsole() {
        return new ApplicationRunner() {
            @Override
            public void run(ApplicationArguments args) throws Exception {
                String contextPath = Optional.ofNullable(SpringContextUtils.getProperty("server.servlet.context-path")).orElse("");
                String host = InetAddress.getLocalHost().getHostAddress();
                String port = Optional.ofNullable(SpringContextUtils.getProperty("server.port")).orElse("8080");
                String profile = SpringContextUtils.getProfile();
                log.info("\n----------------------------------------------------------\n" +
                        "\t Application: '{}' is running! \n" +
                        "\t Environment:  {} \n" +
                        "\t Spring Doc:  http://{}:{}{}/swagger-ui.html \n" +
                        "----------------------------------------------------------",
                    applicationName, profile, host, port, contextPath);
            }
        };
    }

    private Info info() {
        return new Info()
            .title(applicationName)
            .description(applicationName + " APIs")
            .version("1.0-SNAPSHOT")
            .contact(new Contact().name("xiaoyureed").email("xiaoyureed@gmail.com").url("xiaoyureed.github.io"))
            .license(license());
    }

    private ExternalDocumentation externalDocumentation() {
        return new ExternalDocumentation()
            .description("这是一个额外的描述。")
            .url("https://shijizhe.github.io/");
    }


    private License license() {
        return new License()
            .name("MIT")
            .url("https://opensource.org/licenses/MIT");
    }
}
