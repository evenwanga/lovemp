package com.lovemp.domain.person.domain.model.valueobject;

/**
 * 教育水平枚举
 */
public enum EducationLevel {
    /**
     * 小学
     */
    PRIMARY_SCHOOL("小学"),
    
    /**
     * 初中
     */
    JUNIOR_HIGH_SCHOOL("初中"),
    
    /**
     * 高中
     */
    HIGH_SCHOOL("高中"),
    
    /**
     * 中专
     */
    TECHNICAL_SCHOOL("中专"),
    
    /**
     * 大专
     */
    COLLEGE("大专"),
    
    /**
     * 本科
     */
    UNDERGRADUATE("本科"),
    
    /**
     * 硕士
     */
    MASTER("硕士"),
    
    /**
     * 博士
     */
    DOCTORATE("博士"),
    
    /**
     * 博士后
     */
    POST_DOCTORATE("博士后"),
    
    /**
     * 其他
     */
    OTHER("其他");
    
    private final String description;
    
    EducationLevel(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}