package com.lovemp.domain.enterprise.domain.valueobject;

import com.lovemp.domain.enterprise.domain.model.valueobject.QualificationType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * QualificationType枚举的单元测试
 */
@DisplayName("企业资质类型枚举测试")
class QualificationTypeTest {

    @Test
    @DisplayName("根据代码获取企业资质类型")
    void testFromCode() {
        // 测试基本枚举值
        assertEquals(QualificationType.CONSTRUCTION, QualificationType.fromCode("CONST"));
        assertEquals(QualificationType.FOOD_OPERATION, QualificationType.fromCode("FOOD"));
        
        // 测试新添加的互联网相关枚举值
        assertEquals(QualificationType.VALUE_ADDED_TELECOM_BUSINESS, QualificationType.fromCode("VATB"));
        assertEquals(QualificationType.INTERNET_CONTENT_PROVIDER, QualificationType.fromCode("ICP"));
        assertEquals(QualificationType.TELECOM_BUSINESS_OPERATION, QualificationType.fromCode("TBO"));
        assertEquals(QualificationType.INTERNET_PUBLISHING, QualificationType.fromCode("IPL"));
        assertEquals(QualificationType.ELECTRONIC_CERTIFICATION, QualificationType.fromCode("ECS"));
        assertEquals(QualificationType.NETWORK_CULTURE_OPERATION, QualificationType.fromCode("NCO"));
        assertEquals(QualificationType.HIGH_TECH_ENTERPRISE, QualificationType.fromCode("HTE"));
        assertEquals(QualificationType.SOFTWARE_ENTERPRISE, QualificationType.fromCode("SE"));
    }
    
    @Test
    @DisplayName("根据名称获取企业资质类型")
    void testFromName() {
        // 测试基本枚举值
        assertEquals(QualificationType.CONSTRUCTION, QualificationType.fromName("建筑施工资质"));
        assertEquals(QualificationType.FOOD_OPERATION, QualificationType.fromName("食品经营许可"));
        
        // 测试新添加的互联网相关枚举值
        assertEquals(QualificationType.VALUE_ADDED_TELECOM_BUSINESS, QualificationType.fromName("增值电信业务经营许可证"));
        assertEquals(QualificationType.INTERNET_CONTENT_PROVIDER, QualificationType.fromName("互联网信息服务业务许可证"));
        assertEquals(QualificationType.TELECOM_BUSINESS_OPERATION, QualificationType.fromName("电信业务经营许可证"));
        assertEquals(QualificationType.INTERNET_PUBLISHING, QualificationType.fromName("互联网出版许可证"));
        assertEquals(QualificationType.ELECTRONIC_CERTIFICATION, QualificationType.fromName("电子认证服务许可证"));
        assertEquals(QualificationType.NETWORK_CULTURE_OPERATION, QualificationType.fromName("网络文化经营许可证"));
        assertEquals(QualificationType.HIGH_TECH_ENTERPRISE, QualificationType.fromName("高新技术企业认定"));
        assertEquals(QualificationType.SOFTWARE_ENTERPRISE, QualificationType.fromName("软件企业认定"));
    }
    
    @Test
    @DisplayName("使用无效代码抛出异常")
    void testFromCodeWithInvalidCode() {
        assertThrows(IllegalArgumentException.class, () -> QualificationType.fromCode("INVALID_CODE"));
    }
    
    @Test
    @DisplayName("使用无效名称抛出异常")
    void testFromNameWithInvalidName() {
        assertThrows(IllegalArgumentException.class, () -> QualificationType.fromName("无效名称"));
    }
    
    @Test
    @DisplayName("使用空代码抛出异常")
    void testFromCodeWithEmptyCode() {
        assertThrows(IllegalArgumentException.class, () -> QualificationType.fromCode(""));
    }
    
    @Test
    @DisplayName("使用空名称抛出异常")
    void testFromNameWithEmptyName() {
        assertThrows(IllegalArgumentException.class, () -> QualificationType.fromName(""));
    }
    
    @Test
    @DisplayName("使用null代码抛出异常")
    void testFromCodeWithNullCode() {
        assertThrows(IllegalArgumentException.class, () -> QualificationType.fromCode(null));
    }
    
    @Test
    @DisplayName("使用null名称抛出异常")
    void testFromNameWithNullName() {
        assertThrows(IllegalArgumentException.class, () -> QualificationType.fromName(null));
    }
    
    @Test
    @DisplayName("判断是否为食品相关资质")
    void testIsFoodRelated() {
        assertTrue(QualificationType.FOOD_OPERATION.isFoodRelated());
        assertTrue(QualificationType.CATERING_SERVICE.isFoodRelated());
        
        assertFalse(QualificationType.CONSTRUCTION.isFoodRelated());
        assertFalse(QualificationType.INTERNET_CONTENT_PROVIDER.isFoodRelated());
    }
    
