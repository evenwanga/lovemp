package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 关系类型枚举
 */
public enum RelationType implements ValueObject {
    
    /**
     * 主动注册
     */
    ACTIVE_REGISTRATION(1, "主动注册"),
    
    /**
     * 导入
     */
    IMPORTED(2, "导入"),
    
    /**
     * 衍生关系
     */
    DERIVED(3, "衍生关系");
    
    private final int code;
    private final String description;
    
    RelationType(int code, String description) {
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
     * 根据编码获取关系类型
     * 
     * @param code 类型编码
     * @return 关系类型
     */
    public static RelationType fromCode(int code) {
        for (RelationType type : values()) {
            if (type.code == code) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的关系类型编码: " + code);
    }
    
    /**
     * 是否为主动注册
     * 
     * @return true-是主动注册，false-不是主动注册
     */
    public boolean isActiveRegistration() {
        return this == ACTIVE_REGISTRATION;
    }
    
    /**
     * 是否为导入关系
     * 
     * @return true-是导入关系，false-不是导入关系
     */
    public boolean isImported() {
        return this == IMPORTED;
    }
    
    /**
     * 是否为衍生关系
     * 
     * @return true-是衍生关系，false-不是衍生关系
     */
    public boolean isDerived() {
        return this == DERIVED;
    }
} 