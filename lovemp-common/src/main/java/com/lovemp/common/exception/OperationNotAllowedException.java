package com.lovemp.common.exception;

/**
 * 操作不允许异常
 * 用于处理当前上下文或状态下不允许执行的操作
 */
public class OperationNotAllowedException extends BusinessException {

    private static final String DEFAULT_ERROR_CODE = "OPERATION_NOT_ALLOWED";

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public OperationNotAllowedException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause 原始异常
     */
    public OperationNotAllowedException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, cause);
    }

    /**
     * 构造函数
     *
     * @param operation 操作名称
     * @param entityType 实体类型
     * @param entityId 实体ID
     */
    public OperationNotAllowedException(String operation, String entityType, Object entityId) {
        super(DEFAULT_ERROR_CODE, 
                String.format("当前状态下不允许对 %s (ID: %s) 执行 %s 操作", 
                        entityType, entityId, operation));
    }

    /**
     * 构造函数
     *
     * @param operation 操作名称
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param reason 原因
     */
    public OperationNotAllowedException(String operation, String entityType, Object entityId, String reason) {
        super(DEFAULT_ERROR_CODE, 
                String.format("当前状态下不允许对 %s (ID: %s) 执行 %s 操作，原因：%s", 
                        entityType, entityId, operation, reason));
    }
} 