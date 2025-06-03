package com.lovemp.domain.person.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

/**
 * 地址值对象
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Address implements ValueObject {
    
    /**
     * 国家
     */
    private String country;
    
    /**
     * 省/州
     */
    private String province;
    
    /**
     * 城市
     */
    private String city;
    
    /**
     * 区/县
     */
    private String district;
    
    /**
     * 街道/乡镇
     */
    private String street;
    
    /**
     * 详细地址
     */
    private String detail;
    
    /**
     * 邮政编码
     */
    private String postalCode;
    
    /**
     * 创建地址值对象
     * 
     * @param country 国家
     * @param province 省/州
     * @param city 城市
     * @param district 区/县
     * @param street 街道/乡镇
     * @param detail 详细地址
     * @param postalCode 邮政编码
     * @return 地址值对象
     */
    public static Address of(String country, String province, String city, 
                            String district, String street, String detail, String postalCode) {
        return new Address(country, province, city, district, street, detail, postalCode);
    }
    
    /**
     * 创建中国地址
     * 
     * @param province 省
     * @param city 市
     * @param district 区/县
     * @param street 街道/乡镇
     * @param detail 详细地址
     * @param postalCode 邮政编码
     * @return 中国地址值对象
     */
    public static Address ofChina(String province, String city, String district, 
                                 String street, String detail, String postalCode) {
        return new Address("中国", province, city, district, street, detail, postalCode);
    }
    
    /**
     * 获取格式化的完整地址
     * 
     * @return 格式化的地址字符串
     */
    public String getFormattedAddress() {
        StringBuilder sb = new StringBuilder();
        if (country != null && !country.isEmpty()) {
            sb.append(country).append(" ");
        }
        if (province != null && !province.isEmpty()) {
            sb.append(province).append(" ");
        }
        if (city != null && !city.isEmpty()) {
            sb.append(city).append(" ");
        }
        if (district != null && !district.isEmpty()) {
            sb.append(district).append(" ");
        }
        if (street != null && !street.isEmpty()) {
            sb.append(street).append(" ");
        }
        if (detail != null && !detail.isEmpty()) {
            sb.append(detail);
        }
        return sb.toString().trim();
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return Objects.equals(country, address.country) &&
               Objects.equals(province, address.province) &&
               Objects.equals(city, address.city) &&
               Objects.equals(district, address.district) &&
               Objects.equals(street, address.street) &&
               Objects.equals(detail, address.detail) &&
               Objects.equals(postalCode, address.postalCode);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(country, province, city, district, street, detail, postalCode);
    }
}