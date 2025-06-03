package com.lovemp.config;

import cn.dev33.satoken.exception.NotLoginException;
import cn.dev33.satoken.exception.NotPermissionException;
import cn.dev33.satoken.exception.NotRoleException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局异常处理配置
 */
@RestControllerAdvice
public class ExceptionHandlerConfig {

    /**
     * 处理Sa-Token未登录异常
     */
    @ExceptionHandler(NotLoginException.class)
    public ResponseEntity<Map<String, Object>> handleNotLoginException(NotLoginException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 401);
        response.put("message", "未登录：" + e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
    }

    /**
     * 处理Sa-Token权限不足异常
     */
    @ExceptionHandler(NotPermissionException.class)
    public ResponseEntity<Map<String, Object>> handleNotPermissionException(NotPermissionException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 403);
        response.put("message", "权限不足：" + e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 处理Sa-Token角色不足异常
     */
    @ExceptionHandler(NotRoleException.class)
    public ResponseEntity<Map<String, Object>> handleNotRoleException(NotRoleException e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 403);
        response.put("message", "角色不足：" + e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(response);
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleException(Exception e) {
        Map<String, Object> response = new HashMap<>();
        response.put("code", 500);
        response.put("message", "服务器内部错误：" + e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
} 