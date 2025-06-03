package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * 顾客关系ID值对象
 */
public class CustomerRelationId implements ValueObject {
    
    private final String value;
    
    private CustomerRelationId(String value) {
        Assert.notEmpty(value, "顾客关系ID不能为空");
        this.value = value;
    }
    
    /**
     * 创建顾客关系ID
     * 
     * @param value ID值
     * @return 顾客关系ID
     */
    public static CustomerRelationId of(String value) {
        return new CustomerRelationId(value);
    }
    
    /**
     * 生成新的顾客关系ID
     * 
     * @return 新的顾客关系ID
     */
    public static CustomerRelationId generate() {
        return new CustomerRelationId(UUID.randomUUID().toString());
    }
    
    /**
     * 获取ID值
     * 
     * @return ID值
     */
    public String getValue() {
        return value;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerRelationId that = (CustomerRelationId) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
} 