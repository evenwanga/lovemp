package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.LocalDateTime;

/**
 * 企业资源账号值对象
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ResourceAccount implements ValueObject {
    
    /**
     * 账号ID
     */
    String id;
    
    /**
     * 账号名称
     */
    String name;
    
    /**
     * 账号类型
     */
    ResourceAccountType type;
    
    /**
     * 账号提供商
     */
    String provider;
    
    /**
     * 账号标识（如账号ID/邮箱等）
     */
    String accountIdentifier;
    
    /**
     * 账号创建时间
     */
    LocalDateTime creationTime;
    
    /**
     * 最后更新时间
     */
    LocalDateTime lastUpdateTime;
    
    /**
     * 关联品牌ID（如果为null则表示企业级账号，否则为品牌级账号）
     */
    String brandId;
    
    /**
     * 管理员联系人
     */
    String administrator;
    
    /**
     * 管理员联系方式
     */
    String administratorContact;
    
    /**
     * 账号状态
     */
    ResourceAccountStatus status;
    
    /**
     * 备注（如密码保存位置等敏感信息提示）
     */
    String remark;
    
    /**
     * 创建资源账号
     * 
     * @param id 账号ID
     * @param name 账号名称
     * @param type 账号类型
     * @param provider 账号提供商
     * @param accountIdentifier 账号标识
     * @param creationTime 账号创建时间
     * @param administrator 管理员联系人
     * @param administratorContact 管理员联系方式
     * @param status 账号状态
     * @param remark 备注
     * @return 资源账号值对象
     */
    public static ResourceAccount of(
            String id,
            String name,
            ResourceAccountType type,
            String provider,
            String accountIdentifier,
            LocalDateTime creationTime,
            String administrator,
            String administratorContact,
            ResourceAccountStatus status,
            String remark) {
        
        return of(id, name, type, provider, accountIdentifier, creationTime,
                  DateTimeUtils.getCurrentDateTime(), null, administrator, administratorContact, status, remark);
    }
    
    /**
     * 创建品牌级资源账号
     * 
     * @param id 账号ID
     * @param name 账号名称
     * @param type 账号类型
     * @param provider 账号提供商
     * @param accountIdentifier 账号标识
     * @param creationTime 账号创建时间
     * @param brandId 关联品牌ID
     * @param administrator 管理员联系人
     * @param administratorContact 管理员联系方式
     * @param status 账号状态
     * @param remark 备注
     * @return 资源账号值对象
     */
    public static ResourceAccount ofBrand(
            String id,
            String name,
            ResourceAccountType type,
            String provider,
            String accountIdentifier,
            LocalDateTime creationTime,
            String brandId,
            String administrator,
            String administratorContact,
            ResourceAccountStatus status,
            String remark) {
        
        if (StringUtils.isBlank(brandId)) {
            throw new DomainRuleViolationException("品牌ID不能为空");
        }
        
        return of(id, name, type, provider, accountIdentifier, creationTime,
                  DateTimeUtils.getCurrentDateTime(), brandId, administrator, administratorContact, status, remark);
    }
    
    /**
     * 创建资源账号（完整参数）
     * 
     * @param id 账号ID
     * @param name 账号名称
     * @param type 账号类型
     * @param provider 账号提供商
     * @param accountIdentifier 账号标识
     * @param creationTime 账号创建时间
     * @param lastUpdateTime 最后更新时间
     * @param brandId 关联品牌ID
     * @param administrator 管理员联系人
     * @param administratorContact 管理员联系方式
     * @param status 账号状态
     * @param remark 备注
     * @return 资源账号值对象
     */
    public static ResourceAccount of(
            String id,
            String name,
            ResourceAccountType type,
            String provider,
            String accountIdentifier,
            LocalDateTime creationTime,
            LocalDateTime lastUpdateTime,
            String brandId,
            String administrator,
            String administratorContact,
            ResourceAccountStatus status,
            String remark) {
        
        if (StringUtils.isBlank(name)) {
            throw new DomainRuleViolationException("账号名称不能为空");
        }
        
        if (type == null) {
            throw new DomainRuleViolationException("账号类型不能为空");
        }
        
        if (StringUtils.isBlank(provider)) {
            throw new DomainRuleViolationException("账号提供商不能为空");
        }
        
        if (StringUtils.isBlank(accountIdentifier)) {
            throw new DomainRuleViolationException("账号标识不能为空");
        }
        
        if (creationTime == null) {
            creationTime = DateTimeUtils.getCurrentDateTime();
        }
        
        if (lastUpdateTime == null) {
            lastUpdateTime = DateTimeUtils.getCurrentDateTime();
        }
        
        if (status == null) {
            status = ResourceAccountStatus.ACTIVE;
        }
        
        return new ResourceAccount(
                id,
                name,
                type,
                provider,
                accountIdentifier,
                creationTime,
                lastUpdateTime,
                brandId,
                administrator,
                administratorContact,
                status,
                remark);
    }
    
    /**
     * 是否为企业级账号
     * 
     * @return 如果是企业级账号返回true
     */
    public boolean isEnterpriseLevel() {
        return StringUtils.isBlank(brandId);
    }
    
    /**
     * 是否为品牌级账号
     * 
     * @return 如果是品牌级账号返回true
     */
    public boolean isBrandLevel() {
        return !isEnterpriseLevel();
    }
    
    /**
     * 账号是否处于活跃状态
     * 
     * @return 如果账号处于活跃状态返回true
     */
    public boolean isActive() {
        return status == ResourceAccountStatus.ACTIVE;
    }
    
    /**
     * 账号是否已暂停
     * 
     * @return 如果账号已暂停返回true
     */
    public boolean isSuspended() {
        return status == ResourceAccountStatus.SUSPENDED;
    }
    
    /**
     * 账号是否已过期
     * 
     * @return 如果账号已过期返回true
     */
    public boolean isExpired() {
        return status == ResourceAccountStatus.EXPIRED;
    }
    
    /**
     * 更新状态
     * 
     * @param status 新状态
     * @return 更新后的资源账号
     */
    public ResourceAccount withStatus(ResourceAccountStatus status) {
        return new ResourceAccount(
                this.id,
                this.name,
                this.type,
                this.provider,
                this.accountIdentifier,
                this.creationTime,
                DateTimeUtils.getCurrentDateTime(),
                this.brandId,
                this.administrator,
                this.administratorContact,
                status,
                this.remark);
    }
    
    /**
     * 更新管理员信息
     * 
     * @param administrator 新管理员
     * @param administratorContact 新管理员联系方式
     * @return 更新后的资源账号
     */
    public ResourceAccount withAdministrator(String administrator, String administratorContact) {
        return new ResourceAccount(
                this.id,
                this.name,
                this.type,
                this.provider,
                this.accountIdentifier,
                this.creationTime,
                DateTimeUtils.getCurrentDateTime(),
                this.brandId,
                administrator,
                administratorContact,
                this.status,
                this.remark);
    }
} 