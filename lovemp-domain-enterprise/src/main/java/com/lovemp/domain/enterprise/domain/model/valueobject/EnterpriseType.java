package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.StringUtils;
import lombok.Getter;
import java.util.stream.Stream;

/**
 * 企业类型枚举
 */
@Getter
public enum EnterpriseType implements ValueObject {
    
    /**
     * 有限责任公司
     */
    LIMITED_LIABILITY_COMPANY("有限责任公司", "LLC"),
    
    /**
     * 股份有限公司
     */
    JOINT_STOCK_COMPANY("股份有限公司", "JSC"),
    
    /**
     * 个人独资企业
     */
    SOLE_PROPRIETORSHIP("个人独资企业", "SP"),
    
    /**
     * 合伙企业
     */
    PARTNERSHIP("合伙企业", "PSHIP"),
    
    /**
     * 有限合伙企业
     */
    LIMITED_PARTNERSHIP("有限合伙企业", "LP"),
    
    /**
     * 个体工商户
     */
    INDIVIDUAL_BUSINESS("个体工商户", "IB"),
    
    /**
     * 外商独资企业
     */
    WHOLLY_FOREIGN_OWNED_ENTERPRISE("外商独资企业", "WFOE"),
    
    /**
     * 中外合资企业
     */
    JOINT_VENTURE("中外合资企业", "JV"),
    
    /**
     * 国有企业
     */
    STATE_OWNED_ENTERPRISE("国有企业", "SOE"),
    
    /**
     * 集体企业
     */
    COLLECTIVE_ENTERPRISE("集体企业", "CE"),
    
    /**
     * 民办非企业单位
     */
    PRIVATE_NON_ENTERPRISE_UNIT("民办非企业单位", "PNEU"),
    
    /**
     * 社会团体
     */
    SOCIAL_ORGANIZATION("社会团体", "SO"),
    
    /**
     * 事业单位
     */
    PUBLIC_INSTITUTION("事业单位", "PI"),
    
    /**
     * 分公司
     */
    BRANCH_COMPANY("分公司", "BC"),
    
    /**
     * 其他
     */
    OTHER("其他", "OTHER");
    
    /**
     * 企业类型中文名称
     */
    private final String chineseName;
    
    /**
     * 企业类型代码
     */
    private final String code;
    
    EnterpriseType(String chineseName, String code) {
        this.chineseName = chineseName;
        this.code = code;
    }
    
    /**
     * 根据代码获取企业类型
     * 
     * @param code 企业类型代码
     * @return 企业类型枚举
     */
    public static EnterpriseType fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new DomainRuleViolationException("企业类型代码不能为空");
        }
        
        return Stream.of(values())
                .filter(type -> type.getCode().equalsIgnoreCase(code))
                .findFirst()
                .orElseThrow(() -> new DomainRuleViolationException("无效的企业类型代码: " + code));
    }
    
    /**
     * 根据中文名称获取企业类型
     * 
     * @param chineseName 企业类型中文名称
     * @return 企业类型枚举
     */
    public static EnterpriseType fromChineseName(String chineseName) {
        if (StringUtils.isBlank(chineseName)) {
            throw new DomainRuleViolationException("企业类型名称不能为空");
        }
        
        return Stream.of(values())
                .filter(type -> type.getChineseName().equals(chineseName))
                .findFirst()
                .orElseThrow(() -> new DomainRuleViolationException("无效的企业类型名称: " + chineseName));
    }
    
    /**
     * 判断是否为个体经营类型
     * 
     * @return 如果是个体经营类型返回true
     */
    public boolean isIndividualBusiness() {
        return this == INDIVIDUAL_BUSINESS || this == SOLE_PROPRIETORSHIP;
    }
    
    /**
     * 判断是否为公司类型
     * 
     * @return 如果是公司类型返回true
     */
    public boolean isCompany() {
        return this == LIMITED_LIABILITY_COMPANY || 
               this == JOINT_STOCK_COMPANY || 
               this == WHOLLY_FOREIGN_OWNED_ENTERPRISE || 
               this == JOINT_VENTURE;
    }
    
    /**
     * 判断是否为非营利组织
     * 
     * @return 如果是非营利组织返回true
     */
    public boolean isNonProfit() {
        return this == PRIVATE_NON_ENTERPRISE_UNIT || 
               this == SOCIAL_ORGANIZATION || 
               this == PUBLIC_INSTITUTION;
    }
} 