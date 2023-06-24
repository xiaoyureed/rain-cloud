package io.github.xiaoyureed.raincloud.core.starter.security.sign;

/**
 * 签名异常
 *
 */
public class SignException extends RuntimeException {

    public SignException(String message) {
        super(message);
    }

    public SignException() {
        super("验签失败");
    }
}
