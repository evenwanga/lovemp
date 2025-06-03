package com.lovemp.domain.brand.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.brand.domain.model.valueobject.BrandStatus;
import lombok.Getter;

/**
 * 品牌状态变更事件
 */
@Getter
public class BrandStatusChangedEvent extends DomainEvent {
    
    /**
     * 品牌ID
     */
    private final BrandId brandId;
    
    /**
     * 品牌状态
     */
    private final BrandStatus status;
    
    /**
     * 构造函数
     * 
     * @param brandId 品牌ID
     * @param status 品牌状态
     */
    public BrandStatusChangedEvent(BrandId brandId, BrandStatus status) {
        super(); // 调用父类构造函数初始化eventId和occurredOn
        this.brandId = brandId;
        this.status = status;
    }
} 