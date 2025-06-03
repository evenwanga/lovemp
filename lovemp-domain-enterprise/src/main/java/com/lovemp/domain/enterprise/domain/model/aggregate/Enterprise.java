package com.lovemp.domain.enterprise.domain.model.aggregate;

import com.lovemp.common.domain.AggregateRoot;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.common.util.Assert;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.domain.enterprise.domain.event.EnterpriseCreatedEvent;
import com.lovemp.domain.enterprise.domain.event.EnterpriseStatusChangedEvent;
import com.lovemp.domain.enterprise.domain.event.EnterpriseUpdatedEvent;
import com.lovemp.domain.enterprise.domain.model.valueobject.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 企业聚合根
 */
@Getter
public class Enterprise extends AggregateRoot<EnterpriseId> {
    
    /**
     * 企业名称
     */
    private String name;
    
    /**
     * 统一社会信用代码
     */
    private String unifiedSocialCreditCode;
    
    /**
     * 工商注册号
     */
    private String businessLicenseNumber;
    
    /**
     * 企业类型
     */
    private EnterpriseType enterpriseType;
    
    /**
     * 法定代表人
     */
    private LegalRepresentative legalRepresentative;
    
    /**
     * 注册资本（单位：万元）
     */
    private Money registeredCapital;
    
    /**
     * 实缴资本（单位：万元）
     */
    private Money paidInCapital;
    
    /**
     * 成立日期
     */
    private LocalDate establishDate;
    
    /**
     * 营业期限
     */
    private BusinessTerm businessTerm;
    
    /**
     * 经营范围
     */
    private String businessScope;
    
    /**
     * 注册地址
     */
    private Address registeredAddress;
    
    /**
     * 办公地址列表
     */
    private List<AddressWithTag> officeAddresses;
    
    /**
     * 联系方式列表
     */
    private List<ContactInfoWithTag> contactInfos;
    
    /**
     * 企业状态
     */
    private EnterpriseStatus status;
    
    /**
     * 纳税人识别号
     */
    private String taxpayerIdentificationNumber;
    
    /**
     * 纳税人资质
     */
    private TaxpayerQualification taxpayerQualification;
    
    /**
     * 企业证书列表
     */
    private List<Certificate> certificates;
    
    /**
     * 企业资质列表
     */
    private List<Qualification> qualifications;
    
    /**
     * 企业备案列表
     */
    private List<Filing> filings;
    
    /**
     * 企业资源账号列表
     */
    private List<ResourceAccount> resourceAccounts;
    
    /**
     * 创建日期
     */
    private LocalDate createDate;
    
    /**
     * 最后更新日期
     */
    private LocalDate lastUpdateDate;
    
    /**
     * 保护构造函数，防止直接实例化
     */
    protected Enterprise() {
        this.officeAddresses = new ArrayList<>();
        this.contactInfos = new ArrayList<>();
        this.certificates = new ArrayList<>();
        this.qualifications = new ArrayList<>();
        this.filings = new ArrayList<>();
        this.resourceAccounts = new ArrayList<>();
    }
    
