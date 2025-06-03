package com.lovemp.domain.enterprise.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.StringUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * 法定代表人值对象
 */
@Value
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LegalRepresentative implements ValueObject {
    
    /**
     * 法定代表人姓名
     */
    String name;
    
    /**
     * 法定代表人身份证号
     */
    String idNumber;
    
    /**
     * 联系电话
     */
    String phoneNumber;
    
    /**
     * 法定代表人类型（自然人、法人）
     */
    RepresentativeType type;
    
    /**
     * 自然人法定代表人
     * 
     * @param name 姓名
     * @param idNumber 身份证号
     * @param phoneNumber 联系电话
     * @return 法定代表人值对象
     */
    public static LegalRepresentative naturalPerson(String name, String idNumber, String phoneNumber) {
        validate(name, idNumber, phoneNumber);
        return new LegalRepresentative(name, idNumber, phoneNumber, RepresentativeType.NATURAL_PERSON);
    }
    
    /**
     * 法人法定代表人
     * 
     * @param name 法人名称
     * @param registrationNumber 法人注册号
     * @param phoneNumber 联系电话
     * @return 法定代表人值对象
     */
    public static LegalRepresentative legalPerson(String name, String registrationNumber, String phoneNumber) {
        validate(name, registrationNumber, phoneNumber);
        return new LegalRepresentative(name, registrationNumber, phoneNumber, RepresentativeType.LEGAL_PERSON);
    }
    
    /**
     * 验证输入参数
     * 
     * @param name 姓名
     * @param idOrRegNumber 身份证号或注册号
     * @param phoneNumber 电话号码
     */
    private static void validate(String name, String idOrRegNumber, String phoneNumber) {
        if (StringUtils.isBlank(name)) {
            throw new DomainRuleViolationException("法定代表人姓名不能为空");
        }
        
        if (StringUtils.isBlank(idOrRegNumber)) {
            throw new DomainRuleViolationException("法定代表人身份证号/注册号不能为空");
        }
        
        // 电话号码可以为空
    }
    
    /**
     * 法定代表人类型枚举
     */
    public enum RepresentativeType {
        /**
         * 自然人
         */
        NATURAL_PERSON,
        
        /**
         * 法人
         */
        LEGAL_PERSON
    }
    
    /**
     * 检查是否为自然人法定代表人
     * 
     * @return 如果是自然人返回true
     */
    public boolean isNaturalPerson() {
        return this.type == RepresentativeType.NATURAL_PERSON;
    }
    
    /**
     * 检查是否为法人法定代表人
     * 
     * @return 如果是法人返回true
     */
    public boolean isLegalPerson() {
        return this.type == RepresentativeType.LEGAL_PERSON;
    }
} 