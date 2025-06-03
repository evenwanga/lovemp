package com.lovemp.domain.person.domain.model.valueobject;

/**
 * 证件状态枚举
 */
public enum IdentityDocumentStatus {
    /**
     * 有效
     */
    VALID("有效"),
    
    /**
     * 过期
     */
    EXPIRED("过期"),
    
    /**
     * 挂失
     */
    LOST("挂失"),
    
    /**
     * 作废
     */
    CANCELLED("作废");
    
    private final String description;
    
    IdentityDocumentStatus(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}