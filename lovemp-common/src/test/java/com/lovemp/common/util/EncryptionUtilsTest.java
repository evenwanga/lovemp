package com.lovemp.common.util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * EncryptionUtils工具类单元测试
 * 
 * 该测试类用于验证EncryptionUtils工具类中各种加密算法的正确性,包括:
 * - MD5加密: 验证32位MD5哈希值的生成
 * - SHA-256加密: 验证64位SHA-256哈希值的生成
 * - AES加密/解密: 验证AES对称加密算法的加解密功能
 * - Base64编码/解码: 验证Base64编码和解码功能
 * 
 * 每个测试用例都会验证:
 * 1. 正常情况 - 方法的基本加密/解密功能
 * 2. 边界情况 - 特殊字符、中文字符等
 * 3. 异常情况 - null值、空字符串等
 * 4. 一致性要求 - 相同输入产生相同输出
 * 
 * @see com.lovemp.common.util.EncryptionUtils
 */
public class EncryptionUtilsTest {
    
    private static final String TEST_TEXT = "这是测试文本123ABC!@#";
    private static final String TEST_KEY = "0123456789abcdef";
    private static final String TEST_IV = "fedcba9876543210";
    
    /**
     * 测试MD5加密
     */
    @Test
    public void testMd5() {
        // 准备测试数据
        String input = "hello";
        
        // 执行MD5加密
        String md5 = EncryptionUtils.md5(input);
        
        // 验证结果 - MD5("hello") = 5d41402abc4b2a76b9719d911017c592
        assertNotNull(md5);
        assertEquals(32, md5.length());
        assertEquals("5d41402abc4b2a76b9719d911017c592", md5);
        
        // 验证空值处理
        assertNull(EncryptionUtils.md5(null));
        assertNull(EncryptionUtils.md5(""));
        
        // 验证相同输入产生相同输出
        assertEquals(md5, EncryptionUtils.md5(input));
    }
    
    /**
     * 测试SHA-256加密
     */
    @Test
    public void testSha256() {
        // 准备测试数据
        String input = "hello";
        
        // 执行SHA-256加密
        String sha256 = EncryptionUtils.sha256(input);
        
        // 验证结果 - SHA-256("hello") = 2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824
        assertNotNull(sha256);
        assertEquals(64, sha256.length());
        assertEquals("2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824", sha256);
        
        // 验证空值处理
        assertNull(EncryptionUtils.sha256(null));
        assertNull(EncryptionUtils.sha256(""));
        
        // 验证相同输入产生相同输出
        assertEquals(sha256, EncryptionUtils.sha256(input));
    }
    
    /**
     * 测试SHA-512加密
     */
    @Test
    public void testSha512() {
        // 执行SHA-512加密
        String sha512 = EncryptionUtils.sha512(TEST_TEXT);
        
        // 验证结果
        assertNotNull(sha512);
        assertEquals(128, sha512.length());
        
        // 验证空值处理
        assertNull(EncryptionUtils.sha512(null));
        assertNull(EncryptionUtils.sha512(""));
    }
    
    /**
     * 测试Base64编码（字节数组）
     */
    @Test
    public void testBase64EncodeByte() {
        // 准备测试数据
        byte[] data = TEST_TEXT.getBytes(StandardCharsets.UTF_8);
        
        // 执行Base64编码
        String encoded = EncryptionUtils.base64Encode(data);
        
        // 验证结果
        assertNotNull(encoded);
        // 验证与标准Java实现一致
        assertEquals(Base64.getEncoder().encodeToString(data), encoded);
        
        // 验证空值处理
        assertNull(EncryptionUtils.base64Encode((byte[])new byte[0]));
        assertNull(EncryptionUtils.base64Encode((byte[])null));
    }
    
    /**
     * 测试Base64编码（字符串）
     */
    @Test
    public void testBase64EncodeString() {
        // 执行Base64编码
        String encoded = EncryptionUtils.base64Encode(TEST_TEXT);
        
        // 验证结果
        assertNotNull(encoded);
        assertEquals(Base64.getEncoder().encodeToString(TEST_TEXT.getBytes(StandardCharsets.UTF_8)), encoded);
        
        // 验证空值处理
        assertNull(EncryptionUtils.base64Encode((String)""));
        assertNull(EncryptionUtils.base64Encode((String)null));
    }
    
    /**
     * 测试Base64解码（字节数组）
     */
    @Test
    public void testBase64Decode() {
        // 准备测试数据
        String base64 = Base64.getEncoder().encodeToString(TEST_TEXT.getBytes(StandardCharsets.UTF_8));
        
        // 执行Base64解码
        byte[] decoded = EncryptionUtils.base64Decode(base64);
        
        // 验证结果
        assertNotNull(decoded);
        assertArrayEquals(TEST_TEXT.getBytes(StandardCharsets.UTF_8), decoded);
        
        // 验证空值处理
        assertNull(EncryptionUtils.base64Decode(""));
        assertNull(EncryptionUtils.base64Decode(null));
    }
    
