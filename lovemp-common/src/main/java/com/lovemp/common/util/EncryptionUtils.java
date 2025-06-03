package com.lovemp.common.util;

import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * 加密工具类
 * 
 * <p>提供常用的加密、哈希计算、编码转换和安全密钥生成等功能，用于保护敏感数据和验证数据完整性。</p>
 * 
 * <h2>功能分类</h2>
 * <ol>
 *   <li>哈希计算：MD5、SHA-256、SHA-512等不可逆哈希算法</li>
 *   <li>对称加密：AES-CBC加密与解密</li>
 *   <li>编码转换：Base64编码解码、十六进制转换</li>
 *   <li>密码安全：PBKDF2密码加密、盐值生成</li>
 *   <li>消息认证：HMAC-SHA256</li>
 *   <li>安全随机：密钥生成、IV向量生成</li>
 * </ol>
 * 
 * <h2>适用场景</h2>
 * <ol>
 *   <li>用户密码加密存储与校验</li>
 *   <li>敏感数据（如个人信息、支付信息）的加密传输和存储</li>
 *   <li>数据完整性校验和防篡改保护</li>
 *   <li>API请求签名验证</li>
 *   <li>令牌生成与验证</li>
 *   <li>数据编码格式转换</li>
 * </ol>
 * 
 * <h2>代码示例</h2>
 * <pre>
 * // 哈希计算示例
 * String passwordHash = EncryptionUtils.sha256("myPassword");
 * 
 * // 密码安全存储示例
 * String salt = EncryptionUtils.generateSalt(16);
 * String securePasswordHash = EncryptionUtils.pbkdf2Encrypt("myPassword", salt, 10000, 256);
 * 
 * // AES加密解密示例
 * String key = EncryptionUtils.generateAESKey(256);
 * String iv = EncryptionUtils.generateAESIV();
 * String encrypted = EncryptionUtils.aesEncrypt("敏感数据", key, iv);
 * String decrypted = EncryptionUtils.aesDecrypt(encrypted, key, iv);
 * 
 * // API签名示例
 * String signature = EncryptionUtils.hmacSha256(requestData, secretKey);
 * </pre>
 * 
 * <h2>使用注意事项</h2>
 * <ol>
 *   <li>对于密码存储，应使用PBKDF2等慢哈希算法而非简单MD5/SHA，并务必使用随机盐值</li>
 *   <li>对称加密(AES)的密钥和IV务必安全保存，且不可重复使用相同IV</li>
 *   <li>MD5和SHA系列算法仅适用于数据完整性校验，不应用于敏感数据保护</li>
 *   <li>密钥生成和加密解密可能会出现性能开销，避免在高频调用场景中使用</li>
 *   <li>所有方法对null输入均返回null，使用前应检查输入</li>
 *   <li>加密相关服务推荐使用领域层服务实现，而非直接调用工具类</li>
 *   <li>特定安全合规场景(等保、金融等)应考虑使用符合标准的加密算法和密钥长度</li>
 * </ol>
 */
public final class EncryptionUtils {

    private EncryptionUtils() {
        // 工具类不允许实例化
    }

