package com.lovemp.domain.brand.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.Assert;

import java.util.Objects;

/**
 * 产品类别值对象
 */
public class ProductCategory implements ValueObject {
    
    private final String code;
    private final String name;
    private final String description;
    private final String parentCode;
    
    public ProductCategory(String code, String name, String description, String parentCode) {
        Assert.notEmpty(code, "类别编码不能为空");
        Assert.notEmpty(name, "类别名称不能为空");
        this.code = code;
        this.name = name;
        this.description = description;
        this.parentCode = parentCode;
    }
    
    /**
     * 创建根类别
     * 
     * @param code 类别编码
     * @param name 类别名称
     * @param description 类别描述
     * @return 产品类别
     */
    public static ProductCategory createRootCategory(String code, String name, String description) {
        return new ProductCategory(code, name, description, null);
    }
    
    /**
     * 创建子类别
     * 
     * @param code 类别编码
     * @param name 类别名称
     * @param description 类别描述
     * @param parentCode 父类别编码
     * @return 产品类别
     */
    public static ProductCategory createSubCategory(String code, String name, String description, String parentCode) {
        Assert.notEmpty(parentCode, "父类别编码不能为空");
        return new ProductCategory(code, name, description, parentCode);
    }
    
    /**
     * 是否为根类别
     * 
     * @return 是否为根类别
     */
    public boolean isRootCategory() {
        return parentCode == null || parentCode.isEmpty();
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
    
    public String getParentCode() {
        return parentCode;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ProductCategory that = (ProductCategory) o;
        return Objects.equals(code, that.code);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(code);
    }
    
    @Override
    public String toString() {
        return "ProductCategory{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", parentCode='" + parentCode + '\'' +
                '}';
    }
} 