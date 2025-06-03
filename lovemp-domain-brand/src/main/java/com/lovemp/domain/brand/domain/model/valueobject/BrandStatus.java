package com.lovemp.domain.brand.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;

/**
 * 品牌状态
 */
public enum BrandStatus implements ValueObject {
    
    /**
     * 活跃
     */
    ACTIVE("ACTIVE", "活跃", "品牌正常运营中"),
    
    /**
     * 暂停
     */
    SUSPENDED("SUSPENDED", "暂停", "品牌运营暂停"),
    
    /**
     * 关闭
     */
    CLOSED("CLOSED", "关闭", "品牌已关闭"),
    
    /**
     * 待审核
     */
    PENDING_APPROVAL("PENDING_APPROVAL", "待审核", "品牌等待审核"),
    
    /**
     * 审核拒绝
     */
    APPROVAL_REJECTED("APPROVAL_REJECTED", "审核拒绝", "品牌审核被拒绝"),
    
    /**
     * 草稿
     */
    DRAFT("DRAFT", "草稿", "品牌尚在规划中");
    
    private final String code;
    private final String name;
    private final String description;
    
    BrandStatus(String code, String name, String description) {
        this.code = code;
        this.name = name;
        this.description = description;
    }
    
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    /**
     * 根据编码获取品牌状态
     *
     * @param code 编码
     * @return 品牌状态
     */
    public static BrandStatus fromCode(String code) {
        for (BrandStatus status : BrandStatus.values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("未知的品牌状态编码: " + code);
    }
    
    /**
     * 判断编码是否有效
     *
     * @param code 编码
     * @return 是否有效
     */
    public static boolean isValidCode(String code) {
        for (BrandStatus status : BrandStatus.values()) {
            if (status.getCode().equals(code)) {
                return true;
            }
        }
        return false;
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
     * 判断是否是关闭状态
     * 
     * @return 是否关闭
     */
    public boolean isClosed() {
        return this == CLOSED;
    }
    
    /**
     * 判断是否是暂停状态
     * 
     * @return 是否暂停
     */
    public boolean isSuspended() {
        return this == SUSPENDED;
    }
} 