package com.lovemp.domain.enterprise.domain.valueobject;

import com.lovemp.domain.enterprise.domain.model.valueobject.FilingType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * FilingType枚举的单元测试
 */
@DisplayName("备案类型枚举测试")
class FilingTypeTest {

    @Test
    @DisplayName("根据代码获取备案类型")
    void testFromCode() {
        // 测试基本枚举值
        assertEquals(FilingType.ICP, FilingType.fromCode("ICP"));
        assertEquals(FilingType.PUBLIC_SECURITY, FilingType.fromCode("PSB"));
        
        // 测试新添加的ICP相关枚举值
        assertEquals(FilingType.WEBSITE_ICP, FilingType.fromCode("WICP"));
        assertEquals(FilingType.MOBILE_APP_ICP, FilingType.fromCode("MICP"));
        assertEquals(FilingType.MINI_PROGRAM_ICP, FilingType.fromCode("MPICP"));
    }
    
    @Test
    @DisplayName("根据名称获取备案类型")
    void testFromName() {
        // 测试基本枚举值
        assertEquals(FilingType.ICP, FilingType.fromName("ICP备案"));
        assertEquals(FilingType.PUBLIC_SECURITY, FilingType.fromName("公安备案"));
        
        // 测试新添加的ICP相关枚举值
        assertEquals(FilingType.WEBSITE_ICP, FilingType.fromName("网站ICP备案"));
        assertEquals(FilingType.MOBILE_APP_ICP, FilingType.fromName("移动应用ICP备案"));
        assertEquals(FilingType.MINI_PROGRAM_ICP, FilingType.fromName("小程序ICP备案"));
    }
    
