package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.Getter;

/**
 * 企业资源账号状态枚举
 */
@Getter
public enum ResourceAccountStatus implements ValueObject {
    
    /**
     * 活跃
     */
    ACTIVE("活跃", "ACTIVE"),
    
    /**
     * 暂停
     */
    SUSPENDED("暂停", "SUSPENDED"),
    
    /**
     * 过期
     */
    EXPIRED("过期", "EXPIRED"),
    
    /**
     * 封禁
     */
    BANNED("封禁", "BANNED"),
    
    /**
     * 注销
     */
    CANCELLED("注销", "CANCELLED");
    
    /**
     * 状态名称
     */
    private final String name;
    
    /**
     * 状态代码
     */
    private final String code;
    
    ResourceAccountStatus(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    /**
     * 根据代码获取资源账号状态
     * 
     * @param code 状态代码
     * @return 资源账号状态枚举
     */
    public static ResourceAccountStatus fromCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("状态代码不能为空");
        }
        
        for (ResourceAccountStatus status : ResourceAccountStatus.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        
        throw new IllegalArgumentException("无效的状态代码: " + code);
    }
    
    /**
     * 根据名称获取资源账号状态
     * 
     * @param name 状态名称
     * @return 资源账号状态枚举
     */
    public static ResourceAccountStatus fromName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("状态名称不能为空");
        }
        
        for (ResourceAccountStatus status : ResourceAccountStatus.values()) {
            if (status.getName().equals(name)) {
                return status;
            }
        }
        
        throw new IllegalArgumentException("无效的状态名称: " + name);
    }
    
    /**
     * 状态是否有效（可使用）
     * 
     * @return 如果状态有效返回true
     */
    public boolean isValid() {
        return this == ACTIVE || this == SUSPENDED;
    }
    
    /**
     * 状态是否无效（不可使用）
     * 
     * @return 如果状态无效返回true
     */
    public boolean isInvalid() {
        return !isValid();
    }
} 