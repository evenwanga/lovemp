package com.lovemp.domain.labor.domain.model.aggregate;

import com.lovemp.common.domain.AggregateRoot;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.Assert;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.labor.domain.event.*;
import com.lovemp.domain.labor.domain.model.entity.EmploymentEvent;
import com.lovemp.domain.labor.domain.model.valueobject.*;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 劳动力资源聚合根
 * 管理与一个自然人相关的所有雇佣关系及其历史记录
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class LaborResource extends AggregateRoot<LaborResourceId> {
    
    /**
     * 劳动力资源ID
     */
    private LaborResourceId id;
    
    /**
     * 关联的自然人ID
     */
    private PersonId personId;
    
    /**
     * 雇佣事件历史记录
     */
    private List<EmploymentEvent> employmentEvents;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdTime;
    
    /**
     * 最后更新时间
     */
    private LocalDateTime lastUpdatedTime;
    
    /**
     * 创建劳动力资源
     *
     * @param id 劳动力资源ID
     * @param personId 自然人ID
     * @return 劳动力资源聚合根
     */
    public static LaborResource create(LaborResourceId id, PersonId personId) {
        Assert.notNull(id, "劳动力资源ID不能为空");
        Assert.notNull(personId, "自然人ID不能为空");
        
        LaborResource laborResource = new LaborResource();
        laborResource.id = id;
        laborResource.personId = personId;
        laborResource.employmentEvents = new ArrayList<>();
        laborResource.createdTime = DateTimeUtils.getCurrentDateTime();
        laborResource.lastUpdatedTime = laborResource.createdTime;
        
        // 发布劳动力资源创建事件
        laborResource.registerEvent(new LaborResourceCreatedEvent(id, personId));
        
        return laborResource;
    }
    
    /**
     * 创建雇佣关系
     *
     * @param employmentType 雇佣类型
     * @param enterpriseId 企业ID
     * @param brandId 品牌ID
     * @param laborPolicyId 用工政策ID
     * @param position 岗位名称
     * @param department 部门名称
     * @param startDate 开始日期
     * @param endDate 结束日期(可为null)
     * @return 新创建的雇佣事件
     */
    public EmploymentEvent createEmployment(
            EmploymentType employmentType,
            EnterpriseId enterpriseId,
            BrandId brandId,
            LaborPolicyId laborPolicyId,
            String position,
            String department,
            LocalDate startDate,
            LocalDate endDate) {
        
        Assert.notNull(employmentType, "雇佣类型不能为空");
        Assert.notNull(enterpriseId, "企业ID不能为空");
        Assert.notNull(brandId, "品牌ID不能为空");
        Assert.notNull(laborPolicyId, "用工政策ID不能为空");
        Assert.notEmpty(position, "岗位名称不能为空");
        Assert.notNull(startDate, "开始日期不能为空");
        
        // 创建雇佣事件
        EmploymentEvent employmentEvent = EmploymentEvent.create(
                EmploymentEventId.generate(),
                this.id,
                employmentType,
                EmploymentStatus.PENDING_ONBOARD,
                enterpriseId,
                brandId,
                laborPolicyId,
                position,
                department,
                startDate,
                endDate,
                "创建雇佣关系"
        );
        
        // 检查时间重叠
        checkTimeOverlap(employmentEvent);
        
        // 添加到事件列表
        employmentEvents.add(employmentEvent);
        
        // 更新时间
        this.lastUpdatedTime = DateTimeUtils.getCurrentDateTime();
        
        // 发布雇佣关系创建事件
        registerEvent(new EmploymentCreatedEvent(
                this.id,
                employmentEvent.getId(),
                this.personId,
                enterpriseId,
                brandId,
                employmentType,
                startDate
        ));
        
        return employmentEvent;
    }
    
    /**
     * 入职
     *
     * @param employmentEventId 雇佣事件ID
     * @param onboardDate 入职日期
     * @param remarks 备注说明
     * @return 入职事件
     */
    public EmploymentEvent onboard(EmploymentEventId employmentEventId, LocalDate onboardDate, String remarks) {
        Assert.notNull(employmentEventId, "雇佣事件ID不能为空");
        Assert.notNull(onboardDate, "入职日期不能为空");
        
        // 查找待入职的雇佣事件
        EmploymentEvent event = getEmploymentEvent(employmentEventId)
                .orElseThrow(() -> new IllegalArgumentException("雇佣事件不存在: " + employmentEventId));
        
        // 创建入职事件
        EmploymentEvent onboardEvent = event.createOnboardEvent(onboardDate, remarks);
        
        // 添加到事件列表
        employmentEvents.add(onboardEvent);
        
        // 更新时间
        this.lastUpdatedTime = DateTimeUtils.getCurrentDateTime();
        
        // 发布入职事件
        registerEvent(new EmploymentOnboardedEvent(
                this.id,
                onboardEvent.getId(),
                this.personId,
                onboardEvent.getEnterpriseId(),
                onboardEvent.getBrandId(),
                onboardDate
        ));
        
        return onboardEvent;
    }
    
    /**
     * 发起离职
     *
     * @param employmentEventId 雇佣事件ID
     * @param leavingDate 离职日期
     * @param remarks 备注
     * @return 新的雇佣事件
     */
    public EmploymentEvent initiateLeaving(EmploymentEventId employmentEventId, LocalDate leavingDate, String remarks) {
        Assert.notNull(employmentEventId, "雇佣事件ID不能为空");
        Assert.notNull(leavingDate, "离职日期不能为空");
        
        // 查找在职的雇佣事件
        EmploymentEvent event = getEmploymentEvent(employmentEventId)
                .orElseThrow(() -> new IllegalArgumentException("雇佣事件不存在: " + employmentEventId));
        
        if (!event.getStatus().canLeave()) {
            throw new DomainRuleViolationException("当前状态不允许发起离职");
        }
        
        // 创建离职中事件
        EmploymentEvent leavingEvent = event.createLeavingEvent(leavingDate, remarks);
        
        // 添加到事件列表
        employmentEvents.add(leavingEvent);
        
        // 更新时间
        this.lastUpdatedTime = DateTimeUtils.getCurrentDateTime();
        
        // 发布离职中事件
        registerEvent(new EmploymentLeavingInitiatedEvent(
                this.id,
                leavingEvent.getId(),
                this.personId,
                leavingEvent.getEnterpriseId(),
                leavingEvent.getBrandId(),
                leavingDate
        ));
        
        return leavingEvent;
    }
    
    /**
     * 完成离职
     *
     * @param employmentEventId 雇佣事件ID
     * @param remarks 备注说明
     * @return 离职完成事件
     */
    public EmploymentEvent terminateEmployment(EmploymentEventId employmentEventId, String remarks) {
        Assert.notNull(employmentEventId, "雇佣事件ID不能为空");
        
        // 查找离职中的雇佣事件
        EmploymentEvent event = getEmploymentEvent(employmentEventId)
                .orElseThrow(() -> new IllegalArgumentException("雇佣事件不存在: " + employmentEventId));
        
        // 创建离职完成事件
        EmploymentEvent terminatedEvent = event.createTerminatedEvent(remarks);
        
        // 添加到事件列表
        employmentEvents.add(terminatedEvent);
        
        // 更新时间
        this.lastUpdatedTime = DateTimeUtils.getCurrentDateTime();
        
        // 发布离职完成事件
        registerEvent(new EmploymentTerminatedEvent(
                this.id,
                terminatedEvent.getId(),
                this.personId,
                terminatedEvent.getEnterpriseId(),
                terminatedEvent.getBrandId()
        ));
        
        return terminatedEvent;
    }
    
    /**
     * 取消雇佣关系
     *
     * @param employmentEventId 雇佣事件ID
     * @param remarks 备注
     * @return 新的雇佣事件
     */
    public EmploymentEvent cancelEmployment(EmploymentEventId employmentEventId, String remarks) {
        Assert.notNull(employmentEventId, "雇佣事件ID不能为空");
        
        // 查找待入职的雇佣事件
        EmploymentEvent event = getEmploymentEvent(employmentEventId)
                .orElseThrow(() -> new IllegalArgumentException("雇佣事件不存在: " + employmentEventId));
        
        if (!event.getStatus().canCancel()) {
            throw new DomainRuleViolationException("当前状态不允许取消");
        }
        
        // 创建已取消事件
        EmploymentEvent canceledEvent = event.createCancelEvent(remarks);
        
        // 添加到事件列表
        employmentEvents.add(canceledEvent);
        
        // 更新时间
        this.lastUpdatedTime = DateTimeUtils.getCurrentDateTime();
        
        // 发布雇佣取消事件
        registerEvent(new EmploymentCanceledEvent(
                this.id,
                canceledEvent.getId(),
                this.personId,
                canceledEvent.getEnterpriseId(),
                canceledEvent.getBrandId()
        ));
        
        return canceledEvent;
    }
    
    /**
     * 根据ID获取雇佣事件
     *
     * @param eventId 雇佣事件ID
     * @return 雇佣事件
     */
    public Optional<EmploymentEvent> getEmploymentEvent(EmploymentEventId eventId) {
        Assert.notNull(eventId, "雇佣事件ID不能为空");
        
        return employmentEvents.stream()
                .filter(event -> event.getId().equals(eventId))
                .findFirst();
    }
    
    /**
     * 获取最新的雇佣事件
     *
     * @return 最新的雇佣事件列表，按创建时间降序排序
     */
    public List<EmploymentEvent> getLatestEmploymentEvents() {
        // 按事件时间降序排序
        return employmentEvents.stream()
                .sorted(Comparator.comparing(EmploymentEvent::getEventTime).reversed())
                .collect(Collectors.toList());
    }
    
    /**
     * 获取活跃的雇佣事件
     *
     * @return 活跃的雇佣事件列表
     */
    public List<EmploymentEvent> getActiveEmploymentEvents() {
        return employmentEvents.stream()
                .filter(event -> event.getStatus() == EmploymentStatus.ACTIVE)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取指定日期的活跃雇佣事件
     *
     * @param date 指定日期
     * @return 活跃的雇佣事件列表
     */
    public List<EmploymentEvent> getActiveEmploymentEvents(LocalDate date) {
        Assert.notNull(date, "日期不能为空");
        return employmentEvents.stream()
                .filter(event -> event.getStatus() == EmploymentStatus.ACTIVE)
                .filter(event -> event.getTimeRange().contains(date))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取特定企业的雇佣事件
     *
     * @param enterpriseId 企业ID
     * @return 该企业的雇佣事件列表
     */
    public List<EmploymentEvent> getEmploymentEventsByEnterprise(EnterpriseId enterpriseId) {
        Assert.notNull(enterpriseId, "企业ID不能为空");
        
        return employmentEvents.stream()
                .filter(event -> event.getEnterpriseId().equals(enterpriseId))
                .collect(Collectors.toList());
    }
    
    /**
     * 获取特定品牌的雇佣事件
     *
     * @param brandId 品牌ID
     * @return 该品牌的雇佣事件列表
     */
    public List<EmploymentEvent> getEmploymentEventsByBrand(BrandId brandId) {
        Assert.notNull(brandId, "品牌ID不能为空");
        
        return employmentEvents.stream()
                .filter(event -> event.getBrandId().equals(brandId))
                .collect(Collectors.toList());
    }
    
    /**
     * 检查指定自然人是否在特定企业有活跃雇佣关系
     *
     * @param enterpriseId 企业ID
     * @return 是否有活跃雇佣关系
     */
    public boolean hasActiveEmploymentWithEnterprise(EnterpriseId enterpriseId) {
        Assert.notNull(enterpriseId, "企业ID不能为空");
        
        LocalDate today = DateTimeUtils.getCurrentDate();
        
        return employmentEvents.stream()
                .anyMatch(event -> 
                        event.getEnterpriseId().equals(enterpriseId) && 
                        event.getStatus().isActive() && 
                        event.getTimeRange().contains(today));
    }
    
    /**
     * 检查雇佣事件的时间范围是否与现有活跃事件重叠
     *
     * @param newEvent 新的雇佣事件
     * @throws DomainRuleViolationException 如果时间重叠
     */
    private void checkTimeOverlap(EmploymentEvent newEvent) {
        // 已离职或已取消的雇佣关系不需要检查重叠
        if (newEvent.getStatus() == EmploymentStatus.TERMINATED || 
            newEvent.getStatus() == EmploymentStatus.CANCELED) {
            return;
        }
        
        // 检查是否与现有活跃雇佣事件重叠
        for (EmploymentEvent existingEvent : employmentEvents) {
            // 只检查活跃状态的雇佣事件
            if (existingEvent.getStatus() != EmploymentStatus.ACTIVE &&
                existingEvent.getStatus() != EmploymentStatus.PENDING_ONBOARD &&
                existingEvent.getStatus() != EmploymentStatus.LEAVING) {
                continue;
            }
            
            // 跳过自身和相关事件（比如入职时的原待入职事件）
            if (existingEvent == newEvent || 
                (existingEvent.getStatus() == EmploymentStatus.PENDING_ONBOARD && 
                 newEvent.getStatus() == EmploymentStatus.ACTIVE &&
                 existingEvent.getTimeRange().equals(newEvent.getTimeRange()))) {
                continue;
            }
            
            if (newEvent.overlapsWith(existingEvent)) {
                throw new DomainRuleViolationException(
                    String.format("雇佣时间范围与现有雇佣关系重叠：开始日期=%s，结束日期=%s", 
                        existingEvent.getTimeRange().getStartDate(),
                        existingEvent.getTimeRange().getEndDate())
                );
            }
        }
    }
}