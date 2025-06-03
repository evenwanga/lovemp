package com.lovemp.domain.customer.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SharingType 枚举测试")
class SharingTypeTest {

    @Test
    @DisplayName("应该包含所有预期的共享类型")
    void should_contain_all_expected_sharing_types() {
        // Act
        SharingType[] types = SharingType.values();
        
        // Assert
        assertThat(types).hasSize(3);
        assertThat(types).containsExactlyInAnyOrder(
                SharingType.AUTO,
                SharingType.AUTH,
                SharingType.RELATED
        );
    }

    @Test
    @DisplayName("AUTO类型应该有正确的属性")
    void should_have_correct_properties_for_auto() {
        // Act
        SharingType auto = SharingType.AUTO;
        
        // Assert
        assertThat(auto.getCode()).isEqualTo(1);
        assertThat(auto.getDescription()).isEqualTo("自动共享");
    }

    @Test
    @DisplayName("AUTH类型应该有正确的属性")
    void should_have_correct_properties_for_auth() {
        // Act
        SharingType auth = SharingType.AUTH;
        
        // Assert
        assertThat(auth.getCode()).isEqualTo(2);
        assertThat(auth.getDescription()).isEqualTo("授权共享");
    }

    @Test
    @DisplayName("RELATED类型应该有正确的属性")
    void should_have_correct_properties_for_related() {
        // Act
        SharingType related = SharingType.RELATED;
        
        // Assert
        assertThat(related.getCode()).isEqualTo(3);
        assertThat(related.getDescription()).isEqualTo("关联共享");
    }

    @ParameterizedTest
    @EnumSource(SharingType.class)
    @DisplayName("所有共享类型都应该有有效的编码和描述")
    void should_have_valid_code_and_description(SharingType type) {
        // Assert
        assertThat(type.getCode()).isGreaterThan(0);
        assertThat(type.getDescription()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("应该能够通过编码查找共享类型")
    void should_find_sharing_type_by_code() {
        // Act & Assert
        assertThat(SharingType.fromCode(1)).isEqualTo(SharingType.AUTO);
        assertThat(SharingType.fromCode(2)).isEqualTo(SharingType.AUTH);
        assertThat(SharingType.fromCode(3)).isEqualTo(SharingType.RELATED);
    }

    @Test
    @DisplayName("使用无效编码查找共享类型应该抛出异常")
    void should_throw_exception_when_finding_with_invalid_code() {
        // Act & Assert
        assertThatThrownBy(() -> SharingType.fromCode(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的共享类型编码");
        
        assertThatThrownBy(() -> SharingType.fromCode(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的共享类型编码");
        
        assertThatThrownBy(() -> SharingType.fromCode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的共享类型编码");
    }

    @Test
    @DisplayName("应该能够判断是否为自动共享")
    void should_check_if_auto() {
        // Assert
        assertThat(SharingType.AUTO.isAuto()).isTrue();
        assertThat(SharingType.AUTH.isAuto()).isFalse();
        assertThat(SharingType.RELATED.isAuto()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为授权共享")
    void should_check_if_auth() {
        // Assert
        assertThat(SharingType.AUTO.isAuth()).isFalse();
        assertThat(SharingType.AUTH.isAuth()).isTrue();
        assertThat(SharingType.RELATED.isAuth()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为关联共享")
    void should_check_if_related() {
        // Assert
        assertThat(SharingType.AUTO.isRelated()).isFalse();
        assertThat(SharingType.AUTH.isRelated()).isFalse();
        assertThat(SharingType.RELATED.isRelated()).isTrue();
    }
} 