    /**
     * 创建企业
     * 
     * @param id 企业ID
     * @param name 企业名称
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @param businessLicenseNumber 工商注册号
     * @param enterpriseType 企业类型
     * @param legalRepresentative 法定代表人
     * @param registeredCapital 注册资本
     * @param paidInCapital 实缴资本
     * @param establishDate 成立日期
     * @param registeredAddress 注册地址
     * @param contactInfo 联系方式
     * @param taxpayerIdentificationNumber 纳税人识别号
     * @param taxpayerQualification 纳税人资质
     * @return 企业实例
     */
    public static Enterprise create(EnterpriseId id, String name, String unifiedSocialCreditCode,
                                  String businessLicenseNumber, EnterpriseType enterpriseType, 
                                  LegalRepresentative legalRepresentative,
                                  Money registeredCapital, Money paidInCapital, LocalDate establishDate,
                                  Address registeredAddress, ContactInfo contactInfo,
                                  String taxpayerIdentificationNumber, TaxpayerQualification taxpayerQualification) {
        Assert.notNull(id, "企业ID不能为空");
        Assert.notEmpty(name, "企业名称不能为空");
        Assert.notEmpty(unifiedSocialCreditCode, "统一社会信用代码不能为空");
        Assert.notNull(enterpriseType, "企业类型不能为空");
        Assert.notNull(legalRepresentative, "法定代表人不能为空");
        Assert.notNull(registeredCapital, "注册资本不能为空");
        Assert.notNull(establishDate, "成立日期不能为空");
        Assert.notNull(registeredAddress, "注册地址不能为空");
        Assert.notNull(contactInfo, "联系方式不能为空");
        
        // 成立日期不能是未来日期
        Assert.isTrue(!establishDate.isAfter(DateTimeUtils.getCurrentDate()), "成立日期不能是未来日期");
        
        Enterprise enterprise = new Enterprise();
        enterprise.id = id;
        enterprise.name = name;
        enterprise.unifiedSocialCreditCode = unifiedSocialCreditCode;
        enterprise.businessLicenseNumber = businessLicenseNumber;
        enterprise.enterpriseType = enterpriseType;
        enterprise.legalRepresentative = legalRepresentative;
        enterprise.registeredCapital = registeredCapital;
        enterprise.paidInCapital = paidInCapital;
        enterprise.establishDate = establishDate;
        enterprise.registeredAddress = registeredAddress;
        enterprise.officeAddresses = new ArrayList<>();
        enterprise.officeAddresses.add(AddressWithTag.asHeadquarters(registeredAddress));
        enterprise.contactInfos = new ArrayList<>();
        enterprise.contactInfos.add(ContactInfoWithTag.asDefault(contactInfo));
        enterprise.status = EnterpriseStatus.ACTIVE;
        enterprise.taxpayerIdentificationNumber = taxpayerIdentificationNumber;
        enterprise.taxpayerQualification = taxpayerQualification;
        enterprise.certificates = new ArrayList<>();
        enterprise.qualifications = new ArrayList<>();
        enterprise.filings = new ArrayList<>();
        enterprise.resourceAccounts = new ArrayList<>();
        enterprise.createDate = DateTimeUtils.getCurrentDate();
        enterprise.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        // 注册领域事件
        enterprise.registerEvent(new EnterpriseCreatedEvent(id, name));
        
        return enterprise;
    }
    
    /**
     * 创建企业（简化版本，不包含纳税人相关信息）
     * 
     * @param id 企业ID
     * @param name 企业名称
     * @param unifiedSocialCreditCode 统一社会信用代码
     * @param enterpriseType 企业类型
     * @param legalRepresentative 法定代表人
     * @param registeredCapital 注册资本
     * @param establishDate 成立日期
     * @param registeredAddress 注册地址
     * @param contactInfo 联系方式
     * @return 企业实例
     */
    public static Enterprise create(EnterpriseId id, String name, String unifiedSocialCreditCode,
                                  EnterpriseType enterpriseType, LegalRepresentative legalRepresentative,
                                  Money registeredCapital, LocalDate establishDate,
                                  Address registeredAddress, ContactInfo contactInfo) {
        return create(id, name, unifiedSocialCreditCode, null, enterpriseType, 
                    legalRepresentative, registeredCapital, null, establishDate, 
                    registeredAddress, contactInfo, null, null);
    }
    
