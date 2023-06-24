package io.github.xiaoyureed.raincloud.core.starter.security.sign;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 签名注解
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target(ElementType.METHOD)
public @interface Sign {
    String signKey() default "s";

    String timeStampKey() default "t";

    int timeout() default 30;

    boolean hideError() default false;
}
