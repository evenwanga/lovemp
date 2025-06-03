package com.lovemp.domain.brand.domain.model.valueobject;

import com.lovemp.common.domain.Identifier;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * 用工政策ID值对象
 */
@EqualsAndHashCode(callSuper = true)
public class LaborPolicyId extends Identifier<String> {
    
    private LaborPolicyId(String value) {
        super(value);
    }
    
    /**
     * 生成新的用工政策ID
     * 
     * @return 新的用工政策ID
     */
    public static LaborPolicyId generate() {
        return new LaborPolicyId(UUID.randomUUID().toString());
    }
    
    /**
     * 根据给定字符串创建用工政策ID
     * 
     * @param id 用工政策ID字符串
     * @return 用工政策ID
     */
    public static LaborPolicyId of(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("用工政策ID不能为空");
        }
        return new LaborPolicyId(id);
    }
} 