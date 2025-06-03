package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.Assert;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * 带标签的联系方式值对象
 */
@Getter
@ToString
public class ContactInfoWithTag implements ValueObject {
    
    /**
     * 联系方式
     */
    private final ContactInfo contactInfo;
    
    /**
     * 标签
     */
    private final String tag;
    
    /**
     * 是否为默认联系方式
     */
    private boolean isDefault;
    
    /**
     * 构造函数
     * 
     * @param contactInfo 联系方式
     * @param tag 标签
     * @param isDefault 是否为默认
     */
    public ContactInfoWithTag(ContactInfo contactInfo, String tag, boolean isDefault) {
        Assert.notNull(contactInfo, "联系方式不能为空");
        Assert.notNull(tag, "标签不能为空");
        this.contactInfo = contactInfo;
        this.tag = tag;
        this.isDefault = isDefault;
    }
    
    /**
     * 创建默认联系方式
     * 
     * @param contactInfo 联系方式
     * @return 带默认标签的联系方式
     */
    public static ContactInfoWithTag asDefault(ContactInfo contactInfo) {
        return new ContactInfoWithTag(contactInfo, "默认", true);
    }
    
    /**
     * 创建业务联系方式
     * 
     * @param contactInfo 联系方式
     * @return 带业务标签的联系方式
     */
    public static ContactInfoWithTag asBusiness(ContactInfo contactInfo) {
        return new ContactInfoWithTag(contactInfo, "业务", false);
    }
    
    /**
     * 创建财务联系方式
     * 
     * @param contactInfo 联系方式
     * @return 带财务标签的联系方式
     */
    public static ContactInfoWithTag asFinance(ContactInfo contactInfo) {
        return new ContactInfoWithTag(contactInfo, "财务", false);
    }
    
    /**
     * 创建客服联系方式
     * 
     * @param contactInfo 联系方式
     * @return 带客服标签的联系方式
     */
    public static ContactInfoWithTag asCustomerService(ContactInfo contactInfo) {
        return new ContactInfoWithTag(contactInfo, "客服", false);
    }
    
    /**
     * 设置是否为默认联系方式
     * 
     * @param isDefault 是否为默认
     */
    public void setDefault(boolean isDefault) {
        this.isDefault = isDefault;
    }
    
    /**
     * 比较两个标签联系方式是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        ContactInfoWithTag that = (ContactInfoWithTag) o;
        return isDefault == that.isDefault &&
                Objects.equals(contactInfo, that.contactInfo) &&
                Objects.equals(tag, that.tag);
    }
    
    /**
     * 计算哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(contactInfo, tag, isDefault);
    }
} 