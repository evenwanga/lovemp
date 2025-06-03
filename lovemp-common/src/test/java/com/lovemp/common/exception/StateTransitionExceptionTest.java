package com.lovemp.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 状态转换异常测试
 */
@DisplayName("状态转换异常测试")
class StateTransitionExceptionTest {

    private static final String DEFAULT_ERROR_CODE = "STATE_TRANSITION_ERROR";
    private static final String ERROR_MESSAGE = "无效的状态转换";
    private static final String ENTITY_TYPE = "Order";
    private static final String ENTITY_ID = "ORD-12345";
    private static final String CURRENT_STATE = "PENDING";
    private static final String TARGET_STATE = "COMPLETED";
    private static final String REASON = "订单尚未支付";
    
    @Test
    @DisplayName("测试创建状态转换异常-简单消息")
    void testCreateWithSimpleMessage() {
        // Arrange & Act
        StateTransitionException exception = new StateTransitionException(ERROR_MESSAGE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof BusinessException);
    }
    
    @Test
    @DisplayName("测试创建状态转换异常-带原因")
    void testCreateWithCause() {
        // Arrange
        Throwable cause = new RuntimeException("原始错误");
        
        // Act
        StateTransitionException exception = new StateTransitionException(ERROR_MESSAGE, cause);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("测试创建状态转换异常-实体类型、ID和状态")
    void testCreateWithEntityTypeIdAndStates() {
        // Arrange & Act
        StateTransitionException exception = new StateTransitionException(
                ENTITY_TYPE, ENTITY_ID, CURRENT_STATE, TARGET_STATE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s (ID: %s) 无法从状态 '%s' 转换到 '%s'", 
                ENTITY_TYPE, ENTITY_ID, CURRENT_STATE, TARGET_STATE);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建状态转换异常-带原因说明")
    void testCreateWithEntityTypeIdStatesAndReason() {
        // Arrange & Act
        StateTransitionException exception = new StateTransitionException(
                ENTITY_TYPE, ENTITY_ID, CURRENT_STATE, TARGET_STATE, REASON);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s (ID: %s) 无法从状态 '%s' 转换到 '%s'，原因：%s", 
                ENTITY_TYPE, ENTITY_ID, CURRENT_STATE, TARGET_STATE, REASON);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建状态转换异常-数值类型ID")
    void testCreateWithNumericId() {
        // Arrange
        Long numericId = 12345L;
        
        // Act
        StateTransitionException exception = new StateTransitionException(
                ENTITY_TYPE, numericId, CURRENT_STATE, TARGET_STATE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s (ID: %s) 无法从状态 '%s' 转换到 '%s'", 
                ENTITY_TYPE, numericId, CURRENT_STATE, TARGET_STATE);
        assertEquals(expectedMessage, exception.getMessage());
    }
}