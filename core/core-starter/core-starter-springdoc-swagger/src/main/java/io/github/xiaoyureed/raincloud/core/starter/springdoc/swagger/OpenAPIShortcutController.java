package io.github.xiaoyureed.raincloud.core.starter.springdoc.swagger;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;
import jakarta.servlet.http.HttpServletResponse;

/**
 * xiaoyureed@gmail.com
 * This controller is used to redirect the root path for every single application
 */
@Controller
public class OpenAPIShortcutController {

    private final Environment environment;

    public OpenAPIShortcutController(Environment environment) {
        this.environment = environment;
    }

    /**
     * swagger document 根路径快速跳转
     */
    @GetMapping("/")
    public void index(HttpServletResponse response) throws IOException {
        String contextPath = environment.getProperty("server.servlet.context-path");
        ServletUtils.redirect(response,
            (StringUtils.isNotEmpty(contextPath) ? contextPath : StringUtils.EMPTY).concat("/swagger-ui.html")
        );
    }
}
