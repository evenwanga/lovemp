package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 授权级别枚举
 */
public enum AuthLevel implements ValueObject {
    
    /**
     * 基础信息
     */
    BASIC(1, "基础信息"),
    
    /**
     * 消费信息
     */
    CONSUMPTION(2, "消费信息"),
    
    /**
     * 全部信息
     */
    FULL(3, "全部信息");
    
    private final int code;
    private final String description;
    
    AuthLevel(int code, String description) {
        this.code = code;
        this.description = description;
    }
    
    /**
     * 获取级别编码
     * 
     * @return 级别编码
     */
    public int getCode() {
        return code;
    }
    
    /**
     * 获取级别描述
     * 
     * @return 级别描述
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据编码获取授权级别
     * 
     * @param code 级别编码
     * @return 授权级别
     */
    public static AuthLevel fromCode(int code) {
        for (AuthLevel level : values()) {
            if (level.code == code) {
                return level;
            }
        }
        throw new IllegalArgumentException("未知的授权级别编码: " + code);
    }
    
    /**
     * 是否可以访问指定级别的数据
     * 
     * @param requiredLevel 需要的授权级别
     * @return true-可以访问，false-不可以访问
     */
    public boolean canAccess(AuthLevel requiredLevel) {
        return this.code >= requiredLevel.code;
    }
    
    /**
     * 是否为基础级别
     * 
     * @return true-是基础级别，false-不是基础级别
     */
    public boolean isBasic() {
        return this == BASIC;
    }
    
    /**
     * 是否为消费级别
     * 
     * @return true-是消费级别，false-不是消费级别
     */
    public boolean isConsumption() {
        return this == CONSUMPTION;
    }
    
    /**
     * 是否为完全级别
     * 
     * @return true-是完全级别，false-不是完全级别
     */
    public boolean isFull() {
        return this == FULL;
    }
} 