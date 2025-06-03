package com.lovemp.domain.brand.domain.model.entity;

import com.lovemp.common.domain.Entity;
import com.lovemp.common.util.Assert;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.domain.brand.domain.model.valueobject.BrandId;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyId;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyStatus;
import com.lovemp.domain.brand.domain.model.valueobject.LaborPolicyType;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 用工政策实体
 */
@Getter
public class LaborPolicy extends Entity<LaborPolicyId> {
    
    /**
     * 政策ID
     */
    private final LaborPolicyId id;
    
    /**
     * 所属品牌ID
     */
    private final BrandId brandId;
    
    /**
     * 政策名称
     */
    private String name;
    
    /**
     * 政策编码
     */
    private String code;
    
    /**
     * 政策类型
     */
    private LaborPolicyType type;
    
    /**
     * 政策内容（富文本）
     */
    private String content;
    
    /**
     * 政策摘要
     */
    private String summary;
    
    /**
     * 政策状态
     */
    private LaborPolicyStatus status;
    
    /**
     * 生效日期
     */
    private LocalDate effectiveDate;
    
    /**
     * 失效日期
     */
    private LocalDate expiryDate;
    
    /**
     * 创建日期
     */
    private final LocalDate createdDate;
    
    /**
     * 最后更新日期
     */
    private LocalDate lastUpdatedDate;
    
    /**
     * 版本号
     */
    private int version;
    
    /**
     * 创建新的用工政策
     *
     * @param id 政策ID
     * @param brandId 品牌ID
     * @param name 政策名称
     * @param code 政策编码
     * @param type 政策类型
     * @param content 政策内容
     * @param summary 政策摘要
     * @param effectiveDate 生效日期
     * @param expiryDate 失效日期
     */
    public LaborPolicy(LaborPolicyId id, BrandId brandId, String name, String code, 
                       LaborPolicyType type, String content, String summary,
                       LocalDate effectiveDate, LocalDate expiryDate) {
        
        Assert.notNull(id, "政策ID不能为空");
        Assert.notNull(brandId, "品牌ID不能为空");
        Assert.notEmpty(name, "政策名称不能为空");
        Assert.notEmpty(code, "政策编码不能为空");
        Assert.notNull(type, "政策类型不能为空");
        Assert.notEmpty(content, "政策内容不能为空");
        
        this.id = id;
        this.brandId = brandId;
        this.name = name;
        this.code = code;
        this.type = type;
        this.content = content;
        this.summary = summary;
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
        
        // 默认为草稿状态
        this.status = LaborPolicyStatus.DRAFT;
        
        // 设置时间和版本
        this.createdDate = DateTimeUtils.getCurrentDate();
        this.lastUpdatedDate = this.createdDate;
        this.version = 1;
        
        // 验证日期
        validateDateRange();
    }
    
    /**
     * 验证日期范围
     */
    private void validateDateRange() {
        if (effectiveDate != null && expiryDate != null) {
            if (effectiveDate.isAfter(expiryDate)) {
                throw new IllegalArgumentException("生效日期不能晚于失效日期");
            }
        }
    }
    
    /**
     * 更新政策基本信息
     *
     * @param name 政策名称
     * @param code 政策编码
     * @param type 政策类型
     * @param content 政策内容
     * @param summary 政策摘要
     */
    public void updateBasicInfo(String name, String code, LaborPolicyType type, 
                               String content, String summary) {
        Assert.notEmpty(name, "政策名称不能为空");
        Assert.notEmpty(code, "政策编码不能为空");
        Assert.notNull(type, "政策类型不能为空");
        Assert.notEmpty(content, "政策内容不能为空");
        
        // 只有草稿状态或者待审核被拒绝的政策可以修改
        if (!status.isDraft() && status != LaborPolicyStatus.APPROVAL_REJECTED) {
            throw new IllegalStateException("只有草稿状态或审核拒绝的政策可以修改");
        }
        
        this.name = name;
        this.code = code;
        this.type = type;
        this.content = content;
        this.summary = summary;
        
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        this.version++;
    }
    
    /**
     * 更新政策有效期
     *
     * @param effectiveDate 生效日期
     * @param expiryDate 失效日期
     */
    public void updateValidityPeriod(LocalDate effectiveDate, LocalDate expiryDate) {
        // 只有草稿状态或者待审核被拒绝的政策可以修改
        if (!status.isDraft() && status != LaborPolicyStatus.APPROVAL_REJECTED) {
            throw new IllegalStateException("只有草稿状态或审核拒绝的政策可以修改");
        }
        
        this.effectiveDate = effectiveDate;
        this.expiryDate = expiryDate;
        
        // 验证日期
        validateDateRange();
        
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        this.version++;
    }
    
    /**
     * 提交审核
     */
    public void submitForApproval() {
        if (!status.isDraft() && status != LaborPolicyStatus.APPROVAL_REJECTED) {
            throw new IllegalStateException("只有草稿状态或审核拒绝的政策可以提交审核");
        }
        
        this.status = LaborPolicyStatus.PENDING_APPROVAL;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        this.version++;
    }
    
    /**
     * 审核通过，激活政策
     */
    public void approve() {
        if (status != LaborPolicyStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("只有待审核的政策可以审核通过");
        }
        
        // 如果没有设置生效日期，则使用当前日期
        if (effectiveDate == null) {
            effectiveDate = DateTimeUtils.getCurrentDate();
        }
        
        this.status = LaborPolicyStatus.ACTIVE;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        this.version++;
    }
    
    /**
     * 审核拒绝
     */
    public void reject() {
        if (status != LaborPolicyStatus.PENDING_APPROVAL) {
            throw new IllegalStateException("只有待审核的政策可以审核拒绝");
        }
        
        this.status = LaborPolicyStatus.APPROVAL_REJECTED;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        this.version++;
    }
    
    /**
     * 禁用政策
     */
    public void disable() {
        if (!status.isActive()) {
            throw new IllegalStateException("只有活跃状态的政策可以禁用");
        }
        
        this.status = LaborPolicyStatus.DISABLED;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        this.version++;
    }
    
    /**
     * 启用政策
     */
    public void enable() {
        if (status != LaborPolicyStatus.DISABLED) {
            throw new IllegalStateException("只有禁用状态的政策可以启用");
        }
        
        // 检查有效期
        if (isExpired()) {
            throw new IllegalStateException("已过期的政策不能启用");
        }
        
        this.status = LaborPolicyStatus.ACTIVE;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        this.version++;
    }
    
    /**
     * 检查政策是否过期
     * 
     * @return 是否过期
     */
    public boolean isExpired() {
        if (expiryDate == null) {
            return false;
        }
        return expiryDate.isBefore(DateTimeUtils.getCurrentDate());
    }
    
    /**
     * 标记为过期
     */
    public void markAsExpired() {
        if (status == LaborPolicyStatus.EXPIRED) {
            return;
        }
        
        this.status = LaborPolicyStatus.EXPIRED;
        this.lastUpdatedDate = DateTimeUtils.getCurrentDate();
        this.version++;
    }
    
    /**
     * 检查政策是否可用
     * 
     * @return 是否可用
     */
    public boolean isUsable() {
        if (!status.isActive()) {
            return false;
        }
        
        // 检查是否在有效期内
        LocalDate today = DateTimeUtils.getCurrentDate();
        
        if (effectiveDate != null && effectiveDate.isAfter(today)) {
            return false;
        }
        
        if (expiryDate != null && expiryDate.isBefore(today)) {
            return false;
        }
        
        return true;
    }
    
    @Override
    public LaborPolicyId getId() {
        return id;
    }
} 