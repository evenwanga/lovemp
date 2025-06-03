package com.lovemp.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 并发修改异常测试
 */
@DisplayName("并发修改异常测试")
class ConcurrentModificationExceptionTest {

    private static final String DEFAULT_ERROR_CODE = "CONCURRENT_MODIFICATION";
    private static final String ERROR_MESSAGE = "数据被并发修改";
    private static final String ENTITY_TYPE = "Product";
    private static final String ENTITY_ID = "PRD-12345";
    private static final Long CURRENT_VERSION = 5L;
    private static final Long EXPECTED_VERSION = 4L;
    
    @Test
    @DisplayName("测试创建并发修改异常-简单消息")
    void testCreateWithSimpleMessage() {
        // Arrange & Act
        ConcurrentModificationException exception = new ConcurrentModificationException(ERROR_MESSAGE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof BusinessException);
    }
    
    @Test
    @DisplayName("测试创建并发修改异常-带原因")
    void testCreateWithCause() {
        // Arrange
        Throwable cause = new RuntimeException("原始错误");
        
        // Act
        ConcurrentModificationException exception = new ConcurrentModificationException(ERROR_MESSAGE, cause);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("测试创建并发修改异常-实体类型和ID")
    void testCreateWithEntityTypeAndId() {
        // Arrange & Act
        ConcurrentModificationException exception = new ConcurrentModificationException(
                ENTITY_TYPE, ENTITY_ID);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s (ID: %s) 已被其他用户修改，请刷新后重试", 
                ENTITY_TYPE, ENTITY_ID);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建并发修改异常-带版本信息")
    void testCreateWithVersionInfo() {
        // Arrange & Act
        ConcurrentModificationException exception = new ConcurrentModificationException(
                ENTITY_TYPE, ENTITY_ID, CURRENT_VERSION, EXPECTED_VERSION);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s (ID: %s) 版本冲突，当前版本: %d, 期望版本: %d", 
                ENTITY_TYPE, ENTITY_ID, CURRENT_VERSION, EXPECTED_VERSION);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建并发修改异常-数值类型ID")
    void testCreateWithNumericId() {
        // Arrange
        Long numericId = 12345L;
        
        // Act
        ConcurrentModificationException exception = new ConcurrentModificationException(
                ENTITY_TYPE, numericId);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s (ID: %s) 已被其他用户修改，请刷新后重试", 
                ENTITY_TYPE, numericId);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建并发修改异常-版本为零")
    void testCreateWithZeroVersions() {
        // Arrange
        Long zeroVersion = 0L;
        
        // Act
        ConcurrentModificationException exception = new ConcurrentModificationException(
                ENTITY_TYPE, ENTITY_ID, zeroVersion, zeroVersion);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("%s (ID: %s) 版本冲突，当前版本: %d, 期望版本: %d", 
                ENTITY_TYPE, ENTITY_ID, zeroVersion, zeroVersion);
        assertEquals(expectedMessage, exception.getMessage());
    }
}