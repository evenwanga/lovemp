package com.lovemp.domain.customer.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SharingStatus 枚举测试")
class SharingStatusTest {

    @Test
    @DisplayName("应该包含所有预期的共享状态")
    void should_contain_all_expected_sharing_statuses() {
        // Act
        SharingStatus[] statuses = SharingStatus.values();
        
        // Assert
        assertThat(statuses).hasSize(3);
        assertThat(statuses).containsExactlyInAnyOrder(
                SharingStatus.PENDING,
                SharingStatus.ACTIVE,
                SharingStatus.INACTIVE
        );
    }

    @Test
    @DisplayName("PENDING状态应该有正确的属性")
    void should_have_correct_properties_for_pending() {
        // Act
        SharingStatus pending = SharingStatus.PENDING;
        
        // Assert
        assertThat(pending.getCode()).isEqualTo(0);
        assertThat(pending.getDescription()).isEqualTo("待生效");
    }

    @Test
    @DisplayName("ACTIVE状态应该有正确的属性")
    void should_have_correct_properties_for_active() {
        // Act
        SharingStatus active = SharingStatus.ACTIVE;
        
        // Assert
        assertThat(active.getCode()).isEqualTo(1);
        assertThat(active.getDescription()).isEqualTo("生效中");
    }

    @Test
    @DisplayName("INACTIVE状态应该有正确的属性")
    void should_have_correct_properties_for_inactive() {
        // Act
        SharingStatus inactive = SharingStatus.INACTIVE;
        
        // Assert
        assertThat(inactive.getCode()).isEqualTo(2);
        assertThat(inactive.getDescription()).isEqualTo("已失效");
    }

    @ParameterizedTest
    @EnumSource(SharingStatus.class)
    @DisplayName("所有共享状态都应该有有效的编码和描述")
    void should_have_valid_code_and_description(SharingStatus status) {
        // Assert
        assertThat(status.getCode()).isGreaterThanOrEqualTo(0);
        assertThat(status.getDescription()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("应该能够通过编码查找共享状态")
    void should_find_sharing_status_by_code() {
        // Act & Assert
        assertThat(SharingStatus.fromCode(0)).isEqualTo(SharingStatus.PENDING);
        assertThat(SharingStatus.fromCode(1)).isEqualTo(SharingStatus.ACTIVE);
        assertThat(SharingStatus.fromCode(2)).isEqualTo(SharingStatus.INACTIVE);
    }

    @Test
    @DisplayName("使用无效编码查找共享状态应该抛出异常")
    void should_throw_exception_when_finding_with_invalid_code() {
        // Act & Assert
        assertThatThrownBy(() -> SharingStatus.fromCode(3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的共享状态编码");
        
        assertThatThrownBy(() -> SharingStatus.fromCode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的共享状态编码");
        
        assertThatThrownBy(() -> SharingStatus.fromCode(99))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的共享状态编码");
    }

    @Test
    @DisplayName("应该能够判断是否为待生效状态")
    void should_check_if_pending() {
        // Assert
        assertThat(SharingStatus.PENDING.isPending()).isTrue();
        assertThat(SharingStatus.ACTIVE.isPending()).isFalse();
        assertThat(SharingStatus.INACTIVE.isPending()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为生效状态")
    void should_check_if_active() {
        // Assert
        assertThat(SharingStatus.PENDING.isActive()).isFalse();
        assertThat(SharingStatus.ACTIVE.isActive()).isTrue();
        assertThat(SharingStatus.INACTIVE.isActive()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为失效状态")
    void should_check_if_inactive() {
        // Assert
        assertThat(SharingStatus.PENDING.isInactive()).isFalse();
        assertThat(SharingStatus.ACTIVE.isInactive()).isFalse();
        assertThat(SharingStatus.INACTIVE.isInactive()).isTrue();
    }
} 