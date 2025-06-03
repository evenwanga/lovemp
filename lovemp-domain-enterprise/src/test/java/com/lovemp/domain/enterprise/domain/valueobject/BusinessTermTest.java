package com.lovemp.domain.enterprise.domain.valueobject;

import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.domain.enterprise.domain.model.valueobject.BusinessTerm;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class BusinessTermTest {

    @Test
    void testCreateLongTerm() {
        BusinessTerm businessTerm = BusinessTerm.longTerm();
        
        assertTrue(businessTerm.isLongTerm());
        assertNull(businessTerm.getEndDate());
        assertFalse(businessTerm.isExpired());
        assertEquals(Integer.MAX_VALUE, businessTerm.getRemainingDays());
        assertEquals("长期", businessTerm.toString());
    }
    
    @Test
    void testCreateFixedWithValidEndDate() {
        LocalDate futureDate = DateTimeUtils.getCurrentDate().plusYears(1);
        BusinessTerm businessTerm = BusinessTerm.fixed(futureDate);
        
        assertFalse(businessTerm.isLongTerm());
        assertEquals(futureDate, businessTerm.getEndDate());
        assertFalse(businessTerm.isExpired());
        
        // 计算预期的剩余天数（允许1天的误差，因为测试执行时间可能导致天数计算有微小差异）
        long expectedDays = DateTimeUtils.daysBetween(DateTimeUtils.getCurrentDate(), futureDate);
        assertTrue(Math.abs(expectedDays - businessTerm.getRemainingDays()) <= 1);
        
        assertEquals("至 " + futureDate.toString(), businessTerm.toString());
    }
    
    @Test
    void testCreateFixedWithNullEndDate() {
        assertThrows(DomainRuleViolationException.class, () -> 
            BusinessTerm.fixed(null)
        );
    }
    
    @Test
    void testCreateFixedWithPastEndDate() {
        LocalDate pastDate = DateTimeUtils.getCurrentDate().minusDays(1);
        assertThrows(DomainRuleViolationException.class, () -> 
            BusinessTerm.fixed(pastDate)
        );
    }
    
    @Test
    void testIsExpired() {
        // 这个测试需要利用反射或修改代码来测试已过期的情况，
        // 因为BusinessTerm.fixed()方法不允许创建已过期的实例
        
        // 我们可以创建一个临界日期（明天过期）来测试
        LocalDate tomorrowDate = DateTimeUtils.getCurrentDate().plusDays(1);
        BusinessTerm businessTerm = BusinessTerm.fixed(tomorrowDate);
        
        assertFalse(businessTerm.isExpired());  // 现在还没过期
        assertEquals(1, businessTerm.getRemainingDays());  // 剩余1天
    }
    
    @Test
    void testGetRemainingDays() {
        // 测试各种剩余天数的情况
        LocalDate endDate1 = DateTimeUtils.getCurrentDate().plusDays(10);
        BusinessTerm term1 = BusinessTerm.fixed(endDate1);
        assertEquals(10, term1.getRemainingDays());
        
        LocalDate endDate2 = DateTimeUtils.getCurrentDate().plusYears(1);
        BusinessTerm term2 = BusinessTerm.fixed(endDate2);
        
        // 计算预期的剩余天数（允许1天的误差）
        long expectedDays = DateTimeUtils.daysBetween(DateTimeUtils.getCurrentDate(), endDate2);
        assertTrue(Math.abs(expectedDays - term2.getRemainingDays()) <= 1);
    }
    
    @Test
    void testToString() {
        BusinessTerm longTerm = BusinessTerm.longTerm();
        assertEquals("长期", longTerm.toString());
        
        LocalDate endDate = DateTimeUtils.getCurrentDate().plusYears(1);
        BusinessTerm fixedTerm = BusinessTerm.fixed(endDate);
        assertEquals("至 " + endDate.toString(), fixedTerm.toString());
    }
    
    @Test
    void testEquals() {
        BusinessTerm longTerm1 = BusinessTerm.longTerm();
        BusinessTerm longTerm2 = BusinessTerm.longTerm();
        
        LocalDate endDate = DateTimeUtils.getCurrentDate().plusYears(1);
        BusinessTerm fixedTerm1 = BusinessTerm.fixed(endDate);
        BusinessTerm fixedTerm2 = BusinessTerm.fixed(endDate);
        BusinessTerm fixedTerm3 = BusinessTerm.fixed(endDate.plusDays(1));
        
        // 相同类型和终止日期的实例应该相等
        assertEquals(longTerm1, longTerm2);
        assertEquals(fixedTerm1, fixedTerm2);
        
        // 不同类型或不同终止日期的实例不应该相等
        assertNotEquals(longTerm1, fixedTerm1);
        assertNotEquals(fixedTerm1, fixedTerm3);
        
        // 与null或其他类型比较
        assertNotEquals(longTerm1, null);
        assertNotEquals(longTerm1, "notBusinessTerm");
    }
    
    @Test
    void testHashCode() {
        BusinessTerm longTerm1 = BusinessTerm.longTerm();
        BusinessTerm longTerm2 = BusinessTerm.longTerm();
        
        LocalDate endDate = DateTimeUtils.getCurrentDate().plusYears(1);
        BusinessTerm fixedTerm1 = BusinessTerm.fixed(endDate);
        BusinessTerm fixedTerm2 = BusinessTerm.fixed(endDate);
        
        // 相同的实例应该有相同的哈希码
        assertEquals(longTerm1.hashCode(), longTerm2.hashCode());
        assertEquals(fixedTerm1.hashCode(), fixedTerm2.hashCode());
    }
} 