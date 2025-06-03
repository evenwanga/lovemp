package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.Getter;

/**
 * 企业资质类型枚举
 */
@Getter
public enum QualificationType implements ValueObject {
    
    /**
     * 建筑施工资质
     */
    CONSTRUCTION("建筑施工资质", "CONST"),
    
    /**
     * 设计资质
     */
    DESIGN("设计资质", "DESIGN"),
    
    /**
     * 医疗机构执业许可
     */
    MEDICAL_INSTITUTION("医疗机构执业许可", "MEDICAL"),
    
    /**
     * 食品经营许可
     */
    FOOD_OPERATION("食品经营许可", "FOOD"),
    
    /**
     * 餐饮服务许可
     */
    CATERING_SERVICE("餐饮服务许可", "CATER"),
    
    /**
     * 酒类经营许可
     */
    ALCOHOL_OPERATION("酒类经营许可", "ALCOHOL"),
    
    /**
     * 烟草专卖零售许可
     */
    TOBACCO_RETAIL("烟草专卖零售许可", "TOBACCO"),
    
    /**
     * 出版物经营许可
     */
    PUBLICATION_OPERATION("出版物经营许可", "PUBLISH"),
    
    /**
     * 药品经营许可
     */
    DRUG_OPERATION("药品经营许可", "DRUG"),
    
    /**
     * 医疗器械经营许可
     */
    MEDICAL_DEVICE_OPERATION("医疗器械经营许可", "DEVICE"),
    
    /**
     * 互联网药品信息服务资格
     */
    INTERNET_DRUG_INFORMATION("互联网药品信息服务资质", "IDI"),
    
    /**
     * 危险化学品经营许可
     */
    HAZARDOUS_CHEMICAL_OPERATION("危险化学品经营许可", "CHEM"),
    
    /**
     * 道路运输经营许可
     */
    ROAD_TRANSPORT_OPERATION("道路运输经营许可", "ROAD"),
    
    /**
     * 机动车维修经营许可
     */
    VEHICLE_REPAIR_OPERATION("机动车维修经营许可", "REPAIR"),
    
    /**
     * 旅行社业务经营许可
     */
    TRAVEL_AGENCY_OPERATION("旅行社业务经营许可", "TRAVEL"),
    
    /**
     * 特种设备安装改造维修许可
     */
    SPECIAL_EQUIPMENT_OPERATION("特种设备安装改造维修许可", "SPECIAL"),
    
    /**
     * 增值电信业务经营许可证
     */
    VALUE_ADDED_TELECOM_BUSINESS("增值电信业务经营许可证", "VATB"),
    
    /**
     * 互联网信息服务业务许可证(ICP证)
     */
    INTERNET_CONTENT_PROVIDER("互联网信息服务业务许可证", "ICP"),
    
    /**
     * 电信业务经营许可证
     */
    TELECOM_BUSINESS_OPERATION("电信业务经营许可证", "TBO"),
    
    /**
     * 互联网出版许可证
     */
    INTERNET_PUBLISHING("互联网出版许可证", "IPL"),
    
    /**
     * 电子认证服务许可证
     */
    ELECTRONIC_CERTIFICATION("电子认证服务许可证", "ECS"),
    
    /**
     * 网络文化经营许可证
     */
    NETWORK_CULTURE_OPERATION("网络文化经营许可证", "NCO"),
    
    /**
     * 高新技术企业认定
     */
    HIGH_TECH_ENTERPRISE("高新技术企业认定", "HTE"),
    
    /**
     * 软件企业认定
     */
    SOFTWARE_ENTERPRISE("软件企业认定", "SE"),
    
    /**
     * 其他资质
     */
    OTHER("其他资质", "OTHER");
    
    /**
     * 资质类型名称
     */
    private final String name;
    
    /**
     * 资质类型代码
     */
    private final String code;
    
    QualificationType(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    /**
     * 根据代码获取资质类型
     * 
     * @param code 资质类型代码
     * @return 资质类型枚举
     */
    public static QualificationType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("资质类型代码不能为空");
        }
        
        for (QualificationType type : QualificationType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("无效的资质类型代码: " + code);
    }
    
    /**
     * 根据名称获取资质类型
     * 
     * @param name 资质类型名称
     * @return 资质类型枚举
     */
    public static QualificationType fromName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("资质类型名称不能为空");
        }
        
        for (QualificationType type : QualificationType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("无效的资质类型名称: " + name);
    }
    
    /**
     * 是否为食品相关资质
     * 
     * @return 如果是食品相关资质返回true
     */
    public boolean isFoodRelated() {
        return this == FOOD_OPERATION || this == CATERING_SERVICE;
    }
    
    /**
     * 是否为医药相关资质
     * 
     * @return 如果是医药相关资质返回true
     */
    public boolean isMedicalRelated() {
        return this == MEDICAL_INSTITUTION || this == DRUG_OPERATION || 
               this == MEDICAL_DEVICE_OPERATION || this == INTERNET_DRUG_INFORMATION;
    }
    
    /**
     * 是否为交通运输相关资质
     * 
     * @return 如果是交通运输相关资质返回true
     */
    public boolean isTransportRelated() {
        return this == ROAD_TRANSPORT_OPERATION || this == VEHICLE_REPAIR_OPERATION;
    }
    
    /**
     * 是否为互联网相关资质
     * 
     * @return 如果是互联网相关资质返回true
     */
    public boolean isInternetRelated() {
        return this == VALUE_ADDED_TELECOM_BUSINESS || this == INTERNET_CONTENT_PROVIDER || 
               this == TELECOM_BUSINESS_OPERATION || this == INTERNET_PUBLISHING || 
               this == ELECTRONIC_CERTIFICATION || this == NETWORK_CULTURE_OPERATION;
    }
    
    /**
     * 是否为IT企业认定资质
     * 
     * @return 如果是IT企业认定资质返回true
     */
    public boolean isITEnterpriseCertification() {
        return this == HIGH_TECH_ENTERPRISE || this == SOFTWARE_ENTERPRISE;
    }
    
    /**
     * 返回资质类型的名称
     * 
     * @return 资质类型的名称
     */
    @Override
    public String toString() {
        return this.name;
    }
} 