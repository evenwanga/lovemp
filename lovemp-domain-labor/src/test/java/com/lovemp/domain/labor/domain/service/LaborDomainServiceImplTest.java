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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.doNothing;

/**
 * 劳动力资源领域服务测试
 */
@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
@DisplayName("劳动力资源领域服务测试")
class LaborDomainServiceImplTest {

    @Mock
    private LaborResourceRepository laborResourceRepository;

    @Mock
    private LaborResource laborResource;

    @Mock
    private EmploymentEvent employmentEvent;

    private LaborDomainService laborDomainService;

    // 测试数据
    private PersonId personId;
    private LaborResourceId laborResourceId;
    private EmploymentEventId employmentEventId;
    private EnterpriseId enterpriseId;
    private BrandId brandId;
    private LaborPolicyId laborPolicyId;
    private LocalDate startDate;
    private LocalDate endDate;
    private LocalDate onboardDate;
    private LocalDate leavingDate;
    private String position;
    private String department;
    private String remarks;

    @BeforeEach
    void setUp() {
        // 初始化服务
        laborDomainService = new LaborDomainServiceImpl(laborResourceRepository);

        // 初始化测试数据
        personId = PersonId.of("person-123");
        laborResourceId = LaborResourceId.of("labor-resource-123");
        employmentEventId = EmploymentEventId.of("employment-event-123");
        enterpriseId = EnterpriseId.of("enterprise-123");
        brandId = BrandId.of("brand-123");
        laborPolicyId = LaborPolicyId.of("policy-123");
        startDate = LocalDate.now().minusDays(7);
        endDate = LocalDate.now().plusMonths(6);
        onboardDate = LocalDate.now();
        leavingDate = LocalDate.now().plusDays(30);
        position = "软件工程师";
        department = "研发部";
        remarks = "测试备注";

        // 设置默认Mock行为
        when(laborResource.getId()).thenReturn(laborResourceId);
        when(laborResource.getPersonId()).thenReturn(personId);
    }

    @Test
    @DisplayName("创建劳动力资源 - 成功")
    void createLaborResource_Success() {
        // 准备
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.empty());
        doNothing().when(laborResourceRepository).save(any(LaborResource.class));

        // 执行
        LaborResource result = laborDomainService.createLaborResource(personId);

