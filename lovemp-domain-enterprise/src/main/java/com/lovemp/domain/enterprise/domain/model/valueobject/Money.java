package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Currency;

/**
 * 金额值对象
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Money implements ValueObject {
    
    /**
     * 金额
     */
    BigDecimal amount;
    
    /**
     * 货币
     */
    Currency currency;
    
    /**
     * 创建人民币金额
     * 
     * @param amount 金额
     * @return 人民币金额值对象
     */
    public static Money ofCNY(BigDecimal amount) {
        return new Money(amount.setScale(2, RoundingMode.HALF_UP), Currency.getInstance("CNY"));
    }
    
    /**
     * 创建人民币金额
     * 
     * @param amount 金额
     * @return 人民币金额值对象
     */
    public static Money ofCNY(double amount) {
        return ofCNY(BigDecimal.valueOf(amount));
    }
    
    /**
     * 创建美元金额
     * 
     * @param amount 金额
     * @return 美元金额值对象
     */
    public static Money ofUSD(BigDecimal amount) {
        return new Money(amount.setScale(2, RoundingMode.HALF_UP), Currency.getInstance("USD"));
    }
    
    /**
     * 创建美元金额
     * 
     * @param amount 金额
     * @return 美元金额值对象
     */
    public static Money ofUSD(double amount) {
        return ofUSD(BigDecimal.valueOf(amount));
    }
    
    /**
     * 创建指定货币的金额
     * 
     * @param amount 金额
     * @param currencyCode 货币代码（如CNY、USD等）
     * @return 金额值对象
     */
    public static Money of(BigDecimal amount, String currencyCode) {
        return new Money(amount.setScale(2, RoundingMode.HALF_UP), Currency.getInstance(currencyCode));
    }
    
    /**
     * 创建指定货币的金额
     * 
     * @param amount 金额
     * @param currencyCode 货币代码（如CNY、USD等）
     * @return 金额值对象
     */
    public static Money of(double amount, String currencyCode) {
        return of(BigDecimal.valueOf(amount), currencyCode);
    }
    
    /**
     * 金额相加
     * 
     * @param money 要相加的金额
     * @return 相加后的金额
     */
    public Money add(Money money) {
        if (!this.currency.equals(money.currency)) {
            throw new IllegalArgumentException("不能相加不同货币的金额");
        }
        return new Money(this.amount.add(money.amount), this.currency);
    }
    
    /**
     * 金额相减
     * 
     * @param money 要相减的金额
     * @return 相减后的金额
     */
    public Money subtract(Money money) {
        if (!this.currency.equals(money.currency)) {
            throw new IllegalArgumentException("不能相减不同货币的金额");
        }
        return new Money(this.amount.subtract(money.amount), this.currency);
    }
    
    /**
     * 金额乘以系数
     * 
     * @param multiplier 乘数
     * @return 相乘后的金额
     */
    public Money multiply(BigDecimal multiplier) {
        return new Money(this.amount.multiply(multiplier).setScale(2, RoundingMode.HALF_UP), this.currency);
    }
    
    /**
     * 金额乘以系数
     * 
     * @param multiplier 乘数
     * @return 相乘后的金额
     */
    public Money multiply(double multiplier) {
        return multiply(BigDecimal.valueOf(multiplier));
    }
    
    /**
     * 金额除以除数
     * 
     * @param divisor 除数
     * @return 相除后的金额
     */
    public Money divide(BigDecimal divisor) {
        return new Money(this.amount.divide(divisor, 2, RoundingMode.HALF_UP), this.currency);
    }
    
    /**
     * 金额除以除数
     * 
     * @param divisor 除数
     * @return 相除后的金额
     */
    public Money divide(double divisor) {
        return divide(BigDecimal.valueOf(divisor));
    }
    
    /**
     * 比较两个金额是否相等
     * 
     * @param other 另一个金额
     * @return 如果相等返回true
     */
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof Money)) return false;
        
        Money money = (Money) other;
        return amount.compareTo(money.amount) == 0 && currency.equals(money.currency);
    }
    
    /**
     * 获取哈希码
     * 
     * @return 哈希码
     */
    @Override
    public int hashCode() {
        int result = amount.hashCode();
        result = 31 * result + currency.hashCode();
        return result;
    }
    
    /**
     * 金额格式化为字符串
     * 
     * @return 格式化后的金额字符串
     */
    @Override
    public String toString() {
        return String.format("%s %s", currency.getSymbol(), amount.toString());
    }
    
    /**
     * 判断金额是否为正数
     * 
     * @return 如果金额大于0返回true
     */
    public boolean isPositive() {
        return amount.compareTo(BigDecimal.ZERO) > 0;
    }
    
    /**
     * 判断金额是否为零
     * 
     * @return 如果金额等于0返回true
     */
    public boolean isZero() {
        return amount.compareTo(BigDecimal.ZERO) == 0;
    }
    
    /**
     * 判断金额是否为负数
     * 
     * @return 如果金额小于0返回true
     */
    public boolean isNegative() {
        return amount.compareTo(BigDecimal.ZERO) < 0;
    }
} 