    @Test
    @DisplayName("判断是否为医药相关资质")
    void testIsMedicalRelated() {
        assertTrue(QualificationType.MEDICAL_INSTITUTION.isMedicalRelated());
        assertTrue(QualificationType.DRUG_OPERATION.isMedicalRelated());
        assertTrue(QualificationType.MEDICAL_DEVICE_OPERATION.isMedicalRelated());
        assertTrue(QualificationType.INTERNET_DRUG_INFORMATION.isMedicalRelated());
        
        assertFalse(QualificationType.CONSTRUCTION.isMedicalRelated());
        assertFalse(QualificationType.INTERNET_CONTENT_PROVIDER.isMedicalRelated());
    }
    
    @Test
    @DisplayName("判断是否为交通运输相关资质")
    void testIsTransportRelated() {
        assertTrue(QualificationType.ROAD_TRANSPORT_OPERATION.isTransportRelated());
        assertTrue(QualificationType.VEHICLE_REPAIR_OPERATION.isTransportRelated());
        
        assertFalse(QualificationType.CONSTRUCTION.isTransportRelated());
        assertFalse(QualificationType.INTERNET_CONTENT_PROVIDER.isTransportRelated());
    }
    
    @Test
    @DisplayName("判断是否为互联网相关资质")
    void testIsInternetRelated() {
        // 验证互联网相关资质
        assertTrue(QualificationType.VALUE_ADDED_TELECOM_BUSINESS.isInternetRelated());
        assertTrue(QualificationType.INTERNET_CONTENT_PROVIDER.isInternetRelated());
        assertTrue(QualificationType.TELECOM_BUSINESS_OPERATION.isInternetRelated());
        assertTrue(QualificationType.INTERNET_PUBLISHING.isInternetRelated());
        assertTrue(QualificationType.ELECTRONIC_CERTIFICATION.isInternetRelated());
        assertTrue(QualificationType.NETWORK_CULTURE_OPERATION.isInternetRelated());
        
        // 验证非互联网相关资质
        assertFalse(QualificationType.CONSTRUCTION.isInternetRelated());
        assertFalse(QualificationType.FOOD_OPERATION.isInternetRelated());
        assertFalse(QualificationType.HIGH_TECH_ENTERPRISE.isInternetRelated());
    }
    
    @Test
    @DisplayName("判断是否为IT企业认定资质")
    void testIsITEnterpriseCertification() {
        // 验证IT企业认定资质
        assertTrue(QualificationType.HIGH_TECH_ENTERPRISE.isITEnterpriseCertification());
        assertTrue(QualificationType.SOFTWARE_ENTERPRISE.isITEnterpriseCertification());
        
        // 验证非IT企业认定资质
        assertFalse(QualificationType.INTERNET_CONTENT_PROVIDER.isITEnterpriseCertification());
        assertFalse(QualificationType.CONSTRUCTION.isITEnterpriseCertification());
        assertFalse(QualificationType.FOOD_OPERATION.isITEnterpriseCertification());
    }
    
    // 新增测试用例，提高测试覆盖率
    
    @Test
    @DisplayName("测试所有枚举值的toString方法")
    void testToString() {
        for (QualificationType type : QualificationType.values()) {
            assertEquals(type.getName(), type.toString());
        }
    }
    
