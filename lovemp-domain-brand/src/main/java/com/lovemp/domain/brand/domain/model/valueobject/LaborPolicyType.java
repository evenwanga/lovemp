package com.lovemp.domain.brand.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 用工政策类型
 */
public enum LaborPolicyType implements ValueObject {
    
    /**
     * 标准劳动合同
     */
    STANDARD_LABOR_CONTRACT("STANDARD_LABOR_CONTRACT", "标准劳动合同", "采用标准劳动合同的用工模式"),
    
    /**
     * 灵活用工
     */
    FLEXIBLE_EMPLOYMENT("FLEXIBLE_EMPLOYMENT", "灵活用工", "采用灵活用工模式"),
    
    /**
     * 劳务外包
     */
    LABOR_OUTSOURCING("LABOR_OUTSOURCING", "劳务外包", "采用劳务外包模式"),
    
    /**
     * 实习生
     */
    INTERN("INTERN", "实习生", "针对实习生的用工政策"),
    
    /**
     * 兼职
     */
    PART_TIME("PART_TIME", "兼职", "针对兼职人员的用工政策"),
    
    /**
     * 返聘
     */
    REHIRE("REHIRE", "返聘", "针对退休返聘人员的用工政策");
    
    private final String code;
    private final String name;
    private final String description;
    
    LaborPolicyType(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据编码获取用工政策类型
     *
     * @param code 编码
     * @return 用工政策类型
     */
    public static LaborPolicyType fromCode(String code) {
        for (LaborPolicyType type : LaborPolicyType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的用工政策类型编码: " + code);
    }
    
    /**
     * 判断编码是否有效
     *
     * @param code 编码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        for (LaborPolicyType type : LaborPolicyType.values()) {
            if (type.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
} 