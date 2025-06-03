package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * 地址值对象
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Address implements ValueObject {
    
    /**
     * 省份
     */
    String province;
    
    /**
     * 城市
     */
    String city;
    
    /**
     * 区县
     */
    String district;
    
    /**
     * 街道
     */
    String street;
    
    /**
     * 详细地址
     */
    String detail;
    
    /**
     * 邮政编码
     */
    String zipCode;
    
    /**
     * 经度（可选）
     */
    Double longitude;
    
    /**
     * 纬度（可选）
     */
    Double latitude;
    
    /**
     * 创建地址
     * 
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @param street 街道
     * @param detail 详细地址
     * @param zipCode 邮政编码
     * @return 地址值对象
     */
    public static Address of(String province, String city, String district, String street, String detail, String zipCode) {
        return of(province, city, district, street, detail, zipCode, null, null);
    }
    
    /**
     * 创建地址
     * 
     * @param province 省份
     * @param city 城市
     * @param district 区县
     * @param street 街道
     * @param detail 详细地址
     * @param zipCode 邮政编码
     * @param longitude 经度
     * @param latitude 纬度
     * @return 地址值对象
     */
    public static Address of(String province, String city, String district, String street, String detail, String zipCode, Double longitude, Double latitude) {
        validate(province, city, detail);
        return new Address(province, city, district, street, detail, zipCode, longitude, latitude);
    }
    
    /**
     * 验证地址信息
     * 
     * @param province 省份
     * @param city 城市
     * @param detail 详细地址
     */
    private static void validate(String province, String city, String detail) {
        if (StringUtils.isBlank(province)) {
            throw new DomainRuleViolationException("省份不能为空");
        }
        
        if (StringUtils.isBlank(city)) {
            throw new DomainRuleViolationException("城市不能为空");
        }
        
        if (StringUtils.isBlank(detail)) {
            throw new DomainRuleViolationException("详细地址不能为空");
        }
    }
    
    /**
     * 获取完整地址字符串
     * 
     * @return 完整地址
     */
    public String getFullAddress() {
        StringBuilder sb = new StringBuilder();
        sb.append(province);
        
        if (!StringUtils.isBlank(city)) {
            sb.append(city);
        }
        
        if (!StringUtils.isBlank(district)) {
            sb.append(district);
        }
        
        if (!StringUtils.isBlank(street)) {
            sb.append(street);
        }
        
        sb.append(detail);
        
        return sb.toString();
    }
    
    /**
     * 地址是否包含经纬度
     * 
     * @return 如果包含经纬度返回true
     */
    public boolean hasCoordinates() {
        return longitude != null && latitude != null;
    }
    
    /**
     * 添加经纬度信息
     * 
     * @param longitude 经度
     * @param latitude 纬度
     * @return 更新后的地址
     */
    public Address withCoordinates(Double longitude, Double latitude) {
        return new Address(
                this.province,
                this.city,
                this.district,
                this.street,
                this.detail,
                this.zipCode,
                longitude,
                latitude
        );
    }
} 