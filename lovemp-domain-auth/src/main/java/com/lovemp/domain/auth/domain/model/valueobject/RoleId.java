package com.lovemp.domain.auth.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.Assert;

import java.util.Objects;

/**
 * 角色标识符值对象
 * 
 * <p>用于唯一标识系统中的角色实体</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class RoleId implements ValueObject {
    
    private final String value;
    
    /**
     * 构造角色标识符
     * 
     * @param value 标识符值，不能为空
     * @throws IllegalArgumentException 如果value为空
     */
    public RoleId(String value) {
        Assert.notEmpty(value, "角色标识符不能为空");
        this.value = value;
    }
    
    /**
     * 创建角色标识符
     * 
     * @param value 标识符值
     * @return 角色标识符实例
     */
    public static RoleId of(String value) {
        return new RoleId(value);
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
        RoleId roleId = (RoleId) o;
        return Objects.equals(value, roleId.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "RoleId{" +
                "value='" + value + '\'' +
                '}';
    }
} 