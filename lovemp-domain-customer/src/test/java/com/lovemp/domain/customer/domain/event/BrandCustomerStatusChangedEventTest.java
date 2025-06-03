package com.lovemp.domain.customer.domain.event;

import com.lovemp.domain.customer.domain.model.valueobject.CustomerRelationId;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BrandCustomerStatusChangedEvent 领域事件测试")
class BrandCustomerStatusChangedEventTest {

    @Test
    @DisplayName("应该能够创建带原因的状态变更事件")
    void should_create_status_changed_event_with_reason() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        CustomerStatus oldStatus = CustomerStatus.ACTIVE;
        CustomerStatus newStatus = CustomerStatus.FROZEN;
        String reason = "违规操作";

        // Act
        BrandCustomerStatusChangedEvent event = new BrandCustomerStatusChangedEvent(
                customerRelationId, oldStatus, newStatus, reason
        );

        // Assert
        assertThat(event).isNotNull();
        assertThat(event.getCustomerRelationId()).isEqualTo(customerRelationId);
        assertThat(event.getOldStatus()).isEqualTo(oldStatus);
        assertThat(event.getNewStatus()).isEqualTo(newStatus);
        assertThat(event.getReason()).isEqualTo(reason);
        assertThat(event.getEventId()).isNotNull();
        assertThat(event.getOccurredOn()).isNotNull();
    }

    @Test
    @DisplayName("应该能够创建不带原因的状态变更事件")
    void should_create_status_changed_event_without_reason() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        CustomerStatus oldStatus = CustomerStatus.INACTIVE;
        CustomerStatus newStatus = CustomerStatus.ACTIVE;

        // Act
        BrandCustomerStatusChangedEvent event = new BrandCustomerStatusChangedEvent(
                customerRelationId, oldStatus, newStatus
        );

        // Assert
        assertThat(event).isNotNull();
        assertThat(event.getCustomerRelationId()).isEqualTo(customerRelationId);
        assertThat(event.getOldStatus()).isEqualTo(oldStatus);
        assertThat(event.getNewStatus()).isEqualTo(newStatus);
        assertThat(event.getReason()).isNull();
        assertThat(event.getEventId()).isNotNull();
        assertThat(event.getOccurredOn()).isNotNull();
    }

    @Test
    @DisplayName("应该能够判断是否为激活事件")
    void should_check_if_activation_event() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        
        BrandCustomerStatusChangedEvent activationEvent = new BrandCustomerStatusChangedEvent(
                customerRelationId, CustomerStatus.INACTIVE, CustomerStatus.ACTIVE
        );
        
        BrandCustomerStatusChangedEvent freezeEvent = new BrandCustomerStatusChangedEvent(
                customerRelationId, CustomerStatus.ACTIVE, CustomerStatus.FROZEN
        );

        // Assert
        assertThat(activationEvent.isActivated()).isTrue();
        assertThat(freezeEvent.isActivated()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为冻结事件")
    void should_check_if_freeze_event() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        
        BrandCustomerStatusChangedEvent freezeEvent = new BrandCustomerStatusChangedEvent(
                customerRelationId, CustomerStatus.ACTIVE, CustomerStatus.FROZEN, "违规操作"
        );
        
        BrandCustomerStatusChangedEvent activationEvent = new BrandCustomerStatusChangedEvent(
                customerRelationId, CustomerStatus.INACTIVE, CustomerStatus.ACTIVE
        );

        // Assert
        assertThat(freezeEvent.isFrozen()).isTrue();
        assertThat(activationEvent.isFrozen()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为解冻事件")
    void should_check_if_unfreeze_event() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        
        BrandCustomerStatusChangedEvent unfreezeEvent = new BrandCustomerStatusChangedEvent(
                customerRelationId, CustomerStatus.FROZEN, CustomerStatus.ACTIVE
        );
        
        BrandCustomerStatusChangedEvent freezeEvent = new BrandCustomerStatusChangedEvent(
                customerRelationId, CustomerStatus.ACTIVE, CustomerStatus.FROZEN
        );

        // Assert
        assertThat(unfreezeEvent.isUnfrozen()).isTrue();
        assertThat(freezeEvent.isUnfrozen()).isFalse();
    }

    @Test
    @DisplayName("应该能够获取事件类型")
    void should_get_event_type() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        BrandCustomerStatusChangedEvent event = new BrandCustomerStatusChangedEvent(
                customerRelationId, CustomerStatus.ACTIVE, CustomerStatus.FROZEN
        );

        // Assert
        assertThat(event.getEventType()).isEqualTo("BrandCustomerStatusChanged");
    }

    @Test
    @DisplayName("每个事件应该有唯一的事件ID")
    void should_have_unique_event_id() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();

        // Act
        BrandCustomerStatusChangedEvent event1 = new BrandCustomerStatusChangedEvent(
                customerRelationId, CustomerStatus.ACTIVE, CustomerStatus.FROZEN
        );
        BrandCustomerStatusChangedEvent event2 = new BrandCustomerStatusChangedEvent(
                customerRelationId, CustomerStatus.ACTIVE, CustomerStatus.FROZEN
        );

        // Assert
        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
} 