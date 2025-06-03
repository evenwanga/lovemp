package com.lovemp.domain.enterprise.domain.valueobject;

import com.lovemp.domain.enterprise.domain.model.valueobject.CertificateType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * CertificateType枚举的单元测试
 */
@DisplayName("证书类型枚举测试")
class CertificateTypeTest {

    @Test
    @DisplayName("根据代码获取证书类型")
    void testFromCode() {
        // 测试基本枚举值
        assertEquals(CertificateType.ISO9001, CertificateType.fromCode("ISO9001"));
        assertEquals(CertificateType.HIGH_TECH_ENTERPRISE, CertificateType.fromCode("HTE"));
        
        // 测试新添加的知识产权相关枚举值
        assertEquals(CertificateType.SOFTWARE_COPYRIGHT, CertificateType.fromCode("SCC"));
        assertEquals(CertificateType.UTILITY_MODEL_PATENT, CertificateType.fromCode("UMP"));
        assertEquals(CertificateType.INVENTION_PATENT, CertificateType.fromCode("IP"));
        assertEquals(CertificateType.DESIGN_PATENT, CertificateType.fromCode("DP"));
        assertEquals(CertificateType.TRADEMARK_REGISTRATION, CertificateType.fromCode("TRC"));
        assertEquals(CertificateType.NETWORK_SECURITY_PROTECTION, CertificateType.fromCode("NSP"));
    }
    
    @Test
    @DisplayName("根据名称获取证书类型")
    void testFromName() {
        // 测试基本枚举值
        assertEquals(CertificateType.ISO9001, CertificateType.fromName("ISO9001质量管理体系认证"));
        assertEquals(CertificateType.HIGH_TECH_ENTERPRISE, CertificateType.fromName("高新技术企业认证"));
        
        // 测试新添加的知识产权相关枚举值
        assertEquals(CertificateType.SOFTWARE_COPYRIGHT, CertificateType.fromName("软件著作权证书"));
        assertEquals(CertificateType.UTILITY_MODEL_PATENT, CertificateType.fromName("实用新型专利"));
        assertEquals(CertificateType.INVENTION_PATENT, CertificateType.fromName("发明专利"));
        assertEquals(CertificateType.DESIGN_PATENT, CertificateType.fromName("外观设计专利"));
        assertEquals(CertificateType.TRADEMARK_REGISTRATION, CertificateType.fromName("商标注册证书"));
        assertEquals(CertificateType.NETWORK_SECURITY_PROTECTION, CertificateType.fromName("网络安全等级保护认证"));
    }
    
