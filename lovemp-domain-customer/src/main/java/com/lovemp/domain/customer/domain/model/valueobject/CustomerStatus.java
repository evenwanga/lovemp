package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 顾客状态枚举
 */
public enum CustomerStatus implements ValueObject {
    
    /**
     * 未激活
     */
    INACTIVE(0, "未激活"),
    
    /**
     * 正常
     */
    ACTIVE(1, "正常"),
    
    /**
     * 冻结
     */
    FROZEN(2, "冻结");
    
    private final int code;
    private final String description;
    
    CustomerStatus(int code, String description) {
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
     * 根据编码获取顾客状态
     * 
     * @param code 状态编码
     * @return 顾客状态
     */
    public static CustomerStatus fromCode(int code) {
        for (CustomerStatus status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的顾客状态编码: " + code);
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
     * 是否为冻结状态
     * 
     * @return true-是冻结状态，false-不是冻结状态
     */
    public boolean isFrozen() {
        return this == FROZEN;
    }
    
    /**
     * 是否为未激活状态
     * 
     * @return true-是未激活状态，false-不是未激活状态
     */
    public boolean isInactive() {
        return this == INACTIVE;
    }
    
    /**
     * 是否可以进行业务操作
     * 
     * @return true-可以进行业务操作，false-不可以进行业务操作
     */
    public boolean canOperate() {
        return this == ACTIVE;
    }
} 