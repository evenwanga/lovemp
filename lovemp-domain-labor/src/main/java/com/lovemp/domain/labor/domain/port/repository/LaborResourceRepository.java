package com.lovemp.domain.labor.domain.port.repository;

import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import com.lovemp.domain.labor.domain.model.aggregate.LaborResource;
import com.lovemp.domain.labor.domain.model.valueobject.LaborResourceId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;

import java.util.List;
import java.util.Optional;

/**
 * 劳动力资源仓储接口
 */
public interface LaborResourceRepository {
    
    /**
     * 保存劳动力资源
     *
     * @param laborResource 劳动力资源
     */
    void save(LaborResource laborResource);
    
    /**
     * 根据ID查找劳动力资源
     *
     * @param id 劳动力资源ID
     * @return 劳动力资源
     */
    Optional<LaborResource> findById(LaborResourceId id);
    
    /**
     * 根据自然人ID查找劳动力资源
     *
     * @param personId 自然人ID
     * @return 劳动力资源
     */
    Optional<LaborResource> findByPersonId(PersonId personId);
    
    /**
     * 查找特定企业的劳动力资源
     *
     * @param enterpriseId 企业ID
     * @return 劳动力资源列表
     */
    List<LaborResource> findByEnterpriseId(EnterpriseId enterpriseId);
    
    /**
     * 查找特定品牌的劳动力资源
     *
     * @param brandId 品牌ID
     * @return 劳动力资源列表
     */
    List<LaborResource> findByBrandId(BrandId brandId);
    
    /**
     * 查找企业下的活跃劳动力资源
     *
     * @param enterpriseId 企业ID
     * @return 活跃的劳动力资源列表
     */
    List<LaborResource> findActiveByEnterpriseId(EnterpriseId enterpriseId);
    
    /**
     * 查找品牌下的活跃劳动力资源
     *
     * @param brandId 品牌ID
     * @return 活跃的劳动力资源列表
     */
    List<LaborResource> findActiveByBrandId(BrandId brandId);
}