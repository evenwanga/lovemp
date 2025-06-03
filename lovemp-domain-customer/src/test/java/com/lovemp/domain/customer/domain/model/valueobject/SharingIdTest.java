package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.exception.DomainRuleViolationException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.*;

@DisplayName("SharingId 值对象测试")
class SharingIdTest {

    @Test
    @DisplayName("应该能够创建有效的共享ID")
    void should_create_valid_sharing_id() {
        // Arrange
        String validId = "sharing-123";
        
        // Act
        SharingId sharingId = SharingId.of(validId);
        
        // Assert
        assertThat(sharingId).isNotNull();
        assertThat(sharingId.getValue()).isEqualTo(validId);
    }

    @Test
    @DisplayName("应该能够生成共享ID")
    void should_generate_sharing_id() {
        // Act
        SharingId sharingId = SharingId.generate();
        
        // Assert
        assertThat(sharingId).isNotNull();
        assertThat(sharingId.getValue()).isNotNull().isNotEmpty();
    }

    @Test
    @DisplayName("生成的共享ID应该是唯一的")
    void should_generate_unique_sharing_ids() {
        // Act
        SharingId id1 = SharingId.generate();
        SharingId id2 = SharingId.generate();
        
        // Assert
        assertThat(id1.getValue()).isNotEqualTo(id2.getValue());
    }

    @Test
    @DisplayName("创建共享ID时空值应该抛出异常")
    void should_throw_exception_when_creating_with_null_value() {
        // Act & Assert
        assertThatThrownBy(() -> SharingId.of(null))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("共享ID不能为空");
    }

    @Test
    @DisplayName("创建共享ID时空字符串应该抛出异常")
    void should_throw_exception_when_creating_with_empty_value() {
        // Act & Assert
        assertThatThrownBy(() -> SharingId.of(""))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("共享ID不能为空");
    }

    @Test
    @DisplayName("相同值的共享ID应该相等")
    void should_be_equal_when_same_value() {
        // Arrange
        String id = "sharing-123";
        SharingId sharingId1 = SharingId.of(id);
        SharingId sharingId2 = SharingId.of(id);
        
        // Act & Assert
        assertThat(sharingId1).isEqualTo(sharingId2);
        assertThat(sharingId1.hashCode()).isEqualTo(sharingId2.hashCode());
    }

    @Test
    @DisplayName("不同值的共享ID应该不相等")
    void should_not_be_equal_when_different_value() {
        // Arrange
        SharingId sharingId1 = SharingId.of("sharing-123");
        SharingId sharingId2 = SharingId.of("sharing-456");
        
        // Act & Assert
        assertThat(sharingId1).isNotEqualTo(sharingId2);
    }

    @Test
    @DisplayName("toString方法应该返回ID值")
    void should_return_value_in_toString() {
        // Arrange
        String id = "sharing-123";
        SharingId sharingId = SharingId.of(id);
        
        // Act & Assert
        assertThat(sharingId.toString()).isEqualTo(id);
    }
} 