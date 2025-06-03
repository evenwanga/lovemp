package com.lovemp.common.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * StringUtils工具类的单元测试
 * 
 * <p>该测试类用于验证StringUtils工具类中各种字符串操作方法的正确性,包括:
 * <ul>
 *   <li>空值判断 - 判断字符串是否为null或空字符串</li>
 *   <li>空白判断 - 判断字符串是否为空白字符串</li>
 *   <li>字符串处理 - 字符串的修剪、截取等操作</li>
 * </ul>
 * 
 * <p>测试验证:
 * <ul>
 *   <li>正常情况 - 各种字符串输入的正确判断</li>
 *   <li>边界情况 - null值、空字符串、空白字符串等特殊情况</li>
 *   <li>参数校验 - 输入参数的有效性验证</li>
 * </ul>
 * 
 * @see com.lovemp.common.util.StringUtils
 * @author lovemp
 * @since 1.0
 */
public class StringUtilsTest {

    /**
     * 测试判断字符串是否为空
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "非空字符串"})
    public void testIsEmpty(String input) {
        if (input == null || input.isEmpty()) {
            assertTrue(StringUtils.isEmpty(input));
        } else {
            assertFalse(StringUtils.isEmpty(input));
        }
    }

    /**
     * 测试判断字符串是否不为空
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "非空字符串"})
    public void testIsNotEmpty(String input) {
        if (input == null || input.isEmpty()) {
            assertFalse(StringUtils.isNotEmpty(input));
        } else {
            assertTrue(StringUtils.isNotEmpty(input));
        }
    }

    /**
     * 测试判断字符串是否为空白
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n", "\r", "  \t\n", "非空字符串"})
    public void testIsBlank(String input) {
        if (input == null || input.isEmpty() || input.trim().isEmpty()) {
            assertTrue(StringUtils.isBlank(input));
        } else {
            assertFalse(StringUtils.isBlank(input));
        }
    }

    /**
     * 测试判断字符串是否不为空白
     */
    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {" ", "\t", "\n", "\r", "  \t\n", "非空字符串"})
    public void testIsNotBlank(String input) {
        if (input == null || input.isEmpty() || input.trim().isEmpty()) {
            assertFalse(StringUtils.isNotBlank(input));
        } else {
            assertTrue(StringUtils.isNotBlank(input));
        }
    }

    /**
     * 测试截取字符串
     */
    @Test
    public void testTruncate() {
        // 测试正常截取
        assertEquals("12345", StringUtils.truncate("1234567890", 5));
        
        // 测试字符串长度小于等于最大长度
        assertEquals("123", StringUtils.truncate("123", 5));
        assertEquals("12345", StringUtils.truncate("12345", 5));
        
        // 测试空字符串和null
        assertNull(StringUtils.truncate(null, 5));
        assertEquals("", StringUtils.truncate("", 5));
        
        // 测试最大长度为0
        assertEquals("", StringUtils.truncate("12345", 0));
        
        // 测试负数最大长度（应视为0）
        assertEquals("", StringUtils.truncate("12345", -1));
    }

    /**
     * 测试生成UUID（无连字符）
     */
    @Test
    public void testUuid() {
        String uuid = StringUtils.uuid();
        
        // 验证长度（标准UUID去掉4个连字符后应为32位）
        assertEquals(32, uuid.length());
        
        // 验证不包含连字符
        assertFalse(uuid.contains("-"));
        
        // 验证多次生成的UUID不相同
        String uuid2 = StringUtils.uuid();
        assertNotEquals(uuid, uuid2);
    }

    /**
     * 测试生成带连字符的UUID
     */
    @Test
    public void testUuidWithHyphens() {
        String uuid = StringUtils.uuidWithHyphens();
        
        // 验证长度（标准UUID包含4个连字符，共36位）
        assertEquals(36, uuid.length());
        
        // 验证包含连字符，且数量正确
        assertEquals(4, uuid.chars().filter(ch -> ch == '-').count());
        
        // 验证连字符位置正确
        assertTrue(uuid.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}"));
        
        // 验证多次生成的UUID不相同
        String uuid2 = StringUtils.uuidWithHyphens();
        assertNotEquals(uuid, uuid2);
    }

    /**
     * 测试检查电子邮件格式
     */
    @ParameterizedTest
    @CsvSource({
        "test@example.com, true",
        "test.name@example.com, true",
        "test+name@example.com, true",
        "test@example.co.jp, true",
        "invalid-email, false",
        "test@, false",
        "@example.com, false",
        "test@example, false"
    })
    public void testIsValidEmail(String email, boolean expected) {
        assertEquals(expected, StringUtils.isValidEmail(email));
    }

    /**
     * 测试检查手机号码格式（中国大陆）
     */
    @ParameterizedTest
    @CsvSource({
        "13800138000, true",
        "15900000000, true",
        "18812345678, true",
        "19987654321, true",
        "12345678901, false",  // 非法前缀
        "1380013800, false",   // 位数不足
        "138001380000, false", // 位数过多
        "abcdefghijk, false"   // 非数字
    })
    public void testIsValidMobilePhone(String phone, boolean expected) {
        assertEquals(expected, StringUtils.isValidMobilePhone(phone));
    }

    /**
     * 测试首字母转大写
     */
    @Test
    public void testCapitalize() {
        // 测试普通字符串
        assertEquals("Hello", StringUtils.capitalize("hello"));
        
        // 测试已经是大写的字符串
        assertEquals("Hello", StringUtils.capitalize("Hello"));
        
        // 测试单字符
        assertEquals("A", StringUtils.capitalize("a"));
        
        // 测试空字符串和null
        assertNull(StringUtils.capitalize(null));
        assertEquals("", StringUtils.capitalize(""));
        
        // 测试首字符不是字母的情况
        assertEquals("123abc", StringUtils.capitalize("123abc"));
    }

    /**
     * 测试首字母转小写
     */
    @Test
    public void testUncapitalize() {
        // 测试普通字符串
        assertEquals("hello", StringUtils.uncapitalize("Hello"));
        
        // 测试已经是小写的字符串
        assertEquals("hello", StringUtils.uncapitalize("hello"));
        
        // 测试单字符
        assertEquals("a", StringUtils.uncapitalize("A"));
        
        // 测试空字符串和null
        assertNull(StringUtils.uncapitalize(null));
        assertEquals("", StringUtils.uncapitalize(""));
        
        // 测试首字符不是字母的情况
        assertEquals("123abc", StringUtils.uncapitalize("123abc"));
    }

    /**
     * 测试驼峰命名转下划线命名
     */
    @ParameterizedTest
    @CsvSource({
        "userName, user_name",
        "UserName, user_name",
        "firstName, first_name",
        "firstName123, first_name123",
        "first_name, first_name", // 已经是下划线格式
        "lowercase, lowercase",   // 没有大写字母
        "UPPERCASE, uppercase"    // 全大写
    })
    public void testCamelToUnderscore(String input, String expected) {
        assertEquals(expected, StringUtils.camelToUnderscore(input));
    }

    /**
     * 测试下划线命名转驼峰命名
     */
    @ParameterizedTest
    @CsvSource({
        "user_name, userName",
        "first_name, firstName",
        "first_name_123, firstName123",
        "firstName, firstName",  // 已经是驼峰格式
        "lowercase, lowercase",  // 没有下划线
        "uppercase, uppercase"   // 没有下划线
    })
    public void testUnderscoreToCamel(String input, String expected) {
        assertEquals(expected, StringUtils.underscoreToCamel(input));
    }
    
    /**
     * 测试驼峰与下划线互转的一致性
     */
    @Test
    public void testCamelAndUnderscoreConsistency() {
        // 从驼峰到下划线再回到驼峰
        String camel = "userFirstName";
        String underscore = StringUtils.camelToUnderscore(camel);
        String backToCamel = StringUtils.underscoreToCamel(underscore);
        assertEquals(camel, backToCamel);
        
        // 从下划线到驼峰再回到下划线
        String original = "user_first_name";
        String toCamel = StringUtils.underscoreToCamel(original);
        String backToUnderscore = StringUtils.camelToUnderscore(toCamel);
        assertEquals(original, backToUnderscore);
    }
    
    /**
     * 测试空值处理
     */
    @Test
    public void testNullHandling() {
        // 验证各方法对null的处理
        assertNull(StringUtils.camelToUnderscore(null));
        assertNull(StringUtils.underscoreToCamel(null));
        
        // 验证各方法对空字符串的处理
        assertEquals("", StringUtils.camelToUnderscore(""));
        assertEquals("", StringUtils.underscoreToCamel(""));
    }
} 