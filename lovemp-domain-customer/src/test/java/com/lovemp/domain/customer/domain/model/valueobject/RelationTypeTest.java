package com.lovemp.domain.customer.domain.model.valueobject;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("RelationType 枚举测试")
class RelationTypeTest {

    @Test
    @DisplayName("应该包含所有预期的关系类型")
    void should_contain_all_expected_relation_types() {
        // Act
        RelationType[] types = RelationType.values();
        
        // Assert
        assertThat(types).hasSize(3);
        assertThat(types).containsExactlyInAnyOrder(
                RelationType.ACTIVE_REGISTRATION,
                RelationType.IMPORTED,
                RelationType.DERIVED
        );
    }

    @Test
    @DisplayName("ACTIVE_REGISTRATION类型应该有正确的属性")
    void should_have_correct_properties_for_active_registration() {
        // Act
        RelationType activeRegistration = RelationType.ACTIVE_REGISTRATION;
        
        // Assert
        assertThat(activeRegistration.getCode()).isEqualTo(1);
        assertThat(activeRegistration.getDescription()).isEqualTo("主动注册");
    }

    @Test
    @DisplayName("IMPORTED类型应该有正确的属性")
    void should_have_correct_properties_for_imported() {
        // Act
        RelationType imported = RelationType.IMPORTED;
        
        // Assert
        assertThat(imported.getCode()).isEqualTo(2);
        assertThat(imported.getDescription()).isEqualTo("导入");
    }

    @Test
    @DisplayName("DERIVED类型应该有正确的属性")
    void should_have_correct_properties_for_derived() {
        // Act
        RelationType derived = RelationType.DERIVED;
        
        // Assert
        assertThat(derived.getCode()).isEqualTo(3);
        assertThat(derived.getDescription()).isEqualTo("衍生关系");
    }

    @ParameterizedTest
    @EnumSource(RelationType.class)
    @DisplayName("所有关系类型都应该有有效的编码和描述")
    void should_have_valid_code_and_description(RelationType type) {
        // Assert
        assertThat(type.getCode()).isGreaterThan(0);
        assertThat(type.getDescription()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("应该能够通过编码查找关系类型")
    void should_find_relation_type_by_code() {
        // Act & Assert
        assertThat(RelationType.fromCode(1)).isEqualTo(RelationType.ACTIVE_REGISTRATION);
        assertThat(RelationType.fromCode(2)).isEqualTo(RelationType.IMPORTED);
        assertThat(RelationType.fromCode(3)).isEqualTo(RelationType.DERIVED);
    }

    @Test
    @DisplayName("使用无效编码查找关系类型应该抛出异常")
    void should_throw_exception_when_finding_with_invalid_code() {
        // Act & Assert
        assertThatThrownBy(() -> RelationType.fromCode(0))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的关系类型编码");
        
        assertThatThrownBy(() -> RelationType.fromCode(4))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的关系类型编码");
        
        assertThatThrownBy(() -> RelationType.fromCode(-1))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("未知的关系类型编码");
    }

    @Test
    @DisplayName("应该能够判断是否为主动注册")
    void should_check_if_active_registration() {
        // Assert
        assertThat(RelationType.ACTIVE_REGISTRATION.isActiveRegistration()).isTrue();
        assertThat(RelationType.IMPORTED.isActiveRegistration()).isFalse();
        assertThat(RelationType.DERIVED.isActiveRegistration()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为导入关系")
    void should_check_if_imported() {
        // Assert
        assertThat(RelationType.ACTIVE_REGISTRATION.isImported()).isFalse();
        assertThat(RelationType.IMPORTED.isImported()).isTrue();
        assertThat(RelationType.DERIVED.isImported()).isFalse();
    }

    @Test
    @DisplayName("应该能够判断是否为衍生关系")
    void should_check_if_derived() {
        // Assert
        assertThat(RelationType.ACTIVE_REGISTRATION.isDerived()).isFalse();
        assertThat(RelationType.IMPORTED.isDerived()).isFalse();
        assertThat(RelationType.DERIVED.isDerived()).isTrue();
    }
} 