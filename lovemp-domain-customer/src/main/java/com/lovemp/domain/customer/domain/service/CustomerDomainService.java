package com.lovemp.domain.customer.domain.service;

import com.lovemp.common.util.Assert;
import com.lovemp.domain.customer.domain.model.aggregate.BrandCustomer;
import com.lovemp.domain.customer.domain.model.entity.CustomerSharing;
import com.lovemp.domain.customer.domain.model.valueobject.*;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;

import java.time.LocalDate;
import java.util.List;

/**
 * 顾客领域服务
 * 
 * <p>处理跨聚合的业务逻辑</p>
 */
public class CustomerDomainService {
    
    /**
     * 创建品牌顾客关系
     * 
     * @param brandId 品牌ID
     * @param personId 自然人ID
     * @param customerType 顾客类型
     * @param relationType 关系类型
     * @param brandCode 品牌编码
     * @param sequence 序号
     * @return 品牌顾客实例
     */
    public BrandCustomer createBrandCustomer(String brandId, PersonId personId, 
                                           CustomerType customerType, RelationType relationType,
                                           String brandCode, long sequence) {
        Assert.notEmpty(brandId, "品牌ID不能为空");
        Assert.notNull(personId, "自然人ID不能为空");
        Assert.notNull(customerType, "顾客类型不能为空");
        Assert.notNull(relationType, "关系类型不能为空");
        Assert.notEmpty(brandCode, "品牌编码不能为空");
        Assert.isTrue(sequence > 0, "序号必须大于0");
        
        CustomerRelationId id = CustomerRelationId.generate();
        CustomerCode customerCode = CustomerCode.generate(brandCode, sequence);
        
        return BrandCustomer.create(id, brandId, personId, customerCode, customerType, relationType);
    }
    
    /**
     * 创建顾客共享关系
     * 
     * @param sourceBrandId 源品牌ID
     * @param targetBrandId 目标品牌ID
     * @param personId 自然人ID
     * @param sharingType 共享类型
     * @param authLevel 授权级别
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 顾客共享实例
     */
    public CustomerSharing createCustomerSharing(String sourceBrandId, String targetBrandId,
                                                PersonId personId, SharingType sharingType,
                                                AuthLevel authLevel, LocalDate startDate, LocalDate endDate) {
        Assert.notEmpty(sourceBrandId, "源品牌ID不能为空");
        Assert.notEmpty(targetBrandId, "目标品牌ID不能为空");
        Assert.notNull(personId, "自然人ID不能为空");
        Assert.notNull(sharingType, "共享类型不能为空");
        Assert.notNull(authLevel, "授权级别不能为空");
        Assert.notNull(startDate, "开始日期不能为空");
        
        SharingId sharingId = SharingId.generate();
        
        return CustomerSharing.create(sharingId, sourceBrandId, targetBrandId, personId,
                                    sharingType, authLevel, startDate, endDate);
    }
    
    /**
     * 检查是否可以创建共享关系
     * 
     * @param sourceBrandId 源品牌ID
     * @param targetBrandId 目标品牌ID
     * @param personId 自然人ID
     * @param existingSharings 已存在的共享关系
     * @return true-可以创建，false-不可以创建
     */
    public boolean canCreateSharing(String sourceBrandId, String targetBrandId, PersonId personId,
                                  List<CustomerSharing> existingSharings) {
        Assert.notEmpty(sourceBrandId, "源品牌ID不能为空");
        Assert.notEmpty(targetBrandId, "目标品牌ID不能为空");
        Assert.notNull(personId, "自然人ID不能为空");
        
        if (sourceBrandId.equals(targetBrandId)) {
            return false; // 同一品牌不能创建共享关系
        }
        
        if (existingSharings == null || existingSharings.isEmpty()) {
            return true; // 没有已存在的共享关系，可以创建
        }
        
        // 检查是否已存在相同的共享关系
        return existingSharings.stream()
                .noneMatch(sharing -> 
                    sharing.getSourceBrandId().equals(sourceBrandId) &&
                    sharing.getTargetBrandId().equals(targetBrandId) &&
                    sharing.getPersonId().equals(personId) &&
                    (sharing.getStatus().isActive() || sharing.getStatus().isPending())
                );
    }
    
