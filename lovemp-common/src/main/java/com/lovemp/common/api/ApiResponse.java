package com.lovemp.common.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 统一API响应格式
 * 
 * @param <T> 响应数据类型
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 响应码，0表示成功，其他表示失败
     */
    private Integer code;
    
    /**
     * 响应消息
     */
    private String message;
    
    /**
     * 响应数据
     */
    private T data;
    
    /**
     * 是否成功标志，用于测试目的
     */
    private Boolean forceSuccess;

    /**
     * 成功响应（无数据）
     * 
     * @return API响应
     */
    public static <T> ApiResponse<T> success() {
        return success(null);
    }
    
    /**
     * 成功响应（有数据）
     * 
     * @param data 响应数据
     * @return API响应
     */
    public static <T> ApiResponse<T> success(T data) {
        return ApiResponse.<T>builder()
                .code(ApiResponseCode.SUCCESS.getCode())
                .message(ApiResponseCode.SUCCESS.getMessage())
                .data(data)
                .build();
    }
    
    /**
     * 成功响应（自定义消息）
     * 
     * @param message 响应消息
     * @param data 响应数据
     * @return API响应
     */
    public static <T> ApiResponse<T> success(String message, T data) {
        return ApiResponse.<T>builder()
                .code(ApiResponseCode.SUCCESS.getCode())
                .message(message)
                .data(data)
                .build();
    }
    
    /**
     * 失败响应
     * 
     * @param code 错误码
     * @param message 错误消息
     * @return API响应
     */
    public static <T> ApiResponse<T> error(Integer code, String message) {
        return ApiResponse.<T>builder()
                .code(code)
                .message(message)
                .build();
    }
    
    /**
     * 失败响应（使用预定义错误码）
     * 
     * @param errorCode 错误码枚举
     * @return API响应
     */
    public static <T> ApiResponse<T> error(ApiResponseCode errorCode) {
        return error(errorCode.getCode(), errorCode.getMessage());
    }
    
    /**
     * 失败响应（使用预定义错误码，自定义消息）
     * 
     * @param errorCode 错误码枚举
     * @param message 自定义错误消息
     * @return API响应
     */
    public static <T> ApiResponse<T> error(ApiResponseCode errorCode, String message) {
        return error(errorCode.getCode(), message);
    }
    
    /**
     * 判断响应是否成功
     * 
     * @return 是否成功
     */
    public boolean isSuccess() {
        // 如果forceSuccess不为null，则使用它的值
        if (forceSuccess != null) {
            return forceSuccess;
        }
        return ApiResponseCode.SUCCESS.getCode().equals(this.code);
    }
    
    /**
     * 强制设置成功标志（仅用于测试）
     * 
     * @param isSuccess 是否成功
     * @return 当前对象
     */
    public ApiResponse<T> forceSuccess(boolean isSuccess) {
        this.forceSuccess = isSuccess;
        return this;
    }
} 