package com.lovemp.common.exception;

/**
 * 实体未找到异常
 */
public class EntityNotFoundException extends BusinessException {

    private static final String DEFAULT_ERROR_CODE = "ENTITY_NOT_FOUND";

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public EntityNotFoundException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param entityType 实体类型
     * @param id 实体标识
     */
    public EntityNotFoundException(String entityType, Object id) {
        super(DEFAULT_ERROR_CODE, String.format("未找到%s，ID：%s", entityType, id));
    }

    /**
     * 构造函数
     *
     * @param entityType 实体类型
     * @param fieldName 字段名
     * @param fieldValue 字段值
     */
    public EntityNotFoundException(String entityType, String fieldName, Object fieldValue) {
        super(DEFAULT_ERROR_CODE, String.format("未找到%s，%s=%s", entityType, fieldName, fieldValue));
    }
} 