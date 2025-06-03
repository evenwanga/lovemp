package com.lovemp.domain.person.domain.model.valueobject;

/**
 * 自然人状态枚举
 */
public enum PersonStatus {
    /**
     * 活跃
     */
    ACTIVE("活跃"),
    
    /**
     * 已删除
     */
    DELETED("已删除"),
    
    /**
     * 已故
     */
    DECEASED("已故"),
    
    /**
     * 禁用
     */
    DISABLED("禁用");
    
    private final String description;
    
    PersonStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}