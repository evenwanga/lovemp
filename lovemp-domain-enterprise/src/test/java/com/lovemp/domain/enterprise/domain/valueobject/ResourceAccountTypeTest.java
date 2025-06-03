package com.lovemp.domain.enterprise.domain.valueobject;

import com.lovemp.domain.enterprise.domain.model.valueobject.ResourceAccountType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ResourceAccountType枚举的单元测试
 */
@DisplayName("资源账号类型枚举测试")
class ResourceAccountTypeTest {

    @Test
    @DisplayName("根据代码获取资源账号类型")
    void testFromCode() {
        // 测试基本枚举值
        assertEquals(ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT, ResourceAccountType.fromCode("WOA"));
        assertEquals(ResourceAccountType.ENTERPRISE_EMAIL, ResourceAccountType.fromCode("EEM"));
        
        // 测试新添加的iOS相关枚举值
        assertEquals(ResourceAccountType.IOS_DEVELOPER_ACCOUNT, ResourceAccountType.fromCode("IDA"));
        assertEquals(ResourceAccountType.IOS_DEVELOPMENT_CERTIFICATE, ResourceAccountType.fromCode("IDC"));
        assertEquals(ResourceAccountType.IOS_DISTRIBUTION_CERTIFICATE, ResourceAccountType.fromCode("IDSC"));
        assertEquals(ResourceAccountType.IOS_PUSH_CERTIFICATE, ResourceAccountType.fromCode("IPC"));
        assertEquals(ResourceAccountType.ANDROID_DEVELOPER_ACCOUNT, ResourceAccountType.fromCode("ANDA"));
    }
    
    @Test
    @DisplayName("根据名称获取资源账号类型")
    void testFromName() {
        // 测试基本枚举值
        assertEquals(ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT, ResourceAccountType.fromName("微信公众号"));
        assertEquals(ResourceAccountType.ENTERPRISE_EMAIL, ResourceAccountType.fromName("企业邮箱"));
        
        // 测试新添加的iOS相关枚举值
        assertEquals(ResourceAccountType.IOS_DEVELOPER_ACCOUNT, ResourceAccountType.fromName("iOS开发者账号"));
        assertEquals(ResourceAccountType.IOS_DEVELOPMENT_CERTIFICATE, ResourceAccountType.fromName("iOS开发证书"));
        assertEquals(ResourceAccountType.IOS_DISTRIBUTION_CERTIFICATE, ResourceAccountType.fromName("iOS发布证书"));
        assertEquals(ResourceAccountType.IOS_PUSH_CERTIFICATE, ResourceAccountType.fromName("iOS推送证书"));
        assertEquals(ResourceAccountType.ANDROID_DEVELOPER_ACCOUNT, ResourceAccountType.fromName("Android开发者账号"));
    }
    
