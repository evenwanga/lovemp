package com.lovemp.domain.labor.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.labor.domain.model.valueobject.LaborResourceId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.Getter;

/**
 * 劳动力资源创建事件
 */
@Getter
public class LaborResourceCreatedEvent extends DomainEvent {
    
    /**
     * 劳动力资源ID
     */
    private final LaborResourceId laborResourceId;
    
    /**
     * 自然人ID
     */
    private final PersonId personId;
    
    public LaborResourceCreatedEvent(LaborResourceId laborResourceId, PersonId personId) {
        super();
        this.laborResourceId = laborResourceId;
        this.personId = personId;
    }
}