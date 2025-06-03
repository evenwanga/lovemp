package com.lovemp.common.util;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * ReflectionUtils工具类的单元测试
 * 
 * <p>该测试类用于验证ReflectionUtils工具类中各种反射操作方法的正确性,包括:
 * <ul>
 *   <li>获取字段 - 获取类的所有字段,包括继承的字段</li>
 *   <li>获取方法 - 获取类的所有方法,包括继承的方法</li>
 *   <li>字段操作 - 设置和获取字段值</li>
 *   <li>方法调用 - 调用对象的方法</li>
 * </ul>
 * 
 * <p>测试验证:
 * <ul>
 *   <li>正常情况 - 各种反射操作的正确执行</li>
 *   <li>异常情况 - 访问权限、类型不匹配等异常处理</li>
 *   <li>继承关系 - 父类字段和方法的正确处理</li>
 * </ul>
 * 
 * @see com.lovemp.common.util.ReflectionUtils
 * @author lovemp
 * @since 1.0
 */
class ReflectionUtilsTest {

    // 测试用静态内部类
    private static class TestParent {
        String parentField;
        
        @SuppressWarnings("unused")
        void parentMethod() {}
        
        String getParentField() {
            return parentField;
        }
        
        void setParentField(String parentField) {
            this.parentField = parentField;
        }
    }
    
    private static class TestChild extends TestParent {
        String childField;
        int age;
        
        @SuppressWarnings("unused")
        void childMethod() {}
        
        String getChildField() {
            return childField;
        }
        
        void setChildField(String childField) {
            this.childField = childField;
        }
        
        @SuppressWarnings("unused")
        int getAge() {
            return age;
        }
        
        @SuppressWarnings("unused")
        void setAge(int age) {
            this.age = age;
        }
    }

    @Test
    void getAllFields() {
        // 测试获取所有字段（包括继承的字段）
        List<Field> fields = ReflectionUtils.getAllFields(TestChild.class);
        
        // 应该包含 childField、age 和继承的 parentField
        assertEquals(3, fields.size());
        
        boolean hasChildField = false;
        boolean hasAge = false;
        boolean hasParentField = false;
        
        for (Field field : fields) {
            if ("childField".equals(field.getName())) {
                hasChildField = true;
            } else if ("age".equals(field.getName())) {
                hasAge = true;
            } else if ("parentField".equals(field.getName())) {
                hasParentField = true;
            }
        }
        
        assertTrue(hasChildField);
        assertTrue(hasAge);
        assertTrue(hasParentField);
    }

    @Test
    void getAllMethods() {
        // 测试获取所有方法（包括继承的方法）
        List<Method> methods = ReflectionUtils.getAllMethods(TestChild.class);
        
        // 查找测试类中定义的各个方法
        boolean hasChildMethod = false;
        boolean hasGetChildField = false;
        boolean hasSetChildField = false;
        boolean hasParentMethod = false;
        boolean hasGetParentField = false;
        boolean hasSetParentField = false;
        
        for (Method method : methods) {
            String methodName = method.getName();
            if ("childMethod".equals(methodName)) {
                hasChildMethod = true;
            } else if ("getChildField".equals(methodName)) {
                hasGetChildField = true;
            } else if ("setChildField".equals(methodName)) {
                hasSetChildField = true;
            } else if ("parentMethod".equals(methodName)) {
                hasParentMethod = true;
            } else if ("getParentField".equals(methodName)) {
                hasGetParentField = true;
            } else if ("setParentField".equals(methodName)) {
                hasSetParentField = true;
            }
        }
        
        assertTrue(hasChildMethod);
        assertTrue(hasGetChildField);
        assertTrue(hasSetChildField);
        assertTrue(hasParentMethod);
        assertTrue(hasGetParentField);
        assertTrue(hasSetParentField);
    }

    @Test
    void getField() {
        // 测试获取特定字段
        Field field = ReflectionUtils.getField(TestChild.class, "childField");
        assertNotNull(field);
        assertEquals("childField", field.getName());
        
        // 测试获取父类字段
        field = ReflectionUtils.getField(TestChild.class, "parentField");
        assertNotNull(field);
        assertEquals("parentField", field.getName());
        
        // 测试获取不存在的字段
        field = ReflectionUtils.getField(TestChild.class, "nonExistentField");
        assertNull(field);
    }

