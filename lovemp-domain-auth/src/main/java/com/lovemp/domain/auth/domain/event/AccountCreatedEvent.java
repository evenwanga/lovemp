package com.lovemp.domain.auth.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.auth.domain.model.valueobject.AccountId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;

/**
 * 账号创建事件
 * 
 * <p>当新账号被创建时发布此事件</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class AccountCreatedEvent extends DomainEvent {
    
    private final AccountId accountId;
    private final PersonId personId;
    private final String username;
    private final String email;
    
    public AccountCreatedEvent(AccountId accountId, PersonId personId, String username, String email) {
        super();
        this.accountId = accountId;
        this.personId = personId;
        this.username = username;
        this.email = email;
    }
    
    public AccountId getAccountId() {
        return accountId;
    }
    
    public PersonId getPersonId() {
        return personId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getEmail() {
        return email;
    }
    
    @Override
    public String toString() {
        return "AccountCreatedEvent{" +
                "accountId=" + accountId +
                ", personId=" + personId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", occurredOn=" + getOccurredOn() +
                '}';
    }
} 