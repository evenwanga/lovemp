package com.lovemp.domain.brand.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyId;
import lombok.Getter;

/**
 * 用工政策添加事件
 */
@Getter
public class LaborPolicyAddedEvent extends DomainEvent {
    
    /**
     * 品牌ID
     */
    private final BrandId brandId;
    
    /**
     * 用工政策ID
     */
    private final LaborPolicyId laborPolicyId;
    
    /**
     * 构造函数
     * 
     * @param brandId 品牌ID
     * @param laborPolicyId 用工政策ID
     */
    public LaborPolicyAddedEvent(BrandId brandId, LaborPolicyId laborPolicyId) {
        super(); // 调用父类构造函数初始化eventId和occurredOn
        this.brandId = brandId;
        this.laborPolicyId = laborPolicyId;
    }
} 