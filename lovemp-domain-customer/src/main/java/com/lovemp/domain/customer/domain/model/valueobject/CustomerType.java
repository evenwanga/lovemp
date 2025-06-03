package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 顾客类型枚举
 */
public enum CustomerType implements ValueObject {
    
    /**
     * 普通顾客
     */
    NORMAL(1, "普通顾客"),
    
    /**
     * VIP顾客
     */
    VIP(2, "VIP顾客"),
    
    /**
     * 批发顾客
     */
    WHOLESALE(3, "批发顾客");
    
    private final int code;
    private final String description;
    
    CustomerType(int code, String description) {
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
     * 根据编码获取顾客类型
     * 
     * @param code 类型编码
     * @return 顾客类型
     */
    public static CustomerType fromCode(int code) {
        for (CustomerType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的顾客类型编码: " + code);
    }
    
    /**
     * 是否为VIP顾客
     * 
     * @return true-是VIP顾客，false-不是VIP顾客
     */
    public boolean isVip() {
        return this == VIP;
    }
    
    /**
     * 是否为批发顾客
     * 
     * @return true-是批发顾客，false-不是批发顾客
     */
    public boolean isWholesale() {
        return this == WHOLESALE;
    }
} 