package com.lovemp.domain.person.domain.valueobject;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("姓名值对象测试")
class NameTest {
    
    @Test
    @DisplayName("测试基本构造函数和getter方法")
    void testBasicConstructionAndGetters() {
        // 创建一个简单的姓名值对象
        Name name = Name.of("张三");
        
        // 验证结果
        assertEquals("张三", name.getOriginalName());
        assertNull(name.getProcessedName());
        assertNull(name.getPinyin());
        assertEquals("张三", name.getDisplayName()); // 没有处理后的姓名，显示原始姓名
        assertEquals("张三", name.toString());
    }
    
    @Test
    @DisplayName("测试完整构造函数")
    void testFullConstruction() {
        // 创建一个包含所有字段的姓名值对象
        Name name = Name.of("赵𬊤", "赵(木占)", "ZHAO ZHAN");
        
        // 验证结果
        assertEquals("赵𬊤", name.getOriginalName());
        assertEquals("赵(木占)", name.getProcessedName());
        assertEquals("ZHAO ZHAN", name.getPinyin());
        assertEquals("赵(木占)", name.getDisplayName()); // 有处理后的姓名，显示处理后的姓名
    }
    
    @Test
    @DisplayName("测试withProcessedName方法")
    void testWithProcessedName() {
        // 创建初始对象
        Name name = Name.of("赵𬊤");
        
        // 添加处理后的姓名
        Name updatedName = name.withProcessedName("赵(木占)");
        
        // 验证不可变性
        assertNull(name.getProcessedName());
        assertEquals("赵(木占)", updatedName.getProcessedName());
        
        // 验证显示名称
        assertEquals("赵𬊤", name.getDisplayName());
        assertEquals("赵(木占)", updatedName.getDisplayName());
    }
    
    @Test
    @DisplayName("测试withPinyin方法")
    void testWithPinyin() {
        // 创建初始对象
        Name name = Name.of("赵𬊤", "赵(木占)", null);
        
        // 添加拼音
        Name updatedName = name.withPinyin("ZHAO ZHAN");
        
        // 验证不可变性
        assertNull(name.getPinyin());
        assertEquals("ZHAO ZHAN", updatedName.getPinyin());
        
        // 其他字段应保持不变
        assertEquals("赵𬊤", updatedName.getOriginalName());
        assertEquals("赵(木占)", updatedName.getProcessedName());
    }
    
    @Test
    @DisplayName("测试getDisplayName方法")
    void testGetDisplayName() {
        // 测试只有原始姓名的情况
        Name nameOnly = Name.of("张三");
        assertEquals("张三", nameOnly.getDisplayName());
        
        // 测试有处理后姓名的情况
        Name nameWithProcessed = Name.of("赵𬊤", "赵(木占)", null);
        assertEquals("赵(木占)", nameWithProcessed.getDisplayName());
        
        // 测试处理后姓名为空的情况
        Name nameWithNullProcessed = Name.of("张三", null, "ZHANG SAN");
        assertEquals("张三", nameWithNullProcessed.getDisplayName());
    }
    
    @Test
    @DisplayName("测试equals和hashCode方法")
    void testEqualsAndHashCode() {
        // 创建两个原始姓名相同但处理后姓名和拼音不同的对象
        Name name1 = Name.of("张三", "ZHANG SAN", "zhang san");
        Name name2 = Name.of("张三", "张三(ZHANG SAN)", "ZHANG SAN");
        Name name3 = Name.of("李四");
        
        // 验证equals方法
        assertEquals(name1, name2); // 原始姓名相同，应该相等
        assertNotEquals(name1, name3); // 原始姓名不同，应该不相等
        
        // 验证hashCode方法
        assertEquals(name1.hashCode(), name2.hashCode()); // 相等的对象应有相同的哈希码
        assertNotEquals(name1.hashCode(), name3.hashCode()); // 不相等的对象应有不同的哈希码
    }
    
    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        Name name = Name.of("张三", "ZHANG SAN", "zhang san");
        assertEquals("张三", name.toString());
    }
} 