    /**
     * MD5加密
     *
     * @param text 待加密文本
     * @return MD5加密后的十六进制字符串
     */
    public static String md5(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(text.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }

    /**
     * SHA-256加密
     *
     * @param text 待加密文本
     * @return SHA-256加密后的十六进制字符串
     */
    public static String sha256(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(text.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256加密失败", e);
        }
    }

    /**
     * SHA-512加密
     *
     * @param text 待加密文本
     * @return SHA-512加密后的十六进制字符串
     */
    public static String sha512(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-512");
            byte[] digest = md.digest(text.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-512加密失败", e);
        }
    }

    /**
     * Base64编码
     *
     * @param data 待编码字节数组
     * @return Base64编码后的字符串
     */
    public static String base64Encode(byte[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        
        return Base64.getEncoder().encodeToString(data);
    }

    /**
     * Base64编码
     *
     * @param text 待编码文本
     * @return Base64编码后的字符串
     */
    public static String base64Encode(String text) {
        if (StringUtils.isEmpty(text)) {
            return null;
        }
        
        return Base64.getEncoder().encodeToString(text.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Base64解码为字节数组
     *
     * @param base64 Base64编码的字符串
     * @return 解码后的字节数组
     */
    public static byte[] base64Decode(String base64) {
        if (StringUtils.isEmpty(base64)) {
            return null;
        }
        
        return Base64.getDecoder().decode(base64);
    }

    /**
     * Base64解码为字符串
     *
     * @param base64 Base64编码的字符串
     * @return 解码后的字符串
     */
    public static String base64DecodeToString(String base64) {
        if (StringUtils.isEmpty(base64)) {
            return null;
        }
        
        byte[] bytes = Base64.getDecoder().decode(base64);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    /**
     * HMAC-SHA256 加密
     *
     * @param text 待加密文本
     * @param key  密钥
     * @return HMAC-SHA256 加密后的十六进制字符串
     */
    public static String hmacSha256(String text, String key) {
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(key)) {
            return null;
        }
        
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(secretKeySpec);
            byte[] digest = mac.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return bytesToHex(digest);
        } catch (Exception e) {
            throw new RuntimeException("HMAC-SHA256加密失败", e);
        }
    }

    /**
     * AES-CBC加密
     *
     * @param text 待加密文本
     * @param key  密钥（长度必须是16、24或32字节）
     * @param iv   初始化向量（长度必须是16字节）
     * @return Base64编码的加密结果
     */
    public static String aesEncrypt(String text, String key, String iv) {
        if (StringUtils.isEmpty(text) || StringUtils.isEmpty(key) || StringUtils.isEmpty(iv)) {
            return null;
        }
        
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            byte[] encrypted = cipher.doFinal(text.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(encrypted);
        } catch (Exception e) {
            throw new RuntimeException("AES加密失败", e);
        }
    }

    /**
     * AES-CBC解密
     *
     * @param encryptedText Base64编码的加密文本
     * @param key           密钥（长度必须是16、24或32字节）
     * @param iv            初始化向量（长度必须是16字节）
     * @return 解密后的文本
     */
    public static String aesDecrypt(String encryptedText, String key, String iv) {
        if (StringUtils.isEmpty(encryptedText) || StringUtils.isEmpty(key) || StringUtils.isEmpty(iv)) {
            return null;
        }
        
        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(StandardCharsets.UTF_8), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(StandardCharsets.UTF_8));
            
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);
            byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
            return new String(decrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            throw new RuntimeException("AES解密失败", e);
        }
    }

    /**
     * 生成随机盐值
     *
     * @param length 盐值长度
     * @return 随机盐值的十六进制字符串
     */
    public static String generateSalt(int length) {
        if (length <= 0) {
            return "";
        }
        
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[length];
        random.nextBytes(salt);
        
        return bytesToHex(salt);
    }

    /**
     * PBKDF2加密（用于密码存储）
     *
     * @param password 密码
     * @param salt     盐值
     * @param iterations 迭代次数
     * @param keyLength 生成的密钥长度（位）
     * @return 加密后的十六进制字符串
     */
    public static String pbkdf2Encrypt(String password, String salt, int iterations, int keyLength) {
        if (StringUtils.isEmpty(password) || StringUtils.isEmpty(salt)) {
            return null;
        }
        
        try {
            PBEKeySpec spec = new PBEKeySpec(
                    password.toCharArray(),
                    salt.getBytes(StandardCharsets.UTF_8),
                    iterations,
                    keyLength
            );
            
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            SecretKey key = factory.generateSecret(spec);
            byte[] hash = key.getEncoded();
            
            return bytesToHex(hash);
        } catch (Exception e) {
            throw new RuntimeException("PBKDF2加密失败", e);
        }
    }

    /**
     * 生成随机AES密钥
     *
     * @param keySize 密钥大小（128、192或256）
     * @return 密钥字符串
     */
    public static String generateAESKey(int keySize) {
        if (keySize != 128 && keySize != 192 && keySize != 256) {
            throw new IllegalArgumentException("密钥大小必须是128、192或256位");
        }
        
        try {
            SecureRandom random = new SecureRandom();
            byte[] key = new byte[keySize / 8];
            random.nextBytes(key);
            
            return new String(key, StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            throw new RuntimeException("生成AES密钥失败", e);
        }
    }

    /**
     * 生成随机AES初始化向量
     *
     * @return 初始化向量字符串
     */
    public static String generateAESIV() {
        try {
            SecureRandom random = new SecureRandom();
            byte[] iv = new byte[16]; // AES块大小为128位（16字节）
            random.nextBytes(iv);
            
            return new String(iv, StandardCharsets.ISO_8859_1);
        } catch (Exception e) {
            throw new RuntimeException("生成AES初始化向量失败", e);
        }
    }

    /**
     * 字节数组转十六进制字符串
     *
     * @param bytes 字节数组
     * @return 十六进制字符串
     */
    public static String bytesToHex(byte[] bytes) {
        if (bytes == null) {
            return null;
        }
        
        if (bytes.length == 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder(bytes.length * 2);
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        
        return sb.toString();
    }

    /**
     * 十六进制字符串转字节数组
     *
     * @param hex 十六进制字符串
     * @return 字节数组
     */
    public static byte[] hexToBytes(String hex) {
        if (hex == null) {
            return null;
        }
        
        if (hex.isEmpty()) {
            return new byte[0];
        }
        
        int len = hex.length();
        
        // 检查字符串长度必须是偶数
        if (len % 2 != 0) {
            throw new IllegalArgumentException("十六进制字符串长度必须是偶数");
        }
        
        byte[] data = new byte[len / 2];
        
        try {
            for (int i = 0; i < len; i += 2) {
                int high = Character.digit(hex.charAt(i), 16);
                int low = Character.digit(hex.charAt(i + 1), 16);
                
                // 检查是否为有效的十六进制字符
                if (high == -1 || low == -1) {
                    throw new IllegalArgumentException("包含非十六进制字符");
                }
                
                data[i / 2] = (byte) ((high << 4) + low);
            }
            return data;
        } catch (StringIndexOutOfBoundsException e) {
            throw new IllegalArgumentException("十六进制字符串格式错误", e);
        }
    }
} 