    /**
     * 计算顾客等级
     * 
     * @param points 积分
     * @param customerType 顾客类型
     * @return 等级
     */
    public int calculateCustomerLevel(int points, CustomerType customerType) {
        Assert.isTrue(points >= 0, "积分不能为负数");
        Assert.notNull(customerType, "顾客类型不能为空");
        
        // 根据顾客类型调整等级计算规则
        if (customerType.isVip()) {
            return calculateVipLevel(points);
        } else if (customerType.isWholesale()) {
            return calculateWholesaleLevel(points);
        } else {
            return calculateNormalLevel(points);
        }
    }
    
    /**
     * 检查是否可以升级为VIP
     * 
     * @param customer 品牌顾客
     * @return true-可以升级，false-不可以升级
     */
    public boolean canUpgradeToVip(BrandCustomer customer) {
        Assert.notNull(customer, "品牌顾客不能为空");
        
        if (customer.isVip()) {
            return false; // 已经是VIP，无需升级
        }
        
        if (!customer.canOperate()) {
            return false; // 状态不允许操作
        }
        
        // VIP升级条件：积分达到5000且等级达到3级
        return customer.getPoints() >= 5000 && customer.getLevel() >= 3;
    }
    
    /**
     * 检查共享关系是否即将过期
     * 
     * @param sharings 共享关系列表
     * @param warningDays 预警天数
     * @return 即将过期的共享关系列表
     */
    public List<CustomerSharing> findExpiringSharings(List<CustomerSharing> sharings, int warningDays) {
        Assert.notNull(sharings, "共享关系列表不能为空");
        Assert.isTrue(warningDays > 0, "预警天数必须大于0");
        
        LocalDate warningDate = LocalDate.now().plusDays(warningDays);
        
        return sharings.stream()
                .filter(sharing -> sharing.getStatus().isActive())
                .filter(sharing -> sharing.getEndDate() != null)
                .filter(sharing -> !sharing.getEndDate().isAfter(warningDate))
                .toList();
    }
    
    /**
     * 计算普通顾客等级
     * 
     * @param points 积分
     * @return 等级
     */
    private int calculateNormalLevel(int points) {
        if (points >= 10000) {
            return 5; // 钻石级
        } else if (points >= 5000) {
            return 4; // 白金级
        } else if (points >= 2000) {
            return 3; // 黄金级
        } else if (points >= 500) {
            return 2; // 银牌级
        } else {
            return 1; // 普通级
        }
    }
    
    /**
     * 计算VIP顾客等级
     * 
     * @param points 积分
     * @return 等级
     */
    private int calculateVipLevel(int points) {
        // VIP顾客等级计算规则更宽松
        if (points >= 8000) {
            return 5; // 钻石VIP
        } else if (points >= 4000) {
            return 4; // 白金VIP
        } else if (points >= 1500) {
            return 3; // 黄金VIP
        } else if (points >= 300) {
            return 2; // 银牌VIP
        } else {
            return 1; // 普通VIP
        }
    }
    
    /**
     * 计算批发顾客等级
     * 
     * @param points 积分
     * @return 等级
     */
    private int calculateWholesaleLevel(int points) {
        // 批发顾客等级计算规则更严格
        if (points >= 15000) {
            return 5; // 钻石批发商
        } else if (points >= 8000) {
            return 4; // 白金批发商
        } else if (points >= 3000) {
            return 3; // 黄金批发商
        } else if (points >= 1000) {
            return 2; // 银牌批发商
        } else {
            return 1; // 普通批发商
        }
    }
} 