package com.lovemp.domain.labor.domain.model.entity;

import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.exception.StateTransitionException;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.labor.domain.model.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("雇佣事件实体测试")
class EmploymentEventTest {

    private EmploymentEventId eventId;
    private LaborResourceId laborResourceId;
    private EnterpriseId enterpriseId;
    private BrandId brandId;
    private LaborPolicyId policyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private EmploymentEvent employmentEvent;

    @BeforeEach
    void setUp() {
        // 准备测试数据
        eventId = EmploymentEventId.generate();
        laborResourceId = LaborResourceId.generate();
        enterpriseId = EnterpriseId.of("enterprise-123");
        brandId = BrandId.of("brand-123");
        policyId = LaborPolicyId.of("policy-123");
        startDate = LocalDate.of(2023, 1, 1);
        endDate = LocalDate.of(2023, 12, 31);
        
        // 创建待入职的雇佣事件
        employmentEvent = EmploymentEvent.create(
            eventId, 
            laborResourceId, 
            EmploymentType.FULL_TIME, 
            EmploymentStatus.PENDING_ONBOARD, 
            enterpriseId, 
            brandId, 
            policyId, 
            "软件工程师", 
            "研发部", 
            startDate, 
            endDate, 
            "新雇佣关系"
        );
    }

    @Test
    @DisplayName("创建雇佣事件应成功")
    void shouldCreateEmploymentEvent() {
        // 验证结果
        assertNotNull(employmentEvent);
        assertEquals(eventId, employmentEvent.getId());
        assertEquals(laborResourceId, employmentEvent.getLaborResourceId());
        assertEquals(EmploymentType.FULL_TIME, employmentEvent.getType());
        assertEquals(EmploymentStatus.PENDING_ONBOARD, employmentEvent.getStatus());
        assertEquals(enterpriseId, employmentEvent.getEnterpriseId());
        assertEquals(brandId, employmentEvent.getBrandId());
        assertEquals(policyId, employmentEvent.getLaborPolicyId());
        assertEquals("软件工程师", employmentEvent.getPosition());
        assertEquals("研发部", employmentEvent.getDepartment());
        assertEquals(startDate, employmentEvent.getTimeRange().getStartDate());
        assertEquals(endDate, employmentEvent.getTimeRange().getEndDate());
        assertEquals("新雇佣关系", employmentEvent.getDescription());
        assertEquals(1, employmentEvent.getVersion());
    }
    
    @Test
    @DisplayName("从待入职到入职状态转换应成功")
    void shouldTransitionFromPendingToActive() {
        // 执行测试
        LocalDate onboardDate = LocalDate.of(2023, 1, 15);
        EmploymentEvent onboardEvent = employmentEvent.createOnboardEvent(onboardDate, "顺利入职");
        
        // 验证结果
        assertNotNull(onboardEvent);
        assertEquals(laborResourceId, onboardEvent.getLaborResourceId());
        assertEquals(EmploymentStatus.ACTIVE, onboardEvent.getStatus());
        assertEquals(onboardDate, onboardEvent.getTimeRange().getStartDate());
        assertEquals(endDate, onboardEvent.getTimeRange().getEndDate());
        assertEquals("顺利入职", onboardEvent.getDescription());
    }
    
    @Test
    @DisplayName("非待入职状态不能入职")
    void shouldNotAllowOnboardingIfNotPending() {
        // 先入职
        LocalDate onboardDate = LocalDate.of(2023, 1, 15);
        EmploymentEvent onboardEvent = employmentEvent.createOnboardEvent(onboardDate, null);
        
        // 再次尝试入职应该失败
        assertThrows(StateTransitionException.class, () -> onboardEvent.createOnboardEvent(onboardDate, null));
    }
    
