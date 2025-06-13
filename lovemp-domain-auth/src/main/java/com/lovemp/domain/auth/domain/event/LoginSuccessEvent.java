package com.lovemp.domain.auth.domain.event;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.domain.auth.domain.model.valueobject.AccountId;

import java.time.LocalDateTime;

/**
 * 登录成功事件
 * 
 * <p>当用户成功登录时发布此事件</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class LoginSuccessEvent extends DomainEvent {
    
    private final AccountId accountId;
    private final String username;
    private final LocalDateTime loginAt;
    
    public LoginSuccessEvent(AccountId accountId, String username, LocalDateTime loginAt) {
        super();
        this.accountId = accountId;
        this.username = username;
        this.loginAt = loginAt;
    }
    
    public AccountId getAccountId() {
        return accountId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public LocalDateTime getLoginAt() {
        return loginAt;
    }
    
    @Override
    public String toString() {
        return "LoginSuccessEvent{" +
                "accountId=" + accountId +
                ", username='" + username + '\'' +
                ", loginAt=" + loginAt +
                ", occurredOn=" + getOccurredOn() +
                '}';
    }
} 