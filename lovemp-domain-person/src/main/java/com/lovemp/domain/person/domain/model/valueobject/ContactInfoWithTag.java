package com.lovemp.domain.person.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 带标签的联系方式值对象
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContactInfoWithTag implements ValueObject {
    
    /**
     * 联系方式
     */
    private ContactInfo contactInfo;
    
    /**
     * 联系方式标签（例如：家庭、工作等）
     */
    private String tag;
    
    /**
     * 是否为默认联系方式
     */
    private boolean isDefault;
    
    /**
     * 创建带标签的联系方式
     * 
     * @param contactInfo 联系方式
     * @param tag 标签
     * @param isDefault 是否为默认联系方式
     * @return 带标签的联系方式
     */
    public static ContactInfoWithTag of(ContactInfo contactInfo, String tag, boolean isDefault) {
        return new ContactInfoWithTag(contactInfo, tag, isDefault);
    }
    
    /**
     * 创建带标签的联系方式（非默认）
     * 
     * @param contactInfo 联系方式
     * @param tag 标签
     * @return 带标签的联系方式
     */
    public static ContactInfoWithTag of(ContactInfo contactInfo, String tag) {
        return new ContactInfoWithTag(contactInfo, tag, false);
    }
    
    /**
     * 创建无标签的联系方式
     * 
     * @param contactInfo 联系方式
     * @param isDefault 是否为默认联系方式
     * @return 无标签的联系方式
     */
    public static ContactInfoWithTag ofNoTag(ContactInfo contactInfo, boolean isDefault) {
        return new ContactInfoWithTag(contactInfo, null, isDefault);
    }
    
    /**
     * 创建默认联系方式
     * 
     * @param contactInfo 联系方式
     * @return 默认联系方式
     */
    public static ContactInfoWithTag asDefault(ContactInfo contactInfo) {
        return new ContactInfoWithTag(contactInfo, null, true);
    }
    
    /**
     * 设置为默认联系方式
     * 
     * @return 设置为默认的联系方式对象
     */
    public ContactInfoWithTag setAsDefault() {
        return new ContactInfoWithTag(this.contactInfo, this.tag, true);
    }
    
    /**
     * 设置为非默认联系方式
     * 
     * @return 设置为非默认的联系方式对象
     */
    public ContactInfoWithTag unsetAsDefault() {
        return new ContactInfoWithTag(this.contactInfo, this.tag, false);
    }
    
    /**
     * 更新联系方式标签
     * 
     * @param tag 新标签
     * @return 更新后的联系方式对象
     */
    public ContactInfoWithTag withTag(String tag) {
        return new ContactInfoWithTag(this.contactInfo, tag, this.isDefault);
    }
    
    /**
     * 更新联系方式信息
     * 
     * @param contactInfo 新联系方式
     * @return 更新后的联系方式对象
     */
    public ContactInfoWithTag withContactInfo(ContactInfo contactInfo) {
        return new ContactInfoWithTag(contactInfo, this.tag, this.isDefault);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ContactInfoWithTag that = (ContactInfoWithTag) o;
        return isDefault == that.isDefault &&
               Objects.equals(contactInfo, that.contactInfo) &&
               Objects.equals(tag, that.tag);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(contactInfo, tag, isDefault);
    }
}