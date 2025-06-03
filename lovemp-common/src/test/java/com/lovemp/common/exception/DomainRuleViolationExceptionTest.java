package com.lovemp.common.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * 领域规则违反异常测试
 */
@DisplayName("领域规则违反异常测试")
class DomainRuleViolationExceptionTest {

    private static final String DEFAULT_ERROR_CODE = "DOMAIN_RULE_VIOLATION";
    private static final String ERROR_MESSAGE = "违反了领域规则";
    private static final String RULE_NAME = "不可重复规则";
    private static final String EXPLANATION = "实体ID已存在";
    
    @Test
    @DisplayName("测试创建领域规则违反异常-简单消息")
    void testCreateWithSimpleMessage() {
        // Arrange & Act
        DomainRuleViolationException exception = new DomainRuleViolationException(ERROR_MESSAGE);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
        assertTrue(exception instanceof BusinessException);
    }
    
    @Test
    @DisplayName("测试创建领域规则违反异常-带原因")
    void testCreateWithCause() {
        // Arrange
        Throwable cause = new RuntimeException("原始错误");
        
        // Act
        DomainRuleViolationException exception = new DomainRuleViolationException(ERROR_MESSAGE, cause);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        assertEquals(ERROR_MESSAGE, exception.getMessage());
        assertSame(cause, exception.getCause());
    }
    
    @Test
    @DisplayName("测试创建领域规则违反异常-规则名和解释")
    void testCreateWithRuleNameAndExplanation() {
        // Arrange & Act
        DomainRuleViolationException exception = new DomainRuleViolationException(RULE_NAME, EXPLANATION);
        
        // Assert
        assertEquals(DEFAULT_ERROR_CODE, exception.getErrorCode());
        String expectedMessage = String.format("违反领域规则 '%s': %s", RULE_NAME, EXPLANATION);
        assertEquals(expectedMessage, exception.getMessage());
        assertNull(exception.getCause());
    }
}