    /**
     * 测试Base64解码（字符串）
     */
    @Test
    public void testBase64DecodeToString() {
        // 准备测试数据
        String base64 = Base64.getEncoder().encodeToString(TEST_TEXT.getBytes(StandardCharsets.UTF_8));
        
        // 执行Base64解码
        String decoded = EncryptionUtils.base64DecodeToString(base64);
        
        // 验证结果
        assertNotNull(decoded);
        assertEquals(TEST_TEXT, decoded);
        
        // 验证空值处理
        assertNull(EncryptionUtils.base64DecodeToString(""));
        assertNull(EncryptionUtils.base64DecodeToString(null));
    }
    
    /**
     * 测试HMAC-SHA256加密
     */
    @Test
    public void testHmacSha256() {
        // 执行HMAC-SHA256加密
        String hmac = EncryptionUtils.hmacSha256(TEST_TEXT, TEST_KEY);
        
        // 验证结果
        assertNotNull(hmac);
        assertEquals(64, hmac.length());
        
        // 验证相同输入产生相同输出
        assertEquals(hmac, EncryptionUtils.hmacSha256(TEST_TEXT, TEST_KEY));
        
        // 验证不同输入产生不同输出
        String hmac2 = EncryptionUtils.hmacSha256(TEST_TEXT + "不同", TEST_KEY);
        assertNotEquals(hmac, hmac2);
        
        String hmac3 = EncryptionUtils.hmacSha256(TEST_TEXT, TEST_KEY + "不同");
        assertNotEquals(hmac, hmac3);
        
        // 验证空值处理
        assertNull(EncryptionUtils.hmacSha256(null, TEST_KEY));
        assertNull(EncryptionUtils.hmacSha256(TEST_TEXT, null));
        assertNull(EncryptionUtils.hmacSha256("", TEST_KEY));
        assertNull(EncryptionUtils.hmacSha256(TEST_TEXT, ""));
    }
    
    /**
     * 测试AES加密和解密
     */
    @Test
    public void testAesEncryptAndDecrypt() {
        // 执行AES加密
        String encrypted = EncryptionUtils.aesEncrypt(TEST_TEXT, TEST_KEY, TEST_IV);
        
        // 验证加密结果
        assertNotNull(encrypted);
        
        // 执行AES解密
        String decrypted = EncryptionUtils.aesDecrypt(encrypted, TEST_KEY, TEST_IV);
        
        // 验证原文与解密结果一致
        assertEquals(TEST_TEXT, decrypted);
        
        // 验证不同密钥和IV解密失败
        try {
            EncryptionUtils.aesDecrypt(encrypted, TEST_KEY + "不同", TEST_IV);
            fail("应该抛出异常，因为密钥不正确");
        } catch (RuntimeException e) {
            // 预期的异常
        }
        
        try {
            EncryptionUtils.aesDecrypt(encrypted, TEST_KEY, TEST_IV + "不同");
            fail("应该抛出异常，因为IV不正确");
        } catch (RuntimeException e) {
            // 预期的异常
        }
        
        // 验证空值处理
        assertNull(EncryptionUtils.aesEncrypt(null, TEST_KEY, TEST_IV));
        assertNull(EncryptionUtils.aesEncrypt(TEST_TEXT, null, TEST_IV));
        assertNull(EncryptionUtils.aesEncrypt(TEST_TEXT, TEST_KEY, null));
        assertNull(EncryptionUtils.aesDecrypt(null, TEST_KEY, TEST_IV));
        assertNull(EncryptionUtils.aesDecrypt(encrypted, null, TEST_IV));
        assertNull(EncryptionUtils.aesDecrypt(encrypted, TEST_KEY, null));
    }
    
    /**
     * 测试生成随机盐值
     */
    @Test
    public void testGenerateSalt() {
        // 生成不同长度的盐值
        String salt8 = EncryptionUtils.generateSalt(8);
        String salt16 = EncryptionUtils.generateSalt(16);
        String salt32 = EncryptionUtils.generateSalt(32);
        
        // 验证盐值长度是否符合预期（十六进制表示，每个字节需要2个字符）
        assertEquals(16, salt8.length());  // 8字节 = 16个十六进制字符
        assertEquals(32, salt16.length()); // 16字节 = 32个十六进制字符
        assertEquals(64, salt32.length()); // 32字节 = 64个十六进制字符
        
        // 验证多次生成的盐值不同
        String anotherSalt16 = EncryptionUtils.generateSalt(16);
        assertNotEquals(salt16, anotherSalt16);
        
        // 验证非法长度参数
        assertEquals("", EncryptionUtils.generateSalt(0));
        assertEquals("", EncryptionUtils.generateSalt(-1));
    }
    
