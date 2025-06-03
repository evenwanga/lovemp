package com.lovemp.domain.brand.domain.service;

import com.lovemp.common.domain.DomainService;
import com.lovemp.domain.brand.domain.model.aggregate.Brand;
import com.lovemp.domain.brand.domain.model.entity.LaborPolicy;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.brand.domain.model.valueobject.BrandType;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyId;
import com.lovemp.domain.brand.domain.model.valueobject.ProductCategory;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;

import java.util.List;
import java.util.Optional;

/**
 * 品牌领域服务接口
 */
public interface BrandDomainService extends DomainService {
    
    /**
     * 创建新品牌
     * 
     * @param name 品牌名称
     * @param code 品牌代码
     * @param type 品牌类型
     * @param enterpriseId 所属企业ID
     * @param mainCategory 主要产品类别
     * @return 新创建的品牌
     */
    Brand createBrand(String name, String code, BrandType type, 
                     EnterpriseId enterpriseId, ProductCategory mainCategory);
    
    /**
     * 根据ID查询品牌
     * 
     * @param brandId 品牌ID
     * @return 品牌
     */
    Optional<Brand> findBrandById(BrandId brandId);
    
    /**
     * 根据代码查询品牌
     * 
     * @param code 品牌代码
     * @return 品牌
     */
    Optional<Brand> findBrandByCode(String code);
    
    /**
     * 查询企业下的所有品牌
     * 
     * @param enterpriseId 企业ID
     * @return 品牌列表
     */
    List<Brand> findBrandsByEnterpriseId(EnterpriseId enterpriseId);
    
    /**
     * 更新品牌基本信息
     * 
     * @param brandId 品牌ID
     * @param name 品牌名称
     * @param englishName 品牌英文名称
     * @param description 品牌描述
     * @param logoUrl 品牌Logo URL
     * @return 更新后的品牌
     */
    Brand updateBrandBasicInfo(BrandId brandId, String name, String englishName, 
                              String description, String logoUrl);
    
    /**
     * 更新品牌类型
     * 
     * @param brandId 品牌ID
     * @param type 品牌类型
     * @return 更新后的品牌
     */
    Brand updateBrandType(BrandId brandId, BrandType type);
    
    /**
     * 更新主要产品类别
     * 
     * @param brandId 品牌ID
     * @param mainCategory 主要产品类别
     * @return 更新后的品牌
     */
    Brand updateMainCategory(BrandId brandId, ProductCategory mainCategory);
    
    /**
     * 提交品牌审核
     * 
     * @param brandId 品牌ID
     * @return 更新后的品牌
     */
    Brand submitBrandForApproval(BrandId brandId);
    
    /**
     * 审核通过品牌
     * 
     * @param brandId 品牌ID
     * @return 更新后的品牌
     */
    Brand approveBrand(BrandId brandId);
    
    /**
     * 审核拒绝品牌
     * 
     * @param brandId 品牌ID
     * @return 更新后的品牌
     */
    Brand rejectBrand(BrandId brandId);
    
    /**
     * 暂停品牌
     * 
     * @param brandId 品牌ID
     * @return 更新后的品牌
     */
    Brand suspendBrand(BrandId brandId);
    
    /**
     * 激活品牌
     * 
     * @param brandId 品牌ID
     * @return 更新后的品牌
     */
    Brand activateBrand(BrandId brandId);
    
    /**
     * 关闭品牌
     * 
     * @param brandId 品牌ID
     * @return 更新后的品牌
     */
    Brand closeBrand(BrandId brandId);
    
    /**
     * 添加用工政策
     * 
     * @param brandId 品牌ID
     * @param laborPolicy 用工政策
     * @return 更新后的品牌
     */
    Brand addLaborPolicy(BrandId brandId, LaborPolicy laborPolicy);
    
    /**
     * 获取品牌的用工政策
     * 
     * @param brandId 品牌ID
     * @param laborPolicyId 用工政策ID
     * @return 用工政策
     */
    Optional<LaborPolicy> findLaborPolicy(BrandId brandId, LaborPolicyId laborPolicyId);
    
    /**
     * 获取品牌的所有可用用工政策
     * 
     * @param brandId 品牌ID
     * @return 可用的用工政策列表
     */
    List<LaborPolicy> findUsableLaborPolicies(BrandId brandId);
    
    /**
     * 移除用工政策
     * 
     * @param brandId 品牌ID
     * @param laborPolicyId 用工政策ID
     * @return 更新后的品牌
     */
    Brand removeLaborPolicy(BrandId brandId, LaborPolicyId laborPolicyId);
} 