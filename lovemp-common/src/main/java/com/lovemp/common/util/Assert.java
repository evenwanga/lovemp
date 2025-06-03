package com.lovemp.common.util;

import com.lovemp.common.exception.DomainRuleViolationException;

import java.util.Collection;
import java.util.Map;
import java.util.function.Supplier;

/**
 * 断言工具类
 * 
 * 提供一组断言方法，用于验证领域规则、参数合法性和程序状态，当条件不满足时抛出领域规则违反异常。
 * 有助于及早发现问题，提高代码健壮性和可维护性。
 * 
 * 主要功能包括：
 * 1. 条件断言：验证布尔表达式为真或为假
 * 2. 空值断言：验证对象、字符串、集合或Map不为空
 * 3. 类型断言：验证对象是否为特定类型的实例
 * 4. 状态断言：验证对象或系统状态是否符合预期
 * 
 * 适用场景：
 * - 领域驱动设计(DDD)中验证领域规则
 * - 方法入口参数校验
 * - 业务逻辑处理前的前置条件检查
 * - 系统状态验证
 * - 事务提交前的数据完整性验证
 * 
 * 使用示例：
 * // 参数校验
 * Assert.notNull(userId, "用户ID不能为空");
 * 
 * // 业务规则校验
 * Assert.isTrue(order.canCancel(), "订单当前状态不允许取消");
 * 
 * // 状态检查
 * Assert.state(account.getBalance().compareTo(amount) >= 0, "账户余额不足");
 * 
 * 注意：
 * 1. 所有断言方法在条件不满足时都会抛出DomainRuleViolationException
 * 2. 异常中包含提供的错误消息，便于定位问题
 * 3. 可以使用Supplier延迟构建错误消息，提高性能
 * 4. 断言失败通常表示程序逻辑错误或违反业务规则，不应被捕获处理
 */
public final class Assert {

    private Assert() {
        // 工具类不允许实例化
    }

    /**
     * 断言表达式为真
     *
     * @param expression 待验证表达式
     * @param message 错误消息
     */
    public static void isTrue(boolean expression, String message) {
        if (!expression) {
            throw new DomainRuleViolationException(message);
        }
    }

    /**
     * 断言表达式为假
     *
     * @param expression 待验证表达式
     * @param message 错误消息
     */
    public static void isFalse(boolean expression, String message) {
        if (expression) {
            throw new DomainRuleViolationException(message);
        }
    }

    /**
     * 断言对象不为null
     *
     * @param object 待验证对象
     * @param message 错误消息
     */
    public static void notNull(Object object, String message) {
        if (object == null) {
            throw new DomainRuleViolationException(message);
        }
    }

    /**
     * 断言字符串不为空
     *
     * @param text 待验证字符串
     * @param message 错误消息
     */
    public static void notEmpty(String text, String message) {
        if (text == null || text.isEmpty()) {
            throw new DomainRuleViolationException(message);
        }
    }

    /**
     * 断言集合不为空
     *
     * @param collection 待验证集合
     * @param message 错误消息
     */
    public static void notEmpty(Collection<?> collection, String message) {
        if (collection == null || collection.isEmpty()) {
            throw new DomainRuleViolationException(message);
        }
    }

    /**
     * 断言Map不为空
     *
     * @param map 待验证Map
     * @param message 错误消息
     */
    public static void notEmpty(Map<?, ?> map, String message) {
        if (map == null || map.isEmpty()) {
            throw new DomainRuleViolationException(message);
        }
    }

    /**
     * 断言对象为指定类型的实例
     *
     * @param type 类型
     * @param obj 待验证对象
     * @param message 错误消息
     */
    public static void isInstanceOf(Class<?> type, Object obj, String message) {
        notNull(type, "类型不能为空");
        if (!type.isInstance(obj)) {
            throw new DomainRuleViolationException(message);
        }
    }

    /**
     * 断言状态为真，用于验证领域对象状态
     *
     * @param expression 状态表达式
     * @param message 错误消息
     */
    public static void state(boolean expression, String message) {
        if (!expression) {
            throw new DomainRuleViolationException(message);
        }
    }

    /**
     * 断言状态为真，用于验证领域对象状态，错误消息由Supplier提供
     *
     * @param expression 状态表达式
     * @param messageSupplier 错误消息提供者
     */
    public static void state(boolean expression, Supplier<String> messageSupplier) {
        if (!expression) {
            throw new DomainRuleViolationException(messageSupplier.get());
        }
    }
} 