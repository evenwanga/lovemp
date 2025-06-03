package com.lovemp.domain.brand.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import lombok.Getter;

/**
 * 品牌创建事件
 */
@Getter
public class BrandCreatedEvent extends DomainEvent {
    
    /**
     * 品牌ID
     */
    private final BrandId brandId;
    
    /**
     * 品牌名称
     */
    private final String brandName;
    
    /**
     * 所属企业ID
     */
    private final EnterpriseId enterpriseId;
    
    /**
     * 构造函数
     * 
     * @param brandId 品牌ID
     * @param brandName 品牌名称
     * @param enterpriseId 所属企业ID
     */
    public BrandCreatedEvent(BrandId brandId, String brandName, EnterpriseId enterpriseId) {
        super(); // 调用父类构造函数初始化eventId和occurredOn
        this.brandId = brandId;
        this.brandName = brandName;
        this.enterpriseId = enterpriseId;
    }
} 