        // 验证
        assertNotNull(result);
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResourceRepository).save(any(LaborResource.class));
    }

    @Test
    @DisplayName("创建劳动力资源 - 资源已存在")
    void createLaborResource_AlreadyExists() {
        // 准备
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.of(laborResource));

        // 执行和验证
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> 
            laborDomainService.createLaborResource(personId)
        );
        
        assertEquals("该自然人已经有劳动力资源记录", exception.getMessage());
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResourceRepository, never()).save(any(LaborResource.class));
    }

    @Test
    @DisplayName("获取或创建劳动力资源 - 获取已存在资源")
    void getOrCreateLaborResource_GetExisting() {
        // 准备
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.of(laborResource));

        // 执行
        LaborResource result = laborDomainService.getOrCreateLaborResource(personId);

        // 验证
        assertNotNull(result);
        assertSame(laborResource, result);
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResourceRepository, never()).save(any(LaborResource.class));
    }

    @Test
    @DisplayName("获取或创建劳动力资源 - 创建新资源")
    void getOrCreateLaborResource_CreateNew() {
        // 准备
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.empty());
        doNothing().when(laborResourceRepository).save(any(LaborResource.class));

        // 执行
        LaborResource result = laborDomainService.getOrCreateLaborResource(personId);

        // 验证
        assertNotNull(result);
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResourceRepository).save(any(LaborResource.class));
    }

    @Test
    @DisplayName("创建雇佣关系 - 成功")
    void createEmployment_Success() {
        // 准备
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.of(laborResource));
        when(laborResource.createEmployment(any(), any(), any(), any(), any(), any(), any(), any()))
            .thenReturn(employmentEvent);

        // 执行
        EmploymentEvent result = laborDomainService.createEmployment(
            personId, EmploymentType.FULL_TIME, enterpriseId, brandId, 
            laborPolicyId, position, department, startDate, endDate
        );

        // 验证
        assertNotNull(result);
        assertSame(employmentEvent, result);
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResource).createEmployment(EmploymentType.FULL_TIME, enterpriseId, brandId, 
            laborPolicyId, position, department, startDate, endDate);
        verify(laborResourceRepository).save(laborResource);
    }

    @Test
    @DisplayName("入职 - 成功")
    void onboard_Success() {
        // 准备
        when(laborResourceRepository.findById(laborResourceId)).thenReturn(Optional.of(laborResource));
        when(laborResource.onboard(employmentEventId, onboardDate, remarks)).thenReturn(employmentEvent);

        // 执行
        EmploymentEvent result = laborDomainService.onboard(
            laborResourceId, employmentEventId, onboardDate, remarks
        );

        // 验证
        assertNotNull(result);
        assertSame(employmentEvent, result);
        verify(laborResourceRepository).findById(laborResourceId);
        verify(laborResource).onboard(employmentEventId, onboardDate, remarks);
        verify(laborResourceRepository).save(laborResource);
    }

    @Test
    @DisplayName("入职 - 劳动力资源不存在")
    void onboard_ResourceNotFound() {
        // 准备
        when(laborResourceRepository.findById(laborResourceId)).thenReturn(Optional.empty());

        // 执行和验证
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> 
            laborDomainService.onboard(laborResourceId, employmentEventId, onboardDate, remarks)
        );
        
        assertEquals("劳动力资源不存在: " + laborResourceId, exception.getMessage());
        verify(laborResourceRepository).findById(laborResourceId);
        verify(laborResource, never()).onboard(any(), any(), any());
        verify(laborResourceRepository, never()).save(any());
    }

    @Test
    @DisplayName("发起离职 - 成功")
    void initiateLeaving_Success() {
        // 准备
        when(laborResourceRepository.findById(laborResourceId)).thenReturn(Optional.of(laborResource));
        when(laborResource.initiateLeaving(employmentEventId, leavingDate, remarks)).thenReturn(employmentEvent);

        // 执行
        EmploymentEvent result = laborDomainService.initiateLeaving(
            laborResourceId, employmentEventId, leavingDate, remarks
        );

        // 验证
        assertNotNull(result);
        assertSame(employmentEvent, result);
        verify(laborResourceRepository).findById(laborResourceId);
        verify(laborResource).initiateLeaving(employmentEventId, leavingDate, remarks);
        verify(laborResourceRepository).save(laborResource);
    }

    @Test
    @DisplayName("发起离职 - 劳动力资源不存在")
    void initiateLeaving_ResourceNotFound() {
        // 准备
        when(laborResourceRepository.findById(laborResourceId)).thenReturn(Optional.empty());

        // 执行和验证
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> 
            laborDomainService.initiateLeaving(laborResourceId, employmentEventId, leavingDate, remarks)
        );
        
        assertEquals("劳动力资源不存在: " + laborResourceId, exception.getMessage());
        verify(laborResourceRepository).findById(laborResourceId);
        verify(laborResource, never()).initiateLeaving(any(), any(), any());
        verify(laborResourceRepository, never()).save(any());
    }

    @Test
    @DisplayName("完成离职 - 成功")
    void terminateEmployment_Success() {
        // 准备
        when(laborResourceRepository.findById(laborResourceId)).thenReturn(Optional.of(laborResource));
        when(laborResource.terminateEmployment(employmentEventId, remarks)).thenReturn(employmentEvent);

        // 执行
        EmploymentEvent result = laborDomainService.terminateEmployment(
            laborResourceId, employmentEventId, remarks
        );

        // 验证
        assertNotNull(result);
        assertSame(employmentEvent, result);
        verify(laborResourceRepository).findById(laborResourceId);
        verify(laborResource).terminateEmployment(employmentEventId, remarks);
        verify(laborResourceRepository).save(laborResource);
    }

    @Test
    @DisplayName("完成离职 - 劳动力资源不存在")
    void terminateEmployment_ResourceNotFound() {
        // 准备
        when(laborResourceRepository.findById(laborResourceId)).thenReturn(Optional.empty());

        // 执行和验证
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> 
            laborDomainService.terminateEmployment(laborResourceId, employmentEventId, remarks)
        );
        
        assertEquals("劳动力资源不存在: " + laborResourceId, exception.getMessage());
        verify(laborResourceRepository).findById(laborResourceId);
        verify(laborResource, never()).terminateEmployment(any(), any());
        verify(laborResourceRepository, never()).save(any());
    }

    @Test
    @DisplayName("取消雇佣 - 成功")
    void cancelEmployment_Success() {
        // 准备
        when(laborResourceRepository.findById(laborResourceId)).thenReturn(Optional.of(laborResource));
        when(laborResource.cancelEmployment(employmentEventId, remarks)).thenReturn(employmentEvent);

        // 执行
        EmploymentEvent result = laborDomainService.cancelEmployment(
            laborResourceId, employmentEventId, remarks
        );

        // 验证
        assertNotNull(result);
        assertSame(employmentEvent, result);
        verify(laborResourceRepository).findById(laborResourceId);
        verify(laborResource).cancelEmployment(employmentEventId, remarks);
        verify(laborResourceRepository).save(laborResource);
    }

    @Test
    @DisplayName("取消雇佣 - 劳动力资源不存在")
    void cancelEmployment_ResourceNotFound() {
        // 准备
        when(laborResourceRepository.findById(laborResourceId)).thenReturn(Optional.empty());

        // 执行和验证
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> 
            laborDomainService.cancelEmployment(laborResourceId, employmentEventId, remarks)
        );
        
        assertEquals("劳动力资源不存在: " + laborResourceId, exception.getMessage());
        verify(laborResourceRepository).findById(laborResourceId);
        verify(laborResource, never()).cancelEmployment(any(), any());
        verify(laborResourceRepository, never()).save(any());
    }

    @Test
    @DisplayName("查找活跃雇佣关系 - 有劳动力资源")
    void findActiveEmployments_WithResource() {
        // 准备
        List<EmploymentEvent> employmentEvents = Arrays.asList(employmentEvent);
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.of(laborResource));
        when(laborResource.getActiveEmploymentEvents()).thenReturn(employmentEvents);

        // 执行
        List<EmploymentEvent> result = laborDomainService.findActiveEmployments(personId);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(employmentEvent, result.get(0));
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResource).getActiveEmploymentEvents();
    }

    @Test
    @DisplayName("查找活跃雇佣关系 - 无劳动力资源")
    void findActiveEmployments_NoResource() {
        // 准备
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.empty());

        // 执行
        List<EmploymentEvent> result = laborDomainService.findActiveEmployments(personId);

        // 验证
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResource, never()).getActiveEmploymentEvents();
    }

    @Test
    @DisplayName("检查企业活跃雇佣关系 - 有劳动力资源且有活跃雇佣")
    void hasActiveEmploymentWithEnterprise_WithResourceAndActive() {
        // 准备
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.of(laborResource));
        when(laborResource.hasActiveEmploymentWithEnterprise(enterpriseId)).thenReturn(true);

        // 执行
        boolean result = laborDomainService.hasActiveEmploymentWithEnterprise(personId, enterpriseId);

        // 验证
        assertTrue(result);
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResource).hasActiveEmploymentWithEnterprise(enterpriseId);
    }

    @Test
    @DisplayName("检查企业活跃雇佣关系 - 有劳动力资源但无活跃雇佣")
    void hasActiveEmploymentWithEnterprise_WithResourceButNoActive() {
        // 准备
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.of(laborResource));
        when(laborResource.hasActiveEmploymentWithEnterprise(enterpriseId)).thenReturn(false);

        // 执行
        boolean result = laborDomainService.hasActiveEmploymentWithEnterprise(personId, enterpriseId);

        // 验证
        assertFalse(result);
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResource).hasActiveEmploymentWithEnterprise(enterpriseId);
    }

    @Test
    @DisplayName("检查企业活跃雇佣关系 - 无劳动力资源")
    void hasActiveEmploymentWithEnterprise_NoResource() {
        // 准备
        when(laborResourceRepository.findByPersonId(personId)).thenReturn(Optional.empty());

        // 执行
        boolean result = laborDomainService.hasActiveEmploymentWithEnterprise(personId, enterpriseId);

        // 验证
        assertFalse(result);
        verify(laborResourceRepository).findByPersonId(personId);
        verify(laborResource, never()).hasActiveEmploymentWithEnterprise(any());
    }

    @Test
    @DisplayName("查找企业劳动力资源 - 成功")
    void findLaborResourcesByEnterprise_Success() {
        // 准备
        List<LaborResource> resources = Arrays.asList(laborResource);
        when(laborResourceRepository.findByEnterpriseId(enterpriseId)).thenReturn(resources);

        // 执行
        List<LaborResource> result = laborDomainService.findLaborResourcesByEnterprise(enterpriseId);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(laborResource, result.get(0));
        verify(laborResourceRepository).findByEnterpriseId(enterpriseId);
    }

    @Test
    @DisplayName("查找企业劳动力资源 - 无结果")
    void findLaborResourcesByEnterprise_NoResults() {
        // 准备
        when(laborResourceRepository.findByEnterpriseId(enterpriseId)).thenReturn(Collections.emptyList());

        // 执行
        List<LaborResource> result = laborDomainService.findLaborResourcesByEnterprise(enterpriseId);

        // 验证
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(laborResourceRepository).findByEnterpriseId(enterpriseId);
    }

    @Test
    @DisplayName("查找品牌劳动力资源 - 成功")
    void findLaborResourcesByBrand_Success() {
        // 准备
        List<LaborResource> resources = Arrays.asList(laborResource);
        when(laborResourceRepository.findByBrandId(brandId)).thenReturn(resources);

        // 执行
        List<LaborResource> result = laborDomainService.findLaborResourcesByBrand(brandId);

        // 验证
        assertNotNull(result);
        assertEquals(1, result.size());
        assertSame(laborResource, result.get(0));
        verify(laborResourceRepository).findByBrandId(brandId);
    }

    @Test
    @DisplayName("查找品牌劳动力资源 - 无结果")
    void findLaborResourcesByBrand_NoResults() {
        // 准备
        when(laborResourceRepository.findByBrandId(brandId)).thenReturn(Collections.emptyList());

        // 执行
        List<LaborResource> result = laborDomainService.findLaborResourcesByBrand(brandId);

        // 验证
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(laborResourceRepository).findByBrandId(brandId);
    }
} 