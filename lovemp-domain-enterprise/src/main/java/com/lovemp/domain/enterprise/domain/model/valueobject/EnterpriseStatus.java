package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.StringUtils;
import lombok.Getter;

/**
 * 企业状态枚举
 */
@Getter
public enum EnterpriseStatus implements ValueObject {
    
    /**
     * 筹建中
     */
    PREPARING("筹建中", "PREP"),
    
    /**
     * 正常营业
     */
    ACTIVE("正常营业", "ACT"),
    
    /**
     * 停业整顿
     */
    SUSPENDED("停业整顿", "SUSP"),
    
    /**
     * 经营异常
     */
    ABNORMAL("经营异常", "ABN"),
    
    /**
     * 已注销
     */
    CANCELLED("已注销", "CXL"),
    
    /**
     * 已吊销
     */
    REVOKED("已吊销", "REV"),
    
    /**
     * 迁入
     */
    MOVED_IN("迁入", "MIN"),
    
    /**
     * 迁出
     */
    MOVED_OUT("迁出", "MOUT"),
    
    /**
     * 解散中
     */
    DISSOLVING("解散中", "DISS"),
    
    /**
     * 破产清算
     */
    BANKRUPT("破产清算", "BANK");
    
    /**
     * 状态中文名称
     */
    private final String chineseName;
    
    /**
     * 状态代码
     */
    private final String code;
    
    EnterpriseStatus(String chineseName, String code) {
        this.chineseName = chineseName;
        this.code = code;
    }
    
    /**
     * 根据代码获取企业状态
     * 
     * @param code 状态代码
     * @return 企业状态枚举
     */
    public static EnterpriseStatus fromCode(String code) {
        if (StringUtils.isBlank(code)) {
            throw new DomainRuleViolationException("企业状态代码不能为空");
        }
        
        for (EnterpriseStatus status : EnterpriseStatus.values()) {
            if (status.getCode().equalsIgnoreCase(code)) {
                return status;
            }
        }
        
        throw new DomainRuleViolationException("无效的企业状态代码: " + code);
    }
    
    /**
     * 根据中文名称获取企业状态
     * 
     * @param chineseName 状态中文名称
     * @return 企业状态枚举
     */
    public static EnterpriseStatus fromChineseName(String chineseName) {
        if (StringUtils.isBlank(chineseName)) {
            throw new DomainRuleViolationException("企业状态名称不能为空");
        }
        
        for (EnterpriseStatus status : EnterpriseStatus.values()) {
            if (status.getChineseName().equals(chineseName)) {
                return status;
            }
        }
        
        throw new DomainRuleViolationException("无效的企业状态名称: " + chineseName);
    }
    
    /**
     * 判断企业状态是否正常
     * 
     * @return 如果企业状态正常返回true
     */
    public boolean isActive() {
        return this == ACTIVE || this == MOVED_IN;
    }
    
    /**
     * 判断企业是否已终止
     * 
     * @return 如果企业已终止返回true
     */
    public boolean isTerminated() {
        return this == CANCELLED || 
               this == REVOKED || 
               this == MOVED_OUT || 
               this == BANKRUPT;
    }
    
    /**
     * 判断企业是否异常
     * 
     * @return 如果企业状态异常返回true
     */
    public boolean isAbnormal() {
        return this == ABNORMAL || 
               this == SUSPENDED || 
               this == DISSOLVING;
    }
} 