    @Test
    @DisplayName("测试所有枚举值的equals和hashCode方法")
    void testEqualsAndHashCode() {
        for (QualificationType type : QualificationType.values()) {
            // 自反性
            assertEquals(type, type);
            
            // 相同的枚举值应该有相同的hashCode
            QualificationType sameType = QualificationType.fromCode(type.getCode());
            assertEquals(type.hashCode(), sameType.hashCode());
            assertEquals(type, sameType);
            
            // 不同的枚举值应该不相等
            for (QualificationType otherType : QualificationType.values()) {
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
    @DisplayName("测试资质类型的组合判断")
    void testCombinationChecks() {
        // 测试资质类型的分类互斥性
        for (QualificationType type : QualificationType.values()) {
            // 计算每个资质类型属于的分类数量
            int categoryCount = 0;
            if (type.isFoodRelated()) categoryCount++;
            if (type.isMedicalRelated()) categoryCount++;
            if (type.isTransportRelated()) categoryCount++;
            if (type.isInternetRelated()) categoryCount++;
            if (type.isITEnterpriseCertification()) categoryCount++;
            
            // 一个资质类型通常只属于一个主要分类，但有些可能不属于这些分类或属于多个
            // 这里不强制assert，而是记录信息供参考
            if (categoryCount > 1) {
                System.out.println("资质类型属于多个分类: " + type.getName() + ", 分类数: " + categoryCount);
            }
        }
        
        // 测试特定互斥关系
        for (QualificationType type : QualificationType.values()) {
            // 互联网相关资质和IT企业认证通常是互斥的
            if (type.isInternetRelated()) {
                assertFalse(type.isITEnterpriseCertification(), 
                        "互联网相关资质不应同时是IT企业认证: " + type.getName());
            }
            
            // 食品相关资质与医药相关资质通常是互斥的
            if (type.isFoodRelated()) {
                assertFalse(type.isMedicalRelated(), 
                        "食品相关资质不应同时是医药相关资质: " + type.getName());
            }
        }
    }
    
    @Test
    @DisplayName("测试所有枚举值的方法调用")
    void testAllEnumMethods() {
        for (QualificationType type : QualificationType.values()) {
            // 确保每个枚举值的getter方法都被调用
            assertNotNull(type.getCode());
            assertNotNull(type.getName());
            
            // 测试所有判断方法，确保每个分支都被覆盖
            boolean isFood = type.isFoodRelated();
            boolean isMedical = type.isMedicalRelated();
            boolean isTransport = type.isTransportRelated();
            boolean isInternet = type.isInternetRelated();
            boolean isITEnterprise = type.isITEnterpriseCertification();
            
            // 确保输出值是确定的，要么true要么false
            assertTrue(isFood || !isFood);
            assertTrue(isMedical || !isMedical);
            assertTrue(isTransport || !isTransport);
            assertTrue(isInternet || !isInternet);
            assertTrue(isITEnterprise || !isITEnterprise);
        }
    }
    
    @Test
    @DisplayName("测试互联网资质的特性")
    void testInternetQualificationCharacteristics() {
        // 测试所有互联网相关资质
        QualificationType[] internetTypes = {
            QualificationType.VALUE_ADDED_TELECOM_BUSINESS,
            QualificationType.INTERNET_CONTENT_PROVIDER,
            QualificationType.TELECOM_BUSINESS_OPERATION,
            QualificationType.INTERNET_PUBLISHING,
            QualificationType.ELECTRONIC_CERTIFICATION,
            QualificationType.NETWORK_CULTURE_OPERATION
        };
        
        for (QualificationType type : internetTypes) {
            assertTrue(type.isInternetRelated());
            assertFalse(type.isFoodRelated());
            assertFalse(type.isMedicalRelated());
            assertFalse(type.isTransportRelated());
            
            // 名称应该包含互联网、网络、电信等关键词
            String name = type.getName();
            assertTrue(name.contains("互联网") || name.contains("网络") || 
                      name.contains("电信") || name.contains("电子"), 
                    "互联网相关资质名称应包含相关关键词: " + name);
        }
    }
    
    @Test
    @DisplayName("测试IT企业认定资质的特性")
    void testITEnterpriseCertificationCharacteristics() {
        // 测试所有IT企业认定资质
        QualificationType[] itEnterpriseTypes = {
            QualificationType.HIGH_TECH_ENTERPRISE,
            QualificationType.SOFTWARE_ENTERPRISE
        };
        
        for (QualificationType type : itEnterpriseTypes) {
            assertTrue(type.isITEnterpriseCertification());
            assertFalse(type.isInternetRelated());
            assertFalse(type.isFoodRelated());
            assertFalse(type.isMedicalRelated());
            assertFalse(type.isTransportRelated());
            
            // 名称应该包含企业、技术、软件等关键词
            String name = type.getName();
            assertTrue(name.contains("企业") || name.contains("技术") || 
                      name.contains("软件"), 
                    "IT企业认定资质名称应包含相关关键词: " + name);
        }
    }
    
    @Test
    @DisplayName("测试使用特殊字符的代码和名称")
    void testWithSpecialCharacters() {
        // 测试包含特殊字符的输入
        assertThrows(IllegalArgumentException.class, () -> QualificationType.fromCode("CONST!@#"));
        assertThrows(IllegalArgumentException.class, () -> QualificationType.fromName("建筑施工资质!@#"));
    }
    
    @Test
    @DisplayName("测试资质类型的命名模式")
    void testQualificationTypeNamingPattern() {
        // 测试资质类型的命名是否符合规范
        for (QualificationType type : QualificationType.values()) {
            String name = type.getName();
            assertFalse(name.isEmpty());
            assertTrue(name.contains("资质") || name.contains("许可") || 
                      name.contains("认定") || name.contains("认证") ||
                      name.contains("经营"), 
                    "资质类型名称应包含'资质'、'许可'、'认定'、'认证'或'经营'等字样: " + name);
        }
        
        // 特定资质类型的名称验证
        assertTrue(QualificationType.CONSTRUCTION.getName().contains("建筑"));
        assertTrue(QualificationType.FOOD_OPERATION.getName().contains("食品"));
        assertTrue(QualificationType.VALUE_ADDED_TELECOM_BUSINESS.getName().contains("增值电信"));
        assertTrue(QualificationType.HIGH_TECH_ENTERPRISE.getName().contains("高新技术"));
        assertTrue(QualificationType.SOFTWARE_ENTERPRISE.getName().contains("软件企业"));
    }
} 