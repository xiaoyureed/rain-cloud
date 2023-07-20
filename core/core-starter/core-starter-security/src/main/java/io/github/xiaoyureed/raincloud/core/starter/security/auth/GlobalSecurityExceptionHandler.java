package io.github.xiaoyureed.raincloud.core.starter.security.auth;


import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * xiaoyureed@gmail.com
 */
@ControllerAdvice
@Slf4j
public class GlobalSecurityExceptionHandler {

    @ResponseBody
    @ExceptionHandler(value = AccessDeniedException.class)
    public ResponseWrapper<Void> handleAccessDeniedException(AccessDeniedException e) {
        log.error("❌当前登录用户权限不足, uri: {}, 异常信息: {}", ServletUtils.getRequestInfo(), e.getMessage());
        return ResponseWrapper.error(CodeEnum.UNAUTHORIZED_ACCESS_DENIED_ERROR);
    }

    @ResponseBody
    @ExceptionHandler(AuthenticationException.class)
    public ResponseWrapper<Void> handleAuthenticationException(AuthenticationException e) {
        log.error("❌当前发送请求的用户未经认证, uri: {}, 异常信息: {}", ServletUtils.getRequestInfo(), e.getMessage());
        return ResponseWrapper.error(CodeEnum.UNAUTHENTICATED_ERROR);
    }
}
