package com.lovemp.domain.brand.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 用工政策状态
 */
public enum LaborPolicyStatus implements ValueObject {
    
    /**
     * 活跃
     */
    ACTIVE("ACTIVE", "活跃", "正在使用的用工政策"),
    
    /**
     * 草稿
     */
    DRAFT("DRAFT", "草稿", "草稿状态的用工政策"),
    
    /**
     * 已过期
     */
    EXPIRED("EXPIRED", "已过期", "已过期的用工政策"),
    
    /**
     * 已禁用
     */
    DISABLED("DISABLED", "已禁用", "已禁用的用工政策"),
    
    /**
     * 待审核
     */
    PENDING_APPROVAL("PENDING_APPROVAL", "待审核", "等待审核的用工政策"),
    
    /**
     * 审核拒绝
     */
    APPROVAL_REJECTED("APPROVAL_REJECTED", "审核拒绝", "审核被拒绝的用工政策");
    
    private final String code;
    private final String name;
    private final String description;
    
    LaborPolicyStatus(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据编码获取用工政策状态
     *
     * @param code 编码
     * @return 用工政策状态
     */
    public static LaborPolicyStatus fromCode(String code) {
        for (LaborPolicyStatus status : LaborPolicyStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的用工政策状态编码: " + code);
    }
    
    /**
     * 判断编码是否有效
     *
     * @param code 编码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        for (LaborPolicyStatus status : LaborPolicyStatus.values()) {
            if (status.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * 判断是否是活跃状态
     * 
     * @return 是否活跃
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * 判断是否是草稿状态
     * 
     * @return 是否草稿
     */
    public boolean isDraft() {
        return this == DRAFT;
    }
    
    /**
     * 判断是否已过期
     * 
     * @return 是否已过期
     */
    public boolean isExpired() {
        return this == EXPIRED;
    }
    
    /**
     * 判断是否可用
     * 
     * @return 是否可用
     */
    public boolean isUsable() {
        return this == ACTIVE;
    }
} 