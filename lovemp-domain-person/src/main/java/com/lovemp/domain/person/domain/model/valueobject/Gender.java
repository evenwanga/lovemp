package com.lovemp.domain.person.domain.model.valueobject;

/**
 * 性别枚举
 */
public enum Gender {
    /**
     * 男性
     */
    MALE("男"),
    
    /**
     * 女性
     */
    FEMALE("女"),
    
    /**
     * 其他
     */
    OTHER("其他"),
    
    /**
     * 未知
     */
    UNKNOWN("未知");
    
    private final String description;
    
    Gender(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}