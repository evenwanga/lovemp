package com.lovemp.domain.customer.domain.service;

import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.domain.customer.domain.model.aggregate.BrandCustomer;
import com.lovemp.domain.customer.domain.model.entity.CustomerSharing;
import com.lovemp.domain.customer.domain.model.valueobject.*;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@DisplayName("CustomerDomainService 领域服务测试")
class CustomerDomainServiceTest {

    private CustomerDomainService domainService;
    private String brandId;
    private PersonId personId;
    private CustomerType customerType;
    private RelationType relationType;

    @BeforeEach
    void setUp() {
        brandId = "brand-123";
        personId = PersonId.of("person-456");
        customerType = CustomerType.NORMAL;
        relationType = RelationType.ACTIVE_REGISTRATION;
        
        domainService = new CustomerDomainService();
    }

    @Test
    @DisplayName("应该能够创建品牌顾客关系")
    void should_create_brand_customer_relation() {
        // Arrange
        String brandCode = "BR001";
        long sequence = 1L;

        // Act
        BrandCustomer customer = domainService.createBrandCustomer(
                brandId, personId, customerType, relationType, brandCode, sequence
        );

        // Assert
        assertThat(customer).isNotNull();
        assertThat(customer.getBrandId()).isEqualTo(brandId);
        assertThat(customer.getPersonId()).isEqualTo(personId);
        assertThat(customer.getCustomerType()).isEqualTo(customerType);
        assertThat(customer.getRelationType()).isEqualTo(relationType);
        assertThat(customer.getStatus()).isEqualTo(CustomerStatus.INACTIVE);
        assertThat(customer.getPoints()).isEqualTo(0);
        assertThat(customer.getLevel()).isEqualTo(1);
    }

    @Test
    @DisplayName("创建品牌顾客关系时参数不能为空")
    void should_throw_exception_when_creating_brand_customer_with_null_parameters() {
        // Arrange
        CustomerType customerType = CustomerType.NORMAL;
        RelationType relationType = RelationType.ACTIVE_REGISTRATION;
        String brandCode = "BR001";
        long sequence = 1L;

        // Act & Assert
        assertThatThrownBy(() -> domainService.createBrandCustomer(null, personId, customerType, relationType, brandCode, sequence))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("品牌ID不能为空");

        assertThatThrownBy(() -> domainService.createBrandCustomer("", personId, customerType, relationType, brandCode, sequence))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("品牌ID不能为空");

        assertThatThrownBy(() -> domainService.createBrandCustomer(brandId, null, customerType, relationType, brandCode, sequence))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("自然人ID不能为空");

        assertThatThrownBy(() -> domainService.createBrandCustomer(brandId, personId, null, relationType, brandCode, sequence))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客类型不能为空");

        assertThatThrownBy(() -> domainService.createBrandCustomer(brandId, personId, customerType, null, brandCode, sequence))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("关系类型不能为空");

        assertThatThrownBy(() -> domainService.createBrandCustomer(brandId, personId, customerType, relationType, null, sequence))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("品牌编码不能为空");

        assertThatThrownBy(() -> domainService.createBrandCustomer(brandId, personId, customerType, relationType, "", sequence))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("品牌编码不能为空");

        assertThatThrownBy(() -> domainService.createBrandCustomer(brandId, personId, customerType, relationType, brandCode, 0))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("序号必须大于0");

        assertThatThrownBy(() -> domainService.createBrandCustomer(brandId, personId, customerType, relationType, brandCode, -1))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("序号必须大于0");
    }

