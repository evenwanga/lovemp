package com.lovemp.common.api;

import lombok.Getter;

/**
 * API响应码枚举
 */
@Getter
public enum ApiResponseCode {

    // 成功响应码
    SUCCESS(0, "操作成功"),

    // 客户端错误 (1000-1999)
    PARAM_ERROR(1000, "参数错误"),
    UNAUTHORIZED(1001, "未授权或登录已过期"),
    FORBIDDEN(1003, "没有权限进行此操作"),
    RESOURCE_NOT_FOUND(1004, "请求的资源不存在"),
    METHOD_NOT_ALLOWED(1005, "请求方法不允许"),
    TOO_MANY_REQUESTS(1006, "请求过于频繁，请稍后再试"),
    REQUEST_TIMEOUT(1007, "请求超时"),
    VALIDATION_ERROR(1008, "数据验证失败"),
    ILLEGAL_ARGUMENT(1009, "非法参数"),
    BAD_REQUEST(1010, "错误的请求"),
    
    // 业务逻辑错误 (2000-2999)
    BUSINESS_ERROR(2000, "业务处理失败"),
    DOMAIN_RULE_VIOLATION(2001, "违反领域规则"),
    ENTITY_NOT_FOUND(2002, "实体不存在"),
    CONCURRENT_MODIFICATION(2003, "并发修改冲突"),
    RESOURCE_ALREADY_EXISTS(2004, "资源已存在"),
    STATE_TRANSITION_ERROR(2005, "状态转换错误"),
    OPERATION_NOT_ALLOWED(2006, "操作不允许"),
    
    // 系统内部错误 (3000-3999)
    INTERNAL_SERVER_ERROR(3000, "系统内部错误"),
    SERVICE_UNAVAILABLE(3001, "服务不可用"),
    DATABASE_ERROR(3002, "数据库操作失败"),
    REMOTE_SERVICE_ERROR(3003, "远程服务调用失败"),
    IO_ERROR(3004, "IO操作失败"),
    CONFIGURATION_ERROR(3005, "系统配置错误"),
    
    // 未分类错误 (9000-9999)
    UNKNOWN_ERROR(9999, "未知错误");
    
    /**
     * 响应码
     */
    private final Integer code;
    
    /**
     * 响应消息
     */
    private final String message;
    
    /**
     * 构造函数
     *
     * @param code 响应码
     * @param message 响应消息
     */
    ApiResponseCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }
} 