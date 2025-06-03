package com.lovemp.domain.person.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.Getter;

/**
 * 自然人信息更新事件
 */
@Getter
public class PersonUpdatedEvent extends DomainEvent {
    
    private final PersonId personId;
    private final String updatedFields;
    
    public PersonUpdatedEvent(PersonId personId, String updatedFields) {
        super();
        this.personId = personId;
        this.updatedFields = updatedFields;
    }
}