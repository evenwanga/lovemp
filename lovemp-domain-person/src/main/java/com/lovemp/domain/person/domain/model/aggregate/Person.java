package com.lovemp.domain.person.domain.model.aggregate;

import com.lovemp.common.domain.AggregateRoot;
import com.lovemp.common.util.Assert;
import com.lovemp.common.util.DateTimeUtils;
import com.lovemp.domain.person.domain.event.PersonCreatedEvent;
import com.lovemp.domain.person.domain.event.PersonStatusChangedEvent;
import com.lovemp.domain.person.domain.event.PersonUpdatedEvent;
import com.lovemp.domain.person.domain.model.valueobject.*;
import lombok.Getter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * 自然人聚合根
 */
@Getter
public class Person extends AggregateRoot<PersonId> {
    
    /**
     * 姓名
     */
    private String name;

    /**
     * 姓名是否生僻字
     */
    private boolean isRareCharacter;

    /**
     * 原姓名
     */
    private Name originalName;

    /**
     * 性别
     */
    private Gender gender;
    
    /**
     * 出生日期
     */
    private LocalDate birthDate;
    
    /**
     * 证件信息列表
     */
    private List<IdentityDocument> identityDocuments;
    
    /**
     * 地址信息列表
     */
    private List<AddressWithTag> addresses;
    
    /**
     * 联系方式列表
     */
    private List<ContactInfoWithTag> contactInfos;
    
    /**
     * 婚姻状况
     */
    private MaritalStatus maritalStatus;
    
    /**
     * 教育经历列表
     */
    private List<Education> educations;
    
    /**
     * 生物特征
     */
    private Biometric biometric;
    
    /**
     * 状态
     */
    private PersonStatus status;
    
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
    protected Person() {
        this.identityDocuments = new ArrayList<>();
        this.addresses = new ArrayList<>();
        this.contactInfos = new ArrayList<>();
        this.educations = new ArrayList<>();
    }
    
    /**
     * 创建自然人
     * 
     * @param id 自然人ID
     * @param name 姓名
     * @param gender 性别
     * @param birthDate 出生日期
     * @param primaryDocument 主要证件信息
     * @param contactInfo 主要联系方式
     * @return 自然人实例
     */
    public static Person create(PersonId id, String name, Gender gender, LocalDate birthDate,
                               IdentityDocument primaryDocument, ContactInfo contactInfo) {
        Assert.notNull(id, "自然人ID不能为空");
        Assert.notEmpty(name, "姓名不能为空");
        Assert.notNull(gender, "性别不能为空");
        Assert.notNull(birthDate, "出生日期不能为空");
        Assert.notNull(primaryDocument, "主要证件信息不能为空");
        Assert.notNull(contactInfo, "联系方式不能为空");
        
        Person person = new Person();
        person.id = id;
        person.name = name;
        person.gender = gender;
        person.birthDate = birthDate;
        person.identityDocuments = new ArrayList<>();
        person.identityDocuments.add(primaryDocument);
        person.addresses = new ArrayList<>();
        person.contactInfos = new ArrayList<>();
        person.contactInfos.add(ContactInfoWithTag.asDefault(contactInfo));
        person.maritalStatus = MaritalStatus.UNKNOWN;
        person.educations = new ArrayList<>();
        person.biometric = Biometric.empty();
        person.status = PersonStatus.ACTIVE;
        person.createDate = DateTimeUtils.getCurrentDate();
        person.lastUpdateDate = DateTimeUtils.getCurrentDate();
        
        // 注册领域事件
        person.registerEvent(new PersonCreatedEvent(id, name));
        
        return person;
    }
    
