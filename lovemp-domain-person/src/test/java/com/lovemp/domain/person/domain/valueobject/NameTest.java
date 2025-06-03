package com.lovemp.domain.person.domain.valueobject;

import com.lovemp.domain.person.domain.model.valueobject.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("姓名值对象测试")
class NameTest {

    @Test
    @DisplayName("测试基本工厂方法")
    void testBasicFactory() {
        // 使用基本工厂方法创建
        String originalName = "张三";
        Name name = Name.of(originalName);
        
        // 验证结果
        assertEquals(originalName, name.getOriginalName());
        assertNull(name.getProcessedName());
        assertNull(name.getPinyin());
        assertEquals(originalName, name.getDisplayName()); // 当处理名为空时，显示名应该是原始名
    }
    
    @Test
    @DisplayName("测试完整工厂方法")
    void testFullFactory() {
        // 使用完整工厂方法创建
        String originalName = "李𠄀"; // 包含生僻字
        String processedName = "李(木占)";
        String pinyin = "li zhan";
        
        Name name = Name.of(originalName, processedName, pinyin);
        
        // 验证结果
        assertEquals(originalName, name.getOriginalName());
        assertEquals(processedName, name.getProcessedName());
        assertEquals(pinyin, name.getPinyin());
        assertEquals(processedName, name.getDisplayName()); // 当处理名非空时，显示名应该是处理名
    }
    
    @Test
    @DisplayName("测试生成显示名称")
    void testGetDisplayName() {
        // 情况1：处理名为空
        Name name1 = Name.of("张三");
        assertEquals("张三", name1.getDisplayName());
        
        // 情况2：处理名非空
        Name name2 = Name.of("李𠄀", "李(木占)", "li zhan");
        assertEquals("李(木占)", name2.getDisplayName());
    }
    
    @Test
    @DisplayName("测试更新处理名称")
    void testWithProcessedName() {
        // 创建原始对象
        Name originalName = Name.of("王五", "王5", "wang wu");
        
        // 更新处理名称
        Name updatedName = originalName.withProcessedName("王五(5)");
        
        // 验证结果：原对象不变，新对象的处理名更新了
        assertEquals("王5", originalName.getProcessedName());
        assertEquals("王五(5)", updatedName.getProcessedName());
        
        // 其他属性保持不变
        assertEquals(originalName.getOriginalName(), updatedName.getOriginalName());
        assertEquals(originalName.getPinyin(), updatedName.getPinyin());
    }
    
    @Test
    @DisplayName("测试更新拼音")
    void testWithPinyin() {
        // 创建原始对象
        Name originalName = Name.of("赵六", "赵6", "zhao liu");
        
        // 更新拼音
        Name updatedName = originalName.withPinyin("zhao 6");
        
        // 验证结果：原对象不变，新对象的拼音更新了
        assertEquals("zhao liu", originalName.getPinyin());
        assertEquals("zhao 6", updatedName.getPinyin());
        
        // 其他属性保持不变
        assertEquals(originalName.getOriginalName(), updatedName.getOriginalName());
        assertEquals(originalName.getProcessedName(), updatedName.getProcessedName());
    }
    
    @Test
    @DisplayName("测试equals方法")
    void testEquals() {
        // 相同原始名称的对象应该相等，即使处理名和拼音不同
        Name name1 = Name.of("赵六", "赵6", "zhao liu");
        Name name2 = Name.of("赵六", "赵六", "zhao 6");
        Name name3 = Name.of("李四");
        
        // 验证结果
        assertEquals(name1, name2);
        assertNotEquals(name1, name3);
        assertNotEquals(name2, name3);
        
        // 自反性
        assertEquals(name1, name1);
        
        // 与null比较
        assertNotEquals(null, name1);
        
        // 与其他类型比较
        assertNotEquals(name1, "赵六");
    }
    
    @Test
    @DisplayName("测试hashCode方法")
    void testHashCode() {
        // 相同原始名称的对象应该有相同的哈希码
        Name name1 = Name.of("赵六", "赵6", "zhao liu");
        Name name2 = Name.of("赵六", "赵六", "zhao 6");
        Name name3 = Name.of("李四");
        
        // 验证结果
        assertEquals(name1.hashCode(), name2.hashCode());
        assertNotEquals(name1.hashCode(), name3.hashCode());
    }
    
    @Test
    @DisplayName("测试toString方法")
    void testToString() {
        // 应该返回原始名称
        String originalName = "张三";
        Name name = Name.of(originalName, "zhang san", "zhang san");
        
        // 验证结果
        assertEquals(originalName, name.toString());
    }
}
