package com.lovemp.domain.person.domain.model.valueobject;

/**
 * 证件类型枚举
 */
public enum IdentityDocumentType {
    /**
     * 身份证
     */
    ID_CARD("身份证"),
    
    /**
     * 护照
     */
    PASSPORT("护照"),
    
    /**
     * 军官证
     */
    MILITARY_ID("军官证"),
    
    /**
     * 港澳通行证
     */
    HK_MO_PASS("港澳通行证"),
    
    /**
     * 台湾通行证
     */
    TAIWAN_PASS("台湾通行证"),
    
    /**
     * 外国人永久居留证
     */
    FOREIGN_PERMANENT_RESIDENCE("外国人永久居留证"),
    
    /**
     * 其他
     */
    OTHER("其他");
    
    private final String description;
    
    IdentityDocumentType(String description) {
        this.description = description;
    }
    
    public String getDescription() {
        return description;
    }
}