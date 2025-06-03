package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * 联系方式值对象
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ContactInfo implements ValueObject {
    
    /**
     * 联系类型
     */
    ContactType type;
    
    /**
     * 联系人姓名
     */
    String contactName;
    
    /**
     * 联系方式值（电话、邮箱等）
     */
    String value;
    
    /**
     * 备注
     */
    String remark;
    
    /**
     * 创建电话联系方式
     * 
     * @param phoneNumber 电话号码
     * @param contactName 联系人姓名
     * @return 联系方式值对象
     */
    public static ContactInfo phone(String phoneNumber, String contactName) {
        return phone(phoneNumber, contactName, null);
    }
    
    /**
     * 创建电话联系方式
     * 
     * @param phoneNumber 电话号码
     * @param contactName 联系人姓名
     * @param remark 备注
     * @return 联系方式值对象
     */
    public static ContactInfo phone(String phoneNumber, String contactName, String remark) {
        validate(phoneNumber, "电话号码");
        return new ContactInfo(ContactType.PHONE, contactName, phoneNumber, remark);
    }
    
    /**
     * 创建邮箱联系方式
     * 
     * @param email 邮箱地址
     * @param contactName 联系人姓名
     * @return 联系方式值对象
     */
    public static ContactInfo email(String email, String contactName) {
        return email(email, contactName, null);
    }
    
    /**
     * 创建邮箱联系方式
     * 
     * @param email 邮箱地址
     * @param contactName 联系人姓名
     * @param remark 备注
     * @return 联系方式值对象
     */
    public static ContactInfo email(String email, String contactName, String remark) {
        validate(email, "邮箱地址");
        return new ContactInfo(ContactType.EMAIL, contactName, email, remark);
    }
    
    /**
     * 创建微信联系方式
     * 
     * @param wechat 微信号
     * @param contactName 联系人姓名
     * @return 联系方式值对象
     */
    public static ContactInfo wechat(String wechat, String contactName) {
        return wechat(wechat, contactName, null);
    }
    
    /**
     * 创建微信联系方式
     * 
     * @param wechat 微信号
     * @param contactName 联系人姓名
     * @param remark 备注
     * @return 联系方式值对象
     */
    public static ContactInfo wechat(String wechat, String contactName, String remark) {
        validate(wechat, "微信号");
        return new ContactInfo(ContactType.WECHAT, contactName, wechat, remark);
    }
    
    /**
     * 创建传真联系方式
     * 
     * @param fax 传真号码
     * @param contactName 联系人姓名
     * @return 联系方式值对象
     */
    public static ContactInfo fax(String fax, String contactName) {
        return fax(fax, contactName, null);
    }
    
    /**
     * 创建传真联系方式
     * 
     * @param fax 传真号码
     * @param contactName 联系人姓名
     * @param remark 备注
     * @return 联系方式值对象
     */
    public static ContactInfo fax(String fax, String contactName, String remark) {
        validate(fax, "传真号码");
        return new ContactInfo(ContactType.FAX, contactName, fax, remark);
    }
    
    /**
     * 创建其他类型联系方式
     * 
     * @param value 联系方式值
     * @param contactName 联系人姓名
     * @param remark 备注
     * @return 联系方式值对象
     */
    public static ContactInfo other(String value, String contactName, String remark) {
        validate(value, "联系方式");
        return new ContactInfo(ContactType.OTHER, contactName, value, remark);
    }
    
    /**
     * 验证联系方式值
     * 
     * @param value 联系方式值
     * @param valueName 值名称
     */
    private static void validate(String value, String valueName) {
        if (StringUtils.isBlank(value)) {
            throw new DomainRuleViolationException(valueName + "不能为空");
        }
    }
    
    /**
     * 联系方式类型枚举
     */
    public enum ContactType {
        /**
         * 电话
         */
        PHONE,
        
        /**
         * 邮箱
         */
        EMAIL,
        
        /**
         * 微信
         */
        WECHAT,
        
        /**
         * 传真
         */
        FAX,
        
        /**
         * 其他
         */
        OTHER
    }
} 