package com.lovemp.common.exception;

/**
 * 资源已存在异常
 */
public class ResourceAlreadyExistsException extends BusinessException {

    private static final String DEFAULT_ERROR_CODE = "RESOURCE_ALREADY_EXISTS";

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public ResourceAlreadyExistsException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param resourceType 资源类型
     * @param uniqueField 唯一字段名
     * @param fieldValue 字段值
     */
    public ResourceAlreadyExistsException(String resourceType, String uniqueField, Object fieldValue) {
        super(DEFAULT_ERROR_CODE, String.format("%s已存在，%s=%s", resourceType, uniqueField, fieldValue));
    }

    /**
     * 构造函数
     *
     * @param resourceType 资源类型
     * @param id 资源ID
     */
    public ResourceAlreadyExistsException(String resourceType, Object id) {
        super(DEFAULT_ERROR_CODE, String.format("%s已存在，ID：%s", resourceType, id));
    }
} 