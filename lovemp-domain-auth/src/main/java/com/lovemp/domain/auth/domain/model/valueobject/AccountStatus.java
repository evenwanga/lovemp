package com.lovemp.domain.auth.domain.model.valueobject;

/**
 * 账号状态枚举
 * 
 * <p>定义账号的各种状态</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public enum AccountStatus {
    
    /**
     * 激活状态 - 账号正常可用
     */
    ACTIVE("激活"),
    
    /**
     * 禁用状态 - 账号被管理员禁用
     */
    DISABLED("禁用"),
    
    /**
     * 锁定状态 - 账号因安全原因被锁定（如多次登录失败）
     */
    LOCKED("锁定"),
    
    /**
     * 过期状态 - 账号已过期
     */
    EXPIRED("过期"),
    
    /**
     * 待激活状态 - 新创建的账号，等待激活
     */
    PENDING("待激活");
    
    private final String description;
    
    AccountStatus(String description) {
        this.description = description;
    }
    
    /**
     * 获取状态描述
     * 
     * @return 状态描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 判断账号是否可用
     * 
     * @return 如果账号状态为ACTIVE则返回true，否则返回false
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * 判断账号是否被锁定
     * 
     * @return 如果账号状态为LOCKED则返回true，否则返回false
     */
    public boolean isLocked() {
        return this == LOCKED;
    }
    
    /**
     * 判断账号是否已过期
     * 
     * @return 如果账号状态为EXPIRED则返回true，否则返回false
     */
    public boolean isExpired() {
        return this == EXPIRED;
    }
    
    /**
     * 判断账号是否待激活
     * 
     * @return 如果账号状态为PENDING则返回true，否则返回false
     */
    public boolean isPending() {
        return this == PENDING;
    }
} 