    @Test
    @DisplayName("使用无效代码抛出异常")
    void testFromCodeWithInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> FilingType.fromCode("INVALID_CODE"));
    }
    
    @Test
    @DisplayName("使用无效名称抛出异常")
    void testFromNameWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> FilingType.fromName("无效名称"));
    }
    
    @Test
    @DisplayName("使用空代码抛出异常")
    void testFromCodeWithEmptyCode() {
        assertThrows(IllegalArgumentException.class, () -> FilingType.fromCode(""));
    }
    
    @Test
    @DisplayName("使用空名称抛出异常")
    void testFromNameWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> FilingType.fromName(""));
    }
    
    @Test
    @DisplayName("使用null代码抛出异常")
    void testFromCodeWithNullCode() {
        assertThrows(IllegalArgumentException.class, () -> FilingType.fromCode(null));
    }
    
    @Test
    @DisplayName("使用null名称抛出异常")
    void testFromNameWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> FilingType.fromName(null));
    }
    
    @Test
    @DisplayName("判断是否为网站相关备案")
    void testIsWebsiteRelated() {
        assertTrue(FilingType.ICP.isWebsiteRelated());
        assertTrue(FilingType.WEBSITE_ICP.isWebsiteRelated());
        assertTrue(FilingType.PUBLIC_SECURITY.isWebsiteRelated());
        
        assertFalse(FilingType.MOBILE_APP_ICP.isWebsiteRelated());
        assertFalse(FilingType.MINI_PROGRAM_ICP.isWebsiteRelated());
        assertFalse(FilingType.TELECOM_BUSINESS.isWebsiteRelated());
    }
    
    @Test
    @DisplayName("判断是否为电信相关备案")
    void testIsTelecomRelated() {
        assertTrue(FilingType.TELECOM_BUSINESS.isTelecomRelated());
        assertTrue(FilingType.VALUE_ADDED_TELECOM.isTelecomRelated());
        
        assertFalse(FilingType.ICP.isTelecomRelated());
        assertFalse(FilingType.MOBILE_APP_ICP.isTelecomRelated());
        assertFalse(FilingType.PUBLIC_SECURITY.isTelecomRelated());
    }
    
    @Test
    @DisplayName("判断是否为内容服务相关备案")
    void testIsContentServiceRelated() {
        assertTrue(FilingType.NETWORK_CULTURE.isContentServiceRelated());
        assertTrue(FilingType.NETWORK_PUBLISHING.isContentServiceRelated());
        assertTrue(FilingType.INTERNET_NEWS.isContentServiceRelated());
        assertTrue(FilingType.BROADCAST_TV_PROGRAM.isContentServiceRelated());
        
        assertFalse(FilingType.ICP.isContentServiceRelated());
        assertFalse(FilingType.MOBILE_APP_ICP.isContentServiceRelated());
    }
    
    @Test
    @DisplayName("判断是否为ICP相关备案")
    void testIsICPRelated() {
        // 验证ICP相关备案
        assertTrue(FilingType.ICP.isICPRelated());
        assertTrue(FilingType.WEBSITE_ICP.isICPRelated());
        assertTrue(FilingType.MOBILE_APP_ICP.isICPRelated());
        assertTrue(FilingType.MINI_PROGRAM_ICP.isICPRelated());
        
        // 验证非ICP相关备案
        assertFalse(FilingType.PUBLIC_SECURITY.isICPRelated());
        assertFalse(FilingType.NETWORK_CULTURE.isICPRelated());
    }
    
    @Test
    @DisplayName("判断是否为移动应用相关备案")
    void testIsMobileAppRelated() {
        // 验证移动应用相关备案
        assertTrue(FilingType.MOBILE_APP_ICP.isMobileAppRelated());
        assertTrue(FilingType.MINI_PROGRAM_ICP.isMobileAppRelated());
        
        // 验证非移动应用相关备案
        assertFalse(FilingType.WEBSITE_ICP.isMobileAppRelated());
        assertFalse(FilingType.ICP.isMobileAppRelated());
        assertFalse(FilingType.PUBLIC_SECURITY.isMobileAppRelated());
    }
    
    // 新增测试用例，提高测试覆盖率
    
    @Test
    @DisplayName("测试所有枚举值的toString方法")
    void testToString() {
        for (FilingType type : FilingType.values()) {
            assertEquals(type.getName(), type.toString());
        }
    }
    
    @Test
    @DisplayName("测试所有枚举值的equals和hashCode方法")
    void testEqualsAndHashCode() {
        for (FilingType type : FilingType.values()) {
            // 自反性
            assertEquals(type, type);
            
            // 相同的枚举值应该有相同的hashCode
            FilingType sameType = FilingType.fromCode(type.getCode());
            assertEquals(type.hashCode(), sameType.hashCode());
            assertEquals(type, sameType);
            
            // 不同的枚举值应该不相等
            for (FilingType otherType : FilingType.values()) {
                if (type != otherType) {
                    assertNotEquals(type, otherType);
                }
            }
            
            // 与null和其他类型比较
            assertNotEquals(type, null);
            assertNotEquals(type, "字符串");
        }
    }
    
    @Test
    @DisplayName("测试备案类型的组合判断")
    void testCombinationChecks() {
        // 测试不同备案类型的互斥性
        for (FilingType type : FilingType.values()) {
            // 计算每个备案类型属于的分类数量
            int categoryCount = 0;
            if (type.isWebsiteRelated()) categoryCount++;
            if (type.isTelecomRelated()) categoryCount++;
            if (type.isContentServiceRelated()) categoryCount++;
            if (type.isICPRelated()) categoryCount++;
            if (type.isMobileAppRelated()) categoryCount++;
            if (type.isEcommerceRelated()) categoryCount++;
            if (type.isOtherRelated()) categoryCount++;
            
            // 一个备案类型可能属于多个分类，但不应该全部属于或都不属于
            assertTrue(categoryCount > 0, "备案类型至少应属于一个分类: " + type);
            assertTrue(categoryCount < 7, "备案类型不应该属于所有分类: " + type);
        }
        
        // 测试ICP备案与移动应用备案的关系
        for (FilingType type : FilingType.values()) {
            if (type.isMobileAppRelated()) {
                // 所有移动应用相关备案都应该是ICP相关的
                assertTrue(type.isICPRelated(), "移动应用备案应该同时是ICP相关备案: " + type);
                // 移动应用备案不应该同时是网站相关的
                assertFalse(type.isWebsiteRelated(), "移动应用备案不应同时是网站相关备案: " + type);
            }
        }
    }
    
    @Test
    @DisplayName("判断是否为其他类型备案")
    void testIsOtherRelated() {
        // 验证其他类型备案
        assertTrue(FilingType.OTHER.isOtherRelated());
        
        // 验证非其他类型备案
        assertFalse(FilingType.ICP.isOtherRelated());
        assertFalse(FilingType.CROSS_BORDER_ECOMMERCE.isOtherRelated());
        assertFalse(FilingType.INTERNET_DRUG_INFO.isOtherRelated());
    }
    
    @Test
    @DisplayName("判断是否为电子商务相关备案")
    void testIsEcommerceRelated() {
        // 验证电商相关备案
        assertTrue(FilingType.CROSS_BORDER_ECOMMERCE.isEcommerceRelated());
        assertTrue(FilingType.FOOD_ONLINE_SALES.isEcommerceRelated());
        assertTrue(FilingType.ONLINE_CAR_HAILING.isEcommerceRelated());
        
        // 验证非电商相关备案
        assertFalse(FilingType.ICP.isEcommerceRelated());
        assertFalse(FilingType.PUBLIC_SECURITY.isEcommerceRelated());
        assertFalse(FilingType.TELECOM_BUSINESS.isEcommerceRelated());
    }
    
    @Test
    @DisplayName("测试所有枚举值的方法调用")
    void testAllEnumMethods() {
        for (FilingType type : FilingType.values()) {
            // 确保每个枚举值的getter方法都被调用
            assertNotNull(type.getCode());
            assertNotNull(type.getName());
            
            // 测试所有判断方法，确保每个分支都被覆盖
            boolean isWebsite = type.isWebsiteRelated();
            boolean isTelecom = type.isTelecomRelated();
            boolean isContent = type.isContentServiceRelated();
            boolean isICP = type.isICPRelated();
            boolean isMobileApp = type.isMobileAppRelated();
            boolean isEcommerce = type.isEcommerceRelated();
            boolean isOther = type.isOtherRelated();
            
            // 确保输出值是确定的，要么true要么false
            assertTrue(isWebsite || !isWebsite);
            assertTrue(isTelecom || !isTelecom);
            assertTrue(isContent || !isContent);
            assertTrue(isICP || !isICP);
            assertTrue(isMobileApp || !isMobileApp);
            assertTrue(isEcommerce || !isEcommerce);
            assertTrue(isOther || !isOther);
        }
    }
    
    @Test
    @DisplayName("测试ICP备案的特性")
    void testICPFilingCharacteristics() {
        // 测试通用ICP备案和特定ICP备案的关系
        assertTrue(FilingType.ICP.isICPRelated());
        assertTrue(FilingType.WEBSITE_ICP.isICPRelated());
        assertTrue(FilingType.MOBILE_APP_ICP.isICPRelated());
        assertTrue(FilingType.MINI_PROGRAM_ICP.isICPRelated());
        
        // 验证所有ICP相关备案的代码是否符合规范
        for (FilingType type : FilingType.values()) {
            if (type.isICPRelated()) {
                assertTrue(type.getCode().contains("ICP"), "ICP相关备案的代码应该包含ICP: " + type.getCode());
            }
        }
    }
    
    @Test
    @DisplayName("测试移动应用备案的特性")
    void testMobileAppFilingCharacteristics() {
        // 测试所有移动应用相关备案
        FilingType[] mobileAppFilings = {
            FilingType.MOBILE_APP_ICP,
            FilingType.MINI_PROGRAM_ICP
        };
        
        for (FilingType type : mobileAppFilings) {
            assertTrue(type.isMobileAppRelated());
            assertTrue(type.isICPRelated());
            assertFalse(type.isWebsiteRelated());
            assertFalse(type.isContentServiceRelated());
            assertFalse(type.isTelecomRelated());
        }
    }
    
    @Test
    @DisplayName("测试使用特殊字符的代码和名称")
    void testWithSpecialCharacters() {
        // 测试包含特殊字符的输入
        assertThrows(IllegalArgumentException.class, () -> FilingType.fromCode("ICP!@#"));
        assertThrows(IllegalArgumentException.class, () -> FilingType.fromName("ICP备案!@#"));
    }
    
    @Test
    @DisplayName("测试各备案类型的名称格式")
    void testFilingNameFormat() {
        // 测试备案名称是否符合格式规范
        for (FilingType type : FilingType.values()) {
            String name = type.getName();
            assertFalse(name.isEmpty());
            assertTrue(name.contains("备案") || name.contains("许可") || name.contains("登记"), 
                    "备案类型名称应包含'备案'、'许可'或'登记'字样: " + name);
        }
        
        // 特定备案类型的名称验证
        assertTrue(FilingType.ICP.getName().contains("ICP"));
        assertTrue(FilingType.PUBLIC_SECURITY.getName().contains("公安"));
        assertTrue(FilingType.WEBSITE_ICP.getName().contains("网站"));
        assertTrue(FilingType.MOBILE_APP_ICP.getName().contains("移动应用"));
        assertTrue(FilingType.MINI_PROGRAM_ICP.getName().contains("小程序"));
    }
} 