package com.lovemp.common.util;

import java.util.UUID;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 * 
 * 提供字符串相关的常用操作方法，帮助简化字符串处理的代码，提高开发效率。
 * 
 * 主要功能包括：
 * 1. 字符串判空相关：isEmpty/isNotEmpty/isBlank/isNotBlank等
 * 2. 字符串格式验证：判断是否为合法邮箱、手机号格式
 * 3. 字符串处理与转换：截取字符串、首字母大小写转换
 * 4. 命名风格转换：驼峰命名与下划线命名的互相转换
 * 5. UUID生成：提供生成带连字符和不带连字符的UUID方法
 * 
 * 适用场景：
 * - 需要进行字符串空值检查时
 * - 需要验证字符串格式(如邮箱、手机号)时
 * - 需要进行命名风格转换时
 * - 需要生成唯一标识符时
 * 
 * 使用示例：
 * if (StringUtils.isNotBlank(name)) {
 *     // 处理非空字符串
 * }
 * 
 * String camelName = StringUtils.underscoreToCamel("user_name"); // 返回"userName"
 */
public final class StringUtils {

    private static final Pattern EMAIL_PATTERN = Pattern.compile("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$");
    private static final Pattern PHONE_PATTERN = Pattern.compile("^1[3-9]\\d{9}$");

    private StringUtils() {
        // 工具类不允许实例化
    }

    /**
     * 判断字符串是否为空
     *
     * @param str 待检查字符串
     * @return 为空返回true，否则返回false
     */
    public static boolean isEmpty(String str) {
        return str == null || str.isEmpty();
    }

    /**
     * 判断字符串是否不为空
     *
     * @param str 待检查字符串
     * @return 不为空返回true，否则返回false
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 判断字符串是否为空白
     *
     * @param str 待检查字符串
     * @return 为空白返回true，否则返回false
     */
    public static boolean isBlank(String str) {
        if (isEmpty(str)) {
            return true;
        }
        return str.trim().isEmpty();
    }

    /**
     * 判断字符串是否不为空白
     *
     * @param str 待检查字符串
     * @return 不为空白返回true，否则返回false
     */
    public static boolean isNotBlank(String str) {
        return !isBlank(str);
    }

    /**
     * 截取字符串
     *
     * @param str 源字符串
     * @param maxLength 最大长度
     * @return 截取后的字符串
     */
    public static String truncate(String str, int maxLength) {
        if (isEmpty(str) || maxLength <= 0) {
            return isEmpty(str) ? str : "";
        }
        return str.length() <= maxLength ? str : str.substring(0, maxLength);
    }

    /**
     * 生成UUID（无连字符）
     *
     * @return UUID字符串
     */
    public static String uuid() {
        return UUID.randomUUID().toString().replace("-", "");
    }

    /**
     * 生成带连字符的UUID
     * 
     * @return 带连字符的UUID字符串
     */
    public static String uuidWithHyphens() {
        return UUID.randomUUID().toString();
    }

    /**
     * 检查是否为有效的电子邮件格式
     *
     * @param email 电子邮件地址
     * @return 有效返回true，否则返回false
     */
    public static boolean isValidEmail(String email) {
        if (isEmpty(email)) {
            return false;
        }
        return EMAIL_PATTERN.matcher(email).matches();
    }

    /**
     * 检查是否为有效的手机号码（中国大陆）
     *
     * @param phoneNumber 手机号码
     * @return 有效返回true，否则返回false
     */
    public static boolean isValidMobilePhone(String phoneNumber) {
        if (isEmpty(phoneNumber)) {
            return false;
        }
        return PHONE_PATTERN.matcher(phoneNumber).matches();
    }

    /**
     * 首字母转大写
     *
     * @param str 源字符串
     * @return 首字母大写的字符串
     */
    public static String capitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toUpperCase();
        }
        return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 首字母转小写
     *
     * @param str 源字符串
     * @return 首字母小写的字符串
     */
    public static String uncapitalize(String str) {
        if (isEmpty(str)) {
            return str;
        }
        if (str.length() == 1) {
            return str.toLowerCase();
        }
        return Character.toLowerCase(str.charAt(0)) + str.substring(1);
    }

    /**
     * 驼峰命名转下划线命名
     *
     * @param str 驼峰命名字符串
     * @return 下划线命名字符串
     */
    public static String camelToUnderscore(String str) {
        if (isEmpty(str)) {
            return str;
        }
        return str.replaceAll("([a-z])([A-Z])", "$1_$2").toLowerCase();
    }

    /**
     * 下划线命名转驼峰命名
     *
     * @param str 下划线命名字符串
     * @return 驼峰命名字符串
     */
    public static String underscoreToCamel(String str) {
        if (isEmpty(str)) {
            return str;
        }
        StringBuilder result = new StringBuilder();
        boolean nextUpper = false;
        
        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c == '_') {
                nextUpper = true;
            } else {
                if (nextUpper) {
                    result.append(Character.toUpperCase(c));
                    nextUpper = false;
                } else {
                    result.append(c);
                }
            }
        }
        
        return result.toString();
    }
} 