package com.lovemp.domain.labor.domain.model.valueobject;

/**
 * 雇佣类型 - 值对象
 */
public enum EmploymentType {
    
    /**
     * 全职雇员
     */
    FULL_TIME("全职雇员"),
    
    /**
     * 兼职雇员
     */
    PART_TIME("兼职雇员"),
    
    /**
     * 临时工
     */
    TEMPORARY("临时工"),
    
    /**
     * 合同工
     */
    CONTRACT("合同工"),
    
    /**
     * 实习生
     */
    INTERN("实习生"),
    
    /**
     * 个体工商户
     */
    CONTRACTOR("个体工商户"),
    
    /**
     * 外包
     */
    OUTSOURCE("外包");
    
    private final String description;
    
    EmploymentType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 判断是否为正式雇佣关系
     *
     * @return 是否正式雇佣
     */
    public boolean isFormalEmployment() {
        return this == FULL_TIME || this == PART_TIME;
    }
    
    /**
     * 判断是否为灵活用工
     *
     * @return 是否灵活用工
     */
    public boolean isFlexibleEmployment() {
        return this == TEMPORARY || this == CONTRACT || this == CONTRACTOR;
    }
}