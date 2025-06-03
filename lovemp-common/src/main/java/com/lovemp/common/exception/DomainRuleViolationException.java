package com.lovemp.common.exception;

/**
 * 领域规则违反异常
 * 用于标识违反了领域规则或业务规则的情况
 */
public class DomainRuleViolationException extends BusinessException {

    private static final String DEFAULT_ERROR_CODE = "DOMAIN_RULE_VIOLATION";

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public DomainRuleViolationException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause 原始异常
     */
    public DomainRuleViolationException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, cause);
    }

    /**
     * 构造函数
     *
     * @param ruleName 规则名称
     * @param explanation 解释
     */
    public DomainRuleViolationException(String ruleName, String explanation) {
        super(DEFAULT_ERROR_CODE, String.format("违反领域规则 '%s': %s", ruleName, explanation));
    }
} 