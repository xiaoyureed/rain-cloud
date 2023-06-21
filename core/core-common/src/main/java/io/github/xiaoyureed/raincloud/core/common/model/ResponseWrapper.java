package io.github.xiaoyureed.raincloud.core.common.model;

import java.util.List;

import io.github.xiaoyureed.raincloud.core.common.model.page.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
@Schema(description = "response info wrapper dto")
public class ResponseWrapper<T> {
    @Schema(description = "response code")
    private final String code;

    @Schema(description = "response error message")
    private final String error;

    @Schema(description = "the real data")
    private final T data;

    @Schema(description = "pagination info")
    private final IPage pageInfo;

    public static <T> ResponseWrapper<T> ok() {
        return new ResponseWrapper<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getText(), null, null);
    }

    public static <T> ResponseWrapper<T> ok(T data) {
        return new ResponseWrapper<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getText(), data, null);
    }

    public static <T> ResponseWrapper<List<T>> okPage(List<T> data, IPage pageParam) {
        return new ResponseWrapper<>(CodeEnum.SUCCESS.getCode(), CodeEnum.SUCCESS.getText(), data, pageParam);
    }

    public static <T> ResponseWrapper<T> error(String code, String error) {
        return new ResponseWrapper<>(code, error, null, null);
    }

    public static <T> ResponseWrapper<T> error(CodeEnum errorEnum) {
        return new ResponseWrapper<>(errorEnum.getCode(), errorEnum.getText(), null, null);
    }
}
