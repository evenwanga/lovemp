package com.lovemp.domain.customer.domain.model.entity;

import com.lovemp.common.domain.Entity;
import com.lovemp.common.util.Assert;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.domain.customer.domain.model.valueobject.SharingId;
import com.lovemp.domain.customer.domain.model.valueobject.SharingType;
import com.lovemp.domain.customer.domain.model.valueobject.AuthLevel;
import com.lovemp.domain.customer.domain.model.valueobject.SharingStatus;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 顾客共享实体
 * 
 * <p>管理自然人在不同品牌间的数据共享关系</p>
 */
@Getter
public class CustomerSharing extends Entity<SharingId> {
    
    /**
     * 源品牌ID
     */
    private String sourceBrandId;
    
    /**
     * 目标品牌ID
     */
    private String targetBrandId;
    
    /**
     * 自然人ID
     */
    private PersonId personId;
    
    /**
     * 共享类型
     */
    private SharingType sharingType;
    
    /**
     * 授权级别
     */
    private AuthLevel authLevel;
    
    /**
     * 开始日期
     */
    private LocalDate startDate;
    
    /**
     * 结束日期
     */
    private LocalDate endDate;
    
    /**
     * 状态
     */
    private SharingStatus status;
    
    /**
     * 创建日期
     */
    private LocalDate createDate;
    
    /**
     * 最后更新日期
     */
    private LocalDate lastUpdateDate;
    
    /**
     * 保护构造函数，防止直接实例化
     */
    protected CustomerSharing() {
    }
    
    /**
     * 创建顾客共享关系
     * 
     * @param id 共享ID
     * @param sourceBrandId 源品牌ID
     * @param targetBrandId 目标品牌ID
     * @param personId 自然人ID
     * @param sharingType 共享类型
     * @param authLevel 授权级别
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 顾客共享实例
     */
    public static CustomerSharing create(SharingId id, String sourceBrandId, String targetBrandId,
                                       PersonId personId, SharingType sharingType, AuthLevel authLevel,
                                       LocalDate startDate, LocalDate endDate) {
        Assert.notNull(id, "共享ID不能为空");
        Assert.notEmpty(sourceBrandId, "源品牌ID不能为空");
        Assert.notEmpty(targetBrandId, "目标品牌ID不能为空");
        Assert.notNull(personId, "自然人ID不能为空");
        Assert.notNull(sharingType, "共享类型不能为空");
        Assert.notNull(authLevel, "授权级别不能为空");
        Assert.notNull(startDate, "开始日期不能为空");
        Assert.isTrue(!sourceBrandId.equals(targetBrandId), "源品牌和目标品牌不能相同");
        
        if (endDate != null) {
            Assert.isTrue(!endDate.isBefore(startDate), "结束日期不能早于开始日期");
        }
        
        CustomerSharing sharing = new CustomerSharing();
        sharing.id = id;
        sharing.sourceBrandId = sourceBrandId;
        sharing.targetBrandId = targetBrandId;
        sharing.personId = personId;
        sharing.sharingType = sharingType;
        sharing.authLevel = authLevel;
        sharing.startDate = startDate;
        sharing.endDate = endDate;
        sharing.status = SharingStatus.PENDING; // 默认待生效状态
        sharing.createDate = DateTimeUtils.getCurrentDate();
        sharing.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        return sharing;
    }
    
    /**
     * 激活共享关系
     */
    public void activate() {
        if (status.isActive()) {
            return; // 已经是激活状态，无需重复激活
        }
        
        if (!isInValidPeriod()) {
            throw new IllegalStateException("不在有效期内，无法激活");
        }
        
        this.status = SharingStatus.ACTIVE;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
    }
    
    /**
     * 使共享关系失效
     * 
     * @param reason 失效原因
     */
    public void deactivate(String reason) {
        Assert.notEmpty(reason, "失效原因不能为空");
        
        if (status.isInactive()) {
            return; // 已经是失效状态，无需重复操作
        }
        
        this.status = SharingStatus.INACTIVE;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
    }
    
    /**
     * 更新授权级别
     * 
     * @param authLevel 新的授权级别
     */
    public void updateAuthLevel(AuthLevel authLevel) {
        Assert.notNull(authLevel, "授权级别不能为空");
        
        if (!status.isActive()) {
            throw new IllegalStateException("只有激活状态的共享关系才能更新授权级别");
        }
        
        this.authLevel = authLevel;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
    }
    
    /**
     * 延长有效期
     * 
     * @param newEndDate 新的结束日期
     */
    public void extendValidPeriod(LocalDate newEndDate) {
        Assert.notNull(newEndDate, "新的结束日期不能为空");
        Assert.isTrue(!newEndDate.isBefore(startDate), "新的结束日期不能早于开始日期");
        
        if (endDate != null && !newEndDate.isAfter(endDate)) {
            throw new IllegalArgumentException("新的结束日期必须晚于当前结束日期");
        }
        
        this.endDate = newEndDate;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
    }
    
    /**
     * 是否在有效期内
     * 
     * @return true-在有效期内，false-不在有效期内
     */
    public boolean isInValidPeriod() {
        LocalDate today = DateTimeUtils.getCurrentDate();
        
        if (today.isBefore(startDate)) {
            return false; // 还未到开始日期
        }
        
        if (endDate != null && today.isAfter(endDate)) {
            return false; // 已过结束日期
        }
        
        return true;
    }
    
    /**
     * 是否可以访问指定级别的数据
     * 
     * @param requiredLevel 需要的授权级别
     * @return true-可以访问，false-不可以访问
     */
    public boolean canAccess(AuthLevel requiredLevel) {
        if (!status.isActive()) {
            return false; // 非激活状态不能访问
        }
        
        if (!isInValidPeriod()) {
            return false; // 不在有效期内不能访问
        }
        
        return authLevel.canAccess(requiredLevel);
    }
    
    /**
     * 是否为自动共享
     * 
     * @return true-是自动共享，false-不是自动共享
     */
    public boolean isAutoSharing() {
        return sharingType.isAuto();
    }
    
    /**
     * 是否为授权共享
     * 
     * @return true-是授权共享，false-不是授权共享
     */
    public boolean isAuthSharing() {
        return sharingType.isAuth();
    }
    
    /**
     * 是否为关联共享
     * 
     * @return true-是关联共享，false-不是关联共享
     */
    public boolean isRelatedSharing() {
        return sharingType.isRelated();
    }
    
    /**
     * 检查是否即将过期（7天内）
     * 
     * @return true-即将过期，false-不会即将过期
     */
    public boolean isExpiringSoon() {
        if (endDate == null) {
            return false; // 永久有效，不会过期
        }
        
        LocalDate today = DateTimeUtils.getCurrentDate();
        LocalDate warningDate = endDate.minusDays(7);
        
        return !today.isBefore(warningDate) && !today.isAfter(endDate);
    }
} 