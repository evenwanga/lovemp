package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.Getter;

/**
 * 企业备案类型枚举
 */
@Getter
public enum FilingType implements ValueObject {
    
    /**
     * ICP备案
     */
    ICP("ICP备案", "ICP"),
    
    /**
     * 网站ICP备案
     */
    WEBSITE_ICP("网站ICP备案", "WICP"),
    
    /**
     * 移动应用ICP备案
     */
    MOBILE_APP_ICP("移动应用ICP备案", "MICP"),
    
    /**
     * 小程序ICP备案
     */
    MINI_PROGRAM_ICP("小程序ICP备案", "MPICP"),
    
    /**
     * 公安备案
     */
    PUBLIC_SECURITY("公安备案", "PSB"),
    
    /**
     * 电信业务经营许可备案
     */
    TELECOM_BUSINESS("电信业务经营许可备案", "TBO"),
    
    /**
     * 广播电视节目制作经营备案
     */
    BROADCAST_TV_PROGRAM("广播电视节目制作经营备案", "BTP"),
    
    /**
     * 网络文化经营备案
     */
    NETWORK_CULTURE("网络文化经营备案", "NCP"),
    
    /**
     * 增值电信业务经营许可备案
     */
    VALUE_ADDED_TELECOM("增值电信业务经营许可备案", "VAT"),
    
    /**
     * 网络出版服务备案
     */
    NETWORK_PUBLISHING("网络出版服务备案", "NPB"),
    
    /**
     * 互联网药品信息服务备案
     */
    INTERNET_DRUG_INFO("互联网药品信息服务备案", "IDI"),
    
    /**
     * 互联网新闻信息服务备案
     */
    INTERNET_NEWS("互联网新闻信息服务备案", "INS"),
    
    /**
     * 跨境电子商务备案
     */
    CROSS_BORDER_ECOMMERCE("跨境电子商务备案", "CBE"),
    
    /**
     * 网络预约出租汽车经营备案
     */
    ONLINE_CAR_HAILING("网络预约出租汽车经营备案", "OCH"),
    
    /**
     * 食品经营网络销售备案
     */
    FOOD_ONLINE_SALES("食品经营网络销售备案", "FOS"),
    
    /**
     * 其他备案
     */
    OTHER("其他备案", "OTHER");
    
    /**
     * 备案类型名称
     */
    private final String name;
    
    /**
     * 备案类型代码
     */
    private final String code;
    
    FilingType(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    /**
     * 根据代码获取备案类型
     * 
     * @param code 备案类型代码
     * @return 备案类型枚举
     */
    public static FilingType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("备案类型代码不能为空");
        }
        
        for (FilingType type : FilingType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("无效的备案类型代码: " + code);
    }
    
    /**
     * 根据名称获取备案类型
     * 
     * @param name 备案类型名称
     * @return 备案类型枚举
     */
    public static FilingType fromName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("备案类型名称不能为空");
        }
        
        for (FilingType type : FilingType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("无效的备案类型名称: " + name);
    }
    
    /**
     * 是否为网站相关备案
     * 
     * @return 如果是网站相关备案返回true
     */
    public boolean isWebsiteRelated() {
        return this == ICP || this == WEBSITE_ICP || this == PUBLIC_SECURITY;
    }
    
    /**
     * 是否为电信相关备案
     * 
     * @return 如果是电信相关备案返回true
     */
    public boolean isTelecomRelated() {
        return this == TELECOM_BUSINESS || this == VALUE_ADDED_TELECOM;
    }
    
    /**
     * 是否为内容服务相关备案
     * 
     * @return 如果是内容服务相关备案返回true
     */
    public boolean isContentServiceRelated() {
        return this == NETWORK_CULTURE || this == NETWORK_PUBLISHING || 
               this == INTERNET_NEWS || this == BROADCAST_TV_PROGRAM || 
               this == INTERNET_DRUG_INFO;
    }
    
    /**
     * 是否为ICP相关备案
     * 
     * @return 如果是ICP相关备案返回true
     */
    public boolean isICPRelated() {
        return this == ICP || this == WEBSITE_ICP || this == MOBILE_APP_ICP || this == MINI_PROGRAM_ICP;
    }
    
    /**
     * 是否为移动应用相关备案
     * 
     * @return 如果是移动应用相关备案返回true
     */
    public boolean isMobileAppRelated() {
        return this == MOBILE_APP_ICP || this == MINI_PROGRAM_ICP;
    }
    
    /**
     * 是否为电子商务相关备案
     * 
     * @return 如果是电子商务相关备案返回true
     */
    public boolean isEcommerceRelated() {
        return this == CROSS_BORDER_ECOMMERCE || this == FOOD_ONLINE_SALES || 
               this == ONLINE_CAR_HAILING;
    }
    
    /**
     * 是否为其他类型备案
     * 
     * @return 如果是其他类型备案返回true
     */
    public boolean isOtherRelated() {
        return this == OTHER;
    }
    
    /**
     * 返回备案类型的名称
     * 
     * @return 备案类型的名称
     */
    @Override
    public String toString() {
        return this.name;
    }
} 