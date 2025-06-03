package com.lovemp.domain.person.domain.valueobject;

import com.lovemp.domain.person.domain.model.valueobject.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("地址值对象测试")
class AddressTest {

    @Test
    @DisplayName("测试创建中国地址")
    void testCreateChineseAddress() {
        // 创建中国地址
        Address address = Address.ofChina(
                "北京市",
                "北京市",
                "海淀区",
                "清华园街道",
                "清华园路1号",
                "100084"
        );
        
        // 验证结果
        assertEquals("中国", address.getCountry());
        assertEquals("北京市", address.getProvince());
        assertEquals("北京市", address.getCity());
        assertEquals("海淀区", address.getDistrict());
        assertEquals("清华园街道", address.getStreet());
        assertEquals("清华园路1号", address.getDetail());
        assertEquals("100084", address.getPostalCode());
    }
    
    @Test
    @DisplayName("测试创建国际地址")
    void testCreateInternationalAddress() {
        // 创建国际地址
        Address address = Address.of(
                "美国",
                "加利福尼亚州",
                "旧金山",
                "米申区",
                "市场街",
                "1355号",
                "94103"
        );
        
        // 验证结果
        assertEquals("美国", address.getCountry());
        assertEquals("加利福尼亚州", address.getProvince());
        assertEquals("旧金山", address.getCity());
        assertEquals("米申区", address.getDistrict());
        assertEquals("市场街", address.getStreet());
        assertEquals("1355号", address.getDetail());
        assertEquals("94103", address.getPostalCode());
    }
    
    @Test
    @DisplayName("测试创建不同的地址实例")
    void testDifferentInstances() {
        // 创建两个内容不同的地址
        Address homeAddress = Address.ofChina(
                "北京市",
                "北京市",
                "海淀区",
                "清华园街道",
                "清华园路1号",
                "100084"
        );
        
        Address workAddress = Address.ofChina(
                "北京市",
                "北京市",
                "朝阳区",
                "朝阳门街道",
                "朝阳门外大街甲6号",
                "100020"
        );
        
        // 验证不同内容的地址是不同的实例
        assertNotEquals(homeAddress, workAddress);
        
        // 验证内容相同的地址对象是相等的（值对象特性）
        Address homeAddressCopy = Address.ofChina(
                "北京市",
                "北京市",
                "海淀区",
                "清华园街道",
                "清华园路1号",
                "100084"
        );
        
        assertEquals(homeAddress, homeAddressCopy);
    }
    
    @Test
    @DisplayName("测试格式化地址")
    void testFormatAddress() {
        // 创建中国地址
        Address chinaAddress = Address.ofChina(
                "北京市",
                "北京市",
                "海淀区",
                "清华园街道",
                "清华园路1号",
                "100084"
        );
        
        // 获取格式化地址
        String formatted = chinaAddress.getFormattedAddress();
        
        // 验证包含所有必要的信息
        assertTrue(formatted.contains("中国"));
        assertTrue(formatted.contains("北京市"));
        assertTrue(formatted.contains("海淀区"));
        assertTrue(formatted.contains("清华园街道"));
        assertTrue(formatted.contains("清华园路1号"));
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
        
        // 验证相等性
        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
        
        // 创建不同的地址
        Address address3 = Address.ofChina(
                "北京市",
                "北京市",
                "朝阳区", // 不同的区
                "朝阳门街道",
                "朝阳门外大街甲6号",
                "100020"
        );
        
        assertNotEquals(address1, address3);
    }
} 