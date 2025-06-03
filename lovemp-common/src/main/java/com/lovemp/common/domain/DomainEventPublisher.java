package com.lovemp.common.domain;

/**
 * 领域事件发布者接口
 */
public interface DomainEventPublisher {

    /**
     * 发布领域事件
     *
     * @param event 待发布的领域事件
     */
    void publish(DomainEvent event);

    /**
     * 发布多个领域事件
     *
     * @param events 待发布的领域事件数组
     */
    default void publish(DomainEvent... events) {
        for (DomainEvent event : events) {
            publish(event);
        }
    }
} 