    @Test
    @DisplayName("应该能够创建顾客共享关系")
    void should_create_customer_sharing_relation() {
        // Arrange
        String sourceBrandId = "brand-source";
        String targetBrandId = "brand-target";
        SharingType sharingType = SharingType.AUTO;
        AuthLevel authLevel = AuthLevel.BASIC;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(30);

        // Act
        CustomerSharing sharing = domainService.createCustomerSharing(
                sourceBrandId, targetBrandId, personId, sharingType, authLevel, startDate, endDate
        );

        // Assert
        assertThat(sharing).isNotNull();
        assertThat(sharing.getSourceBrandId()).isEqualTo(sourceBrandId);
        assertThat(sharing.getTargetBrandId()).isEqualTo(targetBrandId);
        assertThat(sharing.getPersonId()).isEqualTo(personId);
        assertThat(sharing.getSharingType()).isEqualTo(sharingType);
        assertThat(sharing.getAuthLevel()).isEqualTo(authLevel);
        assertThat(sharing.getStartDate()).isEqualTo(startDate);
        assertThat(sharing.getEndDate()).isEqualTo(endDate);
        assertThat(sharing.getStatus()).isEqualTo(SharingStatus.PENDING);
    }

    @Test
    @DisplayName("创建顾客共享关系时参数不能为空")
    void should_throw_exception_when_creating_customer_sharing_with_null_parameters() {
        // Arrange
        String sourceBrandId = "brand-source";
        String targetBrandId = "brand-target";
        SharingType sharingType = SharingType.AUTO;
        AuthLevel authLevel = AuthLevel.BASIC;
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(30);

        // Act & Assert
        assertThatThrownBy(() -> domainService.createCustomerSharing(null, targetBrandId, personId, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("源品牌ID不能为空");

        assertThatThrownBy(() -> domainService.createCustomerSharing("", targetBrandId, personId, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("源品牌ID不能为空");

        assertThatThrownBy(() -> domainService.createCustomerSharing(sourceBrandId, null, personId, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("目标品牌ID不能为空");

        assertThatThrownBy(() -> domainService.createCustomerSharing(sourceBrandId, "", personId, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("目标品牌ID不能为空");

        assertThatThrownBy(() -> domainService.createCustomerSharing(sourceBrandId, targetBrandId, null, sharingType, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("自然人ID不能为空");

        assertThatThrownBy(() -> domainService.createCustomerSharing(sourceBrandId, targetBrandId, personId, null, authLevel, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("共享类型不能为空");

        assertThatThrownBy(() -> domainService.createCustomerSharing(sourceBrandId, targetBrandId, personId, sharingType, null, startDate, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("授权级别不能为空");

        assertThatThrownBy(() -> domainService.createCustomerSharing(sourceBrandId, targetBrandId, personId, sharingType, authLevel, null, endDate))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("开始日期不能为空");
    }

    @Test
    @DisplayName("应该能够检查是否可以创建共享关系")
    void should_check_if_can_create_sharing() {
        // Arrange
        String sourceBrandId = "brand-source";
        String targetBrandId = "brand-target";
        List<CustomerSharing> existingSharings = new ArrayList<>();

        // Act & Assert
        // 没有已存在的共享关系，可以创建
        assertThat(domainService.canCreateSharing(sourceBrandId, targetBrandId, personId, existingSharings)).isTrue();
        assertThat(domainService.canCreateSharing(sourceBrandId, targetBrandId, personId, null)).isTrue();
    }

    @Test
    @DisplayName("同一品牌不能创建共享关系")
    void should_not_create_sharing_for_same_brand() {
        // Arrange
        String sameBrandId = "brand-same";
        List<CustomerSharing> existingSharings = new ArrayList<>();

        // Act & Assert
        assertThat(domainService.canCreateSharing(sameBrandId, sameBrandId, personId, existingSharings)).isFalse();
    }

    @Test
    @DisplayName("已存在相同的激活或待生效共享关系时不能创建")
    void should_not_create_sharing_when_same_active_or_pending_exists() {
        // Arrange
        String sourceBrandId = "brand-source";
        String targetBrandId = "brand-target";
        
        CustomerSharing activeSharing = CustomerSharing.create(
                SharingId.generate(), sourceBrandId, targetBrandId, personId,
                SharingType.AUTO, AuthLevel.BASIC, LocalDate.now(), LocalDate.now().plusDays(30)
        );
        activeSharing.activate();
        
        CustomerSharing pendingSharing = CustomerSharing.create(
                SharingId.generate(), sourceBrandId, targetBrandId, personId,
                SharingType.AUTO, AuthLevel.BASIC, LocalDate.now(), LocalDate.now().plusDays(30)
        );
        
        List<CustomerSharing> existingSharings = List.of(activeSharing);
        List<CustomerSharing> pendingSharings = List.of(pendingSharing);

        // Act & Assert
        assertThat(domainService.canCreateSharing(sourceBrandId, targetBrandId, personId, existingSharings)).isFalse();
        assertThat(domainService.canCreateSharing(sourceBrandId, targetBrandId, personId, pendingSharings)).isFalse();
    }

    @Test
    @DisplayName("已存在失效共享关系时可以创建新的")
    void should_create_sharing_when_inactive_exists() {
        // Arrange
        String sourceBrandId = "brand-source";
        String targetBrandId = "brand-target";
        
        CustomerSharing inactiveSharing = CustomerSharing.create(
                SharingId.generate(), sourceBrandId, targetBrandId, personId,
                SharingType.AUTO, AuthLevel.BASIC, LocalDate.now(), LocalDate.now().plusDays(30)
        );
        inactiveSharing.activate();
        inactiveSharing.deactivate("测试失效");
        
        List<CustomerSharing> existingSharings = List.of(inactiveSharing);

        // Act & Assert
        assertThat(domainService.canCreateSharing(sourceBrandId, targetBrandId, personId, existingSharings)).isTrue();
    }

    @Test
    @DisplayName("应该能够计算普通顾客等级")
    void should_calculate_normal_customer_level() {
        // Act & Assert
        assertThat(domainService.calculateCustomerLevel(0, CustomerType.NORMAL)).isEqualTo(1);
        assertThat(domainService.calculateCustomerLevel(499, CustomerType.NORMAL)).isEqualTo(1);
        assertThat(domainService.calculateCustomerLevel(500, CustomerType.NORMAL)).isEqualTo(2);
        assertThat(domainService.calculateCustomerLevel(1999, CustomerType.NORMAL)).isEqualTo(2);
        assertThat(domainService.calculateCustomerLevel(2000, CustomerType.NORMAL)).isEqualTo(3);
        assertThat(domainService.calculateCustomerLevel(4999, CustomerType.NORMAL)).isEqualTo(3);
        assertThat(domainService.calculateCustomerLevel(5000, CustomerType.NORMAL)).isEqualTo(4);
        assertThat(domainService.calculateCustomerLevel(9999, CustomerType.NORMAL)).isEqualTo(4);
        assertThat(domainService.calculateCustomerLevel(10000, CustomerType.NORMAL)).isEqualTo(5);
        assertThat(domainService.calculateCustomerLevel(15000, CustomerType.NORMAL)).isEqualTo(5);
    }

    @Test
    @DisplayName("应该能够计算VIP顾客等级")
    void should_calculate_vip_customer_level() {
        // Act & Assert
        assertThat(domainService.calculateCustomerLevel(0, CustomerType.VIP)).isEqualTo(1);
        assertThat(domainService.calculateCustomerLevel(299, CustomerType.VIP)).isEqualTo(1);
        assertThat(domainService.calculateCustomerLevel(300, CustomerType.VIP)).isEqualTo(2);
        assertThat(domainService.calculateCustomerLevel(1499, CustomerType.VIP)).isEqualTo(2);
        assertThat(domainService.calculateCustomerLevel(1500, CustomerType.VIP)).isEqualTo(3);
        assertThat(domainService.calculateCustomerLevel(3999, CustomerType.VIP)).isEqualTo(3);
        assertThat(domainService.calculateCustomerLevel(4000, CustomerType.VIP)).isEqualTo(4);
        assertThat(domainService.calculateCustomerLevel(7999, CustomerType.VIP)).isEqualTo(4);
        assertThat(domainService.calculateCustomerLevel(8000, CustomerType.VIP)).isEqualTo(5);
        assertThat(domainService.calculateCustomerLevel(12000, CustomerType.VIP)).isEqualTo(5);
    }

    @Test
    @DisplayName("应该能够计算批发顾客等级")
    void should_calculate_wholesale_customer_level() {
        // Act & Assert
        assertThat(domainService.calculateCustomerLevel(0, CustomerType.WHOLESALE)).isEqualTo(1);
        assertThat(domainService.calculateCustomerLevel(999, CustomerType.WHOLESALE)).isEqualTo(1);
        assertThat(domainService.calculateCustomerLevel(1000, CustomerType.WHOLESALE)).isEqualTo(2);
        assertThat(domainService.calculateCustomerLevel(2999, CustomerType.WHOLESALE)).isEqualTo(2);
        assertThat(domainService.calculateCustomerLevel(3000, CustomerType.WHOLESALE)).isEqualTo(3);
        assertThat(domainService.calculateCustomerLevel(7999, CustomerType.WHOLESALE)).isEqualTo(3);
        assertThat(domainService.calculateCustomerLevel(8000, CustomerType.WHOLESALE)).isEqualTo(4);
        assertThat(domainService.calculateCustomerLevel(14999, CustomerType.WHOLESALE)).isEqualTo(4);
        assertThat(domainService.calculateCustomerLevel(15000, CustomerType.WHOLESALE)).isEqualTo(5);
        assertThat(domainService.calculateCustomerLevel(20000, CustomerType.WHOLESALE)).isEqualTo(5);
    }

    @ParameterizedTest
    @ValueSource(ints = {-1, -100, -1000})
    @DisplayName("积分不能为负数")
    void should_throw_exception_when_calculating_level_with_negative_points(int negativePoints) {
        // Act & Assert
        assertThatThrownBy(() -> domainService.calculateCustomerLevel(negativePoints, CustomerType.NORMAL))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("积分不能为负数");
    }

    @Test
    @DisplayName("顾客类型不能为空")
    void should_throw_exception_when_calculating_level_with_null_customer_type() {
        // Act & Assert
        assertThatThrownBy(() -> domainService.calculateCustomerLevel(1000, null))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客类型不能为空");
    }

    @Test
    @DisplayName("应该能够检查是否可以升级为VIP")
    void should_check_if_can_upgrade_to_vip() {
        // Arrange
        BrandCustomer regularCustomer = BrandCustomer.create(
                CustomerRelationId.generate(), brandId, personId,
                CustomerCode.of("C202312001"), CustomerType.NORMAL, RelationType.ACTIVE_REGISTRATION
        );
        regularCustomer.activate();
        regularCustomer.addPoints(5000);
        regularCustomer.setLevel(3);

        BrandCustomer vipCustomer = BrandCustomer.create(
                CustomerRelationId.generate(), brandId, personId,
                CustomerCode.of("C202312002"), CustomerType.VIP, RelationType.ACTIVE_REGISTRATION
        );

        BrandCustomer inactiveCustomer = BrandCustomer.create(
                CustomerRelationId.generate(), brandId, personId,
                CustomerCode.of("C202312003"), CustomerType.NORMAL, RelationType.ACTIVE_REGISTRATION
        );

        BrandCustomer lowPointsCustomer = BrandCustomer.create(
                CustomerRelationId.generate(), brandId, personId,
                CustomerCode.of("C202312004"), CustomerType.NORMAL, RelationType.ACTIVE_REGISTRATION
        );
        lowPointsCustomer.activate();
        lowPointsCustomer.addPoints(3000);

        BrandCustomer lowLevelCustomer = BrandCustomer.create(
                CustomerRelationId.generate(), brandId, personId,
                CustomerCode.of("C202312005"), CustomerType.NORMAL, RelationType.ACTIVE_REGISTRATION
        );
        lowLevelCustomer.activate();
        lowLevelCustomer.addPoints(5000);
        lowLevelCustomer.setLevel(2);

        // Act & Assert
        assertThat(domainService.canUpgradeToVip(regularCustomer)).isTrue();
        assertThat(domainService.canUpgradeToVip(vipCustomer)).isFalse(); // 已经是VIP
        assertThat(domainService.canUpgradeToVip(inactiveCustomer)).isFalse(); // 未激活
        assertThat(domainService.canUpgradeToVip(lowPointsCustomer)).isFalse(); // 积分不足
        assertThat(domainService.canUpgradeToVip(lowLevelCustomer)).isFalse(); // 等级不足
    }

    @Test
    @DisplayName("检查VIP升级时品牌顾客不能为空")
    void should_throw_exception_when_checking_vip_upgrade_with_null_customer() {
        // Act & Assert
        assertThatThrownBy(() -> domainService.canUpgradeToVip(null))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("品牌顾客不能为空");
    }

    @Test
    @DisplayName("应该能够查找即将过期的共享关系")
    void should_find_expiring_sharings() {
        // Arrange
        int warningDays = 7;
        
        // 即将过期的共享关系（7天后过期）
        CustomerSharing expiringSoon = CustomerSharing.create(
                SharingId.generate(), "brand-1", "brand-2", personId,
                SharingType.AUTO, AuthLevel.BASIC, LocalDate.now(), LocalDate.now().plusDays(7)
        );
        expiringSoon.activate();
        
        // 不会即将过期的共享关系（8天后过期）
        CustomerSharing notExpiring = CustomerSharing.create(
                SharingId.generate(), "brand-1", "brand-3", personId,
                SharingType.AUTO, AuthLevel.BASIC, LocalDate.now(), LocalDate.now().plusDays(8)
        );
        notExpiring.activate();
        
        // 永久有效的共享关系
        CustomerSharing permanent = CustomerSharing.create(
                SharingId.generate(), "brand-1", "brand-4", personId,
                SharingType.AUTO, AuthLevel.BASIC, LocalDate.now(), null
        );
        permanent.activate();
        
        // 未激活的共享关系
        CustomerSharing inactive = CustomerSharing.create(
                SharingId.generate(), "brand-1", "brand-5", personId,
                SharingType.AUTO, AuthLevel.BASIC, LocalDate.now(), LocalDate.now().plusDays(5)
        );
        
        List<CustomerSharing> allSharings = List.of(expiringSoon, notExpiring, permanent, inactive);

        // Act
        List<CustomerSharing> expiringSharings = domainService.findExpiringSharings(allSharings, warningDays);

        // Assert
        assertThat(expiringSharings).hasSize(1);
        assertThat(expiringSharings).contains(expiringSoon);
        assertThat(expiringSharings).doesNotContain(notExpiring, permanent, inactive);
    }

    @Test
    @DisplayName("查找即将过期的共享关系时参数不能为空")
    void should_throw_exception_when_finding_expiring_sharings_with_null_parameters() {
        // Arrange
        List<CustomerSharing> sharings = new ArrayList<>();

        // Act & Assert
        assertThatThrownBy(() -> domainService.findExpiringSharings(null, 7))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("共享关系列表不能为空");

        assertThatThrownBy(() -> domainService.findExpiringSharings(sharings, 0))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("预警天数必须大于0");

        assertThatThrownBy(() -> domainService.findExpiringSharings(sharings, -1))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("预警天数必须大于0");
    }

    @Test
    @DisplayName("检查是否可以创建共享关系时参数不能为空")
    void should_throw_exception_when_checking_can_create_sharing_with_null_parameters() {
        // Arrange
        String sourceBrandId = "brand-source";
        String targetBrandId = "brand-target";
        List<CustomerSharing> existingSharings = new ArrayList<>();

        // Act & Assert
        assertThatThrownBy(() -> domainService.canCreateSharing(null, targetBrandId, personId, existingSharings))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("源品牌ID不能为空");

        assertThatThrownBy(() -> domainService.canCreateSharing("", targetBrandId, personId, existingSharings))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("源品牌ID不能为空");

        assertThatThrownBy(() -> domainService.canCreateSharing(sourceBrandId, null, personId, existingSharings))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("目标品牌ID不能为空");

        assertThatThrownBy(() -> domainService.canCreateSharing(sourceBrandId, "", personId, existingSharings))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("目标品牌ID不能为空");

        assertThatThrownBy(() -> domainService.canCreateSharing(sourceBrandId, targetBrandId, null, existingSharings))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("自然人ID不能为空");
    }
} 