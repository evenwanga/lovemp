package com.lovemp.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 远程服务异常测试
 */
@DisplayName("远程服务异常测试")
class RemoteServiceExceptionTest {

    private static final String DEFAULT_ERROR_CODE = "REMOTE_SERVICE_ERROR";
    private static final String ERROR_MESSAGE = "远程服务调用失败";
    private static final String SERVICE_NAME = "UserService";
    private static final String ENDPOINT = "/api/users";
    private static final String ERROR_DETAILS = "连接超时";
    private static final int STATUS_CODE = 500;
    private static final String ERROR_RESPONSE = "Internal Server Error";
    
    @Test
    @DisplayName("测试创建远程服务异常-简单消息")
    void testCreateWithSimpleMessage() {
        // Arrange & Act
        RemoteServiceException exception = new RemoteServiceException(ERROR_MESSAGE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof BusinessException);
    }
    
    @Test
    @DisplayName("测试创建远程服务异常-带原因")
    void testCreateWithCause() {
        // Arrange
        Throwable cause = new RuntimeException("原始错误");
        
        // Act
        RemoteServiceException exception = new RemoteServiceException(ERROR_MESSAGE, cause);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("测试创建远程服务异常-服务名、接口和错误详情")
    void testCreateWithServiceNameEndpointAndErrorDetails() {
        // Arrange & Act
        RemoteServiceException exception = new RemoteServiceException(
                SERVICE_NAME, ENDPOINT, ERROR_DETAILS);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("调用远程服务 %s 的接口 %s 时发生错误: %s", 
                SERVICE_NAME, ENDPOINT, ERROR_DETAILS);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建远程服务异常-服务名、状态码和错误响应")
    void testCreateWithServiceNameStatusCodeAndErrorResponse() {
        // Arrange & Act
        RemoteServiceException exception = new RemoteServiceException(
                SERVICE_NAME, STATUS_CODE, ERROR_RESPONSE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("远程服务 %s 返回错误状态码 %d: %s", 
                SERVICE_NAME, STATUS_CODE, ERROR_RESPONSE);
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @Test
    @DisplayName("测试创建远程服务异常-边界状态码")
    void testCreateWithBoundaryStatusCodes() {
        // Arrange & Act
        RemoteServiceException exception1 = new RemoteServiceException(
                SERVICE_NAME, 0, ERROR_RESPONSE);
        RemoteServiceException exception2 = new RemoteServiceException(
                SERVICE_NAME, 999, ERROR_RESPONSE);
        
        // Assert
        String expectedMessage1 = String.format("远程服务 %s 返回错误状态码 %d: %s", 
                SERVICE_NAME, 0, ERROR_RESPONSE);
        String expectedMessage2 = String.format("远程服务 %s 返回错误状态码 %d: %s", 
                SERVICE_NAME, 999, ERROR_RESPONSE);
        
        assertEquals(expectedMessage1, exception1.getMessage());
        assertEquals(expectedMessage2, exception2.getMessage());
    }
}