    @Test
    @DisplayName("从在职到离职中状态转换应成功")
    void shouldTransitionFromActiveToLeaving() {
        // 先入职
        EmploymentEvent onboardEvent = employmentEvent.createOnboardEvent(startDate, null);
        
        // 执行测试 - 发起离职
        LocalDate leavingDate = LocalDate.of(2023, 6, 30);
        EmploymentEvent leavingEvent = onboardEvent.createLeavingEvent(leavingDate, "个人原因离职");
        
        // 验证结果
        assertNotNull(leavingEvent);
        assertEquals(laborResourceId, leavingEvent.getLaborResourceId());
        assertEquals(EmploymentStatus.LEAVING, leavingEvent.getStatus());
        assertEquals(startDate, leavingEvent.getTimeRange().getStartDate());
        assertEquals(leavingDate, leavingEvent.getTimeRange().getEndDate());
        assertEquals("个人原因离职", leavingEvent.getDescription());
    }
    
    @Test
    @DisplayName("离职日期不能早于雇佣开始日期")
    void shouldNotAllowLeavingDateBeforeStartDate() {
        // 先入职
        EmploymentEvent onboardEvent = employmentEvent.createOnboardEvent(startDate, null);
        
        // 离职日期早于开始日期
        LocalDate invalidLeavingDate = startDate.minusDays(1);
        
        // 执行测试并验证异常
        assertThrows(IllegalArgumentException.class, 
            () -> onboardEvent.createLeavingEvent(invalidLeavingDate, null));
    }
    
    @Test
    @DisplayName("非在职状态不能发起离职")
    void shouldNotAllowLeavingIfNotActive() {
        // 待入职状态不能直接离职
        assertThrows(IllegalStateException.class, 
            () -> employmentEvent.createLeavingEvent(LocalDate.now(), null));
        
        // 先入职再离职
        EmploymentEvent onboardEvent = employmentEvent.createOnboardEvent(startDate, null);
        EmploymentEvent leavingEvent = onboardEvent.createLeavingEvent(LocalDate.now(), null);
        
        // 已经是离职中状态，不能再次发起离职
        assertThrows(IllegalStateException.class, 
            () -> leavingEvent.createLeavingEvent(LocalDate.now(), null));
    }
    
    @Test
    @DisplayName("从离职中到已离职状态转换应成功")
    void shouldTransitionFromLeavingToTerminated() {
        // 入职然后发起离职
        EmploymentEvent onboardEvent = employmentEvent.createOnboardEvent(startDate, null);
        EmploymentEvent leavingEvent = onboardEvent.createLeavingEvent(LocalDate.now(), null);
        
        // 执行测试 - 完成离职
        EmploymentEvent terminatedEvent = leavingEvent.createTerminatedEvent("离职手续办理完成");
        
        // 验证结果
        assertNotNull(terminatedEvent);
        assertEquals(laborResourceId, terminatedEvent.getLaborResourceId());
        assertEquals(EmploymentStatus.TERMINATED, terminatedEvent.getStatus());
        assertEquals("离职手续办理完成", terminatedEvent.getDescription());
    }
    
    @Test
    @DisplayName("非离职中状态不能完成离职")
    void shouldNotAllowTerminationIfNotLeaving() {
        // 待入职状态不能直接完成离职
        assertThrows(IllegalStateException.class, 
            () -> employmentEvent.createTerminatedEvent(null));
        
        // 在职状态不能直接完成离职
        EmploymentEvent onboardEvent = employmentEvent.createOnboardEvent(startDate, null);
        assertThrows(IllegalStateException.class, 
            () -> onboardEvent.createTerminatedEvent(null));
    }
    
    @Test
    @DisplayName("待入职状态可以取消雇佣")
    void shouldAllowCancellationIfPending() {
        // 执行测试 - 取消雇佣
        EmploymentEvent canceledEvent = employmentEvent.createCancelEvent("取消雇佣");
        
        // 验证结果
        assertNotNull(canceledEvent);
        assertEquals(laborResourceId, canceledEvent.getLaborResourceId());
        assertEquals(EmploymentStatus.CANCELED, canceledEvent.getStatus());
        assertEquals("取消雇佣", canceledEvent.getDescription());
        
        // 结束日期应该被设置为当前日期
        assertNotNull(canceledEvent.getTimeRange().getEndDate());
    }
    
