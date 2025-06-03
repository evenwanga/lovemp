package com.lovemp.domain.person.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.Getter;

/**
 * 自然人创建事件
 */
@Getter
public class PersonCreatedEvent extends DomainEvent {
    
    private final PersonId personId;
    private final String name;
    
    public PersonCreatedEvent(PersonId personId, String name) {
        super();
        this.personId = personId;
        this.name = name;
    }
}