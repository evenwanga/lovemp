package com.lovemp.domain.person.domain.valueobject;

import com.lovemp.domain.person.domain.model.valueobject.ContactInfo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("联系方式值对象测试")
class ContactInfoTest {

    @Test
    @DisplayName("测试创建基本联系方式")
    void testCreateBasicContactInfo() {
        // 创建基本联系方式
        ContactInfo contactInfo = ContactInfo.basic("13800138000", "test@example.com");
        
        // 验证结果
        assertEquals("13800138000", contactInfo.getMobile());
        assertEquals("test@example.com", contactInfo.getEmail());
        assertNull(contactInfo.getTelephone());
        assertNull(contactInfo.getWechat());
        assertNull(contactInfo.getEmergencyContact());
        assertNull(contactInfo.getEmergencyPhone());
    }
    
    @Test
    @DisplayName("测试创建完整联系方式")
    void testCreateFullContactInfo() {
        // 创建完整联系方式
        ContactInfo contactInfo = ContactInfo.of(
                "13800138000",
                "test@example.com",
                "010-12345678",
                "wxid_123456",
                "张三",
                "13900139000"
        );
        
        // 验证结果
        assertEquals("13800138000", contactInfo.getMobile());
        assertEquals("test@example.com", contactInfo.getEmail());
        assertEquals("010-12345678", contactInfo.getTelephone());
        assertEquals("wxid_123456", contactInfo.getWechat());
        assertEquals("张三", contactInfo.getEmergencyContact());
        assertEquals("13900139000", contactInfo.getEmergencyPhone());
    }
    
    @Test
    @DisplayName("测试更新手机号")
    void testUpdateMobile() {
        // 创建基本联系方式
        ContactInfo original = ContactInfo.basic("13800138000", "test@example.com");
        
        // 更新手机号
        ContactInfo updated = original.withMobile("13900139000");
        
        // 验证结果
        assertEquals("13900139000", updated.getMobile());
        assertEquals(original.getEmail(), updated.getEmail());
        
        // 原对象不变
        assertEquals("13800138000", original.getMobile());
    }
    
    @Test
    @DisplayName("测试更新邮箱")
    void testUpdateEmail() {
        // 创建基本联系方式
        ContactInfo original = ContactInfo.basic("13800138000", "test@example.com");
        
        // 更新邮箱
        ContactInfo updated = original.withEmail("new@example.com");
        
        // 验证结果
        assertEquals("new@example.com", updated.getEmail());
        assertEquals(original.getMobile(), updated.getMobile());
        
        // 原对象不变
        assertEquals("test@example.com", original.getEmail());
    }
    
    @Test
    @DisplayName("测试更新紧急联系人")
    void testUpdateEmergencyContact() {
        // 创建基本联系方式
        ContactInfo original = ContactInfo.basic("13800138000", "test@example.com");
        
        // 更新紧急联系人
        ContactInfo updated = original.withEmergencyContact("张三", "13900139000");
        
        // 验证结果
        assertEquals("张三", updated.getEmergencyContact());
        assertEquals("13900139000", updated.getEmergencyPhone());
        assertEquals(original.getMobile(), updated.getMobile());
        assertEquals(original.getEmail(), updated.getEmail());
        
        // 原对象不变
        assertNull(original.getEmergencyContact());
        assertNull(original.getEmergencyPhone());
    }
    
    @Test
    @DisplayName("测试值对象相等性")
    void testEquality() {
        // 创建两个相同的联系方式但实例不同
        ContactInfo contact1 = ContactInfo.basic("13800138000", "test@example.com");
        ContactInfo contact2 = ContactInfo.basic("13800138000", "test@example.com");
        
        // 验证相等性
        assertEquals(contact1, contact2);
        assertEquals(contact1.hashCode(), contact2.hashCode());
        
        // 创建不同的联系方式
        ContactInfo contact3 = ContactInfo.basic("13900139000", "test@example.com");
        assertNotEquals(contact1, contact3);
        
        ContactInfo contact4 = ContactInfo.basic("13800138000", "other@example.com");
        assertNotEquals(contact1, contact4);
        
        // 测试完整构造函数创建的对象相等性
        ContactInfo fullContact1 = ContactInfo.of(
                "13800138000",
                "test@example.com",
                "010-12345678",
                "wxid_123456",
                "张三",
                "13900139000"
        );
        
        ContactInfo fullContact2 = ContactInfo.of(
                "13800138000",
                "test@example.com",
                "010-12345678",
                "wxid_123456",
                "张三",
                "13900139000"
        );
        
        assertEquals(fullContact1, fullContact2);
    }
} 