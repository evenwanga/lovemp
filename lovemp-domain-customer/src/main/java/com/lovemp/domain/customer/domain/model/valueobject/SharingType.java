package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 共享类型枚举
 */
public enum SharingType implements ValueObject {
    
    /**
     * 自动共享
     */
    AUTO(1, "自动共享"),
    
    /**
     * 授权共享
     */
    AUTH(2, "授权共享"),
    
    /**
     * 关联共享
     */
    RELATED(3, "关联共享");
    
    private final int code;
    private final String description;
    
    SharingType(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 获取类型编码
     * 
     * @return 类型编码
     */
    public int getCode() {
        return code;
    }
    
    /**
     * 获取类型描述
     * 
     * @return 类型描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据编码获取共享类型
     * 
     * @param code 类型编码
     * @return 共享类型
     */
    public static SharingType fromCode(int code) {
        for (SharingType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的共享类型编码: " + code);
    }
    
    /**
     * 是否为自动共享
     * 
     * @return true-是自动共享，false-不是自动共享
     */
    public boolean isAuto() {
        return this == AUTO;
    }
    
    /**
     * 是否为授权共享
     * 
     * @return true-是授权共享，false-不是授权共享
     */
    public boolean isAuth() {
        return this == AUTH;
    }
    
    /**
     * 是否为关联共享
     * 
     * @return true-是关联共享，false-不是关联共享
     */
    public boolean isRelated() {
        return this == RELATED;
    }
} 