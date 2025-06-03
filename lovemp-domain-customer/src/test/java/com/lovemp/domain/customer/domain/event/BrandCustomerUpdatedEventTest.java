package com.lovemp.domain.customer.domain.event;

import com.lovemp.domain.customer.domain.model.valueobject.CustomerRelationId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BrandCustomerUpdatedEvent 领域事件测试")
class BrandCustomerUpdatedEventTest {

    @Test
    @DisplayName("应该能够创建品牌顾客更新事件")
    void should_create_brand_customer_updated_event() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        String updatedField = "customerType";

        // Act
        BrandCustomerUpdatedEvent event = new BrandCustomerUpdatedEvent(
                customerRelationId, updatedField
        );

        // Assert
        assertThat(event).isNotNull();
        assertThat(event.getCustomerRelationId()).isEqualTo(customerRelationId);
        assertThat(event.getUpdatedField()).isEqualTo(updatedField);
        assertThat(event.getEventId()).isNotNull();
        assertThat(event.getOccurredOn()).isNotNull();
    }

    @Test
    @DisplayName("应该能够判断是否为积分更新事件")
    void should_check_if_points_update_event() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        
        BrandCustomerUpdatedEvent pointsEvent = new BrandCustomerUpdatedEvent(
                customerRelationId, "points"
        );
        
        BrandCustomerUpdatedEvent typeEvent = new BrandCustomerUpdatedEvent(
                customerRelationId, "customerType"
        );

        // Assert
        assertThat(pointsEvent.isPointsUpdated()).isTrue();
        assertThat(typeEvent.isPointsUpdated()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为等级更新事件")
    void should_check_if_level_update_event() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        
        BrandCustomerUpdatedEvent levelEvent = new BrandCustomerUpdatedEvent(
                customerRelationId, "level"
        );
        
        BrandCustomerUpdatedEvent typeEvent = new BrandCustomerUpdatedEvent(
                customerRelationId, "customerType"
        );

        // Assert
        assertThat(levelEvent.isLevelUpdated()).isTrue();
        assertThat(typeEvent.isLevelUpdated()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为类型更新事件")
    void should_check_if_type_update_event() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        
        BrandCustomerUpdatedEvent typeEvent = new BrandCustomerUpdatedEvent(
                customerRelationId, "customerType"
        );
        
        BrandCustomerUpdatedEvent pointsEvent = new BrandCustomerUpdatedEvent(
                customerRelationId, "points"
        );

        // Assert
        assertThat(typeEvent.isCustomerTypeUpdated()).isTrue();
        assertThat(pointsEvent.isCustomerTypeUpdated()).isFalse();
    }

    @Test
    @DisplayName("应该能够获取事件类型")
    void should_get_event_type() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        BrandCustomerUpdatedEvent event = new BrandCustomerUpdatedEvent(
                customerRelationId, "customerType"
        );

        // Assert
        assertThat(event.getEventType()).isEqualTo("BrandCustomerUpdated");
    }

    @Test
    @DisplayName("每个事件应该有唯一的事件ID")
    void should_have_unique_event_id() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();

        // Act
        BrandCustomerUpdatedEvent event1 = new BrandCustomerUpdatedEvent(
                customerRelationId, "customerType"
        );
        BrandCustomerUpdatedEvent event2 = new BrandCustomerUpdatedEvent(
                customerRelationId, "customerType"
        );

        // Assert
        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
} 