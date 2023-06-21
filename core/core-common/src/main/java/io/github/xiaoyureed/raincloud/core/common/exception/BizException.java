package io.github.xiaoyureed.raincloud.core.common.exception;

import io.github.xiaoyureed.raincloud.core.common.model.CodeEnum;
import lombok.Data;

/**
 * This class represent a business exception
 */
@Data
public class BizException extends RuntimeException {
    private final String code;
    private final String error;

    public BizException(CodeEnum errorEnum) {
        super(errorEnum.getCode() + "-" + errorEnum.getText());
        this.code = errorEnum.getCode();
        this.error = errorEnum.getText();

    }

    public BizException(String code, String error) {
        super(code + "-" + error);
        this.code = code;
        this.error = error;
    }

    public BizException(String error) {
        super(error);
        this.code = CodeEnum.BIZ_ERROR.getCode();
        this.error = error;
    }

}
