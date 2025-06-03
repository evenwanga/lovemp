package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.Assert;

import java.util.Objects;
import java.util.UUID;

/**
 * 共享ID值对象
 */
public class SharingId implements ValueObject {
    
    private final String value;
    
    private SharingId(String value) {
        Assert.notEmpty(value, "共享ID不能为空");
        this.value = value;
    }
    
    /**
     * 创建共享ID
     * 
     * @param value ID值
     * @return 共享ID
     */
    public static SharingId of(String value) {
        return new SharingId(value);
    }
    
    /**
     * 生成新的共享ID
     * 
     * @return 新的共享ID
     */
    public static SharingId generate() {
        return new SharingId(UUID.randomUUID().toString());
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
        SharingId sharingId = (SharingId) o;
        return Objects.equals(value, sharingId.value);
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