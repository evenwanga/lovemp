package com.lovemp.domain.brand.domain.model.aggregate;

import com.lovemp.common.domain.AggregateRoot;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.Assert;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.domain.brand.domain.event.BrandCreatedEvent;
import com.lovemp.domain.brand.domain.event.BrandStatusChangedEvent;
import com.lovemp.domain.brand.domain.event.BrandUpdatedEvent;
import com.lovemp.domain.brand.domain.event.LaborPolicyAddedEvent;
import com.lovemp.domain.brand.domain.model.entity.LaborPolicy;
import com.lovemp.domain.brand.domain.model.valueobject.*;
import com.lovemp.domain.enterprise.domain.model.valueobject.EnterpriseId;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 品牌聚合根
 */
@Getter
public class Brand extends AggregateRoot<BrandId> {
    
    /**
     * 品牌名称
     */
    private String name;
    
    /**
     * 品牌英文名称
     */
    private String englishName;
    
    /**
     * 品牌代码
     */
    private String code;
    
    /**
     * 品牌描述
     */
    private String description;
    
    /**
     * 品牌Logo URL
     */
    private String logoUrl;
    
    /**
     * 品牌类型
     */
    private BrandType type;
    
    /**
     * 所属企业ID
     */
    private EnterpriseId enterpriseId;
    
    /**
     * 品牌状态
     */
    private BrandStatus status;
    
    /**
     * 主要产品类别
     */
    private ProductCategory mainCategory;
    
    /**
     * 创建日期
     */
    private final LocalDate createdDate;
    
    /**
     * 最后更新日期
     */
    private LocalDate lastUpdatedDate;
    
    /**
     * 品牌用工政策列表
     */
    private List<LaborPolicy> laborPolicies;
    
    /**
     * 保护构造函数，防止直接实例化
     */
    protected Brand() {
        this.laborPolicies = new ArrayList<>();
        this.createdDate = DateTimeUtils.getCurrentDate();
        this.lastUpdatedDate = this.createdDate;
    }
    
    /**
     * 创建品牌
     * 
     * @param id 品牌ID
     * @param name 品牌名称
     * @param code 品牌代码
     * @param type 品牌类型
     * @param enterpriseId 所属企业ID
     * @param mainCategory 主要产品类别
     * @return 品牌实例
     */
    public static Brand create(BrandId id, String name, String code, BrandType type, 
                             EnterpriseId enterpriseId, ProductCategory mainCategory) {
        Assert.notNull(id, "品牌ID不能为空");
        Assert.notEmpty(name, "品牌名称不能为空");
        Assert.notEmpty(code, "品牌代码不能为空");
        Assert.notNull(type, "品牌类型不能为空");
        Assert.notNull(enterpriseId, "所属企业ID不能为空");
        Assert.notNull(mainCategory, "主要产品类别不能为空");
        
        Brand brand = new Brand();
        brand.id = id;
        brand.name = name;
        brand.code = code;
        brand.type = type;
        brand.enterpriseId = enterpriseId;
        brand.mainCategory = mainCategory;
        brand.status = BrandStatus.DRAFT;
        brand.laborPolicies = new ArrayList<>();
        
        // 发布品牌创建事件
        brand.registerEvent(new BrandCreatedEvent(brand.id, brand.name, brand.enterpriseId));
        
        return brand;
    }
    
    /**
     * 更新品牌基本信息
     * 
     * @param name 品牌名称
     * @param englishName 品牌英文名称
     * @param description 品牌描述
     * @param logoUrl 品牌Logo URL
     */
    public void updateBasicInfo(String name, String englishName, String description, String logoUrl) {
        Assert.notEmpty(name, "品牌名称不能为空");
        
        this.name = name;
        this.englishName = englishName;
        this.description = description;
        this.logoUrl = logoUrl;
        
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布品牌更新事件
        registerEvent(new BrandUpdatedEvent(this.id, this.name));
    }
    
    /**
     * 更新品牌类型
     * 
     * @param type 品牌类型
     */
    public void updateType(BrandType type) {
        Assert.notNull(type, "品牌类型不能为空");
        
        this.type = type;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布品牌更新事件
        registerEvent(new BrandUpdatedEvent(this.id, this.name));
    }
    
    /**
     * 更新主要产品类别
     * 
     * @param mainCategory 主要产品类别
     */
    public void updateMainCategory(ProductCategory mainCategory) {
        Assert.notNull(mainCategory, "主要产品类别不能为空");
        
        this.mainCategory = mainCategory;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布品牌更新事件
        registerEvent(new BrandUpdatedEvent(this.id, this.name));
    }
    
    /**
     * 提交审核
     */
    public void submitForApproval() {
        if (this.status != BrandStatus.DRAFT && this.status != BrandStatus.APPROVAL_REJECTED) {
            throw new DomainRuleViolationException("只有草稿状态或审核被拒绝的品牌可以提交审核");
        }
        
        this.status = BrandStatus.PENDING_APPROVAL;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布品牌状态变更事件
        registerEvent(new BrandStatusChangedEvent(this.id, this.status));
    }
    
