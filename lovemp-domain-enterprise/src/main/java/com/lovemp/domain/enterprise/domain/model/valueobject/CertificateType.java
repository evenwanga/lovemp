package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.Getter;

/**
 * 证书类型枚举
 */
@Getter
public enum CertificateType implements ValueObject {
    
    /**
     * ISO9001质量管理体系认证
     */
    ISO9001("ISO9001质量管理体系认证", "ISO9001"),
    
    /**
     * ISO14001环境管理体系认证
     */
    ISO14001("ISO14001环境管理体系认证", "ISO14001"),
    
    /**
     * ISO45001职业健康安全管理体系认证
     */
    ISO45001("ISO45001职业健康安全管理体系认证", "ISO45001"),
    
    /**
     * ISO27001信息安全管理体系认证
     */
    ISO27001("ISO27001信息安全管理体系认证", "ISO27001"),
    
    /**
     * 高新技术企业认证
     */
    HIGH_TECH_ENTERPRISE("高新技术企业认证", "HTE"),
    
    /**
     * 软件企业认证
     */
    SOFTWARE_ENTERPRISE("软件企业认证", "SE"),
    
    /**
     * 双软认证（软件产品、软件企业）
     */
    DOUBLE_SOFT("双软认证", "DS"),
    
    /**
     * CMMI认证
     */
    CMMI("CMMI认证", "CMMI"),
    
    /**
     * 食品经营许可证
     */
    FOOD_OPERATION_LICENSE("食品经营许可证", "FOL"),
    
    /**
     * 餐饮服务许可证
     */
    CATERING_SERVICE_LICENSE("餐饮服务许可证", "CSL"),
    
    /**
     * 医疗器械经营许可证
     */
    MEDICAL_DEVICE_LICENSE("医疗器械经营许可证", "MDL"),
    
    /**
     * 药品经营许可证
     */
    DRUG_OPERATION_LICENSE("药品经营许可证", "DOL"),
    
    /**
     * 建筑资质证书
     */
    CONSTRUCTION_QUALIFICATION("建筑资质证书", "CQ"),
    
    /**
     * 安全生产许可证
     */
    SAFETY_PRODUCTION_LICENSE("安全生产许可证", "SPL"),
    
    /**
     * 环境影响评价资质证书
     */
    ENVIRONMENTAL_IMPACT_ASSESSMENT("环境影响评价资质证书", "EIA"),
    
    /**
     * 软件著作权证书
     */
    SOFTWARE_COPYRIGHT("软件著作权证书", "SCC"),
    
    /**
     * 实用新型专利
     */
    UTILITY_MODEL_PATENT("实用新型专利", "UMP"),
    
    /**
     * 发明专利
     */
    INVENTION_PATENT("发明专利", "IP"),
    
    /**
     * 外观设计专利
     */
    DESIGN_PATENT("外观设计专利", "DP"),
    
    /**
     * 商标注册证书
     */
    TRADEMARK_REGISTRATION("商标注册证书", "TRC"),
    
    /**
     * 网络安全等级保护认证
     */
    NETWORK_SECURITY_PROTECTION("网络安全等级保护认证", "NSP"),
    
    /**
     * 其他证书
     */
    OTHER("其他证书", "OTHER");
    
    /**
     * 证书类型名称
     */
    private final String name;
    
    /**
     * 证书类型代码
     */
    private final String code;
    
    CertificateType(String name, String code) {
        this.name = name;
        this.code = code;
    }
    
    /**
     * 根据代码获取证书类型
     * 
     * @param code 证书类型代码
     * @return 证书类型枚举
     */
    public static CertificateType fromCode(String code) {
        if (code == null || code.isEmpty()) {
            throw new IllegalArgumentException("证书类型代码不能为空");
        }
        
        for (CertificateType type : CertificateType.values()) {
            if (type.getCode().equalsIgnoreCase(code)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("无效的证书类型代码: " + code);
    }
    
    /**
     * 根据名称获取证书类型
     * 
     * @param name 证书类型名称
     * @return 证书类型枚举
     */
    public static CertificateType fromName(String name) {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("证书类型名称不能为空");
        }
        
        for (CertificateType type : CertificateType.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        
        throw new IllegalArgumentException("无效的证书类型名称: " + name);
    }
    
    /**
     * 是否为ISO证书
     * 
     * @return 如果是ISO证书返回true
     */
    public boolean isISOCertificate() {
        return this == ISO9001 || this == ISO14001 || this == ISO45001 || this == ISO27001;
    }
    
    /**
     * 是否为资质证书
     * 
     * @return 如果是资质证书返回true
     */
    public boolean isQualificationCertificate() {
        return this == HIGH_TECH_ENTERPRISE || this == SOFTWARE_ENTERPRISE || 
               this == DOUBLE_SOFT || this == CMMI || this == CONSTRUCTION_QUALIFICATION ||
               this == ENVIRONMENTAL_IMPACT_ASSESSMENT;
    }
    
    /**
     * 是否为许可证
     * 
     * @return 如果是许可证返回true
     */
    public boolean isLicense() {
        return this == FOOD_OPERATION_LICENSE || this == CATERING_SERVICE_LICENSE ||
               this == MEDICAL_DEVICE_LICENSE || this == DRUG_OPERATION_LICENSE ||
               this == SAFETY_PRODUCTION_LICENSE;
    }
    
    /**
     * 是否为知识产权证书
     * 
     * @return 如果是知识产权证书返回true
     */
    public boolean isIntellectualPropertyCertificate() {
        return this == SOFTWARE_COPYRIGHT || this == UTILITY_MODEL_PATENT || 
               this == INVENTION_PATENT || this == DESIGN_PATENT ||
               this == TRADEMARK_REGISTRATION;
    }
    
    /**
     * 返回证书类型的名称
     * 
     * @return 证书类型的名称
     */
    @Override
    public String toString() {
        return this.name;
    }
} 