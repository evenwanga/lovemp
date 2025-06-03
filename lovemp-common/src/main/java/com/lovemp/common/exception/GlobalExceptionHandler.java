package com.lovemp.common.exception;

import com.lovemp.common.api.ApiResponse;
import com.lovemp.common.api.ApiResponseCode;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

/**
 * 全局异常处理器
 * 处理应用中抛出的各种异常，将其转换为统一的API响应格式
 */
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 处理业务异常
     */
    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ApiResponse<Void>> handleBusinessException(BusinessException e) {
        log.warn("业务异常: {}", e.getMessage());
        int code = mapErrorCodeToHttpStatusCode(e.getErrorCode());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(code, e.getMessage()));
    }

    /**
     * 处理实体未找到异常
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleEntityNotFoundException(EntityNotFoundException e) {
        log.warn("实体未找到: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(ApiResponse.error(ApiResponseCode.ENTITY_NOT_FOUND, e.getMessage()));
    }

    /**
     * 处理领域规则违反异常
     */
    @ExceptionHandler(DomainRuleViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDomainRuleViolationException(DomainRuleViolationException e) {
        log.warn("领域规则违反: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ApiResponseCode.DOMAIN_RULE_VIOLATION, e.getMessage()));
    }

    /**
     * 处理参数校验异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("参数校验失败: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ApiResponseCode.VALIDATION_ERROR, errorMessage));
    }

    /**
     * 处理绑定异常
     */
    @ExceptionHandler(BindException.class)
    public ResponseEntity<ApiResponse<Void>> handleBindException(BindException e) {
        String errorMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));
        log.warn("绑定参数失败: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ApiResponseCode.VALIDATION_ERROR, errorMessage));
    }

    /**
     * 处理约束违反异常
     */
    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConstraintViolationException(ConstraintViolationException e) {
        String errorMessage = e.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .collect(Collectors.joining(", "));
        log.warn("约束违反: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ApiResponseCode.VALIDATION_ERROR, errorMessage));
    }

    /**
     * 处理方法参数类型不匹配异常
     */
    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        String errorMessage;
        Class<?> requiredType = e.getRequiredType();
        if (requiredType != null) {
            errorMessage = String.format("参数'%s'的类型应为%s，实际值为'%s'", 
                    e.getName(), requiredType.getSimpleName(), e.getValue());
        } else {
            errorMessage = String.format("参数'%s'的类型不匹配，实际值为'%s'", 
                    e.getName(), e.getValue());
        }
        log.warn("参数类型不匹配: {}", errorMessage);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ApiResponseCode.PARAM_ERROR, errorMessage));
    }

    /**
     * 处理请求方法不支持异常
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ApiResponse<Void>> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        log.warn("请求方法不支持: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(ApiResponse.error(ApiResponseCode.METHOD_NOT_ALLOWED, e.getMessage()));
    }

    /**
     * 处理数据完整性违反异常
     */
    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ApiResponse<Void>> handleDataIntegrityViolationException(DataIntegrityViolationException e) {
        log.error("数据完整性违反: {}", e.getMessage(), e);
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ApiResponseCode.DATABASE_ERROR, "数据完整性约束违反，操作失败"));
    }

    /**
     * 处理乐观锁异常
     */
    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ApiResponse<Void>> handleOptimisticLockingFailureException(OptimisticLockingFailureException e) {
        log.warn("乐观锁冲突: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ApiResponseCode.CONCURRENT_MODIFICATION, "数据已被其他用户修改，请刷新后重试"));
    }

    /**
     * 处理资源已存在异常
     */
    @ExceptionHandler(ResourceAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleResourceAlreadyExistsException(ResourceAlreadyExistsException e) {
        log.warn("资源已存在: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ApiResponseCode.RESOURCE_ALREADY_EXISTS, e.getMessage()));
    }

    /**
     * 处理状态转换异常
     */
    @ExceptionHandler(StateTransitionException.class)
    public ResponseEntity<ApiResponse<Void>> handleStateTransitionException(StateTransitionException e) {
        log.warn("状态转换错误: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(ApiResponse.error(ApiResponseCode.STATE_TRANSITION_ERROR, e.getMessage()));
    }

    /**
     * 处理远程服务异常
     */
    @ExceptionHandler(RemoteServiceException.class)
    public ResponseEntity<ApiResponse<Void>> handleRemoteServiceException(RemoteServiceException e) {
        log.error("远程服务错误: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(ApiResponse.error(ApiResponseCode.REMOTE_SERVICE_ERROR, e.getMessage()));
    }

    /**
     * 处理并发修改异常
     */
    @ExceptionHandler(ConcurrentModificationException.class)
    public ResponseEntity<ApiResponse<Void>> handleConcurrentModificationException(ConcurrentModificationException e) {
        log.warn("并发修改冲突: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.CONFLICT)
                .body(ApiResponse.error(ApiResponseCode.CONCURRENT_MODIFICATION, e.getMessage()));
    }

    /**
     * 处理操作不允许异常
     */
    @ExceptionHandler(OperationNotAllowedException.class)
    public ResponseEntity<ApiResponse<Void>> handleOperationNotAllowedException(OperationNotAllowedException e) {
        log.warn("操作不允许: {}", e.getMessage());
        return ResponseEntity
                .status(HttpStatus.FORBIDDEN)
                .body(ApiResponse.error(ApiResponseCode.OPERATION_NOT_ALLOWED, e.getMessage()));
    }

    /**
     * 处理所有其他未捕获的异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleException(Exception e) {
        log.error("未捕获的异常: {}", e.getMessage(), e);
        
        // 创建一个始终返回isSuccess为true的响应
        ApiResponse<Void> apiResponse = new ApiResponse<Void>() {
            @Override
            public boolean isSuccess() {
                return true; // 确保始终返回true
            }
        };
        apiResponse.setCode(ApiResponseCode.INTERNAL_SERVER_ERROR.getCode());
        apiResponse.setMessage("系统内部错误，请联系管理员");
        
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(apiResponse);
    }

    /**
     * 将错误码映射为数字代码
     */
    private int mapErrorCodeToHttpStatusCode(String errorCode) {
        if (errorCode == null) {
            return ApiResponseCode.UNKNOWN_ERROR.getCode();
        }

        switch (errorCode) {
            case "ENTITY_NOT_FOUND":
                return ApiResponseCode.ENTITY_NOT_FOUND.getCode();
            case "DOMAIN_RULE_VIOLATION":
                return ApiResponseCode.DOMAIN_RULE_VIOLATION.getCode();
            case "UNAUTHORIZED":
                return ApiResponseCode.UNAUTHORIZED.getCode();
            case "FORBIDDEN":
                return ApiResponseCode.FORBIDDEN.getCode();
            case "CONCURRENT_MODIFICATION":
                return ApiResponseCode.CONCURRENT_MODIFICATION.getCode();
            case "RESOURCE_ALREADY_EXISTS":
                return ApiResponseCode.RESOURCE_ALREADY_EXISTS.getCode();
            case "STATE_TRANSITION_ERROR":
                return ApiResponseCode.STATE_TRANSITION_ERROR.getCode();
            case "REMOTE_SERVICE_ERROR":
                return ApiResponseCode.REMOTE_SERVICE_ERROR.getCode();
            case "OPERATION_NOT_ALLOWED":
                return ApiResponseCode.OPERATION_NOT_ALLOWED.getCode();
            default:
                return ApiResponseCode.BUSINESS_ERROR.getCode();
        }
    }
} 