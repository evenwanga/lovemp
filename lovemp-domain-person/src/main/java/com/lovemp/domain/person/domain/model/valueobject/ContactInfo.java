package com.lovemp.domain.person.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 联系方式值对象
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContactInfo implements ValueObject {
    
    /**
     * 手机号码
     */
    private String mobile;
    
    /**
     * 电子邮箱
     */
    private String email;
    
    /**
     * 固定电话
     */
    private String telephone;
    
    /**
     * 微信号
     */
    private String wechat;
    
    /**
     * 紧急联系人
     */
    private String emergencyContact;
    
    /**
     * 紧急联系人电话
     */
    private String emergencyPhone;
    
    /**
     * 创建联系方式值对象
     * 
     * @param mobile 手机号码
     * @param email 电子邮箱
     * @param telephone 固定电话
     * @param wechat 微信号
     * @param emergencyContact 紧急联系人
     * @param emergencyPhone 紧急联系人电话
     * @return 联系方式值对象
     */
    public static ContactInfo of(String mobile, String email, String telephone,
                               String wechat, String emergencyContact, String emergencyPhone) {
        return new ContactInfo(mobile, email, telephone, wechat, emergencyContact, emergencyPhone);
    }
    
    /**
     * 创建包含基本联系方式的值对象
     * 
     * @param mobile 手机号码
     * @param email 电子邮箱
     * @return 基本联系方式值对象
     */
    public static ContactInfo basic(String mobile, String email) {
        return new ContactInfo(mobile, email, null, null, null, null);
    }
    
    /**
     * 更新手机号码
     * 
     * @param mobile 新的手机号码
     * @return 更新后的联系方式对象
     */
    public ContactInfo withMobile(String mobile) {
        return new ContactInfo(mobile, this.email, this.telephone, this.wechat, 
                              this.emergencyContact, this.emergencyPhone);
    }
    
    /**
     * 更新电子邮箱
     * 
     * @param email 新的电子邮箱
     * @return 更新后的联系方式对象
     */
    public ContactInfo withEmail(String email) {
        return new ContactInfo(this.mobile, email, this.telephone, this.wechat, 
                              this.emergencyContact, this.emergencyPhone);
    }
    
    /**
     * 更新紧急联系人信息
     * 
     * @param emergencyContact 紧急联系人
     * @param emergencyPhone 紧急联系人电话
     * @return 更新后的联系方式对象
     */
    public ContactInfo withEmergencyContact(String emergencyContact, String emergencyPhone) {
        return new ContactInfo(this.mobile, this.email, this.telephone, this.wechat, 
                              emergencyContact, emergencyPhone);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactInfo that = (ContactInfo) o;
        return Objects.equals(mobile, that.mobile) &&
               Objects.equals(email, that.email) &&
               Objects.equals(telephone, that.telephone) &&
               Objects.equals(wechat, that.wechat) &&
               Objects.equals(emergencyContact, that.emergencyContact) &&
               Objects.equals(emergencyPhone, that.emergencyPhone);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(mobile, email, telephone, wechat, emergencyContact, emergencyPhone);
    }
}