package com.lovemp.domain.enterprise.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import lombok.Getter;

/**
 * 企业创建事件
 */
@Getter
public class EnterpriseCreatedEvent extends DomainEvent {
    
    /**
     * 企业ID
     */
    private final EnterpriseId enterpriseId;
    
    /**
     * 企业名称
     */
    private final String enterpriseName;
    
    /**
     * 构造函数
     * 
     * @param enterpriseId 企业ID
     * @param enterpriseName 企业名称
     */
    public EnterpriseCreatedEvent(EnterpriseId enterpriseId, String enterpriseName) {
        super(); // 调用父类构造函数初始化eventId和occurredOn
        this.enterpriseId = enterpriseId;
        this.enterpriseName = enterpriseName;
    }
} 