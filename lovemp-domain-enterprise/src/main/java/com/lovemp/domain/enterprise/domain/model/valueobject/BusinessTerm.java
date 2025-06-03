package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDate;

/**
 * 经营期限值对象
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BusinessTerm implements ValueObject {
    
    /**
     * 是否长期
     */
    boolean isLongTerm;
    
    /**
     * 终止日期（仅在非长期情况下有效）
     */
    LocalDate endDate;
    
    /**
     * 创建长期经营期限
     * 
     * @return 长期经营期限值对象
     */
    public static BusinessTerm longTerm() {
        return new BusinessTerm(true, null);
    }
    
    /**
     * 创建固定经营期限
     * 
     * @param endDate 终止日期
     * @return 固定经营期限值对象
     */
    public static BusinessTerm fixed(LocalDate endDate) {
        if (endDate == null) {
            throw new DomainRuleViolationException("终止日期不能为空");
        }
        
        if (endDate.isBefore(DateTimeUtils.getCurrentDate())) {
            throw new DomainRuleViolationException("终止日期不能早于当前日期");
        }
        
        return new BusinessTerm(false, endDate);
    }
    
    /**
     * 检查经营期限是否过期
     * 
     * @return 如果已过期返回true
     */
    public boolean isExpired() {
        return !isLongTerm && endDate.isBefore(DateTimeUtils.getCurrentDate());
    }
    
    /**
     * 获取剩余经营天数
     * 
     * @return 剩余天数，如果是长期则返回Integer.MAX_VALUE
     */
    public int getRemainingDays() {
        if (isLongTerm) {
            return Integer.MAX_VALUE;
        }
        
        return (int) DateTimeUtils.daysBetween(DateTimeUtils.getCurrentDate(), endDate);
    }
    
    /**
     * 格式化为字符串
     * 
     * @return 格式化后的经营期限字符串
     */
    @Override
    public String toString() {
        if (isLongTerm) {
            return "长期";
        } else {
            return "至 " + endDate.toString();
        }
    }
} 