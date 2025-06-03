package com.lovemp.domain.customer.domain.event;

import com.lovemp.domain.customer.domain.model.valueobject.CustomerCode;
import com.lovemp.domain.customer.domain.model.valueobject.CustomerRelationId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BrandCustomerCreatedEvent 领域事件测试")
class BrandCustomerCreatedEventTest {

    @Test
    @DisplayName("应该能够创建品牌顾客创建事件")
    void should_create_brand_customer_created_event() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        String brandId = "brand-123";
        PersonId personId = PersonId.of("person-456");
        CustomerCode customerCode = CustomerCode.of("C202312001");

        // Act
        BrandCustomerCreatedEvent event = new BrandCustomerCreatedEvent(
                customerRelationId, brandId, personId, customerCode
        );

        // Assert
        assertThat(event).isNotNull();
        assertThat(event.getCustomerRelationId()).isEqualTo(customerRelationId);
        assertThat(event.getBrandId()).isEqualTo(brandId);
        assertThat(event.getPersonId()).isEqualTo(personId);
        assertThat(event.getCustomerCode()).isEqualTo(customerCode);
        assertThat(event.getEventId()).isNotNull();
        assertThat(event.getOccurredOn()).isNotNull();
    }

    @Test
    @DisplayName("应该能够获取事件类型")
    void should_get_event_type() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        String brandId = "brand-123";
        PersonId personId = PersonId.of("person-456");
        CustomerCode customerCode = CustomerCode.of("C202312001");

        // Act
        BrandCustomerCreatedEvent event = new BrandCustomerCreatedEvent(
                customerRelationId, brandId, personId, customerCode
        );

        // Assert
        assertThat(event.getEventType()).isEqualTo("BrandCustomerCreated");
    }

    @Test
    @DisplayName("每个事件应该有唯一的事件ID")
    void should_have_unique_event_id() {
        // Arrange
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        String brandId = "brand-123";
        PersonId personId = PersonId.of("person-456");
        CustomerCode customerCode = CustomerCode.of("C202312001");

        // Act
        BrandCustomerCreatedEvent event1 = new BrandCustomerCreatedEvent(
                customerRelationId, brandId, personId, customerCode
        );
        BrandCustomerCreatedEvent event2 = new BrandCustomerCreatedEvent(
                customerRelationId, brandId, personId, customerCode
        );

        // Assert
        assertThat(event1.getEventId()).isNotEqualTo(event2.getEventId());
    }
} 