package com.lovemp.domain.enterprise.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseStatus;
import lombok.Getter;

/**
 * 企业状态变更事件
 */
@Getter
public class EnterpriseStatusChangedEvent extends DomainEvent {
    
    /**
     * 企业ID
     */
    private final EnterpriseId enterpriseId;
    
    /**
     * 变更前状态
     */
    private final EnterpriseStatus oldStatus;
    
    /**
     * 变更后状态
     */
    private final EnterpriseStatus newStatus;
    
    /**
     * 构造函数
     * 
     * @param enterpriseId 企业ID
     * @param oldStatus 变更前状态
     * @param newStatus 变更后状态
     */
    public EnterpriseStatusChangedEvent(EnterpriseId enterpriseId, EnterpriseStatus oldStatus, EnterpriseStatus newStatus) {
        super(); // 调用父类构造函数初始化eventId和occurredOn
        this.enterpriseId = enterpriseId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
} 