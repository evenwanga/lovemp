package com.lovemp.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 实体未找到异常测试
 */
@DisplayName("实体未找到异常测试")
class EntityNotFoundExceptionTest {

    private static final String DEFAULT_ERROR_CODE = "ENTITY_NOT_FOUND";
    private static final String ERROR_MESSAGE = "未找到实体";
    private static final String ENTITY_TYPE = "Person";
    private static final String ID = "12345";
    private static final String FIELD_NAME = "email";
    private static final String FIELD_VALUE = "test@example.com";
    
    @Test
    @DisplayName("测试创建实体未找到异常-简单消息")
    void testCreateWithSimpleMessage() {
        // Arrange & Act
        EntityNotFoundException exception = new EntityNotFoundException(ERROR_MESSAGE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertTrue(exception instanceof BusinessException);
    }
    
    @Test
    @DisplayName("测试创建实体未找到异常-实体类型和ID")
    void testCreateWithEntityTypeAndId() {
        // Arrange & Act
        EntityNotFoundException exception = new EntityNotFoundException(ENTITY_TYPE, ID);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("未找到%s，ID：%s", ENTITY_TYPE, ID);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建实体未找到异常-实体类型、字段名和字段值")
    void testCreateWithEntityTypeFieldNameAndValue() {
        // Arrange & Act
        EntityNotFoundException exception = new EntityNotFoundException(ENTITY_TYPE, FIELD_NAME, FIELD_VALUE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("未找到%s，%s=%s", ENTITY_TYPE, FIELD_NAME, FIELD_VALUE);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建实体未找到异常-数值类型ID")
    void testCreateWithNumericId() {
        // Arrange
        Long numericId = 12345L;
        
        // Act
        EntityNotFoundException exception = new EntityNotFoundException(ENTITY_TYPE, numericId);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("未找到%s，ID：%s", ENTITY_TYPE, numericId);
        assertEquals(expectedMessage, exception.getMessage());
    }
}