package com.lovemp.domain.person.domain.model.valueobject;

import com.lovemp.common.domain.ValueObject;
import com.lovemp.common.util.DateTimeUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.Objects;

/**
 * 证件信息值对象
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class IdentityDocument implements ValueObject {
    
    /**
     * 证件类型
     */
    private IdentityDocumentType type;
    
    /**
     * 证件号码
     */
    private String number;
    
    /**
     * 发证机关
     */
    private String issuingAuthority;
    
    /**
     * 有效期开始日期
     */
    private LocalDate validFrom;
    
    /**
     * 有效期结束日期
     */
    private LocalDate validUntil;
    
    /**
     * 证件状态
     */
    private IdentityDocumentStatus status;
    
    /**
     * 身份证正面影像链接地址
     */
    private String frontImageUrl;
    
    /**
     * 身份证反面影像链接地址
     */
    private String backImageUrl;
    
    /**
     * 创建证件信息值对象
     * 
     * @param type 证件类型
     * @param number 证件号码
     * @param issuingAuthority 发证机关
     * @param validFrom 有效期开始日期
     * @param validUntil 有效期结束日期
     * @return 证件信息值对象
     */
    public static IdentityDocument of(IdentityDocumentType type, String number, String issuingAuthority,
                                     LocalDate validFrom, LocalDate validUntil) {
        return new IdentityDocument(type, number, issuingAuthority, validFrom, validUntil, 
                                   IdentityDocumentStatus.VALID, null, null);
    }
    
    /**
     * 创建身份证信息值对象
     * 
     * @param number 身份证号码
     * @param issuingAuthority 发证机关
     * @param validFrom 有效期开始日期
     * @param validUntil 有效期结束日期
     * @return 身份证信息值对象
     */
    public static IdentityDocument ofIdCard(String number, String issuingAuthority,
                                           LocalDate validFrom, LocalDate validUntil) {
        return new IdentityDocument(IdentityDocumentType.ID_CARD, number, issuingAuthority, 
                                   validFrom, validUntil, IdentityDocumentStatus.VALID, null, null);
    }
    
    /**
     * 创建护照信息值对象
     * 
     * @param number 护照号码
     * @param issuingAuthority 发证机关
     * @param validFrom 有效期开始日期
     * @param validUntil 有效期结束日期
     * @return 护照信息值对象
     */
    public static IdentityDocument ofPassport(String number, String issuingAuthority,
                                             LocalDate validFrom, LocalDate validUntil) {
        return new IdentityDocument(IdentityDocumentType.PASSPORT, number, issuingAuthority, 
                                   validFrom, validUntil, IdentityDocumentStatus.VALID, null, null);
    }
    
    /**
     * 创建带有影像的身份证信息值对象
     * 
     * @param number 身份证号码
     * @param issuingAuthority 发证机关
     * @param validFrom 有效期开始日期
     * @param validUntil 有效期结束日期
     * @param frontImageUrl 身份证正面影像链接地址
     * @param backImageUrl 身份证反面影像链接地址
     * @return 身份证信息值对象
     */
    public static IdentityDocument ofIdCardWithImages(String number, String issuingAuthority,
                                                     LocalDate validFrom, LocalDate validUntil,
                                                     String frontImageUrl, String backImageUrl) {
        return new IdentityDocument(IdentityDocumentType.ID_CARD, number, issuingAuthority, 
                                   validFrom, validUntil, IdentityDocumentStatus.VALID, 
                                   frontImageUrl, backImageUrl);
    }
    
    /**
     * 检查证件是否有效
     * 
     * @return 如果证件当前有效则返回true
     */
    public boolean isValid() {
        LocalDate now = DateTimeUtils.getCurrentDate();
        return status == IdentityDocumentStatus.VALID && 
               (validFrom == null || !now.isBefore(validFrom)) && 
               (validUntil == null || !now.isAfter(validUntil));
    }
    
    /**
     * 更新证件状态
     * 
     * @param status 新的证件状态
     * @return 更新后的证件信息对象
     */
    public IdentityDocument withStatus(IdentityDocumentStatus status) {
        return new IdentityDocument(this.type, this.number, this.issuingAuthority, 
                                   this.validFrom, this.validUntil, status,
                                   this.frontImageUrl, this.backImageUrl);
    }
    
    /**
     * 更新证件有效期
     * 
     * @param validUntil 新的有效期结束日期
     * @return 更新后的证件信息对象
     */
    public IdentityDocument withValidUntil(LocalDate validUntil) {
        return new IdentityDocument(this.type, this.number, this.issuingAuthority, 
                                   this.validFrom, validUntil, this.status,
                                   this.frontImageUrl, this.backImageUrl);
    }
    
    /**
     * 更新证件影像链接地址
     * 
     * @param frontImageUrl 身份证正面影像链接地址
     * @param backImageUrl 身份证反面影像链接地址
     * @return 更新后的证件信息对象
     */
    public IdentityDocument withImages(String frontImageUrl, String backImageUrl) {
        return new IdentityDocument(this.type, this.number, this.issuingAuthority, 
                                   this.validFrom, this.validUntil, this.status,
                                   frontImageUrl, backImageUrl);
    }
    
    /**
     * 更新证件正面影像链接地址
     * 
     * @param frontImageUrl 身份证正面影像链接地址
     * @return 更新后的证件信息对象
     */
    public IdentityDocument withFrontImage(String frontImageUrl) {
        return new IdentityDocument(this.type, this.number, this.issuingAuthority, 
                                   this.validFrom, this.validUntil, this.status,
                                   frontImageUrl, this.backImageUrl);
    }
    
    /**
     * 更新证件反面影像链接地址
     * 
     * @param backImageUrl 身份证反面影像链接地址
     * @return 更新后的证件信息对象
     */
    public IdentityDocument withBackImage(String backImageUrl) {
        return new IdentityDocument(this.type, this.number, this.issuingAuthority, 
                                   this.validFrom, this.validUntil, this.status,
                                   this.frontImageUrl, backImageUrl);
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        IdentityDocument that = (IdentityDocument) o;
        return type == that.type &&
               Objects.equals(number, that.number) &&
               Objects.equals(issuingAuthority, that.issuingAuthority) &&
               Objects.equals(validFrom, that.validFrom) &&
               Objects.equals(validUntil, that.validUntil) &&
               status == that.status &&
               Objects.equals(frontImageUrl, that.frontImageUrl) &&
               Objects.equals(backImageUrl, that.backImageUrl);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(type, number, issuingAuthority, validFrom, validUntil, status, frontImageUrl, backImageUrl);
    }
}