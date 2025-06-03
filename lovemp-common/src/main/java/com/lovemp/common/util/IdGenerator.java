package com.lovemp.common.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * ID生成工具类
 * 
 * 提供多种ID生成策略，满足不同业务场景下的唯一标识需求，支持UUID、雪花算法、业务编码等多种生成方式。
 * 
 * 主要功能包括：
 * 1. UUID生成：提供标准UUID和无连字符UUID生成
 * 2. 雪花算法ID：基于时间戳的分布式ID生成算法实现
 * 3. 业务编号：生成订单号、流水号等带时间戳的业务编号
 * 4. 随机字符串：生成指定长度的随机数字、字母或混合字符串
 * 5. 前缀编码：支持生成带自定义前缀的唯一编码
 * 
 * 适用场景：
 * - 数据库主键生成
 * - 分布式系统的全局唯一ID
 * - 订单、流水等业务编号生成
 * - 文件名、临时标识符生成
 * - 随机密码或验证码生成
 * - 分布式锁的key生成
 * 
 * 使用示例：
 * // 生成UUID
 * String id = IdGenerator.uuid();
 * 
 * // 生成雪花算法ID
 * long snowflakeId = IdGenerator.snowflakeId();
 * 
 * // 生成订单号
 * String orderNo = IdGenerator.generateOrderNo(); // 例如：20240520153021123456
 * 
 * // 生成带前缀的业务编码
 * String productCode = IdGenerator.generateCodeWithPrefix("P"); // 例如：P20240520153021123456
 * 
 * // 生成6位随机数字验证码
 * String verifyCode = IdGenerator.randomNumeric(6);
 * 
 * 注意：
 * 1. 简化版雪花算法不适用于高并发分布式环境，生产环境应考虑专用ID生成服务
 * 2. UUID较长，存储和索引效率较低，但使用简便
 * 3. 业务编号生成方法在极端并发情况下可能存在重复风险
 * 4. 随机字符串方法使用ThreadLocalRandom提高多线程性能
 */
public final class IdGenerator {

    private IdGenerator() {
        // 工具类不允许实例化
    }

    private static final AtomicInteger SEQUENCE = new AtomicInteger(0);
    private static final int MAX_SEQUENCE = 999999;
    private static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");

    /**
     * 生成32位UUID（无连字符）
     *
     * @return 32位UUID
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成36位带连字符的UUID
     *
     * @return 36位UUID
     */
    public static String uuid36() {
        return UUID.randomUUID().toString();
    }

    /**
     * 雪花算法ID（基础实现，非严格分布式）
     * 注意：这里只是一个简化实现，生产环境中应该使用专业的ID生成服务
     *
     * @return 雪花算法生成的ID
     */
    public static synchronized long snowflakeId() {
        // 起始时间戳：2024-01-01 00:00:00
        long epochMillis = 1704038400000L;
        long currentTimeMillis = System.currentTimeMillis();
        long timestamp = currentTimeMillis - epochMillis;

        // 获取自增序列（这个简化实现仅用于示例，不适用于高并发分布式环境）
        int sequence = SEQUENCE.incrementAndGet();
        if (sequence > MAX_SEQUENCE) {
            SEQUENCE.set(0);
            sequence = 0;
        }

        // 机器ID和数据中心ID（示例中固定为1）
        long machineId = 1L;
        long dataCenterId = 1L;

        // 组装雪花ID：
        // 符号位(1bit) + 时间戳(41bit) + 数据中心ID(5bit) + 机器ID(5bit) + 序列号(12bit)
        return ((timestamp << 22) | (dataCenterId << 17) | (machineId << 12) | sequence);
    }

    /**
     * 生成唯一订单号
     * 格式：年月日时分秒 + 6位随机数
     *
     * @return 订单号
     */
    public static String generateOrderNo() {
        LocalDateTime now = LocalDateTime.now();
        String timeStr = now.format(DATE_TIME_FORMATTER);
        String randomStr = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        return timeStr + randomStr;
    }

    /**
     * 生成带前缀的唯一编码
     * 格式：前缀 + 年月日时分秒 + 6位随机数
     *
     * @param prefix 前缀
     * @return 唯一编码
     */
    public static String generateCodeWithPrefix(String prefix) {
        if (prefix == null) {
            prefix = "";
        }
        
        LocalDateTime now = LocalDateTime.now();
        String timeStr = now.format(DATE_TIME_FORMATTER);
        String randomStr = String.format("%06d", ThreadLocalRandom.current().nextInt(1000000));
        
        return prefix + timeStr + randomStr;
    }

    /**
     * 生成随机数字字符串
     *
     * @param length 长度
     * @return 随机数字字符串
     */
    public static String randomNumeric(int length) {
        if (length <= 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            sb.append(ThreadLocalRandom.current().nextInt(10));
        }
        
        return sb.toString();
    }

    /**
     * 生成随机字母字符串
     *
     * @param length 长度
     * @return 随机字母字符串
     */
    public static String randomAlphabetic(int length) {
        if (length <= 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder(length);
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }

    /**
     * 生成随机字母数字字符串
     *
     * @param length 长度
     * @return 随机字母数字字符串
     */
    public static String randomAlphanumeric(int length) {
        if (length <= 0) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder(length);
        String chars = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }

    /**
     * 生成指定长度的随机字符串，可指定字符集
     *
     * @param length 长度
     * @param chars 字符集
     * @return 随机字符串
     */
    public static String random(int length, String chars) {
        if (length <= 0 || StringUtils.isEmpty(chars)) {
            return "";
        }
        
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int index = ThreadLocalRandom.current().nextInt(chars.length());
            sb.append(chars.charAt(index));
        }
        
        return sb.toString();
    }
} 