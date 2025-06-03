package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

/**
 * 企业备案值对象
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Filing implements ValueObject {
    
    /**
     * 备案ID
     */
    String id;
    
    /**
     * 备案类型
     */
    FilingType type;
    
    /**
     * 备案号
     */
    String filingNumber;
    
    /**
     * 网站/应用名称
     */
    String siteName;
    
    /**
     * 网站域名/应用标识
     */
    String siteIdentifier;
    
    /**
     * 备案主体
     */
    String filingEntity;
    
    /**
     * 备案日期
     */
    LocalDate filingDate;
    
    /**
     * 备案审核通过日期
     */
    LocalDate approvalDate;
    
    /**
     * 备案有效期开始日期
     */
    LocalDate validFromDate;
    
    /**
     * 备案有效期结束日期（如果为null表示长期有效）
     */
    LocalDate validToDate;
    
    /**
     * 备案状态（有效/无效/已注销等）
     */
    String status;
    
    /**
     * 备案单位/机构
     */
    String filingAuthority;
    
    /**
     * 备案证明文件存储路径
     */
    String filePath;
    
    /**
     * 备注
     */
    String remark;
    
    /**
     * 创建企业备案
     * 
     * @param id 备案ID
     * @param type 备案类型
     * @param filingNumber 备案号
     * @param siteName 网站/应用名称
     * @param siteIdentifier 网站域名/应用标识
     * @param filingEntity 备案主体
     * @param filingDate 备案日期
     * @param approvalDate 备案审核通过日期
     * @param validFromDate 备案有效期开始日期
     * @param validToDate 备案有效期结束日期
     * @param status 备案状态
     * @param filingAuthority 备案单位/机构
     * @param filePath 备案证明文件存储路径
     * @param remark 备注
     * @return 企业备案值对象
     */
    public static Filing of(
            String id, 
            FilingType type,
            String filingNumber,
            String siteName,
            String siteIdentifier,
            String filingEntity,
            LocalDate filingDate,
            LocalDate approvalDate,
            LocalDate validFromDate,
            LocalDate validToDate,
            String status,
            String filingAuthority,
            String filePath,
            String remark) {
        
        if (type == null) {
            throw new DomainRuleViolationException("备案类型不能为空");
        }
        
        if (StringUtils.isBlank(filingNumber)) {
            throw new DomainRuleViolationException("备案号不能为空");
        }
        
        if (StringUtils.isBlank(filingEntity)) {
            throw new DomainRuleViolationException("备案主体不能为空");
        }
        
        if (filingDate == null) {
            throw new DomainRuleViolationException("备案日期不能为空");
        }
        
        if (StringUtils.isBlank(filingAuthority)) {
            throw new DomainRuleViolationException("备案单位/机构不能为空");
        }
        
        // 备案类型为网站相关时，网站名称和标识必填
        if (type.isWebsiteRelated()) {
            if (StringUtils.isBlank(siteName)) {
                throw new DomainRuleViolationException("网站名称不能为空");
            }
            
            if (StringUtils.isBlank(siteIdentifier)) {
                throw new DomainRuleViolationException("网站域名/标识不能为空");
            }
        }
        
        if (StringUtils.isBlank(status)) {
            status = "有效";
        }
        
        return new Filing(
                id,
                type,
                filingNumber,
                siteName,
                siteIdentifier,
                filingEntity,
                filingDate,
                approvalDate,
                validFromDate,
                validToDate,
                status,
                filingAuthority,
                filePath,
                remark);
    }
    
    /**
     * 备案是否过期
     * 
     * @return 如果备案已过期返回true
     */
    public boolean isExpired() {
        if (validToDate == null) {
            return false; // 长期有效
        }
        return validToDate.isBefore(DateTimeUtils.getCurrentDate());
    }
    
    /**
     * 备案是否有效
     * 
     * @return 如果备案当前有效返回true
     */
    public boolean isValid() {
        if (!"有效".equals(status)) {
            return false;
        }
        
        if (validFromDate == null) {
            return !isExpired();
        }
        
        LocalDate now = DateTimeUtils.getCurrentDate();
        return !validFromDate.isAfter(now) && (validToDate == null || !validToDate.isBefore(now));
    }
    
    /**
     * 获取备案剩余有效天数
     * 
     * @return 剩余有效天数，如果长期有效则返回Integer.MAX_VALUE
     */
    public int getRemainingDays() {
        if (validToDate == null) {
            return Integer.MAX_VALUE; // 长期有效
        }
        
        return (int) DateTimeUtils.daysBetween(DateTimeUtils.getCurrentDate(), validToDate);
    }
    
    /**
     * 备案是否即将过期
     * 
     * @param daysThreshold 即将过期的天数阈值
     * @return 如果备案即将过期返回true
     */
    public boolean isExpiringsSoon(int daysThreshold) {
        if (validToDate == null) {
            return false; // 长期有效
        }
        
        int remainingDays = getRemainingDays();
        return remainingDays >= 0 && remainingDays <= daysThreshold;
    }
    
    /**
     * 是否为网站备案
     * 
     * @return 如果是网站备案返回true
     */
    public boolean isWebsiteFiling() {
        return type.isWebsiteRelated();
    }
    
    /**
     * 是否为电信业务备案
     * 
     * @return 如果是电信业务备案返回true
     */
    public boolean isTelecomFiling() {
        return type.isTelecomRelated();
    }
    
    /**
     * 是否为内容服务备案
     * 
     * @return 如果是内容服务备案返回true
     */
    public boolean isContentServiceFiling() {
        return type.isContentServiceRelated();
    }
} 