package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.Identifier;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * 企业ID值对象
 */
@EqualsAndHashCode(callSuper = true)
public class EnterpriseId extends Identifier<String> {
    
    private EnterpriseId(String value) {
        super(value);
    }
    
    /**
     * 生成新的企业ID
     * 
     * @return 新的企业ID
     */
    public static EnterpriseId generate() {
        return new EnterpriseId(UUID.randomUUID().toString());
    }
    
    /**
     * 根据给定字符串创建企业ID
     * 
     * @param id 企业ID字符串
     * @return 企业ID
     */
    public static EnterpriseId of(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("企业ID不能为空");
        }
        return new EnterpriseId(id);
    }
} 