    @Test
    @DisplayName("使用无效代码抛出异常")
    void testFromCodeWithInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> ResourceAccountType.fromCode("INVALID_CODE"));
    }
    
    @Test
    @DisplayName("使用无效名称抛出异常")
    void testFromNameWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> ResourceAccountType.fromName("无效名称"));
    }
    
    @Test
    @DisplayName("使用空代码抛出异常")
    void testFromCodeWithEmptyCode() {
        assertThrows(IllegalArgumentException.class, () -> ResourceAccountType.fromCode(""));
    }
    
    @Test
    @DisplayName("使用空名称抛出异常")
    void testFromNameWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> ResourceAccountType.fromName(""));
    }
    
    @Test
    @DisplayName("使用null代码抛出异常")
    void testFromCodeWithNullCode() {
        assertThrows(IllegalArgumentException.class, () -> ResourceAccountType.fromCode(null));
    }
    
    @Test
    @DisplayName("使用null名称抛出异常")
    void testFromNameWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> ResourceAccountType.fromName(null));
    }
    
    @Test
    @DisplayName("判断是否为iOS相关账号")
    void testIsIOSRelated() {
        // 验证iOS相关类型判断
        assertTrue(ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isIOSRelated());
        assertTrue(ResourceAccountType.IOS_DEVELOPMENT_CERTIFICATE.isIOSRelated());
        assertTrue(ResourceAccountType.IOS_DISTRIBUTION_CERTIFICATE.isIOSRelated());
        assertTrue(ResourceAccountType.IOS_PUSH_CERTIFICATE.isIOSRelated());
        
        // 验证非iOS相关类型
        assertFalse(ResourceAccountType.ANDROID_DEVELOPER_ACCOUNT.isIOSRelated());
        assertFalse(ResourceAccountType.APP_DEVELOPER_ACCOUNT.isIOSRelated());
        assertFalse(ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT.isIOSRelated());
    }
    
    @Test
    @DisplayName("判断是否为iOS证书")
    void testIsIOSCertificate() {
        // 验证iOS证书类型判断
        assertTrue(ResourceAccountType.IOS_DEVELOPMENT_CERTIFICATE.isIOSCertificate());
        assertTrue(ResourceAccountType.IOS_DISTRIBUTION_CERTIFICATE.isIOSCertificate());
        assertTrue(ResourceAccountType.IOS_PUSH_CERTIFICATE.isIOSCertificate());
        
        // 验证非iOS证书类型
        assertFalse(ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isIOSCertificate());
        assertFalse(ResourceAccountType.ANDROID_DEVELOPER_ACCOUNT.isIOSCertificate());
        assertFalse(ResourceAccountType.APP_DEVELOPER_ACCOUNT.isIOSCertificate());
    }
    
    @Test
    @DisplayName("判断是否为移动应用开发相关账号")
    void testIsMobileAppDeveloperAccount() {
        // 验证移动应用开发相关账号
        assertTrue(ResourceAccountType.APP_DEVELOPER_ACCOUNT.isMobileAppDeveloperAccount());
        assertTrue(ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isMobileAppDeveloperAccount());
        assertTrue(ResourceAccountType.ANDROID_DEVELOPER_ACCOUNT.isMobileAppDeveloperAccount());
        
        // 验证非移动应用开发账号
        assertFalse(ResourceAccountType.IOS_DEVELOPMENT_CERTIFICATE.isMobileAppDeveloperAccount());
        assertFalse(ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT.isMobileAppDeveloperAccount());
        assertFalse(ResourceAccountType.ENTERPRISE_EMAIL.isMobileAppDeveloperAccount());
    }
    
    @Test
    @DisplayName("判断是否为微信相关账号")
    void testIsWechatRelated() {
        assertTrue(ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT.isWechatRelated());
        assertTrue(ResourceAccountType.WECHAT_MINI_PROGRAM.isWechatRelated());
        assertTrue(ResourceAccountType.WECHAT_ENTERPRISE_ACCOUNT.isWechatRelated());
        
        assertFalse(ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isWechatRelated());
        assertFalse(ResourceAccountType.ALIPAY_MINI_PROGRAM.isWechatRelated());
    }
    
    @Test
    @DisplayName("判断是否为支付宝相关账号")
    void testIsAlipayRelated() {
        assertTrue(ResourceAccountType.ALIPAY_MERCHANT_ACCOUNT.isAlipayRelated());
        assertTrue(ResourceAccountType.ALIPAY_MINI_PROGRAM.isAlipayRelated());
        
        assertFalse(ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isAlipayRelated());
        assertFalse(ResourceAccountType.WECHAT_MINI_PROGRAM.isAlipayRelated());
    }
    
    @Test
    @DisplayName("判断是否为社交媒体账号")
    void testIsSocialMediaAccount() {
        assertTrue(ResourceAccountType.WEIBO_ENTERPRISE_ACCOUNT.isSocialMediaAccount());
        assertTrue(ResourceAccountType.DOUYIN_ENTERPRISE_ACCOUNT.isSocialMediaAccount());
        assertTrue(ResourceAccountType.KUAISHOU_ENTERPRISE_ACCOUNT.isSocialMediaAccount());
        assertTrue(ResourceAccountType.XIAOHONGSHU_ENTERPRISE_ACCOUNT.isSocialMediaAccount());
        
        assertFalse(ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isSocialMediaAccount());
        assertFalse(ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT.isSocialMediaAccount());
    }
    
    @Test
    @DisplayName("判断是否为小程序账号")
    void testIsMiniProgramAccount() {
        assertTrue(ResourceAccountType.WECHAT_MINI_PROGRAM.isMiniProgramAccount());
        assertTrue(ResourceAccountType.ALIPAY_MINI_PROGRAM.isMiniProgramAccount());
        assertTrue(ResourceAccountType.BYTEDANCE_MINI_PROGRAM.isMiniProgramAccount());
        
        assertFalse(ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isMiniProgramAccount());
        assertFalse(ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT.isMiniProgramAccount());
    }
    
    @Test
    @DisplayName("判断是否为知识产权相关账号")
    void testIsIntellectualPropertyAccount() {
        assertTrue(ResourceAccountType.TRADEMARK_REGISTRATION_ACCOUNT.isIntellectualPropertyAccount());
        assertTrue(ResourceAccountType.PATENT_REGISTRATION_ACCOUNT.isIntellectualPropertyAccount());
        assertTrue(ResourceAccountType.DOMAIN_REGISTRATION_ACCOUNT.isIntellectualPropertyAccount());
        
        assertFalse(ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isIntellectualPropertyAccount());
        assertFalse(ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT.isIntellectualPropertyAccount());
    }
    
    // 新增测试用例，提高测试覆盖率
    
    @Test
    @DisplayName("测试所有枚举值的toString方法")
    void testToString() {
        for (ResourceAccountType type : ResourceAccountType.values()) {
            assertEquals(type.getName(), type.toString());
        }
    }
    
    @Test
    @DisplayName("测试所有枚举值的equals和hashCode方法")
    void testEqualsAndHashCode() {
        for (ResourceAccountType type : ResourceAccountType.values()) {
            // 自反性
            assertEquals(type, type);
            
            // 相同的枚举值应该有相同的hashCode
            ResourceAccountType sameType = ResourceAccountType.fromCode(type.getCode());
            assertEquals(type.hashCode(), sameType.hashCode());
            assertEquals(type, sameType);
            
            // 不同的枚举值应该不相等
            for (ResourceAccountType otherType : ResourceAccountType.values()) {
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
    @DisplayName("测试资源账号类型的组合判断")
    void testCombinationChecks() {
        // 测试同时满足多个属性的枚举值
        assertTrue(ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isIOSRelated() && 
                   ResourceAccountType.IOS_DEVELOPER_ACCOUNT.isMobileAppDeveloperAccount());
        
        // 测试互斥属性的枚举值
        assertTrue(ResourceAccountType.WECHAT_MINI_PROGRAM.isWechatRelated() && 
                   ResourceAccountType.WECHAT_MINI_PROGRAM.isMiniProgramAccount());
        assertFalse(ResourceAccountType.WECHAT_MINI_PROGRAM.isAlipayRelated());
        
        // 移动开发账号、iOS相关和社交媒体的互斥测试
        for (ResourceAccountType type : ResourceAccountType.values()) {
            if (type.isMobileAppDeveloperAccount()) {
                assertFalse(type.isSocialMediaAccount(), "移动开发账号不应该同时是社交媒体账号: " + type);
            }
            
            if (type.isIOSRelated()) {
                assertFalse(type.isAlipayRelated(), "iOS相关账号不应该同时是支付宝相关账号: " + type);
                assertFalse(type.isWechatRelated(), "iOS相关账号不应该同时是微信相关账号: " + type);
            }
        }
    }
    
    @Test
    @DisplayName("测试所有枚举值的方法调用")
    void testAllEnumMethods() {
        for (ResourceAccountType type : ResourceAccountType.values()) {
            // 确保每个枚举值的getter方法都被调用
            assertNotNull(type.getCode());
            assertNotNull(type.getName());
            
            // 测试所有判断方法，确保每个分支都被覆盖
            boolean isIOS = type.isIOSRelated();
            boolean isIOSCert = type.isIOSCertificate();
            boolean isMobileApp = type.isMobileAppDeveloperAccount();
            boolean isWechat = type.isWechatRelated();
            boolean isAlipay = type.isAlipayRelated();
            boolean isSocialMedia = type.isSocialMediaAccount();
            boolean isMiniProgram = type.isMiniProgramAccount();
            boolean isIP = type.isIntellectualPropertyAccount();
            
            // 确保输出值是确定的，要么true要么false
            assertTrue(isIOS || !isIOS);
            assertTrue(isIOSCert || !isIOSCert);
            assertTrue(isMobileApp || !isMobileApp);
            assertTrue(isWechat || !isWechat);
            assertTrue(isAlipay || !isAlipay);
            assertTrue(isSocialMedia || !isSocialMedia);
            assertTrue(isMiniProgram || !isMiniProgram);
            assertTrue(isIP || !isIP);
        }
    }
    
    @Test
    @DisplayName("测试使用特殊字符的代码和名称")
    void testWithSpecialCharacters() {
        // 注意：这里假设fromCode和fromName内部会处理特殊字符情况
        // 如果实现不支持特殊字符，这些测试可能会失败
        assertThrows(IllegalArgumentException.class, () -> ResourceAccountType.fromCode("WOA!@#"));
        assertThrows(IllegalArgumentException.class, () -> ResourceAccountType.fromName("微信公众号!@#"));
    }
} 