    @Test
    void getMethod() {
        // 测试获取无参方法
        Method method = ReflectionUtils.getMethod(TestChild.class, "childMethod");
        assertNotNull(method);
        assertEquals("childMethod", method.getName());
        
        // 测试获取带参方法
        method = ReflectionUtils.getMethod(TestChild.class, "setChildField", String.class);
        assertNotNull(method);
        assertEquals("setChildField", method.getName());
        
        // 测试获取父类方法
        method = ReflectionUtils.getMethod(TestChild.class, "parentMethod");
        assertNotNull(method);
        assertEquals("parentMethod", method.getName());
        
        // 测试获取不存在的方法
        method = ReflectionUtils.getMethod(TestChild.class, "nonExistentMethod");
        assertNull(method);
    }

    @Test
    void getFieldValue() {
        // 创建测试对象
        TestChild testObject = new TestChild();
        testObject.setChildField("child value");
        testObject.setParentField("parent value");
        
        // 测试获取字段值通过字段名
        Object value = ReflectionUtils.getFieldValue(testObject, "childField");
        assertEquals("child value", value);
        
        // 测试获取父类字段值
        value = ReflectionUtils.getFieldValue(testObject, "parentField");
        assertEquals("parent value", value);
        
        // 测试获取字段值通过Field对象
        Field field = ReflectionUtils.getField(TestChild.class, "childField");
        value = ReflectionUtils.getFieldValue(testObject, field);
        assertEquals("child value", value);
    }

    @Test
    void setFieldValue() {
        // 创建测试对象
        TestChild testObject = new TestChild();
        
        // 测试设置字段值通过字段名
        ReflectionUtils.setFieldValue(testObject, "childField", "new child value");
        assertEquals("new child value", testObject.getChildField());
        
        // 测试设置父类字段值
        ReflectionUtils.setFieldValue(testObject, "parentField", "new parent value");
        assertEquals("new parent value", testObject.getParentField());
        
        // 测试设置字段值通过Field对象
        Field field = ReflectionUtils.getField(TestChild.class, "childField");
        ReflectionUtils.setFieldValue(testObject, field, "updated child value");
        assertEquals("updated child value", testObject.getChildField());
    }

    @Test
    void invokeMethod() {
        // 创建测试对象
        TestChild testObject = new TestChild();
        
        // 设置初始值
        testObject.setChildField("initial value");
        
        // 测试调用getter方法
        Object result = ReflectionUtils.invokeMethod(testObject, "getChildField");
        assertEquals("initial value", result);
        
        // 测试调用setter方法
        ReflectionUtils.invokeMethod(testObject, "setChildField", "invoked value");
        assertEquals("invoked value", testObject.getChildField());
        
        // 测试调用方法通过Method对象
        Method method = ReflectionUtils.getMethod(TestChild.class, "setChildField", String.class);
        ReflectionUtils.invokeMethod(testObject, method, "method invoked value");
        assertEquals("method invoked value", testObject.getChildField());
    }

    @Test
    void newInstance() {
        // 测试创建新实例
        TestChild instance = ReflectionUtils.newInstance(TestChild.class);
        assertNotNull(instance);
        assertTrue(instance instanceof TestChild);
    }

    @Test
    void isPrimitiveOrWrapper() {
        // 测试原始类型判断
        assertTrue(ReflectionUtils.isPrimitiveOrWrapper(int.class));
        assertTrue(ReflectionUtils.isPrimitiveOrWrapper(Integer.class));
        assertTrue(ReflectionUtils.isPrimitiveOrWrapper(boolean.class));
        assertTrue(ReflectionUtils.isPrimitiveOrWrapper(Boolean.class));
        
        // 测试非原始类型
        assertFalse(ReflectionUtils.isPrimitiveOrWrapper(String.class));
        assertFalse(ReflectionUtils.isPrimitiveOrWrapper(TestChild.class));
    }

    @Test
    void isCollection() {
        // 测试集合类型判断
        assertTrue(ReflectionUtils.isCollection(List.class));
        assertTrue(ReflectionUtils.isCollection(java.util.ArrayList.class));
        assertTrue(ReflectionUtils.isCollection(java.util.Set.class));
        
        // 测试非集合类型
        assertFalse(ReflectionUtils.isCollection(String.class));
        assertFalse(ReflectionUtils.isCollection(TestChild.class));
    }

    @Test
    void isMap() {
        // 测试Map类型判断
        assertTrue(ReflectionUtils.isMap(Map.class));
        assertTrue(ReflectionUtils.isMap(java.util.HashMap.class));
        
        // 测试非Map类型
        assertFalse(ReflectionUtils.isMap(List.class));
        assertFalse(ReflectionUtils.isMap(String.class));
    }
} 