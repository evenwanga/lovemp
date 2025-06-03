package com.lovemp.domain.brand.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 品牌类型
 */
public enum BrandType implements ValueObject {
    
    /**
     * 自有品牌
     */
    OWN_BRAND("OWN_BRAND", "自有品牌", "企业创建并拥有的品牌"),
    
    /**
     * 授权品牌
     */
    AUTHORIZED_BRAND("AUTHORIZED_BRAND", "授权品牌", "通过授权使用的品牌"),
    
    /**
     * 加盟品牌
     */
    FRANCHISE_BRAND("FRANCHISE_BRAND", "加盟品牌", "加盟获得的品牌"),
    
    /**
     * 子品牌
     */
    SUB_BRAND("SUB_BRAND", "子品牌", "隶属于主品牌的子品牌"),
    
    /**
     * 合作品牌
     */
    COOPERATIVE_BRAND("COOPERATIVE_BRAND", "合作品牌", "通过合作关系共有的品牌");
    
    private final String code;
    private final String name;
    private final String description;
    
    BrandType(String code, String name, String description) {
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
     * 根据编码获取品牌类型
     *
     * @param code 编码
     * @return 品牌类型
     */
    public static BrandType fromCode(String code) {
        for (BrandType type : BrandType.values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("未知的品牌类型编码: " + code);
    }
    
    /**
     * 判断编码是否有效
     *
     * @param code 编码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        for (BrandType type : BrandType.values()) {
            if (type.getCode().equals(code)) {
                return true;
            }
        }
        return false;
    }
} 