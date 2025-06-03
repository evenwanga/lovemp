package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.exception.DomainRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;


import static org.assertj.core.api.Assertions.*;

@DisplayName("CustomerRelationId 值对象测试")
class CustomerRelationIdTest {

    @Test
    @DisplayName("应该能够创建有效的顾客关系ID")
    void should_create_valid_customer_relation_id() {
        // Arrange
        String validId = "customer-123";
        
        // Act
        CustomerRelationId customerRelationId = CustomerRelationId.of(validId);
        
        // Assert
        assertThat(customerRelationId).isNotNull();
        assertThat(customerRelationId.getValue()).isEqualTo(validId);
    }

    @Test
    @DisplayName("应该能够生成顾客关系ID")
    void should_generate_customer_relation_id() {
        // Act
        CustomerRelationId customerRelationId = CustomerRelationId.generate();
        
        // Assert
        assertThat(customerRelationId).isNotNull();
        assertThat(customerRelationId.getValue()).isNotNull().isNotEmpty();
        // 生成的ID应该是UUID格式
        assertThat(customerRelationId.getValue()).matches("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}");
    }

    @Test
    @DisplayName("生成的顾客关系ID应该是唯一的")
    void should_generate_unique_customer_relation_ids() {
        // Act
        CustomerRelationId id1 = CustomerRelationId.generate();
        CustomerRelationId id2 = CustomerRelationId.generate();
        
        // Assert
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1.getValue()).isNotEqualTo(id2.getValue());
    }

    @Test
    @DisplayName("应该在创建空字符串ID时抛出异常")
    void should_throw_exception_when_creating_with_empty_id() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerRelationId.of(""))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客关系ID不能为空");
    }

    @Test
    @DisplayName("应该在创建null ID时抛出异常")
    void should_throw_exception_when_creating_with_null_id() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerRelationId.of(null))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客关系ID不能为空");
    }

    @Test
    @DisplayName("相同值的顾客关系ID应该相等")
    void should_be_equal_when_same_value() {
        // Arrange
        String idValue = "customer-123";
        CustomerRelationId id1 = CustomerRelationId.of(idValue);
        CustomerRelationId id2 = CustomerRelationId.of(idValue);
        
        // Assert
        assertThat(id1).isEqualTo(id2);
        assertThat(id1.hashCode()).isEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("不同值的顾客关系ID应该不相等")
    void should_not_be_equal_when_different_value() {
        // Arrange
        CustomerRelationId id1 = CustomerRelationId.of("customer-123");
        CustomerRelationId id2 = CustomerRelationId.of("customer-456");
        
        // Assert
        assertThat(id1).isNotEqualTo(id2);
        assertThat(id1.hashCode()).isNotEqualTo(id2.hashCode());
    }

    @Test
    @DisplayName("toString应该返回ID值")
    void should_return_value_when_to_string() {
        // Arrange
        String idValue = "customer-123";
        CustomerRelationId customerRelationId = CustomerRelationId.of(idValue);
        
        // Act
        String result = customerRelationId.toString();
        
        // Assert
        assertThat(result).isEqualTo(idValue);
    }

    @Test
    @DisplayName("应该验证ID格式")
    void should_validate_id_format() {
        // Arrange
        String validId = "customer-123";
        
        // Act & Assert
        assertThatCode(() -> CustomerRelationId.of(validId))
                .doesNotThrowAnyException();
    }
} 