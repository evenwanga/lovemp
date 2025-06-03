package com.lovemp.domain.customer.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("AuthLevel 枚举测试")
class AuthLevelTest {

    @Test
    @DisplayName("应该包含所有预期的授权级别")
    void should_contain_all_expected_auth_levels() {
        // Act
        AuthLevel[] levels = AuthLevel.values();
        
        // Assert
        assertThat(levels).hasSize(3);
        assertThat(levels).containsExactlyInAnyOrder(
                AuthLevel.BASIC,
                AuthLevel.CONSUMPTION,
                AuthLevel.FULL
        );
    }

    @Test
    @DisplayName("BASIC级别应该有正确的属性")
    void should_have_correct_properties_for_basic() {
        // Act
        AuthLevel basic = AuthLevel.BASIC;
        
        // Assert
        assertThat(basic.getCode()).isEqualTo(1);
        assertThat(basic.getDescription()).isEqualTo("基础信息");
    }

    @Test
    @DisplayName("CONSUMPTION级别应该有正确的属性")
    void should_have_correct_properties_for_consumption() {
        // Act
        AuthLevel consumption = AuthLevel.CONSUMPTION;
        
        // Assert
        assertThat(consumption.getCode()).isEqualTo(2);
        assertThat(consumption.getDescription()).isEqualTo("消费信息");
    }

    @Test
    @DisplayName("FULL级别应该有正确的属性")
    void should_have_correct_properties_for_full() {
        // Act
        AuthLevel full = AuthLevel.FULL;
        
        // Assert
        assertThat(full.getCode()).isEqualTo(3);
        assertThat(full.getDescription()).isEqualTo("全部信息");
    }

    @ParameterizedTest
    @EnumSource(AuthLevel.class)
    @DisplayName("所有授权级别都应该有有效的编码和描述")
    void should_have_valid_code_and_description(AuthLevel level) {
        // Assert
        assertThat(level.getCode()).isGreaterThan(0);
        assertThat(level.getDescription()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("应该能够通过编码查找授权级别")
    void should_find_auth_level_by_code() {
        // Act & Assert
        assertThat(AuthLevel.fromCode(1)).isEqualTo(AuthLevel.BASIC);
        assertThat(AuthLevel.fromCode(2)).isEqualTo(AuthLevel.CONSUMPTION);
        assertThat(AuthLevel.fromCode(3)).isEqualTo(AuthLevel.FULL);
    }

    @Test
    @DisplayName("使用无效编码查找授权级别应该抛出异常")
    void should_throw_exception_when_finding_with_invalid_code() {
        // Act & Assert
        assertThatThrownBy(() -> AuthLevel.fromCode(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的授权级别编码");
        
        assertThatThrownBy(() -> AuthLevel.fromCode(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的授权级别编码");
        
        assertThatThrownBy(() -> AuthLevel.fromCode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的授权级别编码");
    }

    @Test
    @DisplayName("应该能够判断是否可以访问指定级别的数据")
    void should_check_if_can_access() {
        // Assert
        // BASIC级别只能访问BASIC
        assertThat(AuthLevel.BASIC.canAccess(AuthLevel.BASIC)).isTrue();
        assertThat(AuthLevel.BASIC.canAccess(AuthLevel.CONSUMPTION)).isFalse();
        assertThat(AuthLevel.BASIC.canAccess(AuthLevel.FULL)).isFalse();
        
        // CONSUMPTION级别可以访问BASIC和CONSUMPTION
        assertThat(AuthLevel.CONSUMPTION.canAccess(AuthLevel.BASIC)).isTrue();
        assertThat(AuthLevel.CONSUMPTION.canAccess(AuthLevel.CONSUMPTION)).isTrue();
        assertThat(AuthLevel.CONSUMPTION.canAccess(AuthLevel.FULL)).isFalse();
        
        // FULL级别可以访问所有级别
        assertThat(AuthLevel.FULL.canAccess(AuthLevel.BASIC)).isTrue();
        assertThat(AuthLevel.FULL.canAccess(AuthLevel.CONSUMPTION)).isTrue();
        assertThat(AuthLevel.FULL.canAccess(AuthLevel.FULL)).isTrue();
    }

    @Test
    @DisplayName("应该能够判断是否为基础级别")
    void should_check_if_basic() {
        // Assert
        assertThat(AuthLevel.BASIC.isBasic()).isTrue();
        assertThat(AuthLevel.CONSUMPTION.isBasic()).isFalse();
        assertThat(AuthLevel.FULL.isBasic()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为消费级别")
    void should_check_if_consumption() {
        // Assert
        assertThat(AuthLevel.BASIC.isConsumption()).isFalse();
        assertThat(AuthLevel.CONSUMPTION.isConsumption()).isTrue();
        assertThat(AuthLevel.FULL.isConsumption()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为完整级别")
    void should_check_if_full() {
        // Assert
        assertThat(AuthLevel.BASIC.isFull()).isFalse();
        assertThat(AuthLevel.CONSUMPTION.isFull()).isFalse();
        assertThat(AuthLevel.FULL.isFull()).isTrue();
    }
} 