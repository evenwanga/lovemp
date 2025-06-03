package com.lovemp.domain.brand.domain.model.valueobject;

import com.lovemp.common.domain.Identifier;
import lombok.EqualsAndHashCode;

import java.util.UUID;

/**
 * 品牌ID值对象
 */
@EqualsAndHashCode(callSuper = true)
public class BrandId extends Identifier<String> {
    
    private BrandId(String value) {
        super(value);
    }
    
    /**
     * 生成新的品牌ID
     * 
     * @return 新的品牌ID
     */
    public static BrandId generate() {
        return new BrandId(UUID.randomUUID().toString());
    }
    
    /**
     * 根据给定字符串创建品牌ID
     * 
     * @param id 品牌ID字符串
     * @return 品牌ID
     */
    public static BrandId of(String id) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("品牌ID不能为空");
        }
        return new BrandId(id);
    }
} 