    /**
     * 审核通过
     */
    public void approve() {
        if (this.status != BrandStatus.PENDING_APPROVAL) {
            throw new DomainRuleViolationException("只有待审核的品牌可以审核通过");
        }
        
        this.status = BrandStatus.ACTIVE;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布品牌状态变更事件
        registerEvent(new BrandStatusChangedEvent(this.id, this.status));
    }
    
    /**
     * 审核拒绝
     */
    public void reject() {
        if (this.status != BrandStatus.PENDING_APPROVAL) {
            throw new DomainRuleViolationException("只有待审核的品牌可以审核拒绝");
        }
        
        this.status = BrandStatus.APPROVAL_REJECTED;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布品牌状态变更事件
        registerEvent(new BrandStatusChangedEvent(this.id, this.status));
    }
    
    /**
     * 暂停品牌
     */
    public void suspend() {
        if (!this.status.isActive()) {
            throw new DomainRuleViolationException("只有活跃状态的品牌可以暂停");
        }
        
        this.status = BrandStatus.SUSPENDED;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布品牌状态变更事件
        registerEvent(new BrandStatusChangedEvent(this.id, this.status));
    }
    
    /**
     * 恢复品牌
     */
    public void activate() {
        if (this.status != BrandStatus.SUSPENDED) {
            throw new DomainRuleViolationException("只有暂停状态的品牌可以激活");
        }
        
        this.status = BrandStatus.ACTIVE;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布品牌状态变更事件
        registerEvent(new BrandStatusChangedEvent(this.id, this.status));
    }
    
    /**
     * 关闭品牌
     */
    public void close() {
        if (this.status == BrandStatus.CLOSED) {
            return;
        }
        
        this.status = BrandStatus.CLOSED;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布品牌状态变更事件
        registerEvent(new BrandStatusChangedEvent(this.id, this.status));
    }
    
    /**
     * 添加用工政策
     * 
     * @param laborPolicy 用工政策
     */
    public void addLaborPolicy(LaborPolicy laborPolicy) {
        Assert.notNull(laborPolicy, "用工政策不能为空");
        
        // 验证用工政策所属品牌ID是否与当前品牌一致
        if (!laborPolicy.getBrandId().equals(this.id)) {
            throw new DomainRuleViolationException("用工政策所属品牌ID与当前品牌不一致");
        }
        
        // 检查是否已存在同代码的用工政策
        if (getLaborPolicyByCode(laborPolicy.getCode()).isPresent()) {
            throw new DomainRuleViolationException("已存在相同代码的用工政策: " + laborPolicy.getCode());
        }
        
        this.laborPolicies.add(laborPolicy);
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        
        // 发布用工政策添加事件
        registerEvent(new LaborPolicyAddedEvent(this.id, laborPolicy.getId()));
    }
    
    /**
     * 根据ID获取用工政策
     * 
     * @param laborPolicyId 用工政策ID
     * @return 用工政策
     */
    public Optional<LaborPolicy> getLaborPolicy(LaborPolicyId laborPolicyId) {
        Assert.notNull(laborPolicyId, "用工政策ID不能为空");
        
        return laborPolicies.stream()
                .filter(policy -> policy.getId().equals(laborPolicyId))
                .findFirst();
    }
    
    /**
     * 根据代码获取用工政策
     * 
     * @param code 用工政策代码
     * @return 用工政策
     */
    public Optional<LaborPolicy> getLaborPolicyByCode(String code) {
        Assert.notEmpty(code, "用工政策代码不能为空");
        
        return laborPolicies.stream()
                .filter(policy -> policy.getCode().equals(code))
                .findFirst();
    }
    
    /**
     * 获取所有用工政策
     * 
     * @return 用工政策列表
     */
    public List<LaborPolicy> getLaborPolicies() {
        return Collections.unmodifiableList(laborPolicies);
    }
    
    /**
     * 获取所有可用的用工政策
     * 
     * @return 可用的用工政策列表
     */
    public List<LaborPolicy> getUsableLaborPolicies() {
        return laborPolicies.stream()
                .filter(LaborPolicy::isUsable)
                .toList();
    }
    
    /**
     * 获取特定类型的用工政策
     * 
     * @param type 用工政策类型
     * @return 用工政策列表
     */
    public List<LaborPolicy> getLaborPoliciesByType(LaborPolicyType type) {
        Assert.notNull(type, "用工政策类型不能为空");
        
        return laborPolicies.stream()
                .filter(policy -> policy.getType() == type)
                .toList();
    }
    
    /**
     * 移除用工政策
     * 
     * @param laborPolicyId 用工政策ID
     */
    public void removeLaborPolicy(LaborPolicyId laborPolicyId) {
        Assert.notNull(laborPolicyId, "用工政策ID不能为空");
        
        Optional<LaborPolicy> policyOptional = getLaborPolicy(laborPolicyId);
        if (policyOptional.isEmpty()) {
            return;
        }
        
        LaborPolicy policy = policyOptional.get();
        
        // 只能移除草稿或被拒绝状态的政策
        if (!policy.getStatus().isDraft() && policy.getStatus() != LaborPolicyStatus.APPROVAL_REJECTED) {
            throw new DomainRuleViolationException("只能移除草稿或被拒绝状态的用工政策");
        }
        
        laborPolicies.remove(policy);
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
    }
}