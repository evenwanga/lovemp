package com.lovemp.domain.brand.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import lombok.Getter;

/**
 * 品牌更新事件
 */
@Getter
public class BrandUpdatedEvent extends DomainEvent {
    
    /**
     * 品牌ID
     */
    private final BrandId brandId;
    
    /**
     * 品牌名称
     */
    private final String brandName;
    
    /**
     * 构造函数
     * 
     * @param brandId 品牌ID
     * @param brandName 品牌名称
     */
    public BrandUpdatedEvent(BrandId brandId, String brandName) {
        super(); // 调用父类构造函数初始化eventId和occurredOn
        this.brandId = brandId;
        this.brandName = brandName;
    }
} 