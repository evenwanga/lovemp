package com.lovemp.domain.auth.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.Assert;

import java.util.Objects;

/**
 * 账号标识符值对象
 * 
 * <p>用于唯一标识系统中的账号实体</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class AccountId implements ValueObject {
    
    private final String value;
    
    /**
     * 构造账号标识符
     * 
     * @param value 标识符值，不能为空
     * @throws IllegalArgumentException 如果value为空
     */
    public AccountId(String value) {
        Assert.notEmpty(value, "账号标识符不能为空");
        this.value = value;
    }
    
    /**
     * 创建账号标识符
     * 
     * @param value 标识符值
     * @return 账号标识符实例
     */
    public static AccountId of(String value) {
        return new AccountId(value);
    }
    
    /**
     * 获取标识符值
     * 
     * @return 标识符值
     */
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AccountId accountId = (AccountId) o;
        return Objects.equals(value, accountId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "AccountId{" +
                "value='" + value + '\'' +
                '}';
    }
} 