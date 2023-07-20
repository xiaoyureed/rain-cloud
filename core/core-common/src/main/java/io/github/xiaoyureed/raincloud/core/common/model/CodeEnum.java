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

    ////// business error ///////
    BIZ_ERROR("BIZ_ERROR", "Business error."),

    ////// business error ///////

    ///// system error ////////

    SYSTEM_ERROR("SYSTEM_ERROR", "SYSTEM ERROR"),
    ILLEGAL_ARGUMENT("ILLEGAL_ARGUMENT", "参数不合法"),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "请求方法不允许"),

    INTERFACE_SYSTEM_ERROR("INTERFACE_SYSTEM_ERROR", "接口远程调用异常"),

    CONNECT_TIME_OUT("CONNECT_TIME_OUT", "系统超时"),

    ILLEGAL_REQUEST("ILLEGAL_REQUEST", "非法请求"),

    ILLEGAL_CONFIGURATION("ILLEGAL_CONFIGURATION", "配置不合法"),

    ILLEGAL_LOGIN_STATE("ILLEGAL_LOGIN_STATE", "登录状态不合法"),

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

    USERNAME_PASSWORD_WRONG("USERNAME_PASSWORD_WRONG", "用户名 or 密码错误"),
    UNAUTHENTICATED_ERROR("UNAUTHENTICATED_ERROR", "未经认证的请求, 请先认证"),
    UNAUTHORIZED_ACCESS_DENIED_ERROR("UNAUTHORIZED_ACCESS_DENIED_ERROR", "鉴权错误, 当前登录用户权限不足, 拒绝访问"),
    TOKEN_MISSED("TOKEN_MISSED", "缺失 token"),
    TOKEN_FAIL("TOKEN_FAIL", "token校验失败"),
    TOKEN_FORMAT_ERROR("TOKEN_FORMAT_ERROR", "token 格式错误"),

    TOKEN_EXPIRE("TOKEN_EXPIRE", "token过期"),

    BLOCK_EXCEPTION("BLOCK_EXCEPTION", "接口限流降级"),

    ///// system error ////////


    ;


    private final String code;
    private final String text;
}
