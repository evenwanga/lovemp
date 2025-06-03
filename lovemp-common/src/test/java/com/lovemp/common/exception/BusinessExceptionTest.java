package com.lovemp.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 业务异常基类测试
 */
@DisplayName("业务异常基类测试")
class BusinessExceptionTest {

    private static final String ERROR_CODE = "TEST_ERROR";
    private static final String ERROR_MESSAGE = "测试错误消息";
    
    @Test
    @DisplayName("测试创建业务异常-基本构造函数")
    void testCreateBusinessExceptionWithBasicConstructor() {
        // Arrange & Act
        BusinessException exception = new BusinessException(ERROR_CODE, ERROR_MESSAGE);
        
        // Assert
        assertEquals(ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }
    
    @Test
    @DisplayName("测试创建业务异常-带原因构造函数")
    void testCreateBusinessExceptionWithCause() {
        // Arrange
        Throwable cause = new RuntimeException("原始异常");
        
        // Act
        BusinessException exception = new BusinessException(ERROR_CODE, ERROR_MESSAGE, cause);
        
        // Assert
        assertEquals(ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("测试业务异常继承自RuntimeException")
    void testBusinessExceptionExtendsRuntimeException() {
        // Arrange & Act
        BusinessException exception = new BusinessException(ERROR_CODE, ERROR_MESSAGE);
        
        // Assert
        assertTrue(exception instanceof RuntimeException);
    }
}