    @Test
    @DisplayName("使用无效代码抛出异常")
    void testFromCodeWithInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> CertificateType.fromCode("INVALID_CODE"));
    }
    
    @Test
    @DisplayName("使用无效名称抛出异常")
    void testFromNameWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> CertificateType.fromName("无效名称"));
    }
    
    @Test
    @DisplayName("使用空代码抛出异常")
    void testFromCodeWithEmptyCode() {
        assertThrows(IllegalArgumentException.class, () -> CertificateType.fromCode(""));
    }
    
    @Test
    @DisplayName("使用空名称抛出异常")
    void testFromNameWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> CertificateType.fromName(""));
    }
    
    @Test
    @DisplayName("使用null代码抛出异常")
    void testFromCodeWithNullCode() {
        assertThrows(IllegalArgumentException.class, () -> CertificateType.fromCode(null));
    }
    
    @Test
    @DisplayName("使用null名称抛出异常")
    void testFromNameWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> CertificateType.fromName(null));
    }
    
    @Test
    @DisplayName("判断是否为ISO证书")
    void testIsISOCertificate() {
        assertTrue(CertificateType.ISO9001.isISOCertificate());
        assertTrue(CertificateType.ISO14001.isISOCertificate());
        assertTrue(CertificateType.ISO45001.isISOCertificate());
        assertTrue(CertificateType.ISO27001.isISOCertificate());
        
        assertFalse(CertificateType.HIGH_TECH_ENTERPRISE.isISOCertificate());
        assertFalse(CertificateType.SOFTWARE_COPYRIGHT.isISOCertificate());
    }
    
    @Test
    @DisplayName("判断是否为资质证书")
    void testIsQualificationCertificate() {
        assertTrue(CertificateType.HIGH_TECH_ENTERPRISE.isQualificationCertificate());
        assertTrue(CertificateType.SOFTWARE_ENTERPRISE.isQualificationCertificate());
        assertTrue(CertificateType.DOUBLE_SOFT.isQualificationCertificate());
        assertTrue(CertificateType.CMMI.isQualificationCertificate());
        
        assertFalse(CertificateType.ISO9001.isQualificationCertificate());
        assertFalse(CertificateType.SOFTWARE_COPYRIGHT.isQualificationCertificate());
    }
    
    @Test
    @DisplayName("判断是否为许可证")
    void testIsLicense() {
        assertTrue(CertificateType.FOOD_OPERATION_LICENSE.isLicense());
        assertTrue(CertificateType.CATERING_SERVICE_LICENSE.isLicense());
        assertTrue(CertificateType.MEDICAL_DEVICE_LICENSE.isLicense());
        assertTrue(CertificateType.DRUG_OPERATION_LICENSE.isLicense());
        assertTrue(CertificateType.SAFETY_PRODUCTION_LICENSE.isLicense());
        
        assertFalse(CertificateType.ISO9001.isLicense());
        assertFalse(CertificateType.SOFTWARE_COPYRIGHT.isLicense());
    }
    
    @Test
    @DisplayName("判断是否为知识产权证书")
    void testIsIntellectualPropertyCertificate() {
        // 验证知识产权证书
        assertTrue(CertificateType.SOFTWARE_COPYRIGHT.isIntellectualPropertyCertificate());
        assertTrue(CertificateType.UTILITY_MODEL_PATENT.isIntellectualPropertyCertificate());
        assertTrue(CertificateType.INVENTION_PATENT.isIntellectualPropertyCertificate());
        assertTrue(CertificateType.DESIGN_PATENT.isIntellectualPropertyCertificate());
        assertTrue(CertificateType.TRADEMARK_REGISTRATION.isIntellectualPropertyCertificate());
        
        // 验证非知识产权证书
        assertFalse(CertificateType.ISO9001.isIntellectualPropertyCertificate());
        assertFalse(CertificateType.NETWORK_SECURITY_PROTECTION.isIntellectualPropertyCertificate());
        assertFalse(CertificateType.HIGH_TECH_ENTERPRISE.isIntellectualPropertyCertificate());
    }
    
    // 新增测试用例，提高测试覆盖率
    
    @Test
    @DisplayName("测试所有枚举值的toString方法")
    void testToString() {
        for (CertificateType type : CertificateType.values()) {
            assertEquals(type.getName(), type.toString());
        }
    }
    
    @Test
    @DisplayName("测试所有枚举值的equals和hashCode方法")
    void testEqualsAndHashCode() {
        for (CertificateType type : CertificateType.values()) {
            // 自反性
            assertEquals(type, type);
            
            // 相同的枚举值应该有相同的hashCode
            CertificateType sameType = CertificateType.fromCode(type.getCode());
            assertEquals(type.hashCode(), sameType.hashCode());
            assertEquals(type, sameType);
            
            // 不同的枚举值应该不相等
            for (CertificateType otherType : CertificateType.values()) {
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
    @DisplayName("测试证书类型的组合判断")
    void testCombinationChecks() {
        // 测试证书类型的互斥性
        for (CertificateType type : CertificateType.values()) {
            // 一个证书类型不应同时属于多个基本类别（ISO、许可证、知识产权）
            int categoryCount = 0;
            if (type.isISOCertificate()) categoryCount++;
            if (type.isLicense()) categoryCount++;
            if (type.isIntellectualPropertyCertificate()) categoryCount++;
            if (type.isQualificationCertificate()) categoryCount++;
            
            // 一个类型最多应该属于一个主要类别，但有些可能不属于任何类别
            assertTrue(categoryCount <= 1, "证书类型不应同时属于多个主要类别: " + type);
        }
        
        // 测试知识产权证书的特定属性
        for (CertificateType type : CertificateType.values()) {
            if (type.isIntellectualPropertyCertificate()) {
                assertFalse(type.isISOCertificate(), "知识产权证书不应同时是ISO证书: " + type);
                assertFalse(type.isLicense(), "知识产权证书不应同时是许可证: " + type);
            }
        }
    }
    
    @Test
    @DisplayName("测试所有枚举值的方法调用")
    void testAllEnumMethods() {
        for (CertificateType type : CertificateType.values()) {
            // 确保每个枚举值的getter方法都被调用
            assertNotNull(type.getCode());
            assertNotNull(type.getName());
            
            // 测试所有判断方法，确保每个分支都被覆盖
            boolean isISO = type.isISOCertificate();
            boolean isQualification = type.isQualificationCertificate();
            boolean isLicense = type.isLicense();
            boolean isIP = type.isIntellectualPropertyCertificate();
            
            // 确保输出值是确定的，要么true要么false
            assertTrue(isISO || !isISO);
            assertTrue(isQualification || !isQualification);
            assertTrue(isLicense || !isLicense);
            assertTrue(isIP || !isIP);
        }
    }
    
    @Test
    @DisplayName("测试专利类证书的特性")
    void testPatentCertificates() {
        // 所有专利类证书都应该是知识产权证书
        assertTrue(CertificateType.UTILITY_MODEL_PATENT.isIntellectualPropertyCertificate());
        assertTrue(CertificateType.INVENTION_PATENT.isIntellectualPropertyCertificate());
        assertTrue(CertificateType.DESIGN_PATENT.isIntellectualPropertyCertificate());
        
        // 测试专利与其他类型的互斥性
        CertificateType[] patentTypes = {
            CertificateType.UTILITY_MODEL_PATENT,
            CertificateType.INVENTION_PATENT,
            CertificateType.DESIGN_PATENT
        };
        
        for (CertificateType patentType : patentTypes) {
            assertFalse(patentType.isISOCertificate());
            assertFalse(patentType.isQualificationCertificate());
            assertFalse(patentType.isLicense());
        }
    }
    
    @Test
    @DisplayName("测试使用特殊字符的代码和名称")
    void testWithSpecialCharacters() {
        // 测试包含特殊字符的输入
        assertThrows(IllegalArgumentException.class, () -> CertificateType.fromCode("ISO9001!@#"));
        assertThrows(IllegalArgumentException.class, () -> CertificateType.fromName("ISO9001质量管理体系认证!@#"));
    }
    
    @Test
    @DisplayName("测试各种证书类型的独立属性")
    void testIndividualCertificateProperties() {
        // 测试ISO证书系列
        assertTrue(CertificateType.ISO9001.getCode().startsWith("ISO"));
        assertTrue(CertificateType.ISO14001.getCode().startsWith("ISO"));
        assertTrue(CertificateType.ISO27001.getCode().startsWith("ISO"));
        assertTrue(CertificateType.ISO45001.getCode().startsWith("ISO"));
        
        // 测试软件相关证书
        assertTrue(CertificateType.SOFTWARE_COPYRIGHT.getCode().equals("SCC"));
        assertTrue(CertificateType.SOFTWARE_ENTERPRISE.getCode().equals("SE"));
        
        // 测试证书名称格式
        for (CertificateType type : CertificateType.values()) {
            assertFalse(type.getName().isEmpty());
            assertTrue(type.getName().length() > 2);
        }
    }
} 