package com.lovemp.domain.customer.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerRelationId;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerStatus;
import lombok.Getter;

/**
 * 品牌顾客状态变更事件
 */
@Getter
public class BrandCustomerStatusChangedEvent extends DomainEvent {
    
    /**
     * 顾客关系ID
     */
    private final CustomerRelationId customerRelationId;
    
    /**
     * 原状态
     */
    private final CustomerStatus oldStatus;
    
    /**
     * 新状态
     */
    private final CustomerStatus newStatus;
    
    /**
     * 变更原因
     */
    private final String reason;
    
    /**
     * 构造函数
     * 
     * @param customerRelationId 顾客关系ID
     * @param oldStatus 原状态
     * @param newStatus 新状态
     */
    public BrandCustomerStatusChangedEvent(CustomerRelationId customerRelationId, 
                                         CustomerStatus oldStatus, CustomerStatus newStatus) {
        super();
        this.customerRelationId = customerRelationId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.reason = null;
    }
    
    /**
     * 构造函数（带原因）
     * 
     * @param customerRelationId 顾客关系ID
     * @param oldStatus 原状态
     * @param newStatus 新状态
     * @param reason 变更原因
     */
    public BrandCustomerStatusChangedEvent(CustomerRelationId customerRelationId, 
                                         CustomerStatus oldStatus, CustomerStatus newStatus, String reason) {
        super();
        this.customerRelationId = customerRelationId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
        this.reason = reason;
    }
    
    @Override
    public String getEventType() {
        return "BrandCustomerStatusChanged";
    }
    
    /**
     * 是否为激活事件
     * 
     * @return true-是激活事件，false-不是激活事件
     */
    public boolean isActivated() {
        return newStatus.isActive() && !oldStatus.isActive();
    }
    
    /**
     * 是否为冻结事件
     * 
     * @return true-是冻结事件，false-不是冻结事件
     */
    public boolean isFrozen() {
        return newStatus.isFrozen() && !oldStatus.isFrozen();
    }
    
    /**
     * 是否为解冻事件
     * 
     * @return true-是解冻事件，false-不是解冻事件
     */
    public boolean isUnfrozen() {
        return oldStatus.isFrozen() && newStatus.isActive();
    }
} 