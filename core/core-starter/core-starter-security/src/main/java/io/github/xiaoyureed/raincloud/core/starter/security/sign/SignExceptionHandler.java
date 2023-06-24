package io.github.xiaoyureed.raincloud.core.starter.security.sign;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import io.github.xiaoyureed.raincloud.core.common.util.ServletUtils;
import lombok.extern.slf4j.Slf4j;

/**
 * 接口验签异常处理
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class SignExceptionHandler {

    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(SignException.class)
    public ResponseWrapper<Void> handleException(SignException signException) {
        log.error("接口验签异常，可能遭遇攻击，uri：{}，异常信息：{}", ServletUtils.getRequestInfo(), signException.getMessage());
        return ResponseWrapper.error(CodeEnum.ILLEGAL_REQUEST);
    }
}
