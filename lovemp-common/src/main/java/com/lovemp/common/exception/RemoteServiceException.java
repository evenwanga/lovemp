package com.lovemp.common.exception;

/**
 * 远程服务异常
 * 用于处理调用远程服务时发生的错误
 */
public class RemoteServiceException extends BusinessException {

    private static final String DEFAULT_ERROR_CODE = "REMOTE_SERVICE_ERROR";

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public RemoteServiceException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause 原始异常
     */
    public RemoteServiceException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, cause);
    }

    /**
     * 构造函数
     *
     * @param serviceName 服务名称
     * @param endpoint 接口名称
     * @param errorDetails 错误详情
     */
    public RemoteServiceException(String serviceName, String endpoint, String errorDetails) {
        super(DEFAULT_ERROR_CODE, 
                String.format("调用远程服务 %s 的接口 %s 时发生错误: %s", 
                        serviceName, endpoint, errorDetails));
    }

    /**
     * 构造函数
     *
     * @param serviceName 服务名称
     * @param statusCode 状态码
     * @param errorResponse 错误响应
     */
    public RemoteServiceException(String serviceName, int statusCode, String errorResponse) {
        super(DEFAULT_ERROR_CODE, 
                String.format("远程服务 %s 返回错误状态码 %d: %s", 
                        serviceName, statusCode, errorResponse));
    }
} 