    /**
     * 更新姓名
     * 
     * @param name 新姓名
     */
    public void updateName(String name) {
        Assert.notEmpty(name, "姓名不能为空");
        this.name = name;
        this.isRareCharacter = false; // 重置生僻字标志
        this.originalName = null; // 清除原姓名
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "name"));
    }
    
    /**
     * 更新含有生僻字的姓名
     * 
     * @param processedName 处理后的姓名（用于系统显示）
     * @param originalName 包含生僻字的原始姓名
     * @param pinyin 姓名拼音（可选）
     */
    public void updateNameWithRareCharacter(String processedName, String originalName, String pinyin) {
        Assert.notEmpty(processedName, "处理后的姓名不能为空");
        Assert.notEmpty(originalName, "原始姓名不能为空");
        
        this.name = processedName;
        this.isRareCharacter = true;
        this.originalName = Name.of(originalName, processedName, pinyin);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "name"));
    }
    
    /**
     * 获取显示用姓名
     * 如果是生僻字，则返回处理后的姓名（即name字段）
     * 如果不是生僻字，则直接返回name字段
     * 
     * @return 显示用姓名
     */
    public String getDisplayName() {
        return name;
    }
    
    /**
     * 获取完整姓名
     * 如果是生僻字，则返回原始姓名
     * 如果不是生僻字，则返回name字段
     * 
     * @return 完整姓名（可能包含生僻字）
     */
    public String getFullName() {
        if (isRareCharacter && originalName != null) {
            return originalName.getOriginalName();
        }
        return name;
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
        
        if (isDefault) {
            // 先将所有联系方式设为非默认
            this.contactInfos.replaceAll(ci -> ci.isDefault() ? ci.unsetAsDefault() : ci);
        }
        
        this.contactInfos.add(ContactInfoWithTag.of(contactInfo, tag, isDefault));
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "contactInfos"));
    }
    
    /**
     * 设置默认联系方式
     * 
     * @param index 联系方式索引
     */
    public void setDefaultContactInfo(int index) {
        if (index < 0 || index >= contactInfos.size()) {
            throw new IllegalArgumentException("联系方式索引无效");
        }
        
        // 先将所有联系方式设为非默认
        for (int i = 0; i < contactInfos.size(); i++) {
            ContactInfoWithTag current = contactInfos.get(i);
            if (i == index) {
                contactInfos.set(i, current.setAsDefault());
            } else if (current.isDefault()) {
                contactInfos.set(i, current.unsetAsDefault());
            }
        }
        
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "contactInfos"));
    }
    
    /**
     * 删除联系方式
     * 
     * @param index 联系方式索引
     */
    public void removeContactInfo(int index) {
        if (index < 0 || index >= contactInfos.size()) {
            throw new IllegalArgumentException("联系方式索引无效");
        }
        
        boolean wasDefault = contactInfos.get(index).isDefault();
        contactInfos.remove(index);
        
        // 如果删除的是默认联系方式，并且还有其他联系方式，将第一个设为默认
        if (wasDefault && !contactInfos.isEmpty()) {
            contactInfos.set(0, contactInfos.get(0).setAsDefault());
        }
        
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "contactInfos"));
    }
    
    /**
     * 获取默认联系方式
     * 
     * @return 默认联系方式，如果没有则返回第一个
     */
    public ContactInfo getDefaultContactInfo() {
        for (ContactInfoWithTag contactInfoWithTag : contactInfos) {
            if (contactInfoWithTag.isDefault()) {
                return contactInfoWithTag.getContactInfo();
            }
        }
        
        // 如果没有默认的，返回第一个
        return contactInfos.isEmpty() ? null : contactInfos.get(0).getContactInfo();
    }
    
    /**
     * 添加地址
     * 
     * @param address 地址
     * @param tag 标签
     * @param isDefault 是否为默认地址
     */
    public void addAddress(Address address, String tag, boolean isDefault) {
        Assert.notNull(address, "地址不能为空");
        
        if (isDefault) {
            // 先将所有地址设为非默认
            this.addresses.replaceAll(addr -> addr.isDefault() ? addr.unsetAsDefault() : addr);
        }
        
        this.addresses.add(AddressWithTag.of(address, tag, isDefault));
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "addresses"));
    }
    
    /**
     * 设置默认地址
     * 
     * @param index 地址索引
     */
    public void setDefaultAddress(int index) {
        if (index < 0 || index >= addresses.size()) {
            throw new IllegalArgumentException("地址索引无效");
        }
        
        // 先将所有地址设为非默认
        for (int i = 0; i < addresses.size(); i++) {
            AddressWithTag current = addresses.get(i);
            if (i == index) {
                addresses.set(i, current.setAsDefault());
            } else if (current.isDefault()) {
                addresses.set(i, current.unsetAsDefault());
            }
        }
        
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "addresses"));
    }
    
    /**
     * 删除地址
     * 
     * @param index 地址索引
     */
    public void removeAddress(int index) {
        if (index < 0 || index >= addresses.size()) {
            throw new IllegalArgumentException("地址索引无效");
        }
        
        boolean wasDefault = addresses.get(index).isDefault();
        addresses.remove(index);
        
        // 如果删除的是默认地址，并且还有其他地址，将第一个设为默认
        if (wasDefault && !addresses.isEmpty()) {
            addresses.set(0, addresses.get(0).setAsDefault());
        }
        
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "addresses"));
    }
    
    /**
     * 获取默认地址
     * 
     * @return 默认地址，如果没有则返回第一个
     */
    public Address getDefaultAddress() {
        for (AddressWithTag addressWithTag : addresses) {
            if (addressWithTag.isDefault()) {
                return addressWithTag.getAddress();
            }
        }
        
        // 如果没有默认的，返回第一个
        return addresses.isEmpty() ? null : addresses.get(0).getAddress();
    }
    
    /**
     * 添加证件信息
     * 
     * @param document 新的证件信息
     */
    public void addIdentityDocument(IdentityDocument document) {
        Assert.notNull(document, "证件信息不能为空");
        
        // 检查证件类型是否已存在，如果存在则替换
        boolean replaced = false;
        for (int i = 0; i < identityDocuments.size(); i++) {
            if (identityDocuments.get(i).getType() == document.getType()) {
                identityDocuments.set(i, document);
                replaced = true;
                break;
            }
        }
        
        if (!replaced) {
            identityDocuments.add(document);
        }
        
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "identityDocuments"));
    }
    
    /**
     * 添加教育经历
     * 
     * @param education 新的教育经历
     */
    public void addEducation(Education education) {
        Assert.notNull(education, "教育经历不能为空");
        educations.add(education);
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "educations"));
    }
    
    /**
     * 更新生物特征
     * 
     * @param biometric 新的生物特征
     */
    public void updateBiometric(Biometric biometric) {
        Assert.notNull(biometric, "生物特征不能为空");
        this.biometric = biometric;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "biometric"));
    }
    
    /**
     * 更新婚姻状况
     * 
     * @param maritalStatus 新的婚姻状况
     */
    public void updateMaritalStatus(MaritalStatus maritalStatus) {
        Assert.notNull(maritalStatus, "婚姻状况不能为空");
        this.maritalStatus = maritalStatus;
        this.lastUpdateDate = DateTimeUtils.getCurrentDate();
        registerEvent(new PersonUpdatedEvent(this.id, "maritalStatus"));
    }
    
    /**
     * 更改自然人状态
     * 
     * @param status 新状态
     */
    public void changeStatus(PersonStatus status) {
        Assert.notNull(status, "状态不能为空");
        if (this.status != status) {
            PersonStatus oldStatus = this.status;
            this.status = status;
            this.lastUpdateDate = DateTimeUtils.getCurrentDate();
            registerEvent(new PersonStatusChangedEvent(this.id, oldStatus, status));
        }
    }
    
    /**
     * 标记为删除状态
     */
    public void markAsDeleted() {
        changeStatus(PersonStatus.DELETED);
    }
    
    /**
     * 获取主要证件信息
     * 
     * @return 主要证件信息（优先返回身份证）
     */
    public IdentityDocument getPrimaryDocument() {
        return identityDocuments.stream()
                .filter(doc -> doc.getType() == IdentityDocumentType.ID_CARD)
                .findFirst()
                .orElse(identityDocuments.isEmpty() ? null : identityDocuments.get(0));
    }
    
    /**
     * 获取最高学历
     * 
     * @return 最高学历
     */
    public Education getHighestEducation() {
        if (educations.isEmpty()) {
            return null;
        }
        
        return educations.stream()
                .reduce((e1, e2) -> e1.isHigherThan(e2) ? e1 : e2)
                .orElse(null);
    }
    
    /**
     * 计算年龄
     * 
     * @return 当前年龄
     */
    public int getAge() {
        if (birthDate == null) {
            return 0;
        }
        return (int) java.time.temporal.ChronoUnit.YEARS.between(birthDate, DateTimeUtils.getCurrentDate());
    }
    
    /**
     * 获取不可变的证件信息列表
     * 
     * @return 证件信息列表的不可变视图
     */
    public List<IdentityDocument> getIdentityDocuments() {
        return Collections.unmodifiableList(identityDocuments);
    }
    
    /**
     * 获取不可变的地址列表
     * 
     * @return 地址列表的不可变视图
     */
    public List<AddressWithTag> getAddresses() {
        return Collections.unmodifiableList(addresses);
    }
    
    /**
     * 获取不可变的联系方式列表
     * 
     * @return 联系方式列表的不可变视图
     */
    public List<ContactInfoWithTag> getContactInfos() {
        return Collections.unmodifiableList(contactInfos);
    }
    
    /**
     * 获取不可变的教育经历列表
     * 
     * @return 教育经历列表的不可变视图
     */
    public List<Education> getEducations() {
        return Collections.unmodifiableList(educations);
    }
}