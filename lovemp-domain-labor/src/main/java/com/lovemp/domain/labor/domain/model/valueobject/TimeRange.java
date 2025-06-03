package com.lovemp.domain.labor.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.Assert;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

/**
 * 时间范围 - 值对象
 * 表示一个时间范围，包含开始日期和可选的结束日期
 */
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TimeRange implements ValueObject {
    
    /**
     * 开始日期
     */
    private LocalDate startDate;
    
    /**
     * 结束日期（可为空，表示无限期）
     */
    private LocalDate endDate;
    
    private TimeRange(LocalDate startDate, LocalDate endDate) {
        Assert.notNull(startDate, "开始日期不能为空");
        
        // 开始日期可以是任何日期，包括未来日期
        // if (startDate.isAfter(DateTimeUtils.getCurrentDate())) {
        //     throw new DomainRuleViolationException("开始日期不能晚于当前日期");
        // }
        
        // 如果有结束日期,必须晚于开始日期
        if (endDate != null) {
            if (!endDate.isAfter(startDate)) {
                throw new DomainRuleViolationException("结束日期必须晚于开始日期");
            }
            
            // 结束日期不能晚于开始日期后的100年
            if (endDate.isAfter(startDate.plusYears(100))) {
                throw new DomainRuleViolationException("结束日期不能超过开始日期100年");
            }
        }
        
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    /**
     * 创建有限时间范围
     *
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 时间范围
     */
    public static TimeRange of(LocalDate startDate, LocalDate endDate) {
        return new TimeRange(startDate, endDate);
    }
    
    /**
     * 创建无限期时间范围
     *
     * @param startDate 开始日期
     * @return 时间范围
     */
    public static TimeRange ofUnlimited(LocalDate startDate) {
        return new TimeRange(startDate, null);
    }
    
    /**
     * 判断指定日期是否在范围内
     *
     * @param date 日期
     * @return 是否在范围内
     */
    public boolean contains(LocalDate date) {
        Assert.notNull(date, "日期不能为空");
        
        // 日期必须大于等于开始日期
        if (date.isBefore(startDate)) {
            return false;
        }
        
        // 如果有结束日期，日期必须小于等于结束日期
        if (endDate != null && date.isAfter(endDate)) {
            return false;
        }
        
        return true;
    }
    
    /**
     * 判断是否与另一个时间范围重叠
     * 重叠的定义:
     * 1. 一个范围的开始日期在另一个范围内
     * 2. 一个范围的结束日期在另一个范围内
     * 3. 一个范围完全包含另一个范围
     *
     * @param other 另一个时间范围
     * @return 是否重叠
     */
    public boolean overlaps(TimeRange other) {
        if (other == null) {
            return false;
        }
        
        // 如果任一范围是无限期的,只需要检查开始日期
        if (this.endDate == null || other.endDate == null) {
            // 如果两个都是无限期,必定重叠
            if (this.endDate == null && other.endDate == null) {
                return true;
            }
            
            // 一个无限期,一个有限期
            if (this.endDate == null) {
                return !this.startDate.isAfter(other.endDate);
            } else {
                return !other.startDate.isAfter(this.endDate);
            }
        }
        
        // 两个都是有限期
        // 检查一个范围的开始日期是否在另一个范围内
        boolean thisStartInOther = !this.startDate.isBefore(other.startDate) && !this.startDate.isAfter(other.endDate);
        boolean otherStartInThis = !other.startDate.isBefore(this.startDate) && !other.startDate.isAfter(this.endDate);
        
        // 检查一个范围的结束日期是否在另一个范围内
        boolean thisEndInOther = !this.endDate.isBefore(other.startDate) && !this.endDate.isAfter(other.endDate);
        boolean otherEndInThis = !other.endDate.isBefore(this.startDate) && !other.endDate.isAfter(this.endDate);
        
        // 检查一个范围是否完全包含另一个范围
        boolean thisContainsOther = !this.startDate.isAfter(other.startDate) && !this.endDate.isBefore(other.endDate);
        boolean otherContainsThis = !other.startDate.isAfter(this.startDate) && !other.endDate.isBefore(this.endDate);
        
        return thisStartInOther || otherStartInThis || thisEndInOther || otherEndInThis || 
               thisContainsOther || otherContainsThis;
    }
    
    @Override
    public String toString() {
        return String.format("TimeRange{startDate=%s, endDate=%s}", 
                startDate, endDate == null ? "unlimited" : endDate);
    }
}