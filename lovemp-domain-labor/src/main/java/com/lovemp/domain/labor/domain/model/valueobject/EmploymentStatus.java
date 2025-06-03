package com.lovemp.domain.labor.domain.model.valueobject;

/**
 * 雇佣状态 - 值对象
 */
public enum EmploymentStatus {
    
    /**
     * 待入职
     */
    PENDING_ONBOARD("待入职"),
    
    /**
     * 在职
     */
    ACTIVE("在职"),
    
    /**
     * 离职中
     */
    LEAVING("离职中"),
    
    /**
     * 已离职
     */
    TERMINATED("已离职"),
    
    /**
     * 已取消
     */
    CANCELED("已取消");
    
    private final String description;
    
    EmploymentStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 判断是否是活跃状态
     *
     * @return 是否活跃
     */
    public boolean isActive() {
        return this == ACTIVE;
    }
    
    /**
     * 判断是否可以入职
     *
     * @return 是否可以入职
     */
    public boolean canOnboard() {
        return this == PENDING_ONBOARD;
    }
    
    /**
     * 判断是否可以离职
     *
     * @return 是否可以离职
     */
    public boolean canLeave() {
        return this == ACTIVE;
    }
    
    /**
     * 判断是否可以取消
     *
     * @return 是否可以取消
     */
    public boolean canCancel() {
        return this == PENDING_ONBOARD;
    }
    
    /**
     * 检查是否可以发起离职
     */
    public boolean canInitiateLeaving() {
        return this == ACTIVE;
    }
    
    /**
     * 检查是否可以完成离职
     */
    public boolean canTerminate() {
        return this == LEAVING;
    }
}