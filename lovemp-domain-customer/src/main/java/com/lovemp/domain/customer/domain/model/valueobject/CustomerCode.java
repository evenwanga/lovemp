package com.lovemp.domain.customer.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.Assert;
import com.lovemp.common.util.StringUtils;

import java.util.Objects;

/**
 * 顾客编码值对象
 */
public class CustomerCode implements ValueObject {
    
    private final String value;
    
    private CustomerCode(String value) {
        Assert.notEmpty(value, "顾客编码不能为空");
        Assert.isTrue(value.length() <= 30, "顾客编码长度不能超过30个字符");
        this.value = value;
    }
    
    /**
     * 创建顾客编码
     * 
     * @param value 编码值
     * @return 顾客编码
     */
    public static CustomerCode of(String value) {
        return new CustomerCode(value);
    }
    
    /**
     * 生成顾客编码
     * 
     * @param brandCode 品牌编码
     * @param sequence 序号
     * @return 顾客编码
     */
    public static CustomerCode generate(String brandCode, long sequence) {
        Assert.notEmpty(brandCode, "品牌编码不能为空");
        Assert.isTrue(sequence > 0, "序号必须大于0");
        
        String code = String.format("%s%08d", brandCode, sequence);
        return new CustomerCode(code);
    }
    
    /**
     * 获取编码值
     * 
     * @return 编码值
     */
    public String getValue() {
        return value;
    }
    
    /**
     * 是否为有效的顾客编码格式
     * 
     * @param code 编码
     * @return true-有效，false-无效
     */
    public static boolean isValid(String code) {
        if (StringUtils.isEmpty(code)) {
            return false;
        }
        if (code.length() > 30) {
            return false;
        }
        // 可以添加更多的格式验证规则
        return true;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomerCode that = (CustomerCode) o;
        return Objects.equals(value, that.value);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
    
    @Override
    public String toString() {
        return value;
    }
} 