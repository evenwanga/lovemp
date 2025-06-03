package com.lovemp.common.domain;

import lombok.Getter;

import java.time.Instant;
import java.util.UUID;

/**
 * 领域事件基类
 */
@Getter
public abstract class DomainEvent {
    
    /**
     * 事件ID
     */
    private final String eventId;
    
    /**
     * 事件发生时间
     */
    private final Instant occurredOn;
    
    /**
     * 构造函数
     */
    protected DomainEvent() {
        this.eventId = UUID.randomUUID().toString();
        this.occurredOn = Instant.now();
    }
    
    /**
     * 获取事件类型
     * 
     * @return 事件类型名称
     */
    public String getEventType() {
        return this.getClass().getSimpleName();
    }
} 