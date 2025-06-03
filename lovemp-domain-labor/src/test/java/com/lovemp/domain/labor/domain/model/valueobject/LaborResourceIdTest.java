package com.lovemp.domain.labor.domain.model.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("劳动力资源ID值对象测试")
class LaborResourceIdTest {

    @Test
    @DisplayName("生成新ID应该成功")
    void shouldGenerateNewId() {
        // 执行测试
        LaborResourceId id = LaborResourceId.generate();
        
        // 验证结果
        assertNotNull(id);
        assertNotNull(id.getValue());
        assertFalse(id.getValue().isEmpty());
    }
    
    @Test
    @DisplayName("从字符串创建ID应该成功")
    void shouldCreateFromString() {
        // 准备测试数据
        String idValue = "12345678-1234-1234-1234-123456789012";
        
        // 执行测试
        LaborResourceId id = LaborResourceId.of(idValue);
        
        // 验证结果
        assertEquals(idValue, id.getValue());
        assertEquals(idValue, id.toString());
    }
    
    @Test
    @DisplayName("相同值的ID应该相等")
    void identicalIdsShouldBeEqual() {
        // 准备测试数据
        String idValue = "12345678-1234-1234-1234-123456789012";
        LaborResourceId id1 = LaborResourceId.of(idValue);
        LaborResourceId id2 = LaborResourceId.of(idValue);
        
        // 验证结果
        assertEquals(id1, id2);
        assertEquals(id1.hashCode(), id2.hashCode());
    }
    
    @Test
    @DisplayName("不同值的ID不应该相等")
    void differentIdsShouldNotBeEqual() {
        // 准备测试数据
        LaborResourceId id1 = LaborResourceId.of("12345678-1234-1234-1234-123456789012");
        LaborResourceId id2 = LaborResourceId.of("87654321-1234-1234-1234-123456789012");
        
        // 验证结果
        assertNotEquals(id1, id2);
        assertNotEquals(id1.hashCode(), id2.hashCode());
    }
}