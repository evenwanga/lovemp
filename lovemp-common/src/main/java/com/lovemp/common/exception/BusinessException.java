package com.lovemp.common.exception;

import lombok.Getter;

/**
 * 业务异常基类
 */
@Getter
public class BusinessException extends RuntimeException {
    
    /**
     * 错误码
     */
    private final String errorCode;
    
    /**
     * 构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误消息
     */
    public BusinessException(String errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }
    
    /**
     * 构造函数
     * 
     * @param errorCode 错误码
     * @param message 错误消息
     * @param cause 原始异常
     */
    public BusinessException(String errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }
} 