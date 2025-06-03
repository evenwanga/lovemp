package com.lovemp.domain.brand.domain.service.impl;

import com.lovemp.common.exception.EntityNotFoundException;
import com.lovemp.domain.brand.domain.model.aggregate.Brand;
import com.lovemp.domain.brand.domain.model.entity.LaborPolicy;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.brand.domain.model.valueobject.BrandType;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyId;
import com.lovemp.domain.brand.domain.model.valueobject.ProductCategory;
import com.lovemp.domain.brand.domain.port.outgoing.BrandRepository;
import com.lovemp.domain.brand.domain.service.BrandDomainService;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * 品牌领域服务实现
 */
@Service
@RequiredArgsConstructor
public class BrandDomainServiceImpl implements BrandDomainService {
    
    private final BrandRepository brandRepository;
    
    @Override
    public Brand createBrand(String name, String code, BrandType type,
                            EnterpriseId enterpriseId, ProductCategory mainCategory) {
        // 检查是否已存在相同代码的品牌
        brandRepository.findByCode(code).ifPresent(existingBrand -> {
            throw new IllegalArgumentException("已存在相同代码的品牌: " + code);
        });
        
        // 创建新品牌
        Brand brand = Brand.create(BrandId.generate(), name, code, type, enterpriseId, mainCategory);
        
        // 保存并返回
        return brandRepository.save(brand);
    }
    
    @Override
    public Optional<Brand> findBrandById(BrandId brandId) {
        return brandRepository.findById(brandId);
    }
    
    @Override
    public Optional<Brand> findBrandByCode(String code) {
        return brandRepository.findByCode(code);
    }
    
    @Override
    public List<Brand> findBrandsByEnterpriseId(EnterpriseId enterpriseId) {
        return brandRepository.findByEnterpriseId(enterpriseId);
    }
    
    @Override
    public Brand updateBrandBasicInfo(BrandId brandId, String name, String englishName, 
                                     String description, String logoUrl) {
        Brand brand = getBrandOrThrow(brandId);
        brand.updateBasicInfo(name, englishName, description, logoUrl);
        return brandRepository.save(brand);
    }
    
    @Override
    public Brand updateBrandType(BrandId brandId, BrandType type) {
        Brand brand = getBrandOrThrow(brandId);
        brand.updateType(type);
        return brandRepository.save(brand);
    }
    
    @Override
    public Brand updateMainCategory(BrandId brandId, ProductCategory mainCategory) {
        Brand brand = getBrandOrThrow(brandId);
        brand.updateMainCategory(mainCategory);
        return brandRepository.save(brand);
    }
    
    @Override
    public Brand submitBrandForApproval(BrandId brandId) {
        Brand brand = getBrandOrThrow(brandId);
        brand.submitForApproval();
        return brandRepository.save(brand);
    }
    
    @Override
    public Brand approveBrand(BrandId brandId) {
        Brand brand = getBrandOrThrow(brandId);
        brand.approve();
        return brandRepository.save(brand);
    }
    
    @Override
    public Brand rejectBrand(BrandId brandId) {
        Brand brand = getBrandOrThrow(brandId);
        brand.reject();
        return brandRepository.save(brand);
    }
    
    @Override
    public Brand suspendBrand(BrandId brandId) {
        Brand brand = getBrandOrThrow(brandId);
        brand.suspend();
        return brandRepository.save(brand);
    }
    
    @Override
    public Brand activateBrand(BrandId brandId) {
        Brand brand = getBrandOrThrow(brandId);
        brand.activate();
        return brandRepository.save(brand);
    }
    
    @Override
    public Brand closeBrand(BrandId brandId) {
        Brand brand = getBrandOrThrow(brandId);
        brand.close();
        return brandRepository.save(brand);
    }
    
    @Override
    public Brand addLaborPolicy(BrandId brandId, LaborPolicy laborPolicy) {
        Brand brand = getBrandOrThrow(brandId);
        brand.addLaborPolicy(laborPolicy);
        return brandRepository.save(brand);
    }
    
    @Override
    public Optional<LaborPolicy> findLaborPolicy(BrandId brandId, LaborPolicyId laborPolicyId) {
        Brand brand = getBrandOrThrow(brandId);
        return brand.getLaborPolicy(laborPolicyId);
    }
    
    @Override
    public List<LaborPolicy> findUsableLaborPolicies(BrandId brandId) {
        Brand brand = getBrandOrThrow(brandId);
        return brand.getUsableLaborPolicies();
    }
    
    @Override
    public Brand removeLaborPolicy(BrandId brandId, LaborPolicyId laborPolicyId) {
        Brand brand = getBrandOrThrow(brandId);
        brand.removeLaborPolicy(laborPolicyId);
        return brandRepository.save(brand);
    }
    
    /**
     * 获取品牌或抛出异常
     * 
     * @param brandId 品牌ID
     * @return 品牌
     * @throws EntityNotFoundException 如果品牌不存在
     */
    private Brand getBrandOrThrow(BrandId brandId) {
        return brandRepository.findById(brandId)
                .orElseThrow(() -> new EntityNotFoundException("品牌不存在，ID: " + brandId.getValue()));
    }
} 