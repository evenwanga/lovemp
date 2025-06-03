package com.lovemp.domain.customer.domain.model.aggregate;

import com.lovemp.common.domain.AggregateRoot;
import com.lovemp.common.util.Assert;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.domain.customer.domain.event.BrandCustomerCreatedEvent;
import com.lovemp.domain.customer.domain.event.BrandCustomerStatusChangedEvent;
import com.lovemp.domain.customer.domain.event.BrandCustomerUpdatedEvent;
import com.lovemp.domain.customer.domain.model.valueobject.*;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;
import lombok.Getter;

import java.time.LocalDate;

/**
 * 品牌顾客聚合根
 * 
 * <p>表示自然人与品牌之间的顾客关系</p>
 */
@Getter
public class BrandCustomer extends AggregateRoot<CustomerRelationId> {
    
    /**
     * 品牌ID
     */
    private String brandId;
    
    /**
     * 自然人ID
     */
    private PersonId personId;
    
    /**
     * 顾客编码
     */
    private CustomerCode customerCode;
    
    /**
     * 顾客类型
     */
    private CustomerType customerType;
    
    /**
     * 关系类型
     */
    private RelationType relationType;
    
    /**
     * 状态
     */
    private CustomerStatus status;
    
    /**
     * 积分
     */
    private Integer points;
    
    /**
     * 等级
     */
    private Integer level;
    
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
    protected BrandCustomer() {
    }
    
    /**
     * 创建品牌顾客关系
     * 
     * @param id 顾客关系ID
     * @param brandId 品牌ID
     * @param personId 自然人ID
     * @param customerCode 顾客编码
     * @param customerType 顾客类型
     * @param relationType 关系类型
     * @return 品牌顾客实例
     */
    public static BrandCustomer create(CustomerRelationId id, String brandId, PersonId personId,
                                     CustomerCode customerCode, CustomerType customerType,
                                     RelationType relationType) {
        Assert.notNull(id, "顾客关系ID不能为空");
        Assert.notEmpty(brandId, "品牌ID不能为空");
        Assert.notNull(personId, "自然人ID不能为空");
        Assert.notNull(customerCode, "顾客编码不能为空");
        Assert.notNull(customerType, "顾客类型不能为空");
        Assert.notNull(relationType, "关系类型不能为空");
        
        BrandCustomer customer = new BrandCustomer();
        customer.id = id;
        customer.brandId = brandId;
        customer.personId = personId;
        customer.customerCode = customerCode;
        customer.customerType = customerType;
        customer.relationType = relationType;
        customer.status = CustomerStatus.INACTIVE; // 默认未激活状态
        customer.points = 0; // 默认积分为0
        customer.level = 1; // 默认等级为1
        customer.createDate = DateTimeUtils.getCurrentDate();
        customer.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        // 注册领域事件
        customer.registerEvent(new BrandCustomerCreatedEvent(id, brandId, personId, customerCode));
        
        return customer;
    }
    
    /**
     * 激活顾客
     */
    public void activate() {
        if (status.isActive()) {
            return; // 已经是激活状态，无需重复激活
        }
        
        CustomerStatus oldStatus = this.status;
        this.status = CustomerStatus.ACTIVE;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        registerEvent(new BrandCustomerStatusChangedEvent(this.id, oldStatus, this.status));
    }
    
    /**
     * 冻结顾客
     * 
     * @param reason 冻结原因
     */
    public void freeze(String reason) {
        Assert.notEmpty(reason, "冻结原因不能为空");
        
        if (status.isFrozen()) {
            return; // 已经是冻结状态，无需重复冻结
        }
        
        CustomerStatus oldStatus = this.status;
        this.status = CustomerStatus.FROZEN;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        registerEvent(new BrandCustomerStatusChangedEvent(this.id, oldStatus, this.status, reason));
    }
    
    /**
     * 解冻顾客
     */
    public void unfreeze() {
        if (!status.isFrozen()) {
            throw new IllegalStateException("只有冻结状态的顾客才能解冻");
        }
        
        CustomerStatus oldStatus = this.status;
        this.status = CustomerStatus.ACTIVE;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        registerEvent(new BrandCustomerStatusChangedEvent(this.id, oldStatus, this.status));
    }
    
    /**
     * 更新顾客类型
     * 
     * @param customerType 新的顾客类型
     */
    public void updateCustomerType(CustomerType customerType) {
        Assert.notNull(customerType, "顾客类型不能为空");
        
        if (this.customerType == customerType) {
            return; // 类型没有变化，无需更新
        }
        
        this.customerType = customerType;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        registerEvent(new BrandCustomerUpdatedEvent(this.id, "customerType"));
    }
    
    /**
     * 增加积分
     * 
     * @param points 增加的积分数
     */
    public void addPoints(int points) {
        Assert.isTrue(points > 0, "增加的积分必须大于0");
        
        if (!status.canOperate()) {
            throw new IllegalStateException("当前状态不允许积分操作");
        }
        
        this.points += points;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        // 根据积分自动升级
        updateLevelByPoints();
        
        registerEvent(new BrandCustomerUpdatedEvent(this.id, "points"));
    }
    
    /**
     * 扣减积分
     * 
     * @param points 扣减的积分数
     */
    public void deductPoints(int points) {
        Assert.isTrue(points > 0, "扣减的积分必须大于0");
        
        if (!status.canOperate()) {
            throw new IllegalStateException("当前状态不允许积分操作");
        }
        
        if (this.points < points) {
            throw new IllegalArgumentException("积分不足，无法扣减");
        }
        
        this.points -= points;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        // 根据积分自动降级
        updateLevelByPoints();
        
        registerEvent(new BrandCustomerUpdatedEvent(this.id, "points"));
    }
    
    /**
     * 设置等级
     * 
     * @param level 新等级
     */
    public void setLevel(int level) {
        Assert.isTrue(level > 0, "等级必须大于0");
        
        if (this.level == level) {
            return; // 等级没有变化，无需更新
        }
        
        this.level = level;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        registerEvent(new BrandCustomerUpdatedEvent(this.id, "level"));
    }
    
    /**
     * 是否可以进行业务操作
     * 
     * @return true-可以操作，false-不可以操作
     */
    public boolean canOperate() {
        return status.canOperate();
    }
    
    /**
     * 是否为VIP顾客
     * 
     * @return true-是VIP顾客，false-不是VIP顾客
     */
    public boolean isVip() {
        return customerType.isVip();
    }
    
    /**
     * 是否为批发顾客
     * 
     * @return true-是批发顾客，false-不是批发顾客
     */
    public boolean isWholesale() {
        return customerType.isWholesale();
    }
    
    /**
     * 是否为衍生关系
     * 
     * @return true-是衍生关系，false-不是衍生关系
     */
    public boolean isDerivedRelation() {
        return relationType.isDerived();
    }
    
    /**
     * 根据积分自动更新等级
     */
    private void updateLevelByPoints() {
        int newLevel = calculateLevelByPoints(this.points);
        if (newLevel != this.level) {
            this.level = newLevel;
        }
    }
    
    /**
     * 根据积分计算等级
     * 
     * @param points 积分
     * @return 等级
     */
    private int calculateLevelByPoints(int points) {
        // 简单的等级计算规则，可以根据业务需求调整
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
} 