package com.lovemp.domain.brand.domain.port.outgoing;

import com.lovemp.common.domain.Repository;
import com.lovemp.domain.brand.domain.model.aggregate.Brand;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;

import java.util.List;
import java.util.Optional;

/**
 * 品牌仓储接口
 */
public interface BrandRepository extends Repository<Brand, BrandId> {
    
    /**
     * 保存品牌
     * 
     * @param brand 品牌
     * @return 保存后的品牌
     */
    Brand save(Brand brand);
    
    /**
     * 根据ID查询品牌
     * 
     * @param id 品牌ID
     * @return 品牌
     */
    Optional<Brand> findById(BrandId id);
    
    /**
     * 根据代码查询品牌
     * 
     * @param code 品牌代码
     * @return 品牌
     */
    Optional<Brand> findByCode(String code);
    
    /**
     * 根据名称查询品牌
     * 
     * @param name 品牌名称
     * @return 品牌列表
     */
    List<Brand> findByName(String name);
    
    /**
     * 查询企业下的所有品牌
     * 
     * @param enterpriseId 企业ID
     * @return 品牌列表
     */
    List<Brand> findByEnterpriseId(EnterpriseId enterpriseId);
    
    /**
     * 根据类型查询品牌
     * 
     * @param enterpriseId 企业ID
     * @param type 品牌类型代码
     * @return 品牌列表
     */
    List<Brand> findByEnterpriseIdAndType(EnterpriseId enterpriseId, String type);
    
    /**
     * 删除品牌
     * 
     * @param brand 品牌
     */
    void remove(Brand brand);
} 