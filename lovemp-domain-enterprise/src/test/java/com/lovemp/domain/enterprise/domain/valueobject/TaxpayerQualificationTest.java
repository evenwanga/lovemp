package com.lovemp.domain.enterprise.domain.valueobject;

import com.lovemp.domain.enterprise.domain.model.valueobject.TaxpayerQualification;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class TaxpayerQualificationTest {

    @Test
    void testEnumValues() {
        // 测试枚举值的基本属性
        assertEquals("一般纳税人", TaxpayerQualification.GENERAL.getName());
        assertEquals("GTP", TaxpayerQualification.GENERAL.getCode());
        
        assertEquals("小规模纳税人", TaxpayerQualification.SMALL_SCALE.getName());
        assertEquals("SST", TaxpayerQualification.SMALL_SCALE.getCode());
        
        assertEquals("其他", TaxpayerQualification.OTHER.getName());
        assertEquals("OTHER", TaxpayerQualification.OTHER.getCode());
    }
    
    @Test
    void testFromCode() {
        // 测试通过代码获取枚举值
        assertEquals(TaxpayerQualification.GENERAL, TaxpayerQualification.fromCode("GTP"));
        assertEquals(TaxpayerQualification.SMALL_SCALE, TaxpayerQualification.fromCode("SST"));
        assertEquals(TaxpayerQualification.OTHER, TaxpayerQualification.fromCode("OTHER"));
        
        // 测试不区分大小写
        assertEquals(TaxpayerQualification.GENERAL, TaxpayerQualification.fromCode("gtp"));
    }
    
    @Test
    void testFromCodeWithInvalidInput() {
        // 测试空值
        assertThrows(IllegalArgumentException.class, () -> 
            TaxpayerQualification.fromCode(null)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            TaxpayerQualification.fromCode("")
        );
        
        // 测试无效代码
        assertThrows(IllegalArgumentException.class, () -> 
            TaxpayerQualification.fromCode("INVALID_CODE")
        );
    }
    
    @Test
    void testFromName() {
        // 测试通过名称获取枚举值
        assertEquals(TaxpayerQualification.GENERAL, TaxpayerQualification.fromName("一般纳税人"));
        assertEquals(TaxpayerQualification.SMALL_SCALE, TaxpayerQualification.fromName("小规模纳税人"));
        assertEquals(TaxpayerQualification.OTHER, TaxpayerQualification.fromName("其他"));
    }
    
    @Test
    void testFromNameWithInvalidInput() {
        // 测试空值
        assertThrows(IllegalArgumentException.class, () -> 
            TaxpayerQualification.fromName(null)
        );
        
        assertThrows(IllegalArgumentException.class, () -> 
            TaxpayerQualification.fromName("")
        );
        
        // 测试无效名称
        assertThrows(IllegalArgumentException.class, () -> 
            TaxpayerQualification.fromName("不存在的纳税人类型")
        );
    }
    
    @ParameterizedTest
    @EnumSource(TaxpayerQualification.class)
    void testEnumExistence(TaxpayerQualification qualification) {
        // 确保所有枚举值都可访问
        assertNotNull(qualification);
        assertNotNull(qualification.getName());
        assertNotNull(qualification.getCode());
    }
    
    @ParameterizedTest
    @CsvSource({
        "GENERAL, 一般纳税人, GTP",
        "SMALL_SCALE, 小规模纳税人, SST",
        "OTHER, 其他, OTHER"
    })
    void testEnumProperties(String enumName, String expectedName, String expectedCode) {
        TaxpayerQualification qualification = TaxpayerQualification.valueOf(enumName);
        assertEquals(expectedName, qualification.getName());
        assertEquals(expectedCode, qualification.getCode());
    }
} 