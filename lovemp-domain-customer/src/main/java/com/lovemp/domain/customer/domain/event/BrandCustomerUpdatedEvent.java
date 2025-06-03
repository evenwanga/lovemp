package com.lovemp.domain.customer.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerRelationId;
import lombok.Getter;

/**
 * 品牌顾客更新事件
 */
@Getter
public class BrandCustomerUpdatedEvent extends DomainEvent {
    
    /**
     * 顾客关系ID
     */
    private final CustomerRelationId customerRelationId;
    
    /**
     * 更新字段
     */
    private final String updatedField;
    
    /**
     * 构造函数
     * 
     * @param customerRelationId 顾客关系ID
     * @param updatedField 更新字段
     */
    public BrandCustomerUpdatedEvent(CustomerRelationId customerRelationId, String updatedField) {
        super();
        this.customerRelationId = customerRelationId;
        this.updatedField = updatedField;
    }
    
    @Override
    public String getEventType() {
        return "BrandCustomerUpdated";
    }
    
    /**
     * 是否为积分更新事件
     * 
     * @return true-是积分更新事件，false-不是积分更新事件
     */
    public boolean isPointsUpdated() {
        return "points".equals(updatedField);
    }
    
    /**
     * 是否为等级更新事件
     * 
     * @return true-是等级更新事件，false-不是等级更新事件
     */
    public boolean isLevelUpdated() {
        return "level".equals(updatedField);
    }
    
    /**
     * 是否为顾客类型更新事件
     * 
     * @return true-是顾客类型更新事件，false-不是顾客类型更新事件
     */
    public boolean isCustomerTypeUpdated() {
        return "customerType".equals(updatedField);
    }
} 