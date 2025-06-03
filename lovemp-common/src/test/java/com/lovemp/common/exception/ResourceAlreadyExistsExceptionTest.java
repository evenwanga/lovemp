package com.lovemp.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 资源已存在异常测试
 */
@DisplayName("资源已存在异常测试")
class ResourceAlreadyExistsExceptionTest {

    private static final String DEFAULT_ERROR_CODE = "RESOURCE_ALREADY_EXISTS";
    private static final String ERROR_MESSAGE = "资源已存在";
    private static final String RESOURCE_TYPE = "User";
    private static final String UNIQUE_FIELD = "username";
    private static final String FIELD_VALUE = "admin";
    private static final String ID = "12345";
    
    @Test
    @DisplayName("测试创建资源已存在异常-简单消息")
    void testCreateWithSimpleMessage() {
        // Arrange & Act
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(ERROR_MESSAGE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertTrue(exception instanceof BusinessException);
    }
    
    @Test
    @DisplayName("测试创建资源已存在异常-唯一字段约束")
    void testCreateWithUniqueFieldConstraint() {
        // Arrange & Act
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(
                RESOURCE_TYPE, UNIQUE_FIELD, FIELD_VALUE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s已存在，%s=%s", RESOURCE_TYPE, UNIQUE_FIELD, FIELD_VALUE);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建资源已存在异常-ID约束")
    void testCreateWithIdConstraint() {
        // Arrange & Act
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(
                RESOURCE_TYPE, ID);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s已存在，ID：%s", RESOURCE_TYPE, ID);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建资源已存在异常-数值类型字段值")
    void testCreateWithNumericFieldValue() {
        // Arrange
        int numericValue = 12345;
        
        // Act
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(
                RESOURCE_TYPE, UNIQUE_FIELD, numericValue);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s已存在，%s=%s", RESOURCE_TYPE, UNIQUE_FIELD, numericValue);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建资源已存在异常-复杂对象字段值")
    void testCreateWithComplexObjectFieldValue() {
        // Arrange
        Object complexValue = new TestObject("test");
        
        // Act
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(
                RESOURCE_TYPE, UNIQUE_FIELD, complexValue);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s已存在，%s=%s", RESOURCE_TYPE, UNIQUE_FIELD, complexValue);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    // 测试用的内部类
    private static class TestObject {
        private final String value;
        
        TestObject(String value) {
            this.value = value;
        }
        
        @Override
        public String toString() {
            return "TestObject[" + value + "]";
        }
    }
}