    @Test
    @DisplayName("非待入职状态不能取消雇佣")
    void shouldNotAllowCancellationIfNotPending() {
        // 入职
        EmploymentEvent onboardEvent = employmentEvent.createOnboardEvent(startDate, null);
        
        // 尝试取消
        assertThrows(IllegalStateException.class, 
            () -> onboardEvent.createCancelEvent(null));
    }
    
    @Test
    @DisplayName("更新岗位信息应成功")
    void shouldUpdatePosition() {
        // 执行测试
        employmentEvent.updatePosition("高级软件工程师", "平台研发部");
        
        // 验证结果
        assertEquals("高级软件工程师", employmentEvent.getPosition());
        assertEquals("平台研发部", employmentEvent.getDepartment());
    }
    
    @Test
    @DisplayName("岗位名称不能为空")
    void shouldNotAllowEmptyPosition() {
        // 执行测试并验证异常
        assertThrows(DomainRuleViolationException.class, 
            () -> employmentEvent.updatePosition("", "平台研发部"));
        assertThrows(DomainRuleViolationException.class, 
            () -> employmentEvent.updatePosition(null, "平台研发部"));
    }
    
    @Test
    @DisplayName("应正确检测雇佣事件时间重叠")
    void shouldDetectTimeOverlap() {
        // 创建另一个雇佣事件，时间范围重叠
        EmploymentEvent overlappingEvent = EmploymentEvent.create(
            EmploymentEventId.generate(),
            laborResourceId,
            EmploymentType.PART_TIME,
            EmploymentStatus.PENDING_ONBOARD,
            enterpriseId,
            brandId,
            policyId,
            "产品经理",
            "产品部",
            LocalDate.of(2023, 6, 1),  // 与第一个事件重叠
            LocalDate.of(2024, 5, 31),
            "第二个雇佣关系"
        );
        
        // 执行测试并验证结果
        assertTrue(employmentEvent.overlapsWith(overlappingEvent));
        assertTrue(overlappingEvent.overlapsWith(employmentEvent));
        
        // 创建一个不重叠的雇佣事件
        EmploymentEvent nonOverlappingEvent = EmploymentEvent.create(
            EmploymentEventId.generate(),
            laborResourceId,
            EmploymentType.PART_TIME,
            EmploymentStatus.PENDING_ONBOARD,
            enterpriseId,
            brandId,
            policyId,
            "产品经理",
            "产品部",
            LocalDate.of(2024, 1, 1),  // 在第一个事件之后
            LocalDate.of(2024, 12, 31),
            "第三个雇佣关系"
        );
        
        // 验证不重叠
        assertFalse(employmentEvent.overlapsWith(nonOverlappingEvent));
        assertFalse(nonOverlappingEvent.overlapsWith(employmentEvent));
    }
    
    @Test
    @DisplayName("已终止的雇佣事件不应与其他事件重叠")
    void terminatedEventsDoNotOverlap() {
        // 创建一个已离职的雇佣事件
        EmploymentEvent terminatedEvent = EmploymentEvent.create(
            EmploymentEventId.generate(),
            laborResourceId,
            EmploymentType.FULL_TIME,
            EmploymentStatus.TERMINATED,  // 已离职状态
            enterpriseId,
            brandId,
            policyId,
            "软件工程师",
            "研发部",
            LocalDate.of(2023, 1, 1),
            LocalDate.of(2023, 5, 31),  // 时间上会重叠，但是状态是已离职
            "已离职的雇佣关系"
        );
        
        // 执行测试并验证结果
        assertFalse(employmentEvent.overlapsWith(terminatedEvent));
        assertFalse(terminatedEvent.overlapsWith(employmentEvent));
    }
}