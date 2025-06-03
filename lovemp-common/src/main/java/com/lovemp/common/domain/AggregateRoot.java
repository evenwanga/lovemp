package com.lovemp.common.domain;

import lombok.Getter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 聚合根基类
 * 
 * @param <ID> 聚合根标识类型
 */
public abstract class AggregateRoot<ID> extends Entity<ID> {
    
    @Getter
    private final List<DomainEvent> domainEvents = new ArrayList<>();
    
    /**
     * 注册领域事件
     * 
     * @param event 领域事件
     */
    protected void registerEvent(DomainEvent event) {
        domainEvents.add(event);
    }
    
    /**
     * 清除所有已注册的领域事件
     */
    public void clearEvents() {
        domainEvents.clear();
    }
    
    /**
     * 获取不可变的领域事件列表
     * 
     * @return 领域事件列表
     */
    public List<DomainEvent> getUnmodifiableDomainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }
} 