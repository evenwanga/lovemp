package com.lovemp.domain.person.domain.valueobject;

import com.lovemp.domain.person.domain.model.valueobject.ContactInfo;
import com.lovemp.domain.person.domain.model.valueobject.ContactInfoWithTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("带标签的联系方式值对象测试")
class ContactInfoWithTagTest {

    @Test
    @DisplayName("测试创建带标签的联系方式值对象")
    void testCreateWithTag() {
        // 创建联系方式
        ContactInfo contactInfo = ContactInfo.basic("13800138000", "test@example.com");
        
        // 创建带标签的联系方式
        ContactInfoWithTag contactWithTag = ContactInfoWithTag.of(contactInfo, "个人联系方式", true);
        
        // 验证结果
        assertEquals(contactInfo, contactWithTag.getContactInfo());
        assertEquals("个人联系方式", contactWithTag.getTag());
        assertTrue(contactWithTag.isDefault());
    }
    
    @Test
    @DisplayName("测试创建不带标签的联系方式值对象")
    void testCreateWithoutTag() {
        // 创建联系方式
        ContactInfo contactInfo = ContactInfo.basic("13800138000", "test@example.com");
        
        // 创建不带标签的联系方式
        ContactInfoWithTag contactWithTag = ContactInfoWithTag.ofNoTag(contactInfo, false);
        
        // 验证结果
        assertEquals(contactInfo, contactWithTag.getContactInfo());
        assertNull(contactWithTag.getTag());
        assertFalse(contactWithTag.isDefault());
    }
    
    @Test
    @DisplayName("测试创建默认联系方式")
    void testCreateAsDefault() {
        // 创建联系方式
        ContactInfo contactInfo = ContactInfo.basic("13800138000", "test@example.com");
        
        // 创建默认联系方式
        ContactInfoWithTag contactWithTag = ContactInfoWithTag.asDefault(contactInfo);
        
        // 验证结果
        assertEquals(contactInfo, contactWithTag.getContactInfo());
        assertNull(contactWithTag.getTag());
        assertTrue(contactWithTag.isDefault());
    }
    
    @Test
    @DisplayName("测试设置为默认联系方式")
    void testSetAsDefault() {
        // 创建联系方式
        ContactInfo contactInfo = ContactInfo.basic("13800138000", "test@example.com");
        
        // 创建非默认联系方式
        ContactInfoWithTag contactWithTag = ContactInfoWithTag.of(contactInfo, "个人联系方式", false);
        assertFalse(contactWithTag.isDefault());
        
        // 设置为默认联系方式
        ContactInfoWithTag defaultContact = contactWithTag.setAsDefault();
        
        // 验证结果
        assertTrue(defaultContact.isDefault());
        assertEquals(contactInfo, defaultContact.getContactInfo());
        assertEquals("个人联系方式", defaultContact.getTag());
        
        // 原对象不应改变
        assertFalse(contactWithTag.isDefault());
    }
    
    @Test
    @DisplayName("测试从默认联系方式设置为非默认联系方式")
    void testUnsetAsDefault() {
        // 创建默认联系方式
        ContactInfo contactInfo = ContactInfo.basic("13800138000", "test@example.com");
        ContactInfoWithTag defaultContact = ContactInfoWithTag.of(contactInfo, "个人联系方式", true);
        assertTrue(defaultContact.isDefault());
        
        // 设置为非默认联系方式
        ContactInfoWithTag nonDefaultContact = defaultContact.unsetAsDefault();
        
        // 验证结果
        assertFalse(nonDefaultContact.isDefault());
        assertEquals(contactInfo, nonDefaultContact.getContactInfo());
        assertEquals("个人联系方式", nonDefaultContact.getTag());
        
        // 原对象不应改变
        assertTrue(defaultContact.isDefault());
    }
    
    @Test
    @DisplayName("测试更新联系方式标签")
    void testUpdateTag() {
        // 创建联系方式
        ContactInfo contactInfo = ContactInfo.basic("13800138000", "test@example.com");
        ContactInfoWithTag contactWithTag = ContactInfoWithTag.of(contactInfo, "旧标签", true);
        
        // 更新标签
        ContactInfoWithTag updatedContact = contactWithTag.withTag("新标签");
        
        // 验证结果
        assertEquals("新标签", updatedContact.getTag());
        assertEquals(contactInfo, updatedContact.getContactInfo());
        assertTrue(updatedContact.isDefault());
        
        // 原对象不应改变
        assertEquals("旧标签", contactWithTag.getTag());
    }
    
    @Test
    @DisplayName("测试更新联系方式")
    void testUpdateContactInfo() {
        // 创建原始联系方式
        ContactInfo oldContactInfo = ContactInfo.basic("13800138000", "old@example.com");
        ContactInfoWithTag contactWithTag = ContactInfoWithTag.of(oldContactInfo, "个人联系方式", true);
        
        // 创建新联系方式
        ContactInfo newContactInfo = ContactInfo.basic("13900139000", "new@example.com");
        
        // 更新联系方式
        ContactInfoWithTag updatedContact = contactWithTag.withContactInfo(newContactInfo);
        
        // 验证结果
        assertEquals(newContactInfo, updatedContact.getContactInfo());
        assertEquals("个人联系方式", updatedContact.getTag());
        assertTrue(updatedContact.isDefault());
        
        // 原对象不应改变
        assertEquals(oldContactInfo, contactWithTag.getContactInfo());
    }
    
    @Test
    @DisplayName("测试值对象相等性")
    void testEquality() {
        // 创建两个相同的联系方式但实例不同
        ContactInfo contactInfo1 = ContactInfo.basic("13800138000", "test@example.com");
        ContactInfo contactInfo2 = ContactInfo.basic("13800138000", "test@example.com");
        
        // 创建带相同值的带标签联系方式
        ContactInfoWithTag tag1 = ContactInfoWithTag.of(contactInfo1, "个人联系方式", true);
        ContactInfoWithTag tag2 = ContactInfoWithTag.of(contactInfo2, "个人联系方式", true);
        
        // 验证相等性
        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        // 验证不相等的情况
        ContactInfoWithTag tag3 = ContactInfoWithTag.of(contactInfo1, "工作联系方式", true);
        assertNotEquals(tag1, tag3);
        
        ContactInfoWithTag tag4 = ContactInfoWithTag.of(contactInfo1, "个人联系方式", false);
        assertNotEquals(tag1, tag4);
        
        // 创建不同的联系方式
        ContactInfo contactInfo3 = ContactInfo.basic("13900139000", "test@example.com");
        ContactInfoWithTag tag5 = ContactInfoWithTag.of(contactInfo3, "个人联系方式", true);
        assertNotEquals(tag1, tag5);
    }
} 