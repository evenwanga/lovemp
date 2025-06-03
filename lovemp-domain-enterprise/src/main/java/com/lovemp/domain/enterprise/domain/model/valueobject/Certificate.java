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
 * 企业证书值对象
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Certificate implements ValueObject {
    
    /**
     * 证书ID
     */
    String id;
    
    /**
     * 证书名称
     */
    String name;
    
    /**
     * 证书类型
     */
    CertificateType type;
    
    /**
     * 证书编号
     */
    String certificateNumber;
    
    /**
     * 发证机构
     */
    String issuingAuthority;
    
    /**
     * 发证日期
     */
    LocalDate issueDate;
    
    /**
     * 有效期开始日期
     */
    LocalDate validFromDate;
    
    /**
     * 有效期结束日期（如果为null表示长期有效）
     */
    LocalDate validToDate;
    
    /**
     * 证书文件存储路径
     */
    String filePath;
    
    /**
     * 备注
     */
    String remark;
    
    /**
     * 创建证书
     * 
     * @param id 证书ID
     * @param name 证书名称
     * @param type 证书类型
     * @param certificateNumber 证书编号
     * @param issuingAuthority 发证机构
     * @param issueDate 发证日期
     * @param validFromDate 有效期开始日期
     * @param validToDate 有效期结束日期
     * @param filePath 证书文件存储路径
     * @param remark 备注
     * @return 证书值对象
     */
    public static Certificate of(
            String id, 
            String name,
            CertificateType type,
            String certificateNumber,
            String issuingAuthority,
            LocalDate issueDate,
            LocalDate validFromDate,
            LocalDate validToDate,
            String filePath,
            String remark) {
        
        if (StringUtils.isBlank(name)) {
            throw new DomainRuleViolationException("证书名称不能为空");
        }
        
        if (type == null) {
            throw new DomainRuleViolationException("证书类型不能为空");
        }
        
        if (StringUtils.isBlank(certificateNumber)) {
            throw new DomainRuleViolationException("证书编号不能为空");
        }
        
        if (StringUtils.isBlank(issuingAuthority)) {
            throw new DomainRuleViolationException("发证机构不能为空");
        }
        
        if (issueDate == null) {
            throw new DomainRuleViolationException("发证日期不能为空");
        }
        
        if (validFromDate == null) {
            throw new DomainRuleViolationException("有效期开始日期不能为空");
        }
        
        // validToDate可以为null，表示长期有效
        
        return new Certificate(
                id,
                name,
                type,
                certificateNumber,
                issuingAuthority,
                issueDate,
                validFromDate,
                validToDate,
                filePath,
                remark);
    }
    
    /**
     * 证书是否过期
     * 
     * @return 如果证书已过期返回true
     */
    public boolean isExpired() {
        if (validToDate == null) {
            return false; // 长期有效
        }
        return validToDate.isBefore(DateTimeUtils.getCurrentDate());
    }
    
    /**
     * 证书是否有效
     * 
     * @return 如果证书当前有效返回true
     */
    public boolean isValid() {
        LocalDate now = DateTimeUtils.getCurrentDate();
        return !validFromDate.isAfter(now) && (validToDate == null || !validToDate.isBefore(now));
    }
    
    /**
     * 获取证书剩余有效天数
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
     * 证书是否即将过期
     * 
     * @param daysThreshold 即将过期的天数阈值
     * @return 如果证书即将过期返回true
     */
    public boolean isExpiringsSoon(int daysThreshold) {
        if (validToDate == null) {
            return false; // 长期有效
        }
        
        int remainingDays = getRemainingDays();
        return remainingDays >= 0 && remainingDays <= daysThreshold;
    }
} 