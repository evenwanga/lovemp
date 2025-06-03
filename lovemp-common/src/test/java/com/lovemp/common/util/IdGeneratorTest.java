package com.lovemp.common.util;

import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.*;

/**
 * IdGenerator工具类的单元测试
 * 
 * <p>该测试类用于验证IdGenerator工具类中各种ID生成方法的正确性,包括:
 * <ul>
 *   <li>UUID生成 - 生成32位和36位UUID</li>
 *   <li>雪花算法ID - 生成分布式唯一ID</li>
 * </ul>
 * 
 * <p>测试验证:
 * <ul>
 *   <li>生成ID的格式正确性</li>
 *   <li>生成ID的唯一性</li>
 *   <li>生成ID的性能要求</li>
 * </ul>
 * 
 * @see com.lovemp.common.util.IdGenerator
 * @author lovemp
 * @since 1.0
 */
class IdGeneratorTest {

    @Test
    void uuid() {
        // 测试生成的UUID（无连字符）
        String uuid = IdGenerator.uuid();
        assertNotNull(uuid);
        assertEquals(32, uuid.length());
        assertFalse(uuid.contains("-"));
        // 验证是否只包含十六进制字符
        assertTrue(Pattern.matches("[0-9a-f]{32}", uuid));
    }

    @Test
    void uuid36() {
        // 测试生成的带连字符的UUID
        String uuid = IdGenerator.uuid36();
        assertNotNull(uuid);
        assertEquals(36, uuid.length());
        assertTrue(uuid.contains("-"));
        // 验证是否符合标准UUID格式
        assertTrue(Pattern.matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}", uuid));
    }

    @Test
    void snowflakeId() {
        // 测试生成的雪花ID
        long id1 = IdGenerator.snowflakeId();
        long id2 = IdGenerator.snowflakeId();
        
        // ID应该是正数
        assertTrue(id1 > 0);
        assertTrue(id2 > 0);
        
        // 连续生成的ID应该不同
        assertNotEquals(id1, id2);
    }

    @Test
    void snowflakeIdUniqueness() {
        // 测试雪花ID的唯一性
        Set<Long> idSet = new HashSet<>();
        int count = 1000;
        
        for (int i = 0; i < count; i++) {
            idSet.add(IdGenerator.snowflakeId());
        }
        
        // 所有生成的ID应该互不相同
        assertEquals(count, idSet.size());
    }

    @Test
    void generateOrderNo() {
        // 测试生成的订单号格式
        String orderNo = IdGenerator.generateOrderNo();
        assertNotNull(orderNo);
        
        // 订单号应包含当前日期时间前缀（14位）+ 6位随机数，总长度20位
        assertEquals(20, orderNo.length());
        
        // 检查前缀是否为当前日期时间
        String dateTimePrefix = orderNo.substring(0, 14);
        String datePattern = "yyyyMMddHHmmss";
        
        // 解析日期前缀，不应抛出异常
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime orderTime = LocalDateTime.parse(dateTimePrefix, 
                DateTimeFormatter.ofPattern(datePattern));
        
        // 订单时间应该与当前时间接近
        assertTrue(Math.abs(orderTime.getHour() - now.getHour()) <= 1);
        
        // 后6位应该为数字
        String randomPart = orderNo.substring(14);
        assertTrue(Pattern.matches("\\d{6}", randomPart));
    }

    @Test
    void generateCodeWithPrefix() {
        // 测试生成带前缀的编码
        String prefix = "TEST_";
        String code = IdGenerator.generateCodeWithPrefix(prefix);
        assertNotNull(code);
        
        // 应以提供的前缀开头
        assertTrue(code.startsWith(prefix));
        
        // 后面应该是日期时间（14位）+ 6位随机数
        assertEquals(prefix.length() + 20, code.length());
        
        // 检查日期前缀
        String dateTimePrefix = code.substring(prefix.length(), prefix.length() + 14);
        assertTrue(Pattern.matches("\\d{14}", dateTimePrefix));
        
        // 检查随机部分
        String randomPart = code.substring(prefix.length() + 14);
        assertTrue(Pattern.matches("\\d{6}", randomPart));
    }

    @Test
    void randomNumeric() {
        // 测试生成随机数字
        int length = 10;
        String numeric = IdGenerator.randomNumeric(length);
        assertNotNull(numeric);
        assertEquals(length, numeric.length());
        // 应该仅包含数字
        assertTrue(Pattern.matches("\\d{" + length + "}", numeric));
        
        // 测试不同长度
        assertEquals(5, IdGenerator.randomNumeric(5).length());
        assertEquals(15, IdGenerator.randomNumeric(15).length());
        
        // 测试长度为0或负数
        assertEquals("", IdGenerator.randomNumeric(0));
        assertEquals("", IdGenerator.randomNumeric(-1));
    }

    @Test
    void randomAlphabetic() {
        // 测试生成随机字母
        int length = 10;
        String alphabetic = IdGenerator.randomAlphabetic(length);
        assertNotNull(alphabetic);
        assertEquals(length, alphabetic.length());
        // 应该仅包含字母
        assertTrue(Pattern.matches("[a-zA-Z]{" + length + "}", alphabetic));
        
        // 测试不同长度
        assertEquals(5, IdGenerator.randomAlphabetic(5).length());
        assertEquals(15, IdGenerator.randomAlphabetic(15).length());
        
        // 测试长度为0或负数
        assertEquals("", IdGenerator.randomAlphabetic(0));
        assertEquals("", IdGenerator.randomAlphabetic(-1));
    }

    @Test
    void randomAlphanumeric() {
        // 测试生成随机字母数字
        int length = 10;
        String alphanumeric = IdGenerator.randomAlphanumeric(length);
        assertNotNull(alphanumeric);
        assertEquals(length, alphanumeric.length());
        // 应该仅包含字母和数字
        assertTrue(Pattern.matches("[a-zA-Z0-9]{" + length + "}", alphanumeric));
        
        // 测试不同长度
        assertEquals(5, IdGenerator.randomAlphanumeric(5).length());
        assertEquals(15, IdGenerator.randomAlphanumeric(15).length());
        
        // 测试长度为0或负数
        assertEquals("", IdGenerator.randomAlphanumeric(0));
        assertEquals("", IdGenerator.randomAlphanumeric(-1));
    }

    @Test
    void random() {
        // 测试生成自定义字符集的随机字符串
        int length = 10;
        String chars = "ABCDE12345";
        String random = IdGenerator.random(length, chars);
        assertNotNull(random);
        assertEquals(length, random.length());
        
        // 字符串应只包含指定字符集中的字符
        for (char c : random.toCharArray()) {
            assertTrue(chars.indexOf(c) >= 0);
        }
        
        // 测试空字符集
        assertEquals("", IdGenerator.random(10, ""));
        assertEquals("", IdGenerator.random(10, null));
        
        // 测试长度为0或负数
        assertEquals("", IdGenerator.random(0, chars));
        assertEquals("", IdGenerator.random(-1, chars));
    }
} 