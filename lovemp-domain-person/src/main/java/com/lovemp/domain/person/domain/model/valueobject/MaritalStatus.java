package com.lovemp.domain.person.domain.model.valueobject;

/**
 * 婚姻状况枚举
 */
public enum MaritalStatus {
    /**
     * 未婚
     */
    SINGLE("未婚"),
    
    /**
     * 已婚
     */
    MARRIED("已婚"),
    
    /**
     * 离异
     */
    DIVORCED("离异"),
    
    /**
     * 丧偶
     */
    WIDOWED("丧偶"),
    
    /**
     * 未知
     */
    UNKNOWN("未知");
    
    private final String description;
    
    MaritalStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}