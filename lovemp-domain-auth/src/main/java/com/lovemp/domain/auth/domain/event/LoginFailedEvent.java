package com.lovemp.domain.auth.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.auth.domain.model.valueobject.AccountId;

import java.time.LocalDateTime;

/**
 * 登录失败事件
 * 
 * <p>当用户登录失败时发布此事件</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class LoginFailedEvent extends DomainEvent {
    
    private final AccountId accountId;
    private final String username;
    private final int failedAttempts;
    private final LocalDateTime failedAt;
    
    public LoginFailedEvent(AccountId accountId, String username, int failedAttempts, LocalDateTime failedAt) {
        super();
        this.accountId = accountId;
        this.username = username;
        this.failedAttempts = failedAttempts;
        this.failedAt = failedAt;
    }
    
    public AccountId getAccountId() {
        return accountId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public int getFailedAttempts() {
        return failedAttempts;
    }
    
    public LocalDateTime getFailedAt() {
        return failedAt;
    }
    
    @Override
    public String toString() {
        return "LoginFailedEvent{" +
                "accountId=" + accountId +
                ", username='" + username + '\'' +
                ", failedAttempts=" + failedAttempts +
                ", failedAt=" + failedAt +
                ", occurredOn=" + getOccurredOn() +
                '}';
    }
} 