package com.lovemp.domain.customer.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerCode;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerRelationId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.Getter;

/**
 * 品牌顾客创建事件
 */
@Getter
public class BrandCustomerCreatedEvent extends DomainEvent {
    
    /**
     * 顾客关系ID
     */
    private final CustomerRelationId customerRelationId;
    
    /**
     * 品牌ID
     */
    private final String brandId;
    
    /**
     * 自然人ID
     */
    private final PersonId personId;
    
    /**
     * 顾客编码
     */
    private final CustomerCode customerCode;
    
    /**
     * 构造函数
     * 
     * @param customerRelationId 顾客关系ID
     * @param brandId 品牌ID
     * @param personId 自然人ID
     * @param customerCode 顾客编码
     */
    public BrandCustomerCreatedEvent(CustomerRelationId customerRelationId, String brandId, 
                                   PersonId personId, CustomerCode customerCode) {
        super();
        this.customerRelationId = customerRelationId;
        this.brandId = brandId;
        this.personId = personId;
        this.customerCode = customerCode;
    }
    
    @Override
    public String getEventType() {
        return "BrandCustomerCreated";
    }
} 