package com.lovemp.common.exception;

/**
 * 状态转换异常
 * 用于处理实体状态转换过程中的错误
 */
public class StateTransitionException extends BusinessException {

    private static final String DEFAULT_ERROR_CODE = "STATE_TRANSITION_ERROR";

    /**
     * 构造函数
     *
     * @param message 错误消息
     */
    public StateTransitionException(String message) {
        super(DEFAULT_ERROR_CODE, message);
    }

    /**
     * 构造函数
     *
     * @param message 错误消息
     * @param cause 原始异常
     */
    public StateTransitionException(String message, Throwable cause) {
        super(DEFAULT_ERROR_CODE, message, cause);
    }

    /**
     * 构造函数
     *
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param currentState 当前状态
     * @param targetState 目标状态
     */
    public StateTransitionException(String entityType, Object entityId, String currentState, String targetState) {
        super(DEFAULT_ERROR_CODE, 
                String.format("%s (ID: %s) 无法从状态 '%s' 转换到 '%s'", 
                        entityType, entityId, currentState, targetState));
    }

    /**
     * 构造函数
     *
     * @param entityType 实体类型
     * @param entityId 实体ID
     * @param currentState 当前状态
     * @param targetState 目标状态
     * @param reason 原因
     */
    public StateTransitionException(String entityType, Object entityId, String currentState, String targetState, String reason) {
        super(DEFAULT_ERROR_CODE, 
                String.format("%s (ID: %s) 无法从状态 '%s' 转换到 '%s'，原因：%s", 
                        entityType, entityId, currentState, targetState, reason));
    }
} 