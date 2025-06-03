package com.lovemp.domain.person.domain.valueobject;

import com.lovemp.domain.person.domain.model.valueobject.Address;
import com.lovemp.domain.person.domain.model.valueobject.AddressWithTag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("带标签的地址值对象测试")
class AddressWithTagTest {

    @Test
    @DisplayName("测试创建带标签的地址值对象")
    void testCreateWithTag() {
        // 创建地址
        Address address = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        
        // 创建带标签的地址
        AddressWithTag addressWithTag = AddressWithTag.of(address, "家庭地址", true);
        
        // 验证结果
        assertEquals(address, addressWithTag.getAddress());
        assertEquals("家庭地址", addressWithTag.getTag());
        assertTrue(addressWithTag.isDefault());
    }
    
    @Test
    @DisplayName("测试创建不带标签的地址值对象")
    void testCreateWithoutTag() {
        // 创建地址
        Address address = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        
        // 创建不带标签的地址
        AddressWithTag addressWithTag = AddressWithTag.ofNoTag(address, false);
        
        // 验证结果
        assertEquals(address, addressWithTag.getAddress());
        assertNull(addressWithTag.getTag());
        assertFalse(addressWithTag.isDefault());
    }
    
    @Test
    @DisplayName("测试设置为默认地址")
    void testSetAsDefault() {
        // 创建地址
        Address address = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        
        // 创建非默认地址
        AddressWithTag addressWithTag = AddressWithTag.of(address, "家庭地址", false);
        assertFalse(addressWithTag.isDefault());
        
        // 设置为默认地址
        AddressWithTag defaultAddress = addressWithTag.setAsDefault();
        
        // 验证结果
        assertTrue(defaultAddress.isDefault());
        assertEquals(address, defaultAddress.getAddress());
        assertEquals("家庭地址", defaultAddress.getTag());
        
        // 原对象不应改变
        assertFalse(addressWithTag.isDefault());
    }
    
    @Test
    @DisplayName("测试从默认地址设置为非默认地址")
    void testUnsetAsDefault() {
        // 创建默认地址
        Address address = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        AddressWithTag defaultAddress = AddressWithTag.of(address, "家庭地址", true);
        assertTrue(defaultAddress.isDefault());
        
        // 设置为非默认地址
        AddressWithTag nonDefaultAddress = defaultAddress.unsetAsDefault();
        
        // 验证结果
        assertFalse(nonDefaultAddress.isDefault());
        assertEquals(address, nonDefaultAddress.getAddress());
        assertEquals("家庭地址", nonDefaultAddress.getTag());
        
        // 原对象不应改变
        assertTrue(defaultAddress.isDefault());
    }
    
    @Test
    @DisplayName("测试更新地址标签")
    void testUpdateTag() {
        // 创建地址
        Address address = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        AddressWithTag addressWithTag = AddressWithTag.of(address, "旧标签", true);
        
        // 更新标签
        AddressWithTag updatedAddress = addressWithTag.withTag("新标签");
        
        // 验证结果
        assertEquals("新标签", updatedAddress.getTag());
        assertEquals(address, updatedAddress.getAddress());
        assertTrue(updatedAddress.isDefault());
        
        // 原对象不应改变
        assertEquals("旧标签", addressWithTag.getTag());
    }
    
    @Test
    @DisplayName("测试更新地址")
    void testUpdateAddress() {
        // 创建原始地址
        Address oldAddress = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        AddressWithTag addressWithTag = AddressWithTag.of(oldAddress, "家庭地址", true);
        
        // 创建新地址
        Address newAddress = Address.ofChina(
                "上海市", 
                "上海市", 
                "浦东新区", 
                "张江镇", 
                "张江高科技园区", 
                "201203"
        );
        
        // 更新地址
        AddressWithTag updatedAddress = addressWithTag.withAddress(newAddress);
        
        // 验证结果
        assertEquals(newAddress, updatedAddress.getAddress());
        assertEquals("家庭地址", updatedAddress.getTag());
        assertTrue(updatedAddress.isDefault());
        
        // 原对象不应改变
        assertEquals(oldAddress, addressWithTag.getAddress());
    }
    
    @Test
    @DisplayName("测试值对象相等性")
    void testEquality() {
        // 创建两个相同的地址但实例不同
        Address address1 = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        
        Address address2 = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        
        // 创建带相同值的带标签地址
        AddressWithTag tag1 = AddressWithTag.of(address1, "家庭地址", true);
        AddressWithTag tag2 = AddressWithTag.of(address2, "家庭地址", true);
        
        // 验证相等性
        assertEquals(tag1, tag2);
        assertEquals(tag1.hashCode(), tag2.hashCode());
        
        // 验证不相等的情况
        AddressWithTag tag3 = AddressWithTag.of(address1, "工作地址", true);
        assertNotEquals(tag1, tag3);
        
        AddressWithTag tag4 = AddressWithTag.of(address1, "家庭地址", false);
        assertNotEquals(tag1, tag4);
    }
} 