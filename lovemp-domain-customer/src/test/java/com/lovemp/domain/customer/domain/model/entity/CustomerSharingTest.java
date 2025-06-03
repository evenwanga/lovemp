package com.lovemp.domain.customer.domain.model.entity;

import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.domain.customer.domain.model.valueobject.*;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CustomerSharing 实体测试")
class CustomerSharingTest {

    private SharingId sharingId;
    private String sourceBrandId;
    private String targetBrandId;
    private PersonId personId;
    private SharingType sharingType;
    private AuthLevel authLevel;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        sharingId = SharingId.generate();
        sourceBrandId = "brand-source";
        targetBrandId = "brand-target";
        personId = PersonId.of("person-456");
        sharingType = SharingType.AUTO;
        authLevel = AuthLevel.BASIC;
        startDate = LocalDate.now();
        endDate = LocalDate.now().plusDays(30);
    }

    @Test
    @DisplayName("应该能够创建顾客共享关系")
    void should_create_customer_sharing() {
        // Act
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );

        // Assert
        assertThat(sharing).isNotNull();
        assertThat(sharing.getId()).isEqualTo(sharingId);
        assertThat(sharing.getSourceBrandId()).isEqualTo(sourceBrandId);
        assertThat(sharing.getTargetBrandId()).isEqualTo(targetBrandId);
        assertThat(sharing.getPersonId()).isEqualTo(personId);
        assertThat(sharing.getSharingType()).isEqualTo(sharingType);
        assertThat(sharing.getAuthLevel()).isEqualTo(authLevel);
        assertThat(sharing.getStartDate()).isEqualTo(startDate);
        assertThat(sharing.getEndDate()).isEqualTo(endDate);
        assertThat(sharing.getStatus()).isEqualTo(SharingStatus.PENDING);
        assertThat(sharing.getCreateDate()).isEqualTo(LocalDate.now());
        assertThat(sharing.getLastUpdateDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("创建顾客共享关系时参数不能为空")
    void should_throw_exception_when_creating_with_null_parameters() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerSharing.create(null, sourceBrandId, targetBrandId, personId, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("共享ID不能为空");

        assertThatThrownBy(() -> CustomerSharing.create(sharingId, null, targetBrandId, personId, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("源品牌ID不能为空");

        assertThatThrownBy(() -> CustomerSharing.create(sharingId, "", targetBrandId, personId, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("源品牌ID不能为空");

        assertThatThrownBy(() -> CustomerSharing.create(sharingId, sourceBrandId, null, personId, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("目标品牌ID不能为空");

        assertThatThrownBy(() -> CustomerSharing.create(sharingId, sourceBrandId, "", personId, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("目标品牌ID不能为空");

        assertThatThrownBy(() -> CustomerSharing.create(sharingId, sourceBrandId, targetBrandId, null, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("自然人ID不能为空");

        assertThatThrownBy(() -> CustomerSharing.create(sharingId, sourceBrandId, targetBrandId, personId, null, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("共享类型不能为空");

        assertThatThrownBy(() -> CustomerSharing.create(sharingId, sourceBrandId, targetBrandId, personId, sharingType, null, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("授权级别不能为空");

        assertThatThrownBy(() -> CustomerSharing.create(sharingId, sourceBrandId, targetBrandId, personId, sharingType, authLevel, null, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("开始日期不能为空");
    }

    @Test
    @DisplayName("源品牌和目标品牌不能相同")
    void should_throw_exception_when_source_and_target_brand_are_same() {
        // Act & Assert
        assertThatThrownBy(() -> CustomerSharing.create(
                sharingId, sourceBrandId, sourceBrandId, personId,
                sharingType, authLevel, startDate, endDate
        ))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("源品牌和目标品牌不能相同");
    }

    @Test
    @DisplayName("结束日期不能早于开始日期")
    void should_throw_exception_when_end_date_before_start_date() {
        // Arrange
        LocalDate invalidEndDate = startDate.minusDays(1);

        // Act & Assert
        assertThatThrownBy(() -> CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, invalidEndDate
        ))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("结束日期不能早于开始日期");
    }

    @Test
    @DisplayName("应该能够创建永久有效的共享关系")
    void should_create_permanent_sharing() {
        // Act
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, null
        );

        // Assert
        assertThat(sharing.getEndDate()).isNull();
        assertThat(sharing.isInValidPeriod()).isTrue();
    }

    @Test
    @DisplayName("应该能够激活共享关系")
    void should_activate_sharing() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );

        // Act
        sharing.activate();

        // Assert
        assertThat(sharing.getStatus()).isEqualTo(SharingStatus.ACTIVE);
        assertThat(sharing.getLastUpdateDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("重复激活已激活的共享关系应该无效果")
    void should_do_nothing_when_activating_already_active_sharing() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );
        sharing.activate();
        LocalDate lastUpdateDate = sharing.getLastUpdateDate();

        // Act
        sharing.activate();

        // Assert
        assertThat(sharing.getStatus()).isEqualTo(SharingStatus.ACTIVE);
        assertThat(sharing.getLastUpdateDate()).isEqualTo(lastUpdateDate);
    }

    @Test
    @DisplayName("不在有效期内不能激活")
    void should_throw_exception_when_activating_out_of_valid_period() {
        // Arrange - 创建一个已过期的共享关系
        LocalDate pastStartDate = LocalDate.now().minusDays(10);
        LocalDate pastEndDate = LocalDate.now().minusDays(1);
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, pastStartDate, pastEndDate
        );

        // Act & Assert
        assertThatThrownBy(() -> sharing.activate())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("不在有效期内，无法激活");
    }

    @Test
    @DisplayName("应该能够使共享关系失效")
    void should_deactivate_sharing() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );
        sharing.activate();
        String reason = "测试失效";

        // Act
        sharing.deactivate(reason);

        // Assert
        assertThat(sharing.getStatus()).isEqualTo(SharingStatus.INACTIVE);
        assertThat(sharing.getLastUpdateDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("失效原因不能为空")
    void should_throw_exception_when_deactivating_with_empty_reason() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );

        // Act & Assert
        assertThatThrownBy(() -> sharing.deactivate(null))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("失效原因不能为空");

        assertThatThrownBy(() -> sharing.deactivate(""))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("失效原因不能为空");
    }

    @Test
    @DisplayName("应该能够更新授权级别")
    void should_update_auth_level() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );
        sharing.activate();
        AuthLevel newAuthLevel = AuthLevel.FULL;

        // Act
        sharing.updateAuthLevel(newAuthLevel);

        // Assert
        assertThat(sharing.getAuthLevel()).isEqualTo(newAuthLevel);
        assertThat(sharing.getLastUpdateDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("只有激活状态的共享关系才能更新授权级别")
    void should_throw_exception_when_updating_auth_level_for_inactive_sharing() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );
        AuthLevel newAuthLevel = AuthLevel.FULL;

        // Act & Assert
        assertThatThrownBy(() -> sharing.updateAuthLevel(newAuthLevel))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("只有激活状态的共享关系才能更新授权级别");
    }

    @Test
    @DisplayName("应该能够延长有效期")
    void should_extend_valid_period() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );
        LocalDate newEndDate = endDate.plusDays(30);

        // Act
        sharing.extendValidPeriod(newEndDate);

        // Assert
        assertThat(sharing.getEndDate()).isEqualTo(newEndDate);
        assertThat(sharing.getLastUpdateDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("新的结束日期必须晚于当前结束日期")
    void should_throw_exception_when_extending_with_earlier_date() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );
        LocalDate earlierDate = endDate.minusDays(1);

        // Act & Assert
        assertThatThrownBy(() -> sharing.extendValidPeriod(earlierDate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("新的结束日期必须晚于当前结束日期");
    }

    @Test
    @DisplayName("新的结束日期不能早于开始日期")
    void should_throw_exception_when_extending_with_date_before_start() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );
        LocalDate invalidDate = startDate.minusDays(1);

        // Act & Assert
        assertThatThrownBy(() -> sharing.extendValidPeriod(invalidDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("新的结束日期不能早于开始日期");
    }

    @Test
    @DisplayName("应该能够正确判断是否在有效期内")
    void should_check_if_in_valid_period_correctly() {
        // Arrange - 当前有效的共享关系
        CustomerSharing currentSharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, LocalDate.now().minusDays(1), LocalDate.now().plusDays(1)
        );

        // Arrange - 未来的共享关系
        CustomerSharing futureSharing = CustomerSharing.create(
                SharingId.generate(), sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, LocalDate.now().plusDays(1), LocalDate.now().plusDays(10)
        );

        // Arrange - 过期的共享关系
        CustomerSharing expiredSharing = CustomerSharing.create(
                SharingId.generate(), sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, LocalDate.now().minusDays(10), LocalDate.now().minusDays(1)
        );

        // Arrange - 永久有效的共享关系
        CustomerSharing permanentSharing = CustomerSharing.create(
                SharingId.generate(), sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, LocalDate.now().minusDays(1), null
        );

        // Assert
        assertThat(currentSharing.isInValidPeriod()).isTrue();
        assertThat(futureSharing.isInValidPeriod()).isFalse();
        assertThat(expiredSharing.isInValidPeriod()).isFalse();
        assertThat(permanentSharing.isInValidPeriod()).isTrue();
    }

    @Test
    @DisplayName("应该能够正确判断是否可以访问指定级别的数据")
    void should_check_if_can_access_correctly() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, AuthLevel.CONSUMPTION, startDate, endDate
        );
        sharing.activate();

        // Act & Assert
        assertThat(sharing.canAccess(AuthLevel.BASIC)).isTrue();
        assertThat(sharing.canAccess(AuthLevel.CONSUMPTION)).isTrue();
        assertThat(sharing.canAccess(AuthLevel.FULL)).isFalse();
    }

    @Test
    @DisplayName("非激活状态不能访问数据")
    void should_not_access_when_not_active() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );

        // Act & Assert
        assertThat(sharing.canAccess(AuthLevel.BASIC)).isFalse();
    }

    @Test
    @DisplayName("不在有效期内不能访问数据")
    void should_not_access_when_out_of_valid_period() {
        // Arrange
        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, LocalDate.now().minusDays(10), LocalDate.now().minusDays(1)
        );

        // Act & Assert
        assertThat(sharing.canAccess(AuthLevel.BASIC)).isFalse();
    }

    @Test
    @DisplayName("应该能够判断共享类型")
    void should_check_sharing_type() {
        // Arrange
        CustomerSharing autoSharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                SharingType.AUTO, authLevel, startDate, endDate
        );
        CustomerSharing authSharing = CustomerSharing.create(
                SharingId.generate(), sourceBrandId, targetBrandId, personId,
                SharingType.AUTH, authLevel, startDate, endDate
        );
        CustomerSharing relatedSharing = CustomerSharing.create(
                SharingId.generate(), sourceBrandId, targetBrandId, personId,
                SharingType.RELATED, authLevel, startDate, endDate
        );

        // Assert
        assertThat(autoSharing.isAutoSharing()).isTrue();
        assertThat(autoSharing.isAuthSharing()).isFalse();
        assertThat(autoSharing.isRelatedSharing()).isFalse();

        assertThat(authSharing.isAutoSharing()).isFalse();
        assertThat(authSharing.isAuthSharing()).isTrue();
        assertThat(authSharing.isRelatedSharing()).isFalse();

        assertThat(relatedSharing.isAutoSharing()).isFalse();
        assertThat(relatedSharing.isAuthSharing()).isFalse();
        assertThat(relatedSharing.isRelatedSharing()).isTrue();
    }

    @Test
    @DisplayName("应该能够检查是否即将过期")
    void should_check_if_expiring_soon() {
        // Arrange
        SharingId sharingId = SharingId.generate();
        String sourceBrandId = "brand-source";
        String targetBrandId = "brand-target";
        PersonId personId = PersonId.of("person-123");
        SharingType sharingType = SharingType.AUTO;
        AuthLevel authLevel = AuthLevel.BASIC;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(5); // 5天后过期，即将过期

        CustomerSharing sharing = CustomerSharing.create(
                sharingId, sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, endDate
        );
        sharing.activate();

        // Act & Assert
        assertThat(sharing.isExpiringSoon()).isTrue(); // 5天后过期，在7天预警期内
        
        // 测试不会即将过期的情况
        CustomerSharing notExpiring = CustomerSharing.create(
                SharingId.generate(), sourceBrandId, targetBrandId, personId,
                sharingType, authLevel, startDate, LocalDate.now().plusDays(10)
        );
        notExpiring.activate();
        assertThat(notExpiring.isExpiringSoon()).isFalse(); // 10天后过期，不在7天预警期内
    }
} 