package com.lovemp.domain.customer.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CustomerType 枚举测试")
class CustomerTypeTest {

    @Test
    @DisplayName("应该包含所有预期的顾客类型")
    void should_contain_all_expected_customer_types() {
        // Act
        CustomerType[] types = CustomerType.values();
        
        // Assert
        assertThat(types).hasSize(3);
        assertThat(types).containsExactlyInAnyOrder(
                CustomerType.NORMAL,
                CustomerType.VIP,
                CustomerType.WHOLESALE
        );
    }

    @Test
    @DisplayName("NORMAL类型应该有正确的属性")
    void should_have_correct_properties_for_normal() {
        // Act
        CustomerType normal = CustomerType.NORMAL;
        
        // Assert
        assertThat(normal.getCode()).isEqualTo(1);
        assertThat(normal.getDescription()).isEqualTo("普通顾客");
    }

    @Test
    @DisplayName("VIP类型应该有正确的属性")
    void should_have_correct_properties_for_vip() {
        // Act
        CustomerType vip = CustomerType.VIP;
        
        // Assert
        assertThat(vip.getCode()).isEqualTo(2);
        assertThat(vip.getDescription()).isEqualTo("VIP顾客");
    }

    @Test
    @DisplayName("WHOLESALE类型应该有正确的属性")
    void should_have_correct_properties_for_wholesale() {
        // Act
        CustomerType wholesale = CustomerType.WHOLESALE;
        
        // Assert
        assertThat(wholesale.getCode()).isEqualTo(3);
        assertThat(wholesale.getDescription()).isEqualTo("批发顾客");
    }

    @ParameterizedTest
    @EnumSource(CustomerType.class)
    @DisplayName("所有顾客类型都应该有有效的编码和描述")
    void should_have_valid_code_and_description(CustomerType type) {
        // Assert
        assertThat(type.getCode()).isGreaterThan(0);
        assertThat(type.getDescription()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("应该能够通过编码查找顾客类型")
    void should_find_customer_type_by_code() {
        // Act & Assert
        assertThat(CustomerType.fromCode(1)).isEqualTo(CustomerType.NORMAL);
        assertThat(CustomerType.fromCode(2)).isEqualTo(CustomerType.VIP);
        assertThat(CustomerType.fromCode(3)).isEqualTo(CustomerType.WHOLESALE);
    }

    @Test
    @DisplayName("使用无效编码查找顾客类型应该抛出异常")
    void should_throw_exception_when_finding_with_invalid_code() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerType.fromCode(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的顾客类型编码");
        
        assertThatThrownBy(() -> CustomerType.fromCode(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的顾客类型编码");
        
        assertThatThrownBy(() -> CustomerType.fromCode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的顾客类型编码");
    }

    @Test
    @DisplayName("应该能够判断是否为VIP顾客")
    void should_check_if_vip() {
        // Assert
        assertThat(CustomerType.NORMAL.isVip()).isFalse();
        assertThat(CustomerType.VIP.isVip()).isTrue();
        assertThat(CustomerType.WHOLESALE.isVip()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为批发顾客")
    void should_check_if_wholesale() {
        // Assert
        assertThat(CustomerType.NORMAL.isWholesale()).isFalse();
        assertThat(CustomerType.VIP.isWholesale()).isFalse();
        assertThat(CustomerType.WHOLESALE.isWholesale()).isTrue();
    }
} 