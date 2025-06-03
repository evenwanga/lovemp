package com.lovemp.domain.customer.domain.model.aggregate;

import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.domain.customer.domain.event.BrandCustomerCreatedEvent;
import com.lovemp.domain.customer.domain.event.BrandCustomerStatusChangedEvent;
import com.lovemp.domain.customer.domain.event.BrandCustomerUpdatedEvent;
import com.lovemp.domain.customer.domain.model.valueobject.*;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.*;

@DisplayName("BrandCustomer 聚合根测试")
class BrandCustomerTest {

    private CustomerRelationId customerRelationId;
    private String brandId;
    private PersonId personId;
    private CustomerCode customerCode;
    private CustomerType customerType;
    private RelationType relationType;

    @BeforeEach
    void setUp() {
        customerRelationId = CustomerRelationId.generate();
        brandId = "brand-123";
        personId = PersonId.of("person-456");
        customerCode = CustomerCode.of("C202312001");
        customerType = CustomerType.NORMAL;
        relationType = RelationType.ACTIVE_REGISTRATION;
    }

    @Test
    @DisplayName("应该能够创建品牌顾客")
    void should_create_brand_customer() {
        // Act
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );

        // Assert
        assertThat(customer).isNotNull();
        assertThat(customer.getId()).isEqualTo(customerRelationId);
        assertThat(customer.getBrandId()).isEqualTo(brandId);
        assertThat(customer.getPersonId()).isEqualTo(personId);
        assertThat(customer.getCustomerCode()).isEqualTo(customerCode);
        assertThat(customer.getCustomerType()).isEqualTo(customerType);
        assertThat(customer.getRelationType()).isEqualTo(relationType);
        assertThat(customer.getStatus()).isEqualTo(CustomerStatus.INACTIVE);
        assertThat(customer.getPoints()).isEqualTo(0);
        assertThat(customer.getLevel()).isEqualTo(1);
        assertThat(customer.getCreateDate()).isEqualTo(LocalDate.now());
        assertThat(customer.getLastUpdateDate()).isEqualTo(LocalDate.now());
    }

    @Test
    @DisplayName("创建品牌顾客时应该发布创建事件")
    void should_publish_created_event_when_creating_brand_customer() {
        // Act
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );

        // Assert
        assertThat(customer.getDomainEvents()).hasSize(1);
        assertThat(customer.getDomainEvents().get(0)).isInstanceOf(BrandCustomerCreatedEvent.class);
        
        BrandCustomerCreatedEvent event = (BrandCustomerCreatedEvent) customer.getDomainEvents().get(0);
        assertThat(event.getCustomerRelationId()).isEqualTo(customerRelationId);
        assertThat(event.getBrandId()).isEqualTo(brandId);
        assertThat(event.getPersonId()).isEqualTo(personId);
        assertThat(event.getCustomerCode()).isEqualTo(customerCode);
    }

    @Test
    @DisplayName("创建品牌顾客时参数不能为空")
    void should_throw_exception_when_creating_with_null_parameters() {
        // Act & Assert
        assertThatThrownBy(() -> BrandCustomer.create(null, brandId, personId, customerCode, customerType, relationType))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客关系ID不能为空");

        assertThatThrownBy(() -> BrandCustomer.create(customerRelationId, null, personId, customerCode, customerType, relationType))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("品牌ID不能为空");

        assertThatThrownBy(() -> BrandCustomer.create(customerRelationId, "", personId, customerCode, customerType, relationType))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("品牌ID不能为空");

        assertThatThrownBy(() -> BrandCustomer.create(customerRelationId, brandId, null, customerCode, customerType, relationType))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("自然人ID不能为空");

        assertThatThrownBy(() -> BrandCustomer.create(customerRelationId, brandId, personId, null, customerType, relationType))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客编码不能为空");

        assertThatThrownBy(() -> BrandCustomer.create(customerRelationId, brandId, personId, customerCode, null, relationType))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("顾客类型不能为空");

        assertThatThrownBy(() -> BrandCustomer.create(customerRelationId, brandId, personId, customerCode, customerType, null))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("关系类型不能为空");
    }

    @Test
    @DisplayName("应该能够激活顾客")
    void should_activate_customer() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );

        // Act
        customer.activate();

        // Assert
        assertThat(customer.getStatus()).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(customer.getLastUpdateDate()).isEqualTo(LocalDate.now());
        
        assertThat(customer.getDomainEvents()).hasSize(2);
    }

    @Test
    @DisplayName("重复激活已激活的顾客应该无效果")
    void should_do_nothing_when_activating_already_active_customer() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        customer.activate();

        // Act
        customer.activate();

        // Assert
        assertThat(customer.getStatus()).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(customer.getDomainEvents()).hasSize(2);
    }

    @Test
    @DisplayName("应该能够冻结顾客")
    void should_freeze_customer() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        customer.activate();
        String reason = "违规操作";

        // Act
        customer.freeze(reason);

        // Assert
        assertThat(customer.getStatus()).isEqualTo(CustomerStatus.FROZEN);
        assertThat(customer.getLastUpdateDate()).isEqualTo(LocalDate.now());
        
        assertThat(customer.getDomainEvents()).hasSizeGreaterThanOrEqualTo(1);
        
        BrandCustomerStatusChangedEvent freezeEvent = customer.getDomainEvents().stream()
                .filter(event -> event instanceof BrandCustomerStatusChangedEvent)
                .map(event -> (BrandCustomerStatusChangedEvent) event)
                .filter(event -> event.getNewStatus() == CustomerStatus.FROZEN)
                .findFirst()
                .orElse(null);
        
        assertThat(freezeEvent).isNotNull();
        assertThat(freezeEvent.getCustomerRelationId()).isEqualTo(customerRelationId);
        assertThat(freezeEvent.getOldStatus()).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(freezeEvent.getNewStatus()).isEqualTo(CustomerStatus.FROZEN);
        assertThat(freezeEvent.getReason()).isEqualTo(reason);
    }

    @Test
    @DisplayName("冻结顾客时原因不能为空")
    void should_throw_exception_when_freezing_with_empty_reason() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );

        // Act & Assert
        assertThatThrownBy(() -> customer.freeze(null))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("冻结原因不能为空");

        assertThatThrownBy(() -> customer.freeze(""))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("冻结原因不能为空");
    }

    @Test
    @DisplayName("应该能够解冻顾客")
    void should_unfreeze_customer() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        customer.activate();
        customer.freeze("测试冻结");

        // Act
        customer.unfreeze();

        // Assert
        assertThat(customer.getStatus()).isEqualTo(CustomerStatus.ACTIVE);
        assertThat(customer.getLastUpdateDate()).isEqualTo(LocalDate.now());
        
        assertThat(customer.getDomainEvents()).hasSizeGreaterThanOrEqualTo(1);
        
        BrandCustomerStatusChangedEvent unfreezeEvent = customer.getDomainEvents().stream()
                .filter(event -> event instanceof BrandCustomerStatusChangedEvent)
                .map(event -> (BrandCustomerStatusChangedEvent) event)
                .filter(event -> event.getOldStatus() == CustomerStatus.FROZEN && event.getNewStatus() == CustomerStatus.ACTIVE)
                .findFirst()
                .orElse(null);
        
        assertThat(unfreezeEvent).isNotNull();
        assertThat(unfreezeEvent.getCustomerRelationId()).isEqualTo(customerRelationId);
        assertThat(unfreezeEvent.getOldStatus()).isEqualTo(CustomerStatus.FROZEN);
        assertThat(unfreezeEvent.getNewStatus()).isEqualTo(CustomerStatus.ACTIVE);
    }

    @Test
    @DisplayName("只有冻结状态的顾客才能解冻")
    void should_throw_exception_when_unfreezing_non_frozen_customer() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );

        // Act & Assert
        assertThatThrownBy(() -> customer.unfreeze())
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("只有冻结状态的顾客才能解冻");
    }

    @Test
    @DisplayName("应该能够更新顾客类型")
    void should_update_customer_type() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        CustomerType newType = CustomerType.VIP;

        // Act
        customer.updateCustomerType(newType);

        // Assert
        assertThat(customer.getCustomerType()).isEqualTo(newType);
        assertThat(customer.getLastUpdateDate()).isEqualTo(LocalDate.now());
        
        assertThat(customer.getDomainEvents()).hasSizeGreaterThanOrEqualTo(1);
        
        BrandCustomerUpdatedEvent updateEvent = customer.getDomainEvents().stream()
                .filter(event -> event instanceof BrandCustomerUpdatedEvent)
                .map(event -> (BrandCustomerUpdatedEvent) event)
                .filter(event -> "customerType".equals(event.getUpdatedField()))
                .findFirst()
                .orElse(null);
        
        assertThat(updateEvent).isNotNull();
        assertThat(updateEvent.getCustomerRelationId()).isEqualTo(customerRelationId);
        assertThat(updateEvent.getUpdatedField()).isEqualTo("customerType");
    }

    @Test
    @DisplayName("更新为相同的顾客类型应该无效果")
    void should_do_nothing_when_updating_to_same_customer_type() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );

        // Act
        customer.updateCustomerType(customerType);

        // Assert
        assertThat(customer.getCustomerType()).isEqualTo(customerType);
        assertThat(customer.getDomainEvents()).hasSize(1); // 只有创建事件
    }

    @Test
    @DisplayName("应该能够增加积分")
    void should_add_points() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        customer.activate(); // 激活后才能操作积分
        int pointsToAdd = 100;

        // Act
        customer.addPoints(pointsToAdd);

        // Assert
        assertThat(customer.getPoints()).isEqualTo(pointsToAdd);
        assertThat(customer.getLastUpdateDate()).isEqualTo(LocalDate.now());
        
        assertThat(customer.getDomainEvents()).hasSizeGreaterThanOrEqualTo(1);
        
        BrandCustomerUpdatedEvent pointsEvent = customer.getDomainEvents().stream()
                .filter(event -> event instanceof BrandCustomerUpdatedEvent)
                .map(event -> (BrandCustomerUpdatedEvent) event)
                .filter(event -> "points".equals(event.getUpdatedField()))
                .findFirst()
                .orElse(null);
        
        assertThat(pointsEvent).isNotNull();
        assertThat(pointsEvent.getCustomerRelationId()).isEqualTo(customerRelationId);
        assertThat(pointsEvent.getUpdatedField()).isEqualTo("points");
    }

    @Test
    @DisplayName("增加积分应该自动升级等级")
    void should_auto_upgrade_level_when_adding_points() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        customer.activate();

        // Act
        customer.addPoints(600); // 应该升级到等级2

        // Assert
        assertThat(customer.getPoints()).isEqualTo(600);
        assertThat(customer.getLevel()).isEqualTo(2);
    }

    @ParameterizedTest
    @ValueSource(ints = {0, -1, -100})
    @DisplayName("增加的积分必须大于0")
    void should_throw_exception_when_adding_invalid_points(int invalidPoints) {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        customer.activate();

        // Act & Assert
        assertThatThrownBy(() -> customer.addPoints(invalidPoints))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("增加的积分必须大于0");
    }

    @Test
    @DisplayName("非激活状态不能增加积分")
    void should_throw_exception_when_adding_points_to_inactive_customer() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );

        // Act & Assert
        assertThatThrownBy(() -> customer.addPoints(100))
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("当前状态不允许积分操作");
    }

    @Test
    @DisplayName("应该能够扣减积分")
    void should_deduct_points() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        customer.activate();
        customer.addPoints(200);
        int pointsToDeduct = 50;

        // Act
        customer.deductPoints(pointsToDeduct);

        // Assert
        assertThat(customer.getPoints()).isEqualTo(150);
        assertThat(customer.getLastUpdateDate()).isEqualTo(LocalDate.now());
        
        assertThat(customer.getDomainEvents()).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("积分不足时不能扣减")
    void should_throw_exception_when_deducting_insufficient_points() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        customer.activate();
        customer.addPoints(50);

        // Act & Assert
        assertThatThrownBy(() -> customer.deductPoints(200))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("积分不足，无法扣减");
    }

    @Test
    @DisplayName("应该能够设置等级")
    void should_set_level() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        int newLevel = 3;

        // Act
        customer.setLevel(newLevel);

        // Assert
        assertThat(customer.getLevel()).isEqualTo(newLevel);
        assertThat(customer.getLastUpdateDate()).isEqualTo(LocalDate.now());
        
        assertThat(customer.getDomainEvents()).hasSizeGreaterThanOrEqualTo(1);
    }

    @Test
    @DisplayName("等级必须大于0")
    void should_throw_exception_when_setting_invalid_level() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );

        // Act & Assert
        assertThatThrownBy(() -> customer.setLevel(0))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("等级必须大于0");

        assertThatThrownBy(() -> customer.setLevel(-1))
                .isInstanceOf(DomainRuleViolationException.class)
                .hasMessageContaining("等级必须大于0");
    }

    @Test
    @DisplayName("应该能够判断是否可以操作")
    void should_check_if_can_operate() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );

        // Assert
        assertThat(customer.canOperate()).isFalse(); // 未激活状态不能操作

        customer.activate();
        assertThat(customer.canOperate()).isTrue(); // 激活状态可以操作

        customer.freeze("测试冻结");
        assertThat(customer.canOperate()).isFalse(); // 冻结状态不能操作
    }

    @Test
    @DisplayName("应该能够判断是否为VIP顾客")
    void should_check_if_vip() {
        // Arrange
        BrandCustomer normalCustomer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, CustomerType.NORMAL, relationType
        );
        BrandCustomer vipCustomer = BrandCustomer.create(
                CustomerRelationId.generate(), brandId, personId, CustomerCode.of("C202312002"), CustomerType.VIP, relationType
        );

        // Assert
        assertThat(normalCustomer.isVip()).isFalse();
        assertThat(vipCustomer.isVip()).isTrue();
    }

    @Test
    @DisplayName("应该能够判断是否为批发顾客")
    void should_check_if_wholesale() {
        // Arrange
        BrandCustomer normalCustomer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, CustomerType.NORMAL, relationType
        );
        BrandCustomer wholesaleCustomer = BrandCustomer.create(
                CustomerRelationId.generate(), brandId, personId, CustomerCode.of("C202312003"), CustomerType.WHOLESALE, relationType
        );

        // Assert
        assertThat(normalCustomer.isWholesale()).isFalse();
        assertThat(wholesaleCustomer.isWholesale()).isTrue();
    }

    @Test
    @DisplayName("应该能够判断是否为衍生关系")
    void should_check_if_derived_relation() {
        // Arrange
        BrandCustomer activeCustomer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, RelationType.ACTIVE_REGISTRATION
        );
        BrandCustomer derivedCustomer = BrandCustomer.create(
                CustomerRelationId.generate(), brandId, personId, CustomerCode.of("C202312004"), customerType, RelationType.DERIVED
        );

        // Assert
        assertThat(activeCustomer.isDerivedRelation()).isFalse();
        assertThat(derivedCustomer.isDerivedRelation()).isTrue();
    }

    @Test
    @DisplayName("应该根据积分正确计算等级")
    void should_calculate_level_by_points_correctly() {
        // Arrange
        BrandCustomer customer = BrandCustomer.create(
                customerRelationId, brandId, personId, customerCode, customerType, relationType
        );
        customer.activate();

        // Act & Assert
        customer.addPoints(400); // 总积分400，应该是等级1
        assertThat(customer.getLevel()).isEqualTo(1);

        customer.addPoints(200); // 总积分600，应该是等级2
        assertThat(customer.getLevel()).isEqualTo(2);

        customer.addPoints(1500); // 总积分2100，应该是等级3
        assertThat(customer.getLevel()).isEqualTo(3);

        customer.addPoints(3000); // 总积分5100，应该是等级4
        assertThat(customer.getLevel()).isEqualTo(4);

        customer.addPoints(5000); // 总积分10100，应该是等级5
        assertThat(customer.getLevel()).isEqualTo(5);
    }
} 