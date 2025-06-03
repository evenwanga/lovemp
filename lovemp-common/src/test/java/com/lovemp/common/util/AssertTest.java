package com.lovemp.common.util;

import com.lovemp.common.exception.DomainRuleViolationException;
import org.junit.jupiter.api.Test;

import java.util.*;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Assert工具类的单元测试类
 * 
 * 该测试类用于验证Assert工具类中各种断言方法的正确性,包括:
 * - isTrue(): 验证条件为真
 * - isFalse(): 验证条件为假
 * - notNull(): 验证对象不为null
 * - notEmpty(): 验证字符串/集合/Map不为空
 * - isInstanceOf(): 验证对象类型
 * - state(): 验证系统状态
 * 
 * 每个测试用例都会验证:
 * 1. 正常情况 - 断言条件满足时不抛出异常
 * 2. 异常情况 - 断言条件不满足时抛出DomainRuleViolationException异常
 * 3. 异常消息的正确性
 * 
 * @see com.lovemp.common.util.Assert
 * @see com.lovemp.common.exception.DomainRuleViolationException
 */
class AssertTest {

    @Test
    void isTrue() {
        // 测试表达式为真的情况
        assertDoesNotThrow(() -> Assert.isTrue(true, "不应抛出异常"));

        // 测试表达式为假的情况，应该抛出异常
        DomainRuleViolationException exception = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.isTrue(false, "条件不满足"));
        assertEquals("条件不满足", exception.getMessage());
    }

    @Test
    void isFalse() {
        // 测试表达式为假的情况
        assertDoesNotThrow(() -> Assert.isFalse(false, "不应抛出异常"));

        // 测试表达式为真的情况，应该抛出异常
        DomainRuleViolationException exception = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.isFalse(true, "条件不满足"));
        assertEquals("条件不满足", exception.getMessage());
    }

    @Test
    void notNull() {
        // 测试对象不为null的情况
        Object obj = new Object();
        assertDoesNotThrow(() -> Assert.notNull(obj, "不应抛出异常"));

        // 测试对象为null的情况，应该抛出异常
        DomainRuleViolationException exception = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.notNull(null, "对象不能为空"));
        assertEquals("对象不能为空", exception.getMessage());
    }

    @Test
    void notEmptyString() {
        // 测试字符串不为空的情况
        assertDoesNotThrow(() -> Assert.notEmpty("test", "不应抛出异常"));

        // 测试空字符串，应该抛出异常
        DomainRuleViolationException exception1 = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.notEmpty("", "字符串不能为空"));
        assertEquals("字符串不能为空", exception1.getMessage());

        // 测试null字符串，应该抛出异常
        DomainRuleViolationException exception2 = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.notEmpty((String) null, "字符串不能为null"));
        assertEquals("字符串不能为null", exception2.getMessage());
    }

    @Test
    void notEmptyCollection() {
        // 测试集合不为空的情况
        List<String> list = Arrays.asList("a", "b", "c");
        assertDoesNotThrow(() -> Assert.notEmpty(list, "不应抛出异常"));

        // 测试空集合，应该抛出异常
        DomainRuleViolationException exception1 = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.notEmpty(new ArrayList<>(), "集合不能为空"));
        assertEquals("集合不能为空", exception1.getMessage());

        // 测试null集合，应该抛出异常
        DomainRuleViolationException exception2 = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.notEmpty((Collection<?>) null, "集合不能为null"));
        assertEquals("集合不能为null", exception2.getMessage());
    }

    @Test
    void notEmptyMap() {
        // 测试Map不为空的情况
        Map<String, String> map = new HashMap<>();
        map.put("key", "value");
        assertDoesNotThrow(() -> Assert.notEmpty(map, "不应抛出异常"));

        // 测试空Map，应该抛出异常
        DomainRuleViolationException exception1 = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.notEmpty(new HashMap<>(), "Map不能为空"));
        assertEquals("Map不能为空", exception1.getMessage());

        // 测试null Map，应该抛出异常
        DomainRuleViolationException exception2 = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.notEmpty((Map<?, ?>) null, "Map不能为null"));
        assertEquals("Map不能为null", exception2.getMessage());
    }

    @Test
    void isInstanceOf() {
        // 测试对象是指定类型的实例
        String str = "test";
        assertDoesNotThrow(() -> Assert.isInstanceOf(String.class, str, "不应抛出异常"));

        // 测试对象不是指定类型的实例，应该抛出异常
        DomainRuleViolationException exception = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.isInstanceOf(Integer.class, str, "类型不匹配"));
        assertEquals("类型不匹配", exception.getMessage());

        // 测试类型为null的情况，应该抛出异常
        DomainRuleViolationException exception2 = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.isInstanceOf(null, str, "消息不重要"));
        assertEquals("类型不能为空", exception2.getMessage());
    }

    @Test
    void state() {
        // 测试状态为真的情况
        assertDoesNotThrow(() -> Assert.state(true, "不应抛出异常"));

        // 测试状态为假的情况，应该抛出异常
        DomainRuleViolationException exception = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.state(false, "状态不满足"));
        assertEquals("状态不满足", exception.getMessage());
    }

    @Test
    void stateWithSupplier() {
        // 测试状态为真的情况
        Supplier<String> messageSupplier = () -> "动态生成的消息";
        assertDoesNotThrow(() -> Assert.state(true, messageSupplier));

        // 测试状态为假的情况，应该抛出异常
        DomainRuleViolationException exception = assertThrows(
                DomainRuleViolationException.class,
                () -> Assert.state(false, messageSupplier));
        assertEquals("动态生成的消息", exception.getMessage());
        
        // 测试supplier为null的情况
        assertDoesNotThrow(() -> Assert.state(true, (Supplier<String>) null));
    }
} 