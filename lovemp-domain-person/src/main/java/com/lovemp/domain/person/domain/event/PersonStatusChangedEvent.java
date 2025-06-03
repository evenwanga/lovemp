package com.lovemp.domain.person.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import com.lovemp.domain.person.domain.model.valueobject.PersonStatus;
import lombok.Getter;

/**
 * 自然人状态变更事件
 */
@Getter
public class PersonStatusChangedEvent extends DomainEvent {
    
    private final PersonId personId;
    private final PersonStatus oldStatus;
    private final PersonStatus newStatus;
    
    public PersonStatusChangedEvent(PersonId personId, PersonStatus oldStatus, PersonStatus newStatus) {
        super();
        this.personId = personId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
}