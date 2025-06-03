package com.lovemp.domain.customer.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CustomerStatus 枚举测试")
class CustomerStatusTest {

    @Test
    @DisplayName("应该包含所有预期的顾客状态")
    void should_contain_all_expected_customer_statuses() {
        // Act
        CustomerStatus[] statuses = CustomerStatus.values();
        
        // Assert
        assertThat(statuses).hasSize(3);
        assertThat(statuses).containsExactlyInAnyOrder(
                CustomerStatus.INACTIVE,
                CustomerStatus.ACTIVE,
                CustomerStatus.FROZEN
        );
    }

    @Test
    @DisplayName("INACTIVE状态应该有正确的属性")
    void should_have_correct_properties_for_inactive() {
        // Act
        CustomerStatus inactive = CustomerStatus.INACTIVE;
        
        // Assert
        assertThat(inactive.getCode()).isEqualTo(0);
        assertThat(inactive.getDescription()).isEqualTo("未激活");
        assertThat(inactive.canOperate()).isFalse();
    }

    @Test
    @DisplayName("ACTIVE状态应该有正确的属性")
    void should_have_correct_properties_for_active() {
        // Act
        CustomerStatus active = CustomerStatus.ACTIVE;
        
        // Assert
        assertThat(active.getCode()).isEqualTo(1);
        assertThat(active.getDescription()).isEqualTo("正常");
        assertThat(active.canOperate()).isTrue();
    }

    @Test
    @DisplayName("FROZEN状态应该有正确的属性")
    void should_have_correct_properties_for_frozen() {
        // Act
        CustomerStatus frozen = CustomerStatus.FROZEN;
        
        // Assert
        assertThat(frozen.getCode()).isEqualTo(2);
        assertThat(frozen.getDescription()).isEqualTo("冻结");
        assertThat(frozen.canOperate()).isFalse();
    }

    @ParameterizedTest
    @EnumSource(CustomerStatus.class)
    @DisplayName("所有顾客状态都应该有有效的编码和描述")
    void should_have_valid_code_and_description(CustomerStatus status) {
        // Assert
        assertThat(status.getCode()).isGreaterThanOrEqualTo(0);
        assertThat(status.getDescription()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("应该能够通过编码查找顾客状态")
    void should_find_customer_status_by_code() {
        // Act & Assert
        assertThat(CustomerStatus.fromCode(0)).isEqualTo(CustomerStatus.INACTIVE);
        assertThat(CustomerStatus.fromCode(1)).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(CustomerStatus.fromCode(2)).isEqualTo(CustomerStatus.FROZEN);
    }

    @Test
    @DisplayName("使用无效编码查找顾客状态应该抛出异常")
    void should_throw_exception_when_finding_with_invalid_code() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerStatus.fromCode(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的顾客状态编码");
        
        assertThatThrownBy(() -> CustomerStatus.fromCode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的顾客状态编码");
        
        assertThatThrownBy(() -> CustomerStatus.fromCode(99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的顾客状态编码");
    }

    @Test
    @DisplayName("应该能够判断是否为激活状态")
    void should_check_if_active() {
        // Assert
        assertThat(CustomerStatus.INACTIVE.isActive()).isFalse();
        assertThat(CustomerStatus.ACTIVE.isActive()).isTrue();
        assertThat(CustomerStatus.FROZEN.isActive()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为冻结状态")
    void should_check_if_frozen() {
        // Assert
        assertThat(CustomerStatus.INACTIVE.isFrozen()).isFalse();
        assertThat(CustomerStatus.ACTIVE.isFrozen()).isFalse();
        assertThat(CustomerStatus.FROZEN.isFrozen()).isTrue();
    }

    @Test
    @DisplayName("应该能够判断是否为未激活状态")
    void should_check_if_inactive() {
        // Assert
        assertThat(CustomerStatus.INACTIVE.isInactive()).isTrue();
        assertThat(CustomerStatus.ACTIVE.isInactive()).isFalse();
        assertThat(CustomerStatus.FROZEN.isInactive()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否可以进行业务操作")
    void should_check_if_can_operate() {
        // Assert
        assertThat(CustomerStatus.INACTIVE.canOperate()).isFalse();
        assertThat(CustomerStatus.ACTIVE.canOperate()).isTrue();
        assertThat(CustomerStatus.FROZEN.canOperate()).isFalse();
    }
} 