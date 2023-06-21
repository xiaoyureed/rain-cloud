package io.github.xiaoyureed.raincloud.core.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * xiaoyureed@gmail.com
 */
@Getter
@AllArgsConstructor
public enum CodeEnum {

    SUCCESS("SUCCESS", "success"),
    RESOURCES_NOT_EXIST("RESOURCES_NOT_EXIST", "The resources requested don't exist."),
    BIZ_ERROR("BIZ_ERROR", "Business error."),
    SYSTEM_ERROR("SYSTEM_ERROR", "SYSTEM ERROR"),
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "参数不合法"),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "请求方法不允许"),

    INTERFACE_SYSTEM_ERROR("INTERFACE_SYSTEM_ERROR", "外部接口调用异常"),

    CONNECT_TIME_OUT("CONNECT_TIME_OUT", "系统超时"),

    NULL_ARGUMENT("NULL_ARGUMENT", "参数为空"),

    ILLEGAL_REQUEST("ILLEGAL_REQUEST", "非法请求"),

    ILLEGAL_CONFIGURATION("ILLEGAL_CONFIGURATION", "配置不合法"),

    ILLEGAL_STATE("ILLEGAL_STATE", "状态不合法"),

    ENUM_CODE_ERROR("ENUM_CODE_ERROR", "错误的枚举编码"),

    LOGIC_ERROR("LOGIC_ERROR", "逻辑错误"),

    CONCURRENT_ERROR("CONCURRENT_ERROR", "并发异常"),

    ILLEGAL_OPERATION("ILLEGAL_OPERATION", "非法操作"),

    REPETITIVE_OPERATION("REPETITIVE_OPERATION", "重复操作"),

    NO_OPERATE_PERMISSION("NO_OPERATE_PERMISSION", "无操作权限"),

    RESOURCE_NOT_FOUND("RESOURCE_NOT_FOUND", "资源不存在"),

    RESOURCE_ALREADY_EXIST("RESOURCE_ALREADY_EXIST", "资源已存在"),

    TYPE_UN_MATCH("TYPE_UN_MATCH", "类型不匹配"),

    FILE_NOT_EXIST("FILE_NOT_EXIST", "文件不存在"),

    LIMIT_BLOCK("LIMIT_BLOCK", "请求限流阻断"),

    TOKEN_FAIL("TOKEN_FAIL", "token校验失败"),

    TOKEN_EXPIRE("TOKEN_EXPIRE", "token过期"),

    REQUEST_EXCEPTION("REQUEST_EXCEPTION", "请求异常"),

    BLOCK_EXCEPTION("BLOCK_EXCEPTION", "接口限流降级"),

    ;


    private final String code;
    private final String text;
}
