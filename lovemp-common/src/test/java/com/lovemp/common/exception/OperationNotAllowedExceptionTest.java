package com.lovemp.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 操作不允许异常测试
 */
@DisplayName("操作不允许异常测试")
class OperationNotAllowedExceptionTest {

    private static final String DEFAULT_ERROR_CODE = "OPERATION_NOT_ALLOWED";
    private static final String ERROR_MESSAGE = "当前状态下不允许此操作";
    private static final String OPERATION = "cancel";
    private static final String ENTITY_TYPE = "Order";
    private static final String ENTITY_ID = "ORD-12345";
    private static final String REASON = "订单已发货";
    
    @Test
    @DisplayName("测试创建操作不允许异常-简单消息")
    void testCreateWithSimpleMessage() {
        // Arrange & Act
        OperationNotAllowedException exception = new OperationNotAllowedException(ERROR_MESSAGE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof BusinessException);
    }
    
    @Test
    @DisplayName("测试创建操作不允许异常-带原因")
    void testCreateWithCause() {
        // Arrange
        Throwable cause = new RuntimeException("原始错误");
        
        // Act
        OperationNotAllowedException exception = new OperationNotAllowedException(ERROR_MESSAGE, cause);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("测试创建操作不允许异常-操作、实体类型和实体ID")
    void testCreateWithOperationEntityTypeAndId() {
        // Arrange & Act
        OperationNotAllowedException exception = new OperationNotAllowedException(
                OPERATION, ENTITY_TYPE, ENTITY_ID);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("当前状态下不允许对 %s (ID: %s) 执行 %s 操作", 
                ENTITY_TYPE, ENTITY_ID, OPERATION);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建操作不允许异常-带原因说明")
    void testCreateWithOperationEntityTypeIdAndReason() {
        // Arrange & Act
        OperationNotAllowedException exception = new OperationNotAllowedException(
                OPERATION, ENTITY_TYPE, ENTITY_ID, REASON);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("当前状态下不允许对 %s (ID: %s) 执行 %s 操作，原因：%s", 
                ENTITY_TYPE, ENTITY_ID, OPERATION, REASON);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建操作不允许异常-数值类型ID")
    void testCreateWithNumericId() {
        // Arrange
        Long numericId = 12345L;
        
        // Act
        OperationNotAllowedException exception = new OperationNotAllowedException(
                OPERATION, ENTITY_TYPE, numericId);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("当前状态下不允许对 %s (ID: %s) 执行 %s 操作", 
                ENTITY_TYPE, numericId, OPERATION);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建操作不允许异常-空参数")
    void testCreateWithEmptyParameters() {
        // Arrange & Act
        OperationNotAllowedException exception = new OperationNotAllowedException(
                "", "", "");
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("当前状态下不允许对 %s (ID: %s) 执行 %s 操作", 
                "", "", "");
        assertEquals(expectedMessage, exception.getMessage());
    }
}