    /**
     * 测试PBKDF2加密
     */
    @Test
    public void testPbkdf2Encrypt() {
        // 准备测试数据
        String password = "password123";
        String salt = EncryptionUtils.generateSalt(16);
        int iterations = 10000;
        int keyLength = 256;
        
        // 执行PBKDF2加密
        String encrypted = EncryptionUtils.pbkdf2Encrypt(password, salt, iterations, keyLength);
        
        // 验证结果
        assertNotNull(encrypted);
        
        // 验证相同输入产生相同输出
        assertEquals(encrypted, EncryptionUtils.pbkdf2Encrypt(password, salt, iterations, keyLength));
        
        // 验证不同输入产生不同输出
        String encrypted2 = EncryptionUtils.pbkdf2Encrypt(password + "不同", salt, iterations, keyLength);
        assertNotEquals(encrypted, encrypted2);
        
        String encrypted3 = EncryptionUtils.pbkdf2Encrypt(password, EncryptionUtils.generateSalt(16), iterations, keyLength);
        assertNotEquals(encrypted, encrypted3);
        
        // 验证空值处理
        assertNull(EncryptionUtils.pbkdf2Encrypt(null, salt, iterations, keyLength));
        assertNull(EncryptionUtils.pbkdf2Encrypt(password, null, iterations, keyLength));
        assertNull(EncryptionUtils.pbkdf2Encrypt("", salt, iterations, keyLength));
        assertNull(EncryptionUtils.pbkdf2Encrypt(password, "", iterations, keyLength));
    }
    
    /**
     * 测试生成AES密钥
     */
    @Test
    public void testGenerateAESKey() {
        // 生成不同长度的AES密钥
        String key128 = EncryptionUtils.generateAESKey(128);
        String key192 = EncryptionUtils.generateAESKey(192);
        String key256 = EncryptionUtils.generateAESKey(256);
        
        // 验证密钥长度是否符合预期
        assertEquals(16, key128.length()); // 128位 = 16字节 = 16个字符
        assertEquals(24, key192.length()); // 192位 = 24字节 = 24个字符
        assertEquals(32, key256.length()); // 256位 = 32字节 = 32个字符
        
        // 验证多次生成的密钥不同
        String anotherKey128 = EncryptionUtils.generateAESKey(128);
        assertNotEquals(key128, anotherKey128);
        
        // 验证非法密钥长度参数
        try {
            EncryptionUtils.generateAESKey(123);
            fail("应该抛出异常，因为密钥长度不合法");
        } catch (IllegalArgumentException e) {
            // 预期的异常
        }
    }
    
    /**
     * 测试生成AES初始化向量
     */
    @Test
    public void testGenerateAESIV() {
        // 生成AES初始化向量
        String iv = EncryptionUtils.generateAESIV();
        
        // 验证IV长度是否符合预期
        assertEquals(16, iv.length()); // AES块大小为128位 = 16字节 = 16个字符
        
        // 验证多次生成的IV不同
        String anotherIV = EncryptionUtils.generateAESIV();
        assertNotEquals(iv, anotherIV);
    }
    
    /**
     * 测试字节数组与十六进制字符串的相互转换
     */
    @Test
    public void testBytesHexConversion() {
        // 准备测试数据
        byte[] bytes = {(byte) 0xAB, (byte) 0xCD, (byte) 0xEF, (byte) 0x12, (byte) 0x34, (byte) 0x56};
        String hex = "abcdef123456";
        
        // 测试字节数组转十六进制
        String convertedHex = EncryptionUtils.bytesToHex(bytes);
        assertEquals(hex, convertedHex.toLowerCase());
        
        // 测试十六进制转字节数组
        byte[] convertedBytes = EncryptionUtils.hexToBytes(hex);
        assertArrayEquals(bytes, convertedBytes);
        
        // 验证空值处理
        assertNull(EncryptionUtils.bytesToHex(null));
        assertEquals("", EncryptionUtils.bytesToHex(new byte[0]));
        assertNull(EncryptionUtils.hexToBytes(null));
        assertArrayEquals(new byte[0], EncryptionUtils.hexToBytes(""));
        
        // 验证非法十六进制字符串
        try {
            EncryptionUtils.hexToBytes("abcdefg"); // 长度为奇数
            fail("应该抛出异常，因为十六进制字符串长度必须是偶数");
        } catch (IllegalArgumentException e) {
            // 预期的异常
        }
        
        try {
            EncryptionUtils.hexToBytes("abcdxyz"); // 包含非十六进制字符
            fail("应该抛出异常，因为包含非十六进制字符");
        } catch (IllegalArgumentException e) {
            // 预期的异常
        }
    }
} 