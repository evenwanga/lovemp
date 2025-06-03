package com.lovemp.domain.labor.domain.service;

import com.lovemp.common.exception.EntityNotFoundException;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.labor.domain.model.aggregate.LaborResource;
import com.lovemp.domain.labor.domain.model.entity.EmploymentEvent;
import com.lovemp.domain.labor.domain.model.valueobject.EmploymentEventId;
import com.lovemp.domain.labor.domain.model.valueobject.EmploymentType;
import com.lovemp.domain.labor.domain.model.valueobject.LaborResourceId;
import com.lovemp.domain.labor.domain.port.repository.LaborResourceRepository;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 劳动力资源领域服务实现
 */
@RequiredArgsConstructor
public class LaborDomainServiceImpl implements LaborDomainService {
    
    private final LaborResourceRepository laborResourceRepository;
    
    @Override
    public LaborResource createLaborResource(PersonId personId) {
        // 检查是否已存在
        Optional<LaborResource> existingResource = laborResourceRepository.findByPersonId(personId);
        if (existingResource.isPresent()) {
            throw new IllegalStateException("该自然人已经有劳动力资源记录");
        }
        
        // 创建新的劳动力资源
        LaborResource laborResource = LaborResource.create(LaborResourceId.generate(), personId);
        
        // 保存到仓储
        laborResourceRepository.save(laborResource);
        
        return laborResource;
    }
    
    @Override
    public LaborResource getOrCreateLaborResource(PersonId personId) {
        // 查找已有的劳动力资源
        Optional<LaborResource> existingResource = laborResourceRepository.findByPersonId(personId);
        
        // 如果存在则返回，否则创建新的
        return existingResource.orElseGet(() -> {
            LaborResource newResource = LaborResource.create(LaborResourceId.generate(), personId);
            laborResourceRepository.save(newResource);
            return newResource;
        });
    }
    
    @Override
    public EmploymentEvent createEmployment(PersonId personId, EmploymentType employmentType, 
                                          EnterpriseId enterpriseId, BrandId brandId, 
                                          LaborPolicyId laborPolicyId, String position, 
                                          String department, LocalDate startDate, LocalDate endDate) {
        // 获取或创建劳动力资源
        LaborResource laborResource = getOrCreateLaborResource(personId);
        
        // 创建雇佣关系
        EmploymentEvent employmentEvent = laborResource.createEmployment(
                employmentType, enterpriseId, brandId, laborPolicyId, 
                position, department, startDate, endDate);
        
        // 保存更新
        laborResourceRepository.save(laborResource);
        
        return employmentEvent;
    }
    
    @Override
    public EmploymentEvent onboard(LaborResourceId laborResourceId, EmploymentEventId employmentEventId, 
                                 LocalDate onboardDate, String remarks) {
        // 查找劳动力资源
        LaborResource laborResource = laborResourceRepository.findById(laborResourceId)
                .orElseThrow(() -> new EntityNotFoundException("劳动力资源不存在: " + laborResourceId));
        
        // 执行入职操作
        EmploymentEvent onboardEvent = laborResource.onboard(employmentEventId, onboardDate, remarks);
        
        // 保存更新
        laborResourceRepository.save(laborResource);
        
        return onboardEvent;
    }
    
    @Override
    public EmploymentEvent initiateLeaving(LaborResourceId laborResourceId, EmploymentEventId employmentEventId, 
                                         LocalDate leavingDate, String remarks) {
        // 查找劳动力资源
        LaborResource laborResource = laborResourceRepository.findById(laborResourceId)
                .orElseThrow(() -> new EntityNotFoundException("劳动力资源不存在: " + laborResourceId));
        
        // 执行发起离职操作
        EmploymentEvent leavingEvent = laborResource.initiateLeaving(employmentEventId, leavingDate, remarks);
        
        // 保存更新
        laborResourceRepository.save(laborResource);
        
        return leavingEvent;
    }
    
    @Override
    public EmploymentEvent terminateEmployment(LaborResourceId laborResourceId, EmploymentEventId employmentEventId, 
                                         String remarks) {
        // 查找劳动力资源
        LaborResource laborResource = laborResourceRepository.findById(laborResourceId)
                .orElseThrow(() -> new EntityNotFoundException("劳动力资源不存在: " + laborResourceId));
        
        // 执行完成离职操作
        EmploymentEvent terminatedEvent = laborResource.terminateEmployment(employmentEventId, remarks);
        
        // 保存更新
        laborResourceRepository.save(laborResource);
        
        return terminatedEvent;
    }
    
    @Override
    public EmploymentEvent cancelEmployment(LaborResourceId laborResourceId, EmploymentEventId employmentEventId, 
                                          String remarks) {
        // 查找劳动力资源
        LaborResource laborResource = laborResourceRepository.findById(laborResourceId)
                .orElseThrow(() -> new EntityNotFoundException("劳动力资源不存在: " + laborResourceId));
        
        // 执行取消雇佣操作
        EmploymentEvent canceledEvent = laborResource.cancelEmployment(employmentEventId, remarks);
        
        // 保存更新
        laborResourceRepository.save(laborResource);
        
        return canceledEvent;
    }
    
    @Override
    public List<EmploymentEvent> findActiveEmployments(PersonId personId) {
        // 查找劳动力资源
        Optional<LaborResource> laborResourceOpt = laborResourceRepository.findByPersonId(personId);
        
        // 如果存在，获取活跃雇佣事件
        return laborResourceOpt
                .map(LaborResource::getActiveEmploymentEvents)
                .orElse(Collections.emptyList());
    }
    
    @Override
    public boolean hasActiveEmploymentWithEnterprise(PersonId personId, EnterpriseId enterpriseId) {
        // 查找劳动力资源
        Optional<LaborResource> laborResourceOpt = laborResourceRepository.findByPersonId(personId);
        
        // 如果存在，检查是否有与特定企业的活跃雇佣关系
        return laborResourceOpt
                .map(resource -> resource.hasActiveEmploymentWithEnterprise(enterpriseId))
                .orElse(false);
    }
    
    @Override
    public List<LaborResource> findLaborResourcesByEnterprise(EnterpriseId enterpriseId) {
        return laborResourceRepository.findByEnterpriseId(enterpriseId);
    }
    
    @Override
    public List<LaborResource> findLaborResourcesByBrand(BrandId brandId) {
        return laborResourceRepository.findByBrandId(brandId);
    }
}