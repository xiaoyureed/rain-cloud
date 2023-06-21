package io.github.xiaoyureed.raincloud.core.starter.feign.exception;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import feign.FeignException;
import feign.codec.DecodeException;
import io.github.xiaoyureed.raincloud.core.common.exception.BizException;
import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import io.github.xiaoyureed.raincloud.core.common.model.ResponseWrapper;
import lombok.extern.slf4j.Slf4j;

/**
 * 远程调用异常处理
 */
@Slf4j
@ControllerAdvice
@Order(Ordered.HIGHEST_PRECEDENCE)
public class FeignExceptionHandler {
    /**
     * feign异常（远程服务抛出的异常）
     */
    @ResponseBody
    @ExceptionHandler(FeignException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseWrapper<Void> handleException(FeignException exception) {
        // 打印堆栈信息
        log.error("❌远程调用时发生系统异常，ℹ原始异常信息：【{}】", exception.getMessage());
        return ResponseWrapper.error(CodeEnum.INTERFACE_SYSTEM_ERROR.getCode(), "原始异常信息：" + exception.getMessage());
    }


    @ResponseBody
    @ExceptionHandler(DecodeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseWrapper<Void> handleException(DecodeException exception) {
        if (exception.getCause() instanceof BizException bizException) {
            // 业务异常，包装返回
            return ResponseWrapper.error(bizException.getCode(), bizException.getError());
        }
        // 否则打印错误日志并返回
        log.error("❌远程调用结束解析返回值时出现异常", exception);
        return ResponseWrapper.error(CodeEnum.SYSTEM_ERROR.getCode(), exception.getMessage());
    }

}
