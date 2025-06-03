package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.Getter;

/**
 * 纳税人资质枚举
 */
@Getter
public enum TaxpayerQualification implements ValueObject {
    
    /**
     * 一般纳税人
     */
    GENERAL("一般纳税人", "GTP"),
    
    /**
     * 小规模纳税人
     */
    SMALL_SCALE("小规模纳税人", "SST"),
    
    /**
     * 其他
     */
    OTHER("其他", "OTHER");
    
    /**
     * 资质名称
     */
    private final String name;
    
    /**
     * 资质代码
     */
    private final String code;
    
    TaxpayerQualification(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    /**
     * 根据代码获取纳税人资质
     * 
     * @param code 资质代码
     * @return 纳税人资质枚举
     */
    public static TaxpayerQualification fromCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("资质代码不能为空");
        }
        
        for (TaxpayerQualification qualification : TaxpayerQualification.values()) {
            if (qualification.getCode().equalsIgnoreCase(code)) {
                return qualification;
            }
        }
        
        throw new IllegalArgumentException("无效的资质代码: " + code);
    }
    
    /**
     * 根据名称获取纳税人资质
     * 
     * @param name 资质名称
     * @return 纳税人资质枚举
     */
    public static TaxpayerQualification fromName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("资质名称不能为空");
        }
        
        for (TaxpayerQualification qualification : TaxpayerQualification.values()) {
            if (qualification.getName().equals(name)) {
                return qualification;
            }
        }
        
        throw new IllegalArgumentException("无效的资质名称: " + name);
    }
} 