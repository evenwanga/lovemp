package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.Assert;
import lombok.Getter;
import lombok.ToString;

import java.util.Objects;

/**
 * 带标签的地址值对象
 */
@Getter
@ToString
public class AddressWithTag implements ValueObject {
    
    /**
     * 地址
     */
    private final Address address;
    
    /**
     * 标签
     */
    private final String tag;
    
    /**
     * 是否为总部地址
     */
    private boolean isHeadquarters;
    
    /**
     * 构造函数
     * 
     * @param address 地址
     * @param tag 标签
     * @param isHeadquarters 是否为总部
     */
    public AddressWithTag(Address address, String tag, boolean isHeadquarters) {
        Assert.notNull(address, "地址不能为空");
        Assert.notNull(tag, "标签不能为空");
        this.address = address;
        this.tag = tag;
        this.isHeadquarters = isHeadquarters;
    }
    
    /**
     * 创建总部地址
     * 
     * @param address 地址
     * @return 带总部标签的地址
     */
    public static AddressWithTag asHeadquarters(Address address) {
        return new AddressWithTag(address, "总部", true);
    }
    
    /**
     * 创建分支机构地址
     * 
     * @param address 地址
     * @param branchName 分支机构名称
     * @return 带分支标签的地址
     */
    public static AddressWithTag asBranch(Address address, String branchName) {
        return new AddressWithTag(address, "分支机构-" + branchName, false);
    }
    
    /**
     * 创建办公地址
     * 
     * @param address 地址
     * @return 带办公室标签的地址
     */
    public static AddressWithTag asOffice(Address address) {
        return new AddressWithTag(address, "办公室", false);
    }
    
    /**
     * 设置是否为总部地址
     * 
     * @param isHeadquarters 是否为总部
     */
    public void setHeadquarters(boolean isHeadquarters) {
        this.isHeadquarters = isHeadquarters;
    }
    
    /**
     * 比较两个标签地址是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        AddressWithTag that = (AddressWithTag) o;
        return isHeadquarters == that.isHeadquarters &&
                Objects.equals(address, that.address) &&
                Objects.equals(tag, that.tag);
    }
    
    /**
     * 计算哈希码
     */
    @Override
    public int hashCode() {
        return Objects.hash(address, tag, isHeadquarters);
    }
} 