package com.lovemp.domain.auth.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.auth.domain.model.valueobject.AccountId;
import com.lovemp.domain.auth.domain.model.valueobject.AccountStatus;

/**
 * 账号状态变更事件
 * 
 * <p>当账号状态发生变更时发布此事件</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class AccountStatusChangedEvent extends DomainEvent {
    
    private final AccountId accountId;
    private final AccountStatus oldStatus;
    private final AccountStatus newStatus;
    
    public AccountStatusChangedEvent(AccountId accountId, AccountStatus oldStatus, AccountStatus newStatus) {
        super();
        this.accountId = accountId;
        this.oldStatus = oldStatus;
        this.newStatus = newStatus;
    }
    
    public AccountId getAccountId() {
        return accountId;
    }
    
    public AccountStatus getOldStatus() {
        return oldStatus;
    }
    
    public AccountStatus getNewStatus() {
        return newStatus;
    }
    
    @Override
    public String toString() {
        return "AccountStatusChangedEvent{" +
                "accountId=" + accountId +
                ", oldStatus=" + oldStatus +
                ", newStatus=" + newStatus +
                ", occurredOn=" + getOccurredOn() +
                '}';
    }
} 