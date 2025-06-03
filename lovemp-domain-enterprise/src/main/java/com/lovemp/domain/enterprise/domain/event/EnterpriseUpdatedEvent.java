package com.lovemp.domain.enterprise.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import lombok.Getter;

/**
 * 企业更新事件
 */
@Getter
public class EnterpriseUpdatedEvent extends DomainEvent {
    
    /**
     * 企业ID
     */
    private final EnterpriseId enterpriseId;
    
    /**
     * 更新字段
     */
    private final String updatedField;
    
    /**
     * 构造函数
     * 
     * @param enterpriseId 企业ID
     * @param updatedField 更新字段
     */
    public EnterpriseUpdatedEvent(EnterpriseId enterpriseId, String updatedField) {
        super(); // 调用父类构造函数初始化eventId和occurredOn
        this.enterpriseId = enterpriseId;
        this.updatedField = updatedField;
    }
} 