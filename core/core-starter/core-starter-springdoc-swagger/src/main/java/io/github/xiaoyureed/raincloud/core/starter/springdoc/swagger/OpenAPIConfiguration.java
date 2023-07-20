package io.github.xiaoyureed.raincloud.core.starter.springdoc.swagger;

import java.net.Inet4Address;
import java.util.Optional;

import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springdoc.core.properties.SwaggerUiConfigProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

import io.github.xiaoyureed.raincloud.core.starter.common.util.SpringContextUtils;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 * To see how to use springdoc please check :
 * https://juejin.cn/post/7080328458206707720
 * https://blog.lanweihong.com/posts/1527/
 * <p>
 * https://blog.csdn.net/shijizhe1/article/details/130495081
 * https://www.baeldung.com/spring-rest-openapi-documentation
 *
 * @Api → @Tag
 * @ApiIgnore → @Parameter(hidden = true) or @Operation(hidden = true) or @Hidden
 * @ApiImplicitParam → @Parameter
 * @ApiImplicitParams → @Parameters
 * @ApiModel → @Schema
 * @ApiModelProperty(hidden = true) → @Schema(accessMode = READ_ONLY)
 * @ApiModelProperty → @Schema
 * @ApiOperation(value = "foo", notes = "bar") → @Operation(summary = "foo", description = "bar")
 * @ApiParam → @Parameter
 * @ApiResponse(code = 404, message = "foo") → @ApiResponse(responseCode = "404", description = "foo")
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

    @Value("${servlet.port:8080}")
    private String port;


    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
//            .specVersion(SpecVersion.V31)

            // the server url will be auto generated if you haven't specified it
            //默认情况下，Server 地址是获取的当前机器应用的 IP+Port，但是现如今除开发环境外我们服务几乎都是在 Docker 容器中，
            // 这样一来自动获取的时候可能会拿到容器内部的 IP 或者 宿主机 IP，此时使用 swagger 页面访问接口可能会发生 CORS 跨域错误。
            // 所以我们可以在向 Spring 容器注入 OpenAPI 时自己设置 server 列表
//            .servers(List.of(
//                new Server().url(IpUtils.getSelfIp() + ":" + port)
//            ))
            // todo
//            .servers(List.of(
//                new Server().url("http://localhost:8888/" + applicationName)
//            ))

            // oauth2 password
            // 设置 spring security jwt accessToken 认证的请求头 Authorization: Bearer xxx.xxx.xxx
            .addSecurityItem(new SecurityRequirement().addList("Bearer Authentication"))
            .components(new Components()
                .addSecuritySchemes("Bearer Authentication",    // 显示的名字
                    new SecurityScheme()
                        .type(SecurityScheme.Type.HTTP)
                        .name("Authentication")         // 接收token 的参数名字
                        .in(SecurityScheme.In.HEADER)   // 位置
                        .bearerFormat("JWT")
                        .scheme("bearer")
                )
            )
            // 效果等价上面的 components()
//            .schemaRequirement("Bearer Authentication",
//                new SecurityScheme().type(SecurityScheme.Type.HTTP) // 类型
//                    .name(HttpHeaders.AUTHORIZATION) //请求头的name
//                    .in(SecurityScheme.In.HEADER) // token 所在位置
//                    .bearerFormat("JWT")
//                    .scheme("bearer")
//
//            )

            .externalDocs(externalDocumentation())
            .info(info());

    }

    /**
     * 用来在代码中自定义springdoc 配置
     */
//    @Bean
    public SpringDocConfigProperties springDocConfigProperties(SpringDocConfigProperties config) {
        config.setEnableJavadoc(true);
//        config.setEnableSpringSecurity(true);
        config.setShowActuator(true);
        config.setModelAndViewAllowed(true); //#运行modelAndView展示（返回页面）

        SpringDocConfigProperties.ApiDocs apiDocs = new SpringDocConfigProperties.ApiDocs();
        apiDocs.setEnabled(true);
        apiDocs.setPath("/v3/api-docs");
        apiDocs.setVersion(SpringDocConfigProperties.ApiDocs.OpenApiVersion.OPENAPI_3_1);
        config.setApiDocs(apiDocs);


//        config.setGroupConfigs();


        return config;
    }

//    @Bean
//    @Primary
    public SwaggerUiConfigProperties swaggerUiConfigProperties(SwaggerUiConfigProperties config) {
        config.setEnabled(true);
        config.setDisplayRequestDuration(true);
        config.setDisableSwaggerDefaultUrl(true);
        config.setPath("/swagger-ui.html");
        config.setOperationsSorter("method");//api排序方式 alpha 字母 method http方法
//        config.setGroupsOrder(AbstractSwaggerUiConfigProperties.Direction.ASC);


        SwaggerUiConfigProperties.Csrf csrf = new SwaggerUiConfigProperties.Csrf();
        csrf.setEnabled(true);
        config.setCsrf(csrf);


        return config;
    }

    @Bean
    public ApplicationRunner openAPIConsole() {
        return args -> {
            String contextPath = Optional.ofNullable(SpringContextUtils.getProperty("server.servlet.context-path")).orElse("");
            String swaggerUiPath = Optional.ofNullable(SpringContextUtils.getProperty("springdoc.swagger-ui.path")).orElse("/swagger-ui.html");
            String host = Inet4Address.getLocalHost().getHostAddress();
            String port = Optional.ofNullable(SpringContextUtils.getProperty("server.port")).orElse("8080");
            String profile = SpringContextUtils.getPrettyActiveProfiles();
            log.info("\n----------------------------------------------------------\n" +
                    "\t Application: '{}' is running! \n" +
                    "\t Environment:  {} \n" +
                    "\t Spring Doc (remote):  http://{}:{}{}{} \n" +
                    "\t Spring Doc (local):  http://localhost:{}{}{} \n" +
                    "----------------------------------------------------------",
                applicationName,
                profile,
                host, port, contextPath, swaggerUiPath,
                port, contextPath, swaggerUiPath);
        };
    }

    private Info info() {
        return new Info()
            .title(applicationName)
            .description(applicationName + " APIs")
            .version(openAPIProperties.getVersion())
            .contact(new Contact().name("xiaoyureed").email("xiaoyureed@gmail.com").url("xiaoyureed.github.io"))
            .license(license());
    }

    private ExternalDocumentation externalDocumentation() {
        return new ExternalDocumentation()
            .description("这是一个额外的描述。")
            .url("xxx");
    }


    private License license() {
        return new License()
            .name("MIT")
            .url("https://opensource.org/licenses/MIT");
    }
}
