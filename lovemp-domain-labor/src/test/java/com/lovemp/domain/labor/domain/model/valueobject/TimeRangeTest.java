package com.lovemp.domain.labor.domain.model.valueobject;

import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.DateTimeUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("时间范围值对象测试")
class TimeRangeTest {
    
    @Test
    @DisplayName("创建有限时间范围应成功")
    void shouldCreateTimeRangeWithEndDate() {
        LocalDate startDate = DateTimeUtils.getCurrentDate().minusMonths(1);
        LocalDate endDate = DateTimeUtils.getCurrentDate().plusMonths(1);
        
        TimeRange timeRange = TimeRange.of(startDate, endDate);
        
        assertNotNull(timeRange);
        assertEquals(startDate, timeRange.getStartDate());
        assertEquals(endDate, timeRange.getEndDate());
    }
    
    @Test
    @DisplayName("创建无限期时间范围应成功")
    void shouldCreateUnlimitedTimeRange() {
        LocalDate startDate = DateTimeUtils.getCurrentDate().minusMonths(1);
        
        TimeRange timeRange = TimeRange.ofUnlimited(startDate);
        
        assertNotNull(timeRange);
        assertEquals(startDate, timeRange.getStartDate());
        assertNull(timeRange.getEndDate());
    }
    
    @Test
    @DisplayName("开始日期可以是未来日期")
    void shouldAllowFutureStartDate() {
        LocalDate futureStartDate = DateTimeUtils.getCurrentDate().plusDays(1);
        LocalDate endDate = futureStartDate.plusMonths(1);
        
        TimeRange timeRange = TimeRange.of(futureStartDate, endDate);
        
        assertNotNull(timeRange);
        assertEquals(futureStartDate, timeRange.getStartDate());
        assertEquals(endDate, timeRange.getEndDate());
    }
    
    @Test
    @DisplayName("结束日期早于开始日期应抛出异常")
    void shouldThrowExceptionWhenEndDateBeforeStartDate() {
        LocalDate startDate = DateTimeUtils.getCurrentDate().minusMonths(1);
        LocalDate endDate = startDate.minusDays(1);
        
        assertThrows(DomainRuleViolationException.class,
                () -> TimeRange.of(startDate, endDate),
                "结束日期必须晚于开始日期");
    }
    
    @Test
    @DisplayName("结束日期超过100年应抛出异常")
    void shouldThrowExceptionWhenEndDateExceeds100Years() {
        LocalDate startDate = DateTimeUtils.getCurrentDate().minusMonths(1);
        LocalDate endDate = startDate.plusYears(101);
        
        assertThrows(DomainRuleViolationException.class,
                () -> TimeRange.of(startDate, endDate),
                "结束日期不能超过开始日期100年");
    }
    
    @Test
    @DisplayName("开始日期为空应抛出异常")
    void shouldThrowExceptionWhenStartDateIsNull() {
        LocalDate endDate = DateTimeUtils.getCurrentDate().plusMonths(1);
        
        assertThrows(DomainRuleViolationException.class,
                () -> TimeRange.of(null, endDate),
                "开始日期不能为空");
    }
    
    @Test
    @DisplayName("判断日期是否在范围内应正确")
    void shouldCheckIfDateIsContained() {
        LocalDate startDate = DateTimeUtils.getCurrentDate().minusMonths(1);
        LocalDate endDate = DateTimeUtils.getCurrentDate().plusMonths(1);
        TimeRange timeRange = TimeRange.of(startDate, endDate);
        
        // 范围内的日期
        assertTrue(timeRange.contains(DateTimeUtils.getCurrentDate()));
        assertTrue(timeRange.contains(startDate));
        assertTrue(timeRange.contains(endDate));
        
        // 范围外的日期
        assertFalse(timeRange.contains(startDate.minusDays(1)));
        assertFalse(timeRange.contains(endDate.plusDays(1)));
    }
    
    @Test
    @DisplayName("无限期范围的日期判断应正确")
    void shouldCheckIfDateIsContainedInUnlimitedRange() {
        LocalDate startDate = DateTimeUtils.getCurrentDate().minusMonths(1);
        TimeRange timeRange = TimeRange.ofUnlimited(startDate);
        
        // 范围内的日期
        assertTrue(timeRange.contains(startDate));
        assertTrue(timeRange.contains(DateTimeUtils.getCurrentDate()));
        assertTrue(timeRange.contains(startDate.plusYears(1)));
        
        // 范围外的日期
        assertFalse(timeRange.contains(startDate.minusDays(1)));
    }
    
