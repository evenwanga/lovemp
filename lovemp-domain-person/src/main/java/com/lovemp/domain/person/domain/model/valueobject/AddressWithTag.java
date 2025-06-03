package com.lovemp.domain.person.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 带标签的地址值对象
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AddressWithTag implements ValueObject {
    
    /**
     * 地址
     */
    private Address address;
    
    /**
     * 地址标签（例如：家、公司等）
     */
    private String tag;
    
    /**
     * 是否为默认地址
     */
    private boolean isDefault;
    
    /**
     * 创建带标签的地址
     * 
     * @param address 地址
     * @param tag 标签
     * @param isDefault 是否为默认地址
     * @return 带标签的地址
     */
    public static AddressWithTag of(Address address, String tag, boolean isDefault) {
        return new AddressWithTag(address, tag, isDefault);
    }
    
    /**
     * 创建带标签的地址（非默认）
     * 
     * @param address 地址
     * @param tag 标签
     * @return 带标签的地址
     */
    public static AddressWithTag of(Address address, String tag) {
        return new AddressWithTag(address, tag, false);
    }
    
    /**
     * 创建无标签的地址
     * 
     * @param address 地址
     * @param isDefault 是否为默认地址
     * @return 无标签的地址
     */
    public static AddressWithTag ofNoTag(Address address, boolean isDefault) {
        return new AddressWithTag(address, null, isDefault);
    }
    
    /**
     * 创建默认地址
     * 
     * @param address 地址
     * @return 默认地址
     */
    public static AddressWithTag asDefault(Address address) {
        return new AddressWithTag(address, null, true);
    }
    
    /**
     * 设置为默认地址
     * 
     * @return 设置为默认的地址对象
     */
    public AddressWithTag setAsDefault() {
        return new AddressWithTag(this.address, this.tag, true);
    }
    
    /**
     * 设置为非默认地址
     * 
     * @return 设置为非默认的地址对象
     */
    public AddressWithTag unsetAsDefault() {
        return new AddressWithTag(this.address, this.tag, false);
    }
    
    /**
     * 更新地址标签
     * 
     * @param tag 新标签
     * @return 更新后的地址对象
     */
    public AddressWithTag withTag(String tag) {
        return new AddressWithTag(this.address, tag, this.isDefault);
    }
    
    /**
     * 更新地址信息
     * 
     * @param address 新地址
     * @return 更新后的地址对象
     */
    public AddressWithTag withAddress(Address address) {
        return new AddressWithTag(address, this.tag, this.isDefault);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AddressWithTag that = (AddressWithTag) o;
        return isDefault == that.isDefault &&
               Objects.equals(address, that.address) &&
               Objects.equals(tag, that.tag);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(address, tag, isDefault);
    }
}