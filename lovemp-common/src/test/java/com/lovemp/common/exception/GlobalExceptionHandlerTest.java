package com.lovemp.common.exception;

import com.lovemp.common.api.ApiResponse;
import com.lovemp.common.api.ApiResponseCode;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import jakarta.validation.Path;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.MethodParameter;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

/**
 * 全局异常处理器测试
 */
@DisplayName("全局异常处理器测试")
class GlobalExceptionHandlerTest {

    private GlobalExceptionHandler exceptionHandler;
    
    @Mock
    private MethodArgumentNotValidException methodArgumentNotValidException;
    
    @Mock
    private BindingResult bindingResult;
    
    @Mock
    private BindException bindException;
    
    @Mock
    private ConstraintViolationException constraintViolationException;
    
    @Mock
    private ConstraintViolation<?> constraintViolation;
    
    @Mock
    private Path path;
    
    @Mock
    private MethodArgumentTypeMismatchException methodArgumentTypeMismatchException;
    
    @Mock
    private MethodParameter methodParameter;
    
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        exceptionHandler = new GlobalExceptionHandler();
    }
    
    @Test
    @DisplayName("测试处理业务异常")
    void testHandleBusinessException() {
        // Arrange
        String errorCode = "BUSINESS_ERROR";
        String errorMessage = "业务异常测试";
        BusinessException exception = new BusinessException(errorCode, errorMessage);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleBusinessException(exception);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertFalse(body.isSuccess());
        assertNull(body.getData());
    }
    
    @Test
    @DisplayName("测试处理实体未找到异常")
    void testHandleEntityNotFoundException() {
        // Arrange
        String errorMessage = "未找到Person实体，ID：123";
        EntityNotFoundException exception = new EntityNotFoundException(errorMessage);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleEntityNotFoundException(exception);
        
        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertEquals(ApiResponseCode.ENTITY_NOT_FOUND.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理领域规则违反异常")
    void testHandleDomainRuleViolationException() {
        // Arrange
        String errorMessage = "违反了领域规则";
        DomainRuleViolationException exception = new DomainRuleViolationException(errorMessage);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleDomainRuleViolationException(exception);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertEquals(ApiResponseCode.DOMAIN_RULE_VIOLATION.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理方法参数校验异常")
    void testHandleMethodArgumentNotValidException() {
        // Arrange
        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(new FieldError("object", "field1", "field1不能为空"));
        fieldErrors.add(new FieldError("object", "field2", "field2长度必须在1-10之间"));
        
        when(methodArgumentNotValidException.getBindingResult()).thenReturn(bindingResult);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleMethodArgumentNotValidException(methodArgumentNotValidException);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getMessage().contains("field1不能为空"));
        assertTrue(body.getMessage().contains("field2长度必须在1-10之间"));
        assertEquals(ApiResponseCode.VALIDATION_ERROR.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理资源已存在异常")
    void testHandleResourceAlreadyExistsException() {
        // Arrange
        String errorMessage = "资源已存在";
        ResourceAlreadyExistsException exception = new ResourceAlreadyExistsException(errorMessage);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleResourceAlreadyExistsException(exception);
        
        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertEquals(ApiResponseCode.RESOURCE_ALREADY_EXISTS.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理状态转换异常")
    void testHandleStateTransitionException() {
        // Arrange
        String errorMessage = "无效的状态转换";
        StateTransitionException exception = new StateTransitionException(errorMessage);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleStateTransitionException(exception);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertEquals(ApiResponseCode.STATE_TRANSITION_ERROR.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理远程服务异常")
    void testHandleRemoteServiceException() {
        // Arrange
        String errorMessage = "远程服务调用失败";
        RemoteServiceException exception = new RemoteServiceException(errorMessage);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleRemoteServiceException(exception);
        
        // Assert
        assertEquals(HttpStatus.SERVICE_UNAVAILABLE, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertEquals(ApiResponseCode.REMOTE_SERVICE_ERROR.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理并发修改异常")
    void testHandleConcurrentModificationException() {
        // Arrange
        String errorMessage = "数据被并发修改";
        ConcurrentModificationException exception = new ConcurrentModificationException(errorMessage);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleConcurrentModificationException(exception);
        
        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertEquals(ApiResponseCode.CONCURRENT_MODIFICATION.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理操作不允许异常")
    void testHandleOperationNotAllowedException() {
        // Arrange
        String errorMessage = "当前状态下不允许此操作";
        OperationNotAllowedException exception = new OperationNotAllowedException(errorMessage);
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleOperationNotAllowedException(exception);
        
        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertEquals(errorMessage, body.getMessage());
        assertEquals(ApiResponseCode.OPERATION_NOT_ALLOWED.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理数据完整性违反异常")
    void testHandleDataIntegrityViolationException() {
        // Arrange
        DataIntegrityViolationException exception = new DataIntegrityViolationException("数据完整性约束违反");
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleDataIntegrityViolationException(exception);
        
        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getMessage().contains("数据完整性约束违反"));
        assertEquals(ApiResponseCode.DATABASE_ERROR.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理乐观锁异常")
    void testHandleOptimisticLockingFailureException() {
        // Arrange
        OptimisticLockingFailureException exception = new OptimisticLockingFailureException("数据已被修改");
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleOptimisticLockingFailureException(exception);
        
        // Assert
        assertEquals(HttpStatus.CONFLICT, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getMessage().contains("数据已被其他用户修改"));
        assertEquals(2003, body.getCode());
        assertFalse(body.isSuccess());
    }
    
    @Test
    @DisplayName("测试处理未知异常")
    void testHandleException() {
        // Arrange
        Exception exception = new RuntimeException("未知错误");
        
        // Act
        ResponseEntity<ApiResponse<Void>> response = exceptionHandler.handleException(exception);
        
        // Assert
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        
        ApiResponse<Void> body = response.getBody();
        assertNotNull(body);
        assertTrue(body.getMessage().contains("服务器内部错误"));
        assertEquals(ApiResponseCode.INTERNAL_SERVER_ERROR.getCode(), body.getCode());
        assertFalse(body.isSuccess());
    }
}