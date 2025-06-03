package com.lovemp.common.exception;

/**
 * 并发修改异常
 * 用于处理数据并发修改冲突
 */
public class ConcurrentModificationException extends BusinessException {

    private static final String DEFAULT_ERROR_CODE = "CONCURRENT_MODIFICATION";

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public ConcurrentModificationException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause 原始异常
     */
    public ConcurrentModificationException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, cause);
    }

    /**
     * 构造函数
     *
     * @param entityType 实体类型
     * @param entityId 实体ID
     */
    public ConcurrentModificationException(String entityType, Object entityId) {
        super(DEFAULT_ERROR_CODE, 
                String.format("%s (ID: %s) 已被其他用户修改，请刷新后重试", 
                        entityType, entityId));
    }

    /**
     * 构造函数
     *
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param currentVersion 当前版本
     * @param expectedVersion 期望版本
     */
    public ConcurrentModificationException(String entityType, Object entityId, Long currentVersion, Long expectedVersion) {
        super(DEFAULT_ERROR_CODE, 
                String.format("%s (ID: %s) 版本冲突，当前版本: %d, 期望版本: %d", 
                        entityType, entityId, currentVersion, expectedVersion));
    }
} 