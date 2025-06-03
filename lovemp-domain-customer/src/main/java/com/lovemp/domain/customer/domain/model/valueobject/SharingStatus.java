package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 共享状态枚举
 */
public enum SharingStatus implements ValueObject {
    
    /**
     * 待生效
     */
    PENDING(0, "待生效"),
    
    /**
     * 生效中
     */
    ACTIVE(1, "生效中"),
    
    /**
     * 已失效
     */
    INACTIVE(2, "已失效");
    
    private final int code;
    private final String description;
    
    SharingStatus(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 获取状态编码
     * 
     * @return 状态编码
     */
    public int getCode() {
        return code;
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
     * 根据编码获取共享状态
     * 
     * @param code 状态编码
     * @return 共享状态
     */
    public static SharingStatus fromCode(int code) {
        for (SharingStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的共享状态编码: " + code);
    }
    
    /**
     * 是否为待生效状态
     * 
     * @return true-是待生效状态，false-不是待生效状态
     */
    public boolean isPending() {
        return this == PENDING;
    }
    
    /**
     * 是否为激活状态
     * 
     * @return true-是激活状态，false-不是激活状态
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * 是否为失效状态
     * 
     * @return true-是失效状态，false-不是失效状态
     */
    public boolean isInactive() {
        return this == INACTIVE;
    }
} 