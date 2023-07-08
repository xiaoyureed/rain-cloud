package io.github.xiaoyureed.raincloud.core.common.model;

import java.io.Serializable;
import java.util.List;

import org.springframework.http.HttpStatus;

import io.github.xiaoyureed.raincloud.core.common.model.page.IPage;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(title = "response info wrapper dto", description = "All of the rest apis return this dto.",
    example = "ResponseWrapper.ok(xxx)")
@Accessors
public class ResponseWrapper<T> implements Serializable {

    @Schema(title = "http status")
    private int httpStatus;

    @Schema(description = "response code")
    private String code;

    @Schema(description = "response error message")
    private String error;

    @Schema(description = "the real data")
    private T data;

    @Schema(description = "pagination info")
    private IPage pageInfo;

    public static <T> ResponseWrapper<T> ok() {
        return new ResponseWrapper<T>()
            .setCode(CodeEnum.SUCCESS.getCode())
            .setHttpStatus(HttpStatus.OK.value());
    }

    public static <T> ResponseWrapper<T> ok(T data) {
        return new ResponseWrapper<T>()
            .setCode(CodeEnum.SUCCESS.getCode())
            .setHttpStatus(HttpStatus.OK.value())
            .setData(data);
    }

    public static <T> ResponseWrapper<List<T>> okPage(List<T> data, IPage pageParam) {
        return new ResponseWrapper<List<T>>()
            .setHttpStatus(HttpStatus.OK.value())
            .setCode(CodeEnum.SUCCESS.getCode())
            .setData(data)
            .setPageInfo(pageParam);
    }

    public static <T> ResponseWrapper<T> error(String code, String error) {
        return new ResponseWrapper<T>()
            .setHttpStatus(HttpStatus.OK.value())
            .setCode(code)
            .setError(error);
    }

    public static <T> ResponseWrapper<T> error(CodeEnum errorEnum) {
        return error(errorEnum, HttpStatus.OK);
    }

    public static <T> ResponseWrapper<T> error(CodeEnum errorEnum, HttpStatus httpStatus) {
        return new ResponseWrapper<T>()
            .setHttpStatus(httpStatus.value())
            .setCode(errorEnum.getCode())
            .setError(errorEnum.getText());
    }
}
