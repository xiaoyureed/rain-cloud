package io.github.xiaoyureed.raincloud.core.starter.springdoc.swagger;

import java.net.Inet4Address;
import java.util.Optional;

import org.springdoc.core.models.GroupedOpenApi;
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

    private static final String security_schema_name_http_bearer = "Bearer Authentication";

    @Autowired
    private OpenAPIProperties openAPIProperties;

    @Value("${spring.application.name:unknown}")
    private String applicationName;

    @Value("${servlet.port:8080}")
    private String port;

//    @Bean
    public GroupedOpenApi groupedOpenApiaaa() {
        return GroupedOpenApi.builder()
            .group("aaa") // showed name
            .pathsToMatch("/aaa/**") // matched paths
            .build();
    }

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

            .addSecurityItem(new SecurityRequirement().addList(security_schema_name_http_bearer)) // 和后面的schema名字必须一致
            //swagger ui 页面会新增一个组件 "Authentication", 点开可以填充 token
            .components(new Components()
                .addSecuritySchemes(security_schema_name_http_bearer,    // ui 上显示的名字
                    new SecurityScheme()
                        /**
                         * 认证类型
                         * - http (常用): 比如
                         *      - http basic 认证, 不推荐
                         *      - http bearer 认证
                         * - apikey: 就是在Header、Query或者Cookie中附加特定的访问Key即可，如果你愿意，放在Url Path中都行
                         *      - 明文 key: 服务器分配一个 key 给 Client, 调用方直接附加这个值用于认证, 等同于Basic认证。(一旦泄露，即可被重复使用)
                         *      - 可解密的 key: 服务器先生成一个类似 md5 加密的Token再交予客户端, 等同 bearer 认证
                         *      - 可按私有规则解密的 key (用于服务器对服务器场景): 比如这个 key -> sign = md5(arg1+'+'+arg2+'+'+timestamp+'+'+secret),
                         *          再把userid、sign和明文的arg1、arg2和时间戳一起传给服务器，服务器拿出该user的secret后再拼接出这个sign，比较之后就算通过。
                         * - oauth2
                         *      - Authorization code模式 (用于第三方应用场景)
                         *      - Implicit : 简单版Authorization code，不能刷新，只能通过浏览器维持登录
                         */
                        .type(SecurityScheme.Type.HTTP)
                        /**
                         * 仅适用于 http 认证类型:
                         * - basic : 前缀 Basic
                         * - bearer: 前缀 Bearer
                         */
                        .scheme("bearer")
                        /**
                         * 用于 apiKey 认证类型下, key 的名字 (http bearer 认证类型下会忽略)
                         */
                        .name(security_schema_name_http_bearer)
                        // 参数所在位置, 适用于 apikey 类型 (http 认证会忽略, 始终是在 header, )
                        .in(SecurityScheme.In.HEADER)
                        .bearerFormat("JWT") // optional, jwt http bearer 认证可加上
                        // 支持的 flow types 的配置信息, 仅适用于 oauth 认证
//                        .flows()
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
