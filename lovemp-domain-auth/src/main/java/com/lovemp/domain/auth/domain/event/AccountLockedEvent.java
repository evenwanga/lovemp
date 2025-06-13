package com.lovemp.domain.auth.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.auth.domain.model.valueobject.AccountId;

import java.time.LocalDateTime;

/**
 * 账号锁定事件
 * 
 * <p>当账号被锁定时发布此事件</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class AccountLockedEvent extends DomainEvent {
    
    private final AccountId accountId;
    private final String username;
    private final LocalDateTime lockedAt;
    
    public AccountLockedEvent(AccountId accountId, String username, LocalDateTime lockedAt) {
        super();
        this.accountId = accountId;
        this.username = username;
        this.lockedAt = lockedAt;
    }
    
    public AccountId getAccountId() {
        return accountId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public LocalDateTime getLockedAt() {
        return lockedAt;
    }
    
    @Override
    public String toString() {
        return "AccountLockedEvent{" +
                "accountId=" + accountId +
                ", username='" + username + '\'' +
                ", lockedAt=" + lockedAt +
                ", occurredOn=" + getOccurredOn() +
                '}';
    }
} 