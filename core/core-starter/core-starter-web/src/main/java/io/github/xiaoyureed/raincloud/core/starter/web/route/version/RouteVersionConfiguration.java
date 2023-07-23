package io.github.xiaoyureed.raincloud.core.starter.web.route.version;

import java.lang.reflect.Method;

import org.springframework.boot.autoconfigure.web.servlet.WebMvcRegistrations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.servlet.mvc.condition.RequestCondition;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * https://www.cnblogs.com/maggieq8324/p/15126269.html
 * https://www.magese.com/2021/10/14/Springboot%E4%BD%BF%E7%94%A8%E8%87%AA%E5%AE%9A%E4%B9%89%E6%B3%A8%E8%A7%A3%E8%BF%9B%E8%A1%8C%E6%96%B9%E6%B3%95%E8%B7%AF%E7%94%B1/
 * https://juejin.cn/post/7120858364229189646
 * xiaoyureed@gmail.com
 */
//@Configuration
public class RouteVersionConfiguration {

    @Bean
    public WebMvcRegistrations webMvcRegistrations() {
        return new WebMvcRegistrations() {
            @Override
            public RequestMappingHandlerMapping getRequestMappingHandlerMapping() {
                return new RequestMappingHandlerMapping() {

                    /**
                     * 获取类级别的自定义条件
                     */
                    @Override
                    protected RequestCondition<?> getCustomTypeCondition(Class<?> handlerClazzType) {
                        ApiRouteVersion anno = AnnotationUtils.findAnnotation(handlerClazzType, ApiRouteVersion.class);
                        if (anno != null) {
                            return new RouteVersionRequestCondition(anno.value());
                        }

                        return null;
                    }

                    /**
                     * 方法级别的自定义条件
                     */
                    @Override
                    protected RequestCondition<?> getCustomMethodCondition(Method method) {
                        ApiRouteVersion annotation = AnnotationUtils.findAnnotation(method, ApiRouteVersion.class);
                        if (annotation != null) {
                            return new RouteVersionRequestCondition(annotation.value());
                        }
                        return null;
                    }
                };
            }

        };
    }

}
