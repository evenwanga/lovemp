package com.lovemp.domain.labor.domain.service;

import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.labor.domain.model.aggregate.LaborResource;
import com.lovemp.domain.labor.domain.model.entity.EmploymentEvent;
import com.lovemp.domain.labor.domain.model.valueobject.EmploymentEventId;
import com.lovemp.domain.labor.domain.model.valueobject.EmploymentType;
import com.lovemp.domain.labor.domain.model.valueobject.LaborResourceId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;

import java.time.LocalDate;
import java.util.List;

/**
 * 劳动力资源领域服务接口
 */
public interface LaborDomainService {
    
    /**
     * 创建劳动力资源
     *
     * @param personId 自然人ID
     * @return 劳动力资源
     */
    LaborResource createLaborResource(PersonId personId);
    
    /**
     * 根据自然人ID获取或创建劳动力资源
     *
     * @param personId 自然人ID
     * @return 劳动力资源
     */
    LaborResource getOrCreateLaborResource(PersonId personId);
    
    /**
     * 创建雇佣关系
     *
     * @param personId 自然人ID
     * @param employmentType 雇佣类型
     * @param enterpriseId 企业ID
     * @param brandId 品牌ID
     * @param laborPolicyId 用工政策ID
     * @param position 岗位名称
     * @param department 部门名称
     * @param startDate 开始日期
     * @param endDate 结束日期(可为null)
     * @return 创建的雇佣事件
     */
    EmploymentEvent createEmployment(
            PersonId personId,
            EmploymentType employmentType,
            EnterpriseId enterpriseId,
            BrandId brandId,
            LaborPolicyId laborPolicyId,
            String position,
            String department,
            LocalDate startDate,
            LocalDate endDate);
    
    /**
     * 入职
     *
     * @param laborResourceId 劳动力资源ID
     * @param employmentEventId 雇佣事件ID
     * @param onboardDate 入职日期
     * @param remarks 备注
     * @return 入职后的雇佣事件
     */
    EmploymentEvent onboard(
            LaborResourceId laborResourceId,
            EmploymentEventId employmentEventId,
            LocalDate onboardDate,
            String remarks);
    
    /**
     * 发起离职
     *
     * @param laborResourceId 劳动力资源ID
     * @param employmentEventId 雇佣事件ID
     * @param leavingDate 离职日期
     * @param remarks 备注
     * @return 离职中的雇佣事件
     */
    EmploymentEvent initiateLeaving(
            LaborResourceId laborResourceId,
            EmploymentEventId employmentEventId,
            LocalDate leavingDate,
            String remarks);
    
    /**
     * 完成离职
     *
     * @param laborResourceId 劳动力资源ID
     * @param employmentEventId 雇佣事件ID
     * @param remarks 备注
     * @return 已离职的雇佣事件
     */
    EmploymentEvent terminateEmployment(
            LaborResourceId laborResourceId,
            EmploymentEventId employmentEventId,
            String remarks);
    
    /**
     * 取消雇佣关系
     *
     * @param laborResourceId 劳动力资源ID
     * @param employmentEventId 雇佣事件ID
     * @param remarks 备注
     * @return 已取消的雇佣事件
     */
    EmploymentEvent cancelEmployment(
            LaborResourceId laborResourceId,
            EmploymentEventId employmentEventId,
            String remarks);
    
    /**
     * 查找自然人的活跃雇佣关系
     *
     * @param personId 自然人ID
     * @return 活跃的雇佣事件列表
     */
    List<EmploymentEvent> findActiveEmployments(PersonId personId);
    
    /**
     * 检查自然人是否在特定企业有活跃雇佣关系
     *
     * @param personId 自然人ID
     * @param enterpriseId 企业ID
     * @return 是否有活跃雇佣关系
     */
    boolean hasActiveEmploymentWithEnterprise(PersonId personId, EnterpriseId enterpriseId);
    
    /**
     * 获取企业的所有劳动力资源
     *
     * @param enterpriseId 企业ID
     * @return 劳动力资源列表
     */
    List<LaborResource> findLaborResourcesByEnterprise(EnterpriseId enterpriseId);
    
    /**
     * 获取品牌的所有劳动力资源
     *
     * @param brandId 品牌ID
     * @return 劳动力资源列表
     */
    List<LaborResource> findLaborResourcesByBrand(BrandId brandId);
}