    /**
     * 设置长期经营期限
     */
    public void setLongTermBusinessTerm() {
        this.businessTerm = BusinessTerm.longTerm();
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "businessTerm"));
    }
    
    /**
     * 设置固定经营期限
     * 
     * @param endDate 终止日期
     */
    public void setFixedBusinessTerm(LocalDate endDate) {
        Assert.notNull(endDate, "终止日期不能为空");
        Assert.isTrue(endDate.isAfter(this.establishDate), "终止日期必须晚于成立日期");
        
        this.businessTerm = BusinessTerm.fixed(endDate);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "businessTerm"));
    }
    
    /**
     * 更新企业名称
     * 
     * @param name 新名称
     */
    public void updateName(String name) {
        Assert.notEmpty(name, "企业名称不能为空");
        this.name = name;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "name"));
    }
    
    /**
     * 更新工商注册号
     * 
     * @param businessLicenseNumber 工商注册号
     */
    public void updateBusinessLicenseNumber(String businessLicenseNumber) {
        this.businessLicenseNumber = businessLicenseNumber;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "businessLicenseNumber"));
    }
    
    /**
     * 更新法定代表人
     * 
     * @param legalRepresentative 新的法定代表人
     */
    public void updateLegalRepresentative(LegalRepresentative legalRepresentative) {
        Assert.notNull(legalRepresentative, "法定代表人不能为空");
        this.legalRepresentative = legalRepresentative;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "legalRepresentative"));
    }
    
    /**
     * 更新注册资本
     * 
     * @param registeredCapital 新的注册资本
     */
    public void updateRegisteredCapital(Money registeredCapital) {
        Assert.notNull(registeredCapital, "注册资本不能为空");
        Assert.isTrue(registeredCapital.getAmount().compareTo(java.math.BigDecimal.ZERO) >= 0, "注册资本不能为负");
        
        this.registeredCapital = registeredCapital;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "registeredCapital"));
    }
    
    /**
     * 更新实缴资本
     * 
     * @param paidInCapital 新的实缴资本
     */
    public void updatePaidInCapital(Money paidInCapital) {
        Assert.notNull(paidInCapital, "实缴资本不能为空");
        Assert.isTrue(paidInCapital.getAmount().compareTo(java.math.BigDecimal.ZERO) >= 0, "实缴资本不能为负");
        
        if (this.registeredCapital != null) {
            Assert.isTrue(paidInCapital.getAmount().compareTo(this.registeredCapital.getAmount()) <= 0, 
                "实缴资本不能大于注册资本");
        }
        
        this.paidInCapital = paidInCapital;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "paidInCapital"));
    }
    
    /**
     * 更新经营范围
     * 
     * @param businessScope 新的经营范围
     */
    public void updateBusinessScope(String businessScope) {
        Assert.notEmpty(businessScope, "经营范围不能为空");
        this.businessScope = businessScope;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "businessScope"));
    }
    
    /**
     * 更新注册地址
     * 
     * @param registeredAddress 新的注册地址
     */
    public void updateRegisteredAddress(Address registeredAddress) {
        Assert.notNull(registeredAddress, "注册地址不能为空");
        this.registeredAddress = registeredAddress;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "registeredAddress"));
    }
    
    /**
     * 更新纳税人识别号
     * 
     * @param taxpayerIdentificationNumber 纳税人识别号
     */
    public void updateTaxpayerIdentificationNumber(String taxpayerIdentificationNumber) {
        this.taxpayerIdentificationNumber = taxpayerIdentificationNumber;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "taxpayerIdentificationNumber"));
    }
    
    /**
     * 更新纳税人资质
     * 
     * @param taxpayerQualification 纳税人资质
     */
    public void updateTaxpayerQualification(TaxpayerQualification taxpayerQualification) {
        this.taxpayerQualification = taxpayerQualification;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "taxpayerQualification"));
    }
    
    /**
     * 添加办公地址
     * 
     * @param address 地址
     * @param tag 标签
     * @param isHeadquarters 是否为总部
     */
    public void addOfficeAddress(Address address, String tag, boolean isHeadquarters) {
        Assert.notNull(address, "地址不能为空");
        Assert.notEmpty(tag, "标签不能为空");
        
        AddressWithTag newAddress = new AddressWithTag(address, tag, isHeadquarters);
        
        if (isHeadquarters) {
            // 如果新地址是总部，则将现有总部改为非总部
            this.officeAddresses.stream()
                    .filter(AddressWithTag::isHeadquarters)
                    .forEach(addr -> addr.setHeadquarters(false));
        }
        
        this.officeAddresses.add(newAddress);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "officeAddresses"));
    }
    
    /**
     * 设置办公地址为总部
     * 
     * @param index 地址索引
     */
    public void setHeadquartersAddress(int index) {
        Assert.isTrue(index >= 0 && index < this.officeAddresses.size(), "无效的地址索引");
        
        // 将现有总部改为非总部
        this.officeAddresses.stream()
                .filter(AddressWithTag::isHeadquarters)
                .forEach(addr -> addr.setHeadquarters(false));
        
        // 设置新的总部
        this.officeAddresses.get(index).setHeadquarters(true);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "officeAddresses"));
    }
    
    /**
     * 删除办公地址
     * 
     * @param index 地址索引
     */
    public void removeOfficeAddress(int index) {
        Assert.isTrue(index >= 0 && index < this.officeAddresses.size(), "无效的地址索引");
        
        // 不允许删除总部地址
        Assert.isFalse(this.officeAddresses.get(index).isHeadquarters(), "不能删除总部地址");
        
        this.officeAddresses.remove(index);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "officeAddresses"));
    }
    
    /**
     * 获取总部地址
     * 
     * @return 总部地址
     */
    public Address getHeadquartersAddress() {
        return this.officeAddresses.stream()
                .filter(AddressWithTag::isHeadquarters)
                .findFirst()
                .map(AddressWithTag::getAddress)
                .orElse(this.registeredAddress); // 如果没有指定总部，返回注册地址
    }
    
    /**
     * 添加联系方式
     * 
     * @param contactInfo 联系方式
     * @param tag 标签
     * @param isDefault 是否为默认联系方式
     */
    public void addContactInfo(ContactInfo contactInfo, String tag, boolean isDefault) {
        Assert.notNull(contactInfo, "联系方式不能为空");
        Assert.notEmpty(tag, "标签不能为空");
        
        ContactInfoWithTag newContactInfo = new ContactInfoWithTag(contactInfo, tag, isDefault);
        
        if (isDefault) {
            // 如果新联系方式是默认，则将现有默认联系方式改为非默认
            this.contactInfos.stream()
                    .filter(ContactInfoWithTag::isDefault)
                    .forEach(ci -> ci.setDefault(false));
        }
        
        this.contactInfos.add(newContactInfo);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "contactInfos"));
    }
    
    /**
     * 设置默认联系方式
     * 
     * @param index 联系方式索引
     */
    public void setDefaultContactInfo(int index) {
        Assert.isTrue(index >= 0 && index < this.contactInfos.size(), "无效的联系方式索引");
        
        // 将现有默认联系方式改为非默认
        this.contactInfos.stream()
                .filter(ContactInfoWithTag::isDefault)
                .forEach(ci -> ci.setDefault(false));
        
        // 设置新的默认联系方式
        this.contactInfos.get(index).setDefault(true);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "contactInfos"));
    }
    
    /**
     * 删除联系方式
     * 
     * @param index 联系方式索引
     */
    public void removeContactInfo(int index) {
        Assert.isTrue(index >= 0 && index < this.contactInfos.size(), "无效的联系方式索引");
        
        // 不允许删除唯一的联系方式
        Assert.isTrue(this.contactInfos.size() > 1, "不能删除唯一的联系方式");
        
        // 如果删除的是默认联系方式，需要设置新的默认
        boolean needNewDefault = this.contactInfos.get(index).isDefault();
        this.contactInfos.remove(index);
        
        if (needNewDefault && !this.contactInfos.isEmpty()) {
            this.contactInfos.get(0).setDefault(true);
        }
        
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "contactInfos"));
    }
    
    /**
     * 获取默认联系方式
     * 
     * @return 默认联系方式
     */
    public ContactInfo getDefaultContactInfo() {
        return this.contactInfos.stream()
                .filter(ContactInfoWithTag::isDefault)
                .findFirst()
                .map(ContactInfoWithTag::getContactInfo)
                .orElseGet(() -> this.contactInfos.isEmpty() ? null : this.contactInfos.get(0).getContactInfo());
    }
    
    /**
     * 添加企业证书
     * 
     * @param certificate 企业证书
     */
    public void addCertificate(Certificate certificate) {
        Assert.notNull(certificate, "证书不能为空");
        
        this.certificates.add(certificate);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "certificates"));
    }
    
    /**
     * 删除企业证书
     * 
     * @param certificateId 证书ID
     */
    public void removeCertificate(String certificateId) {
        Assert.notEmpty(certificateId, "证书ID不能为空");
        
        boolean removed = this.certificates.removeIf(cert -> cert.getId().equals(certificateId));
        
        if (removed) {
            this.lastUpdateDate = DateTimeUtils.getCurrentDate();
            registerEvent(new EnterpriseUpdatedEvent(this.id, "certificates"));
        }
    }
    
    /**
     * 获取企业证书
     * 
     * @param certificateId 证书ID
     * @return 证书对象（如果存在）
     */
    public Optional<Certificate> getCertificate(String certificateId) {
        return this.certificates.stream()
                .filter(cert -> cert.getId().equals(certificateId))
                .findFirst();
    }
    
    /**
     * 获取指定类型的证书列表
     * 
     * @param type 证书类型
     * @return 指定类型的证书列表
     */
    public List<Certificate> getCertificatesByType(CertificateType type) {
        return this.certificates.stream()
                .filter(cert -> cert.getType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有有效证书
     * 
     * @return 有效证书列表
     */
    public List<Certificate> getValidCertificates() {
        return this.certificates.stream()
                .filter(Certificate::isValid)
                .collect(Collectors.toList());
    }
    
    /**
     * 添加企业资质
     * 
     * @param qualification 企业资质
     */
    public void addQualification(Qualification qualification) {
        Assert.notNull(qualification, "资质不能为空");
        
        this.qualifications.add(qualification);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "qualifications"));
    }
    
    /**
     * 删除企业资质
     * 
     * @param qualificationId 资质ID
     */
    public void removeQualification(String qualificationId) {
        Assert.notEmpty(qualificationId, "资质ID不能为空");
        
        boolean removed = this.qualifications.removeIf(qual -> qual.getId().equals(qualificationId));
        
        if (removed) {
            this.lastUpdateDate = DateTimeUtils.getCurrentDate();
            registerEvent(new EnterpriseUpdatedEvent(this.id, "qualifications"));
        }
    }
    
    /**
     * 获取企业资质
     * 
     * @param qualificationId 资质ID
     * @return 资质对象（如果存在）
     */
    public Optional<Qualification> getQualification(String qualificationId) {
        return this.qualifications.stream()
                .filter(qual -> qual.getId().equals(qualificationId))
                .findFirst();
    }
    
    /**
     * 获取指定类型的资质列表
     * 
     * @param type 资质类型
     * @return 指定类型的资质列表
     */
    public List<Qualification> getQualificationsByType(QualificationType type) {
        return this.qualifications.stream()
                .filter(qual -> qual.getType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有有效资质
     * 
     * @return 有效资质列表
     */
    public List<Qualification> getValidQualifications() {
        return this.qualifications.stream()
                .filter(Qualification::isValid)
                .collect(Collectors.toList());
    }
    
    /**
     * 添加企业备案
     * 
     * @param filing 企业备案
     */
    public void addFiling(Filing filing) {
        Assert.notNull(filing, "备案不能为空");
        
        this.filings.add(filing);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "filings"));
    }
    
    /**
     * 删除企业备案
     * 
     * @param filingId 备案ID
     */
    public void removeFiling(String filingId) {
        Assert.notEmpty(filingId, "备案ID不能为空");
        
        boolean removed = this.filings.removeIf(filing -> filing.getId().equals(filingId));
        
        if (removed) {
            this.lastUpdateDate = DateTimeUtils.getCurrentDate();
            registerEvent(new EnterpriseUpdatedEvent(this.id, "filings"));
        }
    }
    
    /**
     * 获取企业备案
     * 
     * @param filingId 备案ID
     * @return 备案对象（如果存在）
     */
    public Optional<Filing> getFiling(String filingId) {
        return this.filings.stream()
                .filter(filing -> filing.getId().equals(filingId))
                .findFirst();
    }
    
    /**
     * 获取指定类型的备案列表
     * 
     * @param type 备案类型
     * @return 指定类型的备案列表
     */
    public List<Filing> getFilingsByType(FilingType type) {
        return this.filings.stream()
                .filter(filing -> filing.getType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有有效备案
     * 
     * @return 有效备案列表
     */
    public List<Filing> getValidFilings() {
        return this.filings.stream()
                .filter(Filing::isValid)
                .collect(Collectors.toList());
    }
    
    /**
     * 添加企业资源账号
     * 
     * @param resourceAccount 企业资源账号
     */
    public void addResourceAccount(ResourceAccount resourceAccount) {
        Assert.notNull(resourceAccount, "资源账号不能为空");
        
        this.resourceAccounts.add(resourceAccount);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new EnterpriseUpdatedEvent(this.id, "resourceAccounts"));
    }
    
    /**
     * 删除企业资源账号
     * 
     * @param resourceAccountId 资源账号ID
     */
    public void removeResourceAccount(String resourceAccountId) {
        Assert.notEmpty(resourceAccountId, "资源账号ID不能为空");
        
        boolean removed = this.resourceAccounts.removeIf(account -> account.getId().equals(resourceAccountId));
        
        if (removed) {
            this.lastUpdateDate = DateTimeUtils.getCurrentDate();
            registerEvent(new EnterpriseUpdatedEvent(this.id, "resourceAccounts"));
        }
    }
    
    /**
     * 获取企业资源账号
     * 
     * @param resourceAccountId 资源账号ID
     * @return 资源账号对象（如果存在）
     */
    public Optional<ResourceAccount> getResourceAccount(String resourceAccountId) {
        return this.resourceAccounts.stream()
                .filter(account -> account.getId().equals(resourceAccountId))
                .findFirst();
    }
    
    /**
     * 获取指定类型的资源账号列表
     * 
     * @param type 资源账号类型
     * @return 指定类型的资源账号列表
     */
    public List<ResourceAccount> getResourceAccountsByType(ResourceAccountType type) {
        return this.resourceAccounts.stream()
                .filter(account -> account.getType() == type)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有企业级账号（非品牌级账号）
     * 
     * @return 企业级账号列表
     */
    public List<ResourceAccount> getEnterpriseLevelAccounts() {
        return this.resourceAccounts.stream()
                .filter(ResourceAccount::isEnterpriseLevel)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取所有品牌级账号
     * 
     * @return 品牌级账号列表
     */
    public List<ResourceAccount> getBrandLevelAccounts() {
        return this.resourceAccounts.stream()
                .filter(ResourceAccount::isBrandLevel)
                .collect(Collectors.toList());
    }
    
    /**
     * 获取指定品牌的资源账号列表
     * 
     * @param brandId 品牌ID
     * @return 指定品牌的资源账号列表
     */
    public List<ResourceAccount> getResourceAccountsByBrand(String brandId) {
        Assert.notEmpty(brandId, "品牌ID不能为空");
        
        return this.resourceAccounts.stream()
                .filter(account -> brandId.equals(account.getBrandId()))
                .collect(Collectors.toList());
    }
    
    /**
     * 变更企业状态
     * 
     * @param status 新状态
     */
    public void changeStatus(EnterpriseStatus status) {
        Assert.notNull(status, "状态不能为空");
        
        if (this.status == status) {
            return; // 状态未变，不做处理
        }
        
        // 终态不能再变更状态
        if (this.status == EnterpriseStatus.CANCELLED || this.status == EnterpriseStatus.REVOKED) {
            throw new DomainRuleViolationException("企业状态已是终态，不能再变更");
        }
        
        EnterpriseStatus oldStatus = this.status;
        this.status = status;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        // 注册状态变更事件
        registerEvent(new EnterpriseStatusChangedEvent(this.id, oldStatus, status));
    }
    
    /**
     * 标记为已注销
     */
    public void markAsCancelled() {
        changeStatus(EnterpriseStatus.CANCELLED);
    }
    
    /**
     * 标记为已吊销
     */
    public void markAsRevoked() {
        changeStatus(EnterpriseStatus.REVOKED);
    }
    
    /**
     * 获取不可变的办公地址列表
     */
    public List<AddressWithTag> getOfficeAddresses() {
        return Collections.unmodifiableList(officeAddresses);
    }
    
    /**
     * 获取不可变的联系方式列表
     */
    public List<ContactInfoWithTag> getContactInfos() {
        return Collections.unmodifiableList(contactInfos);
    }
    
    /**
     * 获取不可变的证书列表
     */
    public List<Certificate> getCertificates() {
        return Collections.unmodifiableList(certificates);
    }
    
    /**
     * 获取不可变的资质列表
     */
    public List<Qualification> getQualifications() {
        return Collections.unmodifiableList(qualifications);
    }
    
    /**
     * 获取不可变的备案列表
     */
    public List<Filing> getFilings() {
        return Collections.unmodifiableList(filings);
    }
    
    /**
     * 获取不可变的资源账号列表
     */
    public List<ResourceAccount> getResourceAccounts() {
        return Collections.unmodifiableList(resourceAccounts);
    }
} 