    @Test
    @DisplayName("时间范围重叠判断应正确")
    void shouldCheckIfRangesOverlap() {
        LocalDate baseDate = DateTimeUtils.getCurrentDate().minusMonths(2);
        LocalDate date1 = baseDate;
        LocalDate date2 = baseDate.plusMonths(1);
        LocalDate date3 = baseDate.plusMonths(2);
        LocalDate date4 = baseDate.plusMonths(3);
        
        // 完全重叠
        TimeRange range1 = TimeRange.of(date1, date3);
        TimeRange range2 = TimeRange.of(date1, date3);
        assertTrue(range1.overlaps(range2));
        
        // 部分重叠 - 开始日期在范围内
        TimeRange range3 = TimeRange.of(date1, date3);
        TimeRange range4 = TimeRange.of(date2, date4);
        assertTrue(range3.overlaps(range4));
        assertTrue(range4.overlaps(range3));
        
        // 部分重叠 - 结束日期在范围内
        TimeRange range5 = TimeRange.of(date1, date3);
        TimeRange range6 = TimeRange.of(date2, date4);
        assertTrue(range5.overlaps(range6));
        assertTrue(range6.overlaps(range5));
        
        // 完全包含
        TimeRange range7 = TimeRange.of(date1, date4);
        TimeRange range8 = TimeRange.of(date2, date3);
        assertTrue(range7.overlaps(range8));
        assertTrue(range8.overlaps(range7));
        
        // 无重叠
        TimeRange range9 = TimeRange.of(date1, date2);
        TimeRange range10 = TimeRange.of(date3, date4);
        assertFalse(range9.overlaps(range10));
        assertFalse(range10.overlaps(range9));
        
        // 一个无限期，一个有限期 - 有重叠
        TimeRange range11 = TimeRange.ofUnlimited(date1);
        TimeRange range12 = TimeRange.of(date2, date3);
        assertTrue(range11.overlaps(range12));
        assertTrue(range12.overlaps(range11));
        
        // 一个无限期，一个有限期 - 无重叠
        TimeRange range13 = TimeRange.ofUnlimited(date3);
        TimeRange range14 = TimeRange.of(date1, date2);
        assertFalse(range13.overlaps(range14));
        assertFalse(range14.overlaps(range13));
        
        // 两个无限期
        TimeRange range15 = TimeRange.ofUnlimited(date1);
        TimeRange range16 = TimeRange.ofUnlimited(date2);
        assertTrue(range15.overlaps(range16));
        assertTrue(range16.overlaps(range15));
    }
    
    @Test
    @DisplayName("相等性判断应正确")
    void shouldCheckEquality() {
        LocalDate startDate = DateTimeUtils.getCurrentDate().minusMonths(1);
        LocalDate endDate = DateTimeUtils.getCurrentDate().plusMonths(1);
        
        TimeRange range1 = TimeRange.of(startDate, endDate);
        TimeRange range2 = TimeRange.of(startDate, endDate);
        TimeRange range3 = TimeRange.of(startDate, endDate.minusDays(1));
        
        // 相同的时间范围应相等
        assertEquals(range1, range2);
        assertEquals(range1.hashCode(), range2.hashCode());
        
        // 不同的时间范围不应相等
        assertNotEquals(range1, range3);
        assertNotEquals(range1.hashCode(), range3.hashCode());
        
        // 与null比较
        assertNotEquals(range1, null);
        
        // 与其他类型比较
        assertNotEquals(range1, "not a time range");
    }
    
    @Test
    @DisplayName("toString方法应返回正确的字符串表示")
    void shouldHaveCorrectStringRepresentation() {
        LocalDate startDate = DateTimeUtils.getCurrentDate().minusMonths(1);
        LocalDate endDate = DateTimeUtils.getCurrentDate().plusMonths(1);
        
        TimeRange range1 = TimeRange.of(startDate, endDate);
        TimeRange range2 = TimeRange.ofUnlimited(startDate);
        
        assertEquals("TimeRange{startDate=" + startDate + ", endDate=" + endDate + "}", range1.toString());
        assertEquals("TimeRange{startDate=" + startDate + ", endDate=unlimited}", range2.toString());
    }
}