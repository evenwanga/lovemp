package com.lovemp.domain.enterprise.domain.valueobject;

import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.domain.enterprise.domain.model.valueobject.Address;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("地址值对象测试")
class AddressTest {

    @Test
    @DisplayName("创建完整地址")
    void testCreateAddressWithAllFields() {
        Address address = Address.of(
                "广东省",
                "深圳市",
                "南山区",
                "科技园路",
                "1号楼A座101",
                "518000",
                114.345,
                22.765
        );
        
        assertEquals("广东省", address.getProvince());
        assertEquals("深圳市", address.getCity());
        assertEquals("南山区", address.getDistrict());
        assertEquals("科技园路", address.getStreet());
        assertEquals("1号楼A座101", address.getDetail());
        assertEquals("518000", address.getZipCode());
        assertEquals(114.345, address.getLongitude());
        assertEquals(22.765, address.getLatitude());
        assertTrue(address.hasCoordinates());
    }
    
    @Test
    @DisplayName("创建不带经纬度的地址")
    void testCreateAddressWithoutCoordinates() {
        Address address = Address.of(
                "广东省",
                "深圳市",
                "南山区",
                "科技园路",
                "1号楼A座101",
                "518000"
        );
        
        assertEquals("广东省", address.getProvince());
        assertEquals("深圳市", address.getCity());
        assertEquals("南山区", address.getDistrict());
        assertEquals("科技园路", address.getStreet());
        assertEquals("1号楼A座101", address.getDetail());
        assertEquals("518000", address.getZipCode());
        assertNull(address.getLongitude());
        assertNull(address.getLatitude());
        assertFalse(address.hasCoordinates());
    }
    
    @Test
    @DisplayName("创建地址后添加经纬度")
    void testAddCoordinatesToAddress() {
        Address address = Address.of(
                "广东省",
                "深圳市",
                "南山区",
                "科技园路",
                "1号楼A座101",
                "518000"
        );
        
        assertFalse(address.hasCoordinates());
        
        Address updatedAddress = address.withCoordinates(114.345, 22.765);
        
        // 原地址不变
        assertNull(address.getLongitude());
        assertNull(address.getLatitude());
        assertFalse(address.hasCoordinates());
        
        // 新地址有经纬度
        assertEquals(114.345, updatedAddress.getLongitude());
        assertEquals(22.765, updatedAddress.getLatitude());
        assertTrue(updatedAddress.hasCoordinates());
        
        // 其他属性不变
        assertEquals(address.getProvince(), updatedAddress.getProvince());
        assertEquals(address.getCity(), updatedAddress.getCity());
        assertEquals(address.getDetail(), updatedAddress.getDetail());
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("省份为空校验")
    void testCreateAddressWithEmptyProvince(String province) {
        assertThrows(DomainRuleViolationException.class, () -> 
            Address.of(
                province,
                "深圳市",
                "南山区",
                "科技园路",
                "1号楼A座101",
                "518000"
            )
        );
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("城市为空校验")
    void testCreateAddressWithEmptyCity(String city) {
        assertThrows(DomainRuleViolationException.class, () -> 
            Address.of(
                "广东省",
                city,
                "南山区",
                "科技园路",
                "1号楼A座101",
                "518000"
            )
        );
    }
    
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" "})
    @DisplayName("详细地址为空校验")
    void testCreateAddressWithEmptyDetail(String detail) {
        assertThrows(DomainRuleViolationException.class, () -> 
            Address.of(
                "广东省",
                "深圳市",
                "南山区",
                "科技园路",
                detail,
                "518000"
            )
        );
    }
    
    @Test
    @DisplayName("获取完整地址字符串")
    void testGetFullAddress() {
        Address address1 = Address.of(
                "广东省",
                "深圳市",
                "南山区",
                "科技园路",
                "1号楼A座101",
                "518000"
        );
        
        String fullAddress1 = address1.getFullAddress();
        assertEquals("广东省深圳市南山区科技园路1号楼A座101", fullAddress1);
        
        // 测试缺少某些字段的情况
        Address address2 = Address.of(
                "广东省",
                "深圳市",
                null,
                null,
                "1号楼A座101",
                "518000"
        );
        
        String fullAddress2 = address2.getFullAddress();
        assertEquals("广东省深圳市1号楼A座101", fullAddress2);
    }
    
    @Test
    @DisplayName("测试地址相等")
    void testAddressEquals() {
        Address address1 = Address.of(
                "广东省",
                "深圳市",
                "南山区",
                "科技园路",
                "1号楼A座101",
                "518000",
                114.345,
                22.765
        );
        
        Address address2 = Address.of(
                "广东省",
                "深圳市",
                "南山区",
                "科技园路",
                "1号楼A座101",
                "518000",
                114.345,
                22.765
        );
        
        Address address3 = Address.of(
                "广东省",
                "广州市",
                "天河区",
                "天河路",
                "2号楼B座202",
                "510000"
        );
        
        // 相同属性的地址应该相等
        assertEquals(address1, address2);
        assertEquals(address1.hashCode(), address2.hashCode());
        
        // 不同的地址不相等
        assertNotEquals(address1, address3);
        
        // 与null和其他类型不相等
        assertNotEquals(address1, null);
        assertNotEquals(address1, "不是地址");
    }
    
    @Test
    @DisplayName("测试地址字符串表示")
    void testAddressToString() {
        Address address = Address.of(
                "广东省",
                "深圳市",
                "南山区",
                "科技园路",
                "1号楼A座101",
                "518000",
                114.345,
                22.765
        );
        
        String addressString = address.toString();
        assertTrue(addressString.contains("广东省"));
        assertTrue(addressString.contains("深圳市"));
        assertTrue(addressString.contains("南山区"));
        assertTrue(addressString.contains("科技园路"));
        assertTrue(addressString.contains("1号楼A座101"));
        assertTrue(addressString.contains("518000"));
        assertTrue(addressString.contains("114.345"));
        assertTrue(addressString.contains("22.765"));
    }
} 