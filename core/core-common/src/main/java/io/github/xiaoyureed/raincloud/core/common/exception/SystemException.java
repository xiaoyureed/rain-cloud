package io.github.xiaoyureed.raincloud.core.common.exception;

import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import lombok.Data;

/**
 * @author: xiaoyureed@gmail.com
 */
@Data
public class SystemException extends RuntimeException{
    private final String code;
    private final String error;

    public SystemException(CodeEnum code) {
        super(code.getCode() + "-" + code.getText());
        this.code = code.getCode();
        this.error = code.getText();
    }

    public SystemException(String error) {
        super(error);
        this.code = CodeEnum.SYSTEM_ERROR.getCode();
        this.error = error;
    }
}
