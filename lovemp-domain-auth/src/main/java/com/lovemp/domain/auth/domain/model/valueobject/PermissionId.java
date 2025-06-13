package com.lovemp.domain.auth.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.Assert;

import java.util.Objects;

/**
 * 权限标识符值对象
 * 
 * <p>用于唯一标识系统中的权限实体</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class PermissionId implements ValueObject {
    
    private final String value;
    
    /**
     * 构造权限标识符
     * 
     * @param value 标识符值，不能为空
     * @throws IllegalArgumentException 如果value为空
     */
    public PermissionId(String value) {
        Assert.notEmpty(value, "权限标识符不能为空");
        this.value = value;
    }
    
    /**
     * 创建权限标识符
     * 
     * @param value 标识符值
     * @return 权限标识符实例
     */
    public static PermissionId of(String value) {
        return new PermissionId(value);
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
        PermissionId that = (PermissionId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return "PermissionId{" +
                "value='" + value + '\'' +
                '}';
    }
} 