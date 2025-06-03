package com.lovemp.domain.person.domain;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.domain.person.domain.event.PersonCreatedEvent;
import com.lovemp.domain.person.domain.event.PersonStatusChangedEvent;
import com.lovemp.domain.person.domain.event.PersonUpdatedEvent;
import com.lovemp.domain.person.domain.model.aggregate.Person;
import com.lovemp.domain.person.domain.model.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("自然人聚合根测试")
class PersonTest {

    private Person person;
    private PersonId personId;
    private String name;
    private Gender gender;
    private LocalDate birthDate;
    private IdentityDocument identityDocument;
    private ContactInfo contactInfo;

    @BeforeEach
    void setUp() {
        personId = PersonId.of(UUID.randomUUID().toString());
        name = "张三";
        gender = Gender.MALE;
        birthDate = LocalDate.of(1990, 1, 1);
        identityDocument = IdentityDocument.ofIdCard(
                "110101199001011234", 
                "北京市公安局",
                LocalDate.now(),
                LocalDate.now().plusYears(20)
        );
        contactInfo = ContactInfo.basic("13800138000", null);
        
        person = Person.create(personId, name, gender, birthDate, identityDocument, contactInfo);
    }

    @Test
    @DisplayName("测试创建自然人")
    void testCreate() {
        // 验证基本属性
        assertEquals(personId, person.getId());
        assertEquals(name, person.getName());
        assertEquals(gender, person.getGender());
        assertEquals(birthDate, person.getBirthDate());
        
        // 验证默认值
        assertFalse(person.isRareCharacter());
        assertNull(person.getOriginalName());
        assertEquals(MaritalStatus.UNKNOWN, person.getMaritalStatus());
        assertEquals(PersonStatus.ACTIVE, person.getStatus());
        
        // 验证集合
        assertEquals(1, person.getIdentityDocuments().size());
        assertEquals(0, person.getAddresses().size());
        assertEquals(1, person.getContactInfos().size());
        assertEquals(0, person.getEducations().size());
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertFalse(domainEvents.isEmpty());
        assertTrue(domainEvents.get(0) instanceof PersonCreatedEvent);
        PersonCreatedEvent event = (PersonCreatedEvent) domainEvents.get(0);
        assertEquals(personId, event.getPersonId());
        assertEquals(name, event.getName());
    }
    
    @Test
    @DisplayName("测试添加地址信息")
    void testAddAddress() {
        // 准备测试数据
        Address address = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        
        // 执行测试
        person.addAddress(address, "家庭地址", true);
        
        // 验证结果
        assertEquals(1, person.getAddresses().size());
        AddressWithTag addressWithTag = person.getAddresses().get(0);
        assertEquals(address, addressWithTag.getAddress());
        assertEquals("家庭地址", addressWithTag.getTag());
        assertTrue(addressWithTag.isDefault());
        assertEquals(address, person.getDefaultAddress());
        
        // 添加第二个地址
        Address workAddress = Address.ofChina(
                "北京市", 
                "北京市", 
                "朝阳区", 
                "朝阳门街道", 
                "朝阳门外大街甲6号", 
                "100020"
        );
        person.addAddress(workAddress, "工作地址", false);
        
        // 验证结果
        assertEquals(2, person.getAddresses().size());
        assertEquals(address, person.getDefaultAddress()); // 第一个地址仍然是默认地址
        
        // 添加第三个地址并设为默认
        Address tempAddress = Address.ofChina(
                "上海市", 
                "上海市", 
                "浦东新区", 
                "张江镇", 
                "张江高科技园区", 
                "201203"
        );
        person.addAddress(tempAddress, "临时地址", true);
        
        // 验证结果
        assertEquals(3, person.getAddresses().size());
        assertEquals(tempAddress, person.getDefaultAddress()); // 第三个地址现在是默认地址
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(4, domainEvents.size()); // 1个创建事件 + 3个地址更新事件
        assertTrue(domainEvents.get(1) instanceof PersonUpdatedEvent);
        assertTrue(domainEvents.get(2) instanceof PersonUpdatedEvent);
        assertTrue(domainEvents.get(3) instanceof PersonUpdatedEvent);
    }
    
    @Test
    @DisplayName("测试管理地址标签")
    void testManageAddressTags() {
        // 准备测试数据
        Person person = createTestPerson();
        
        // 添加三个地址
        Address homeAddress = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        person.addAddress(homeAddress, "家庭地址", true);
        
        Address workAddress = Address.ofChina(
                "北京市", 
                "北京市", 
                "朝阳区", 
                "朝阳门街道", 
                "朝阳门外大街甲6号", 
                "100020"
        );
        person.addAddress(workAddress, "工作地址", false);
        
        Address parentAddress = Address.ofChina(
                "河北省", 
                "石家庄市", 
                "桥西区", 
                "中山路街道", 
                "中山路100号", 
                "050000"
        );
        person.addAddress(parentAddress, "父母家", false);
        
        // 验证初始状态
        assertEquals(3, person.getAddresses().size());
        assertEquals(homeAddress, person.getDefaultAddress());
        
        // 测试设置默认地址
        person.setDefaultAddress(1); // 设置工作地址为默认地址
        
        // 验证结果
        assertEquals(workAddress, person.getDefaultAddress());
        assertFalse(person.getAddresses().get(0).isDefault());
        assertTrue(person.getAddresses().get(1).isDefault());
        assertFalse(person.getAddresses().get(2).isDefault());
        
        // 测试删除地址
        person.removeAddress(0); // 删除家庭地址
        
        // 验证结果
        assertEquals(2, person.getAddresses().size());
        assertEquals(workAddress, person.getDefaultAddress());
        
        // 删除默认地址
        person.removeAddress(0); // 现在删除工作地址（已成为列表中的第一个）
        
        // 验证结果
        assertEquals(1, person.getAddresses().size());
        assertEquals(parentAddress, person.getDefaultAddress()); // 父母家应该成为默认地址
        assertTrue(person.getAddresses().get(0).isDefault()); // 应该设置为默认
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(7, domainEvents.size()); // 1个创建事件 + 3个地址添加 + 1个设置默认 + 2个删除地址
    }
    
    @Test
    @DisplayName("测试添加联系方式")
    void testAddContactInfo() {
        // 准备测试数据
        Person person = createTestPerson();
        ContactInfo email = ContactInfo.of(null, "zhangsan@example.com", null, null, null, null);
        
        // 执行测试 - 添加新的联系方式(默认联系方式已经存在)
        person.addContactInfo(email, "电子邮件", false);
        
        // 验证结果
        assertEquals(2, person.getContactInfos().size());
        
        // 获取默认联系方式应该是手机号
        ContactInfo defaultContact = person.getDefaultContactInfo();
        assertNotNull(defaultContact.getMobile());
        assertEquals("13800138000", defaultContact.getMobile());
        
        // 添加另一个联系方式并设为默认
        ContactInfo workPhone = ContactInfo.of(null, null, "010-12345678", null, null, null);
        person.addContactInfo(workPhone, "工作电话", true);
        
        // 验证结果
        assertEquals(3, person.getContactInfos().size());
        assertEquals(workPhone, person.getDefaultContactInfo());
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(3, domainEvents.size()); // 1个创建事件 + 2个联系方式更新事件
    }
    
    @Test
    @DisplayName("测试管理联系方式标签")
    void testManageContactInfoTags() {
        // 准备测试数据
        Person person = createTestPerson();
        
        // 添加多个联系方式
        ContactInfo email = ContactInfo.of(null, "zhangsan@example.com", null, null, null, null);
        person.addContactInfo(email, "个人邮箱", false);
        
        ContactInfo workPhone = ContactInfo.of(null, null, "010-12345678", null, null, null);
        person.addContactInfo(workPhone, "工作电话", false);
        
        ContactInfo emergencyContact = ContactInfo.of(null, null, null, null, "李四", "13900139000");
        person.addContactInfo(emergencyContact, "紧急联系人", false);
        
        // 验证初始状态
        assertEquals(4, person.getContactInfos().size());
        ContactInfo defaultContact = person.getDefaultContactInfo();
        assertNotNull(defaultContact.getMobile());
        assertEquals("13800138000", defaultContact.getMobile()); // 初始手机号应为默认联系方式
        
        // 测试设置默认联系方式
        person.setDefaultContactInfo(2); // 设置工作电话为默认联系方式
        
        // 验证结果
        assertEquals(workPhone, person.getDefaultContactInfo());
        assertFalse(person.getContactInfos().get(0).isDefault()); // 原手机号不再是默认
        assertFalse(person.getContactInfos().get(1).isDefault()); // 邮箱不是默认
        assertTrue(person.getContactInfos().get(2).isDefault()); // 工作电话是默认
        assertFalse(person.getContactInfos().get(3).isDefault()); // 紧急联系人不是默认
        
        // 测试删除联系方式
        person.removeContactInfo(1); // 删除个人邮箱
        
        // 验证结果
        assertEquals(3, person.getContactInfos().size());
        assertEquals(workPhone, person.getDefaultContactInfo()); // 工作电话仍然是默认联系方式
        
        // 删除默认联系方式
        person.removeContactInfo(1); // 现在删除工作电话（已成为列表中的第二个）
        
        // 验证结果
        assertEquals(2, person.getContactInfos().size());
        // 首个联系方式应该自动成为默认联系方式
        assertTrue(person.getContactInfos().get(0).isDefault());
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(7, domainEvents.size()); // 1个创建事件 + 3个添加联系方式 + 1个设置默认 + 2个删除联系方式
    }
    
    @Test
    @DisplayName("测试添加证件信息")
    void testAddIdentityDocument() {
        // 准备测试数据
        Person person = createTestPerson();
        
        // 创建护照证件
        IdentityDocument passport = IdentityDocument.ofPassport(
                "P12345678",
                "北京市出入境管理局",
                LocalDate.now(),
                LocalDate.now().plusYears(10)
        );
        
        // 执行测试
        person.addIdentityDocument(passport);
        
        // 验证结果
        assertEquals(2, person.getIdentityDocuments().size());
        
        // 获取主要证件应该仍然是身份证
        IdentityDocument primaryDoc = person.getPrimaryDocument();
        assertEquals(IdentityDocumentType.ID_CARD, primaryDoc.getType());
        
        // 创建新的身份证替换原来的，并带有影像链接
        IdentityDocument newIdCard = IdentityDocument.ofIdCardWithImages(
                "110101199001011234", 
                "北京市公安局海淀分局",
                LocalDate.now(),
                LocalDate.now().plusYears(20),
                "身份证正面图片链接",
                "身份证背面图片链接"
        );
        
        // 执行测试
        person.addIdentityDocument(newIdCard);
        
        // 验证结果 - 数量应该仍然是2，因为新身份证替换了老身份证
        assertEquals(2, person.getIdentityDocuments().size());
        primaryDoc = person.getPrimaryDocument();
        assertEquals("北京市公安局海淀分局", primaryDoc.getIssuingAuthority());
        assertNotNull(primaryDoc.getFrontImageUrl());
        assertNotNull(primaryDoc.getBackImageUrl());
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(3, domainEvents.size()); // 1个创建事件 + 2个证件更新事件
    }
    
    @Test
    @DisplayName("测试添加生物特征")
    void testUpdateBiometric() {
        // 准备测试数据
        Person person = createTestPerson();
        
        // 创建指纹特征列表和虹膜特征列表
        List<String> fingerprints = List.of("指纹数据1");
        List<String> irises = List.of("虹膜数据");
        
        // 创建生物特征
        Biometric biometric = Biometric.of(
                fingerprints,
                "人脸识别数据",
                irises,
                null
        );
        
        // 执行测试
        person.updateBiometric(biometric);
        
        // 验证结果
        assertNotNull(person.getBiometric());
        assertEquals("指纹数据1", person.getBiometric().getFingerprints().get(0));
        assertEquals("人脸识别数据", person.getBiometric().getFaceFeature());
        assertEquals("虹膜数据", person.getBiometric().getIrises().get(0));
        
        // 添加更多指纹数据
        Biometric updatedBiometric = person.getBiometric().addFingerprint("指纹数据2");
        person.updateBiometric(updatedBiometric);
        
        // 验证结果
        assertEquals(2, person.getBiometric().getFingerprints().size());
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(3, domainEvents.size()); // 1个创建事件 + 2个生物特征更新事件
    }
    
    @Test
    @DisplayName("测试更改自然人状态")
    void testChangeStatus() {
        // 准备测试数据
        Person person = createTestPerson();
        
        // 执行测试
        person.changeStatus(PersonStatus.DISABLED);
        
        // 验证结果
        assertEquals(PersonStatus.DISABLED, person.getStatus());
        
        // 检查领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(2, domainEvents.size());
        assertTrue(domainEvents.get(1) instanceof PersonStatusChangedEvent);
        
        PersonStatusChangedEvent event = (PersonStatusChangedEvent) domainEvents.get(1);
        assertEquals(PersonStatus.ACTIVE, event.getOldStatus());
        assertEquals(PersonStatus.DISABLED, event.getNewStatus());
        
        // 测试标记为删除
        person.markAsDeleted();
        assertEquals(PersonStatus.DELETED, person.getStatus());
    }
    
    @Test
    @DisplayName("测试更新姓名")
    void testUpdateName() {
        // 准备测试数据
        Person person = createTestPerson();
        String originalName = person.getName();
        
        // 执行测试
        person.updateName("李四");
        
        // 验证结果
        assertEquals("李四", person.getName());
        assertNotEquals(originalName, person.getName());
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(2, domainEvents.size()); // 1个创建事件 + 1个更新事件
        assertTrue(domainEvents.get(1) instanceof PersonUpdatedEvent);
        
        PersonUpdatedEvent event = (PersonUpdatedEvent) domainEvents.get(1);
        assertEquals(person.getId(), event.getPersonId());
        assertEquals("name", event.getUpdatedFields());
    }
    
    @Test
    @DisplayName("测试添加教育经历")
    void testAddEducation() {
        // 准备测试数据
        Person person = createTestPerson();
        Education education = Education.of(
                "清华大学",
                "计算机科学与技术",
                EducationLevel.UNDERGRADUATE,
                LocalDate.of(2010, 9, 1),
                LocalDate.of(2014, 7, 1),
                true,
                "学士学位"
        );
        
        // 执行测试
        person.addEducation(education);
        
        // 验证结果
        assertEquals(1, person.getEducations().size());
        assertEquals(education, person.getEducations().get(0));
        assertEquals(education, person.getHighestEducation());
        
        // 添加另一个更高学历
        Education masterDegree = Education.of(
                "北京大学",
                "人工智能",
                EducationLevel.MASTER,
                LocalDate.of(2014, 9, 1),
                LocalDate.of(2017, 7, 1),
                true,
                "硕士学位"
        );
        person.addEducation(masterDegree);
        
        // 验证结果
        assertEquals(2, person.getEducations().size());
        assertEquals(masterDegree, person.getHighestEducation()); // 硕士学历更高
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(3, domainEvents.size()); // 1个创建事件 + 2个教育更新事件
        assertTrue(domainEvents.get(1) instanceof PersonUpdatedEvent);
        assertTrue(domainEvents.get(2) instanceof PersonUpdatedEvent);
        
        PersonUpdatedEvent event = (PersonUpdatedEvent) domainEvents.get(2);
        assertEquals("educations", event.getUpdatedFields());
    }
    
    @Test
    @DisplayName("测试更新婚姻状况")
    void testUpdateMaritalStatus() {
        // 准备测试数据
        Person person = createTestPerson();
        assertEquals(MaritalStatus.UNKNOWN, person.getMaritalStatus());
        
        // 执行测试
        person.updateMaritalStatus(MaritalStatus.MARRIED);
        
        // 验证结果
        assertEquals(MaritalStatus.MARRIED, person.getMaritalStatus());
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(2, domainEvents.size()); // 1个创建事件 + 1个婚姻状况更新事件
        assertTrue(domainEvents.get(1) instanceof PersonUpdatedEvent);
        
        PersonUpdatedEvent event = (PersonUpdatedEvent) domainEvents.get(1);
        assertEquals("maritalStatus", event.getUpdatedFields());
    }
    
    @Test
    @DisplayName("测试计算年龄")
    void testGetAge() {
        // 创建出生日期在30年前的人
        PersonId personId = PersonId.of(UUID.randomUUID().toString());
        String name = "张三";
        Gender gender = Gender.MALE;
        LocalDate birthDate = LocalDate.now().minusYears(30);
        
        IdentityDocument idCard = IdentityDocument.ofIdCard(
                "110101199001011234", 
                "北京市公安局",
                LocalDate.now(),
                LocalDate.now().plusYears(20)
        );
        
        ContactInfo contactInfo = ContactInfo.basic("13800138000", null);
        
        Person person = Person.create(personId, name, gender, birthDate, idCard, contactInfo);
        
        // 验证年龄
        assertEquals(30, person.getAge());
    }
    
    @Test
    @DisplayName("测试集合的不可变性")
    void testCollectionImmutability() {
        // 准备测试数据
        Person person = createTestPerson();
        
        // 添加一些数据
        Address address = Address.ofChina(
                "北京市", 
                "北京市", 
                "海淀区", 
                "清华园街道", 
                "清华园路1号", 
                "100084"
        );
        person.addAddress(address, "家庭地址", true);
        
        Education education = Education.of(
                "清华大学",
                "计算机科学与技术",
                EducationLevel.UNDERGRADUATE,
                LocalDate.of(2010, 9, 1),
                LocalDate.of(2014, 7, 1),
                true,
                "学士学位"
        );
        person.addEducation(education);
        
        // 测试获取的集合是不可变的
        List<AddressWithTag> addresses = person.getAddresses();
        List<Education> educations = person.getEducations();
        List<IdentityDocument> documents = person.getIdentityDocuments();
        List<ContactInfoWithTag> contacts = person.getContactInfos();
        
        // 尝试修改地址集合
        assertThrows(UnsupportedOperationException.class, () -> {
            addresses.add(null);
        });
        
        // 尝试修改教育经历集合
        assertThrows(UnsupportedOperationException.class, () -> {
            educations.add(null);
        });
        
        // 尝试修改证件集合
        assertThrows(UnsupportedOperationException.class, () -> {
            documents.add(null);
        });
        
        // 尝试修改联系方式集合
        assertThrows(UnsupportedOperationException.class, () -> {
            contacts.add(null);
        });
    }
    
    @Test
    @DisplayName("测试生僻字姓名相关方法")
    void testRareCharacterName() {
        // 准备含生僻字的姓名数据
        String processedName = "李四";
        String originalName = "李𠄀";  // 使用生僻字
        String pinyin = "li si";
        
        // 更新生僻字姓名
        person.updateNameWithRareCharacter(processedName, originalName, pinyin);
        
        // 验证基本信息
        assertEquals(processedName, person.getName());
        assertTrue(person.isRareCharacter());
        assertNotNull(person.getOriginalName());
        assertEquals(originalName, person.getOriginalName().getOriginalName());
        assertEquals(processedName, person.getOriginalName().getProcessedName());
        assertEquals(pinyin, person.getOriginalName().getPinyin());
        
        // 验证显示名称和完整名称
        assertEquals(processedName, person.getDisplayName());
        assertEquals(originalName, person.getFullName());
        
        // 验证领域事件
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertTrue(domainEvents.stream().anyMatch(e -> e instanceof PersonUpdatedEvent));
    }

    @Test
    @DisplayName("测试日期相关方法")
    void testDateRelatedMethods() {
        // 验证创建日期和更新日期不为空
        assertNotNull(person.getCreateDate());
        assertNotNull(person.getLastUpdateDate());
        
        // 清除已有的领域事件
        person.clearEvents();
        
        // 执行会触发更新的操作
        person.updateName("新名字");
        
        // 验证领域事件是否被正确生成
        List<DomainEvent> domainEvents = person.getDomainEvents();
        assertEquals(1, domainEvents.size());
        assertTrue(domainEvents.get(0) instanceof PersonUpdatedEvent);
        PersonUpdatedEvent event = (PersonUpdatedEvent) domainEvents.get(0);
        assertEquals("name", event.getUpdatedFields());
    }
    
    /**
     * 创建用于测试的自然人对象
     */
    private Person createTestPerson() {
        PersonId personId = PersonId.of(UUID.randomUUID().toString());
        String name = "张三";
        Gender gender = Gender.MALE;
        LocalDate birthDate = LocalDate.of(1990, 1, 1);
        
        IdentityDocument idCard = IdentityDocument.ofIdCard(
                "110101199001011234", 
                "北京市公安局",
                LocalDate.now(),
                LocalDate.now().plusYears(20)
        );
        
        ContactInfo contactInfo = ContactInfo.basic("13800138000", null);
        
        return Person.create(personId, name, gender, birthDate, idCard, contactInfo);
    }
    
    @Test
    @DisplayName("测试参数验证-创建自然人")
    void testCreatePersonParameterValidation() {
        // 测试空ID
        assertThrows(DomainRuleViolationException.class, () -> 
            Person.create(null, "张三", Gender.MALE, LocalDate.of(1990, 1, 1), 
                identityDocument, contactInfo)
        );
        
        // 测试空姓名
        assertThrows(DomainRuleViolationException.class, () -> 
            Person.create(personId, "", Gender.MALE, LocalDate.of(1990, 1, 1), 
                identityDocument, contactInfo)
        );
        
        // 测试空性别
        assertThrows(DomainRuleViolationException.class, () -> 
            Person.create(personId, "张三", null, LocalDate.of(1990, 1, 1), 
                identityDocument, contactInfo)
        );
        
        // 测试空出生日期
        assertThrows(DomainRuleViolationException.class, () -> 
            Person.create(personId, "张三", Gender.MALE, null, 
                identityDocument, contactInfo)
        );
        
        // 测试空证件信息
        assertThrows(DomainRuleViolationException.class, () -> 
            Person.create(personId, "张三", Gender.MALE, LocalDate.of(1990, 1, 1), 
                null, contactInfo)
        );
        
        // 测试空联系方式
        assertThrows(DomainRuleViolationException.class, () -> 
            Person.create(personId, "张三", Gender.MALE, LocalDate.of(1990, 1, 1), 
                identityDocument, null)
        );
    }
    
    @Test
    @DisplayName("测试复杂场景-完整生命周期")
    void testPersonLifecycleWithAllOperations() {
        // 创建自然人
        Person person = createTestPerson();
        
        // 更新姓名（测试连续操作）
        person.updateName("李四");
        person.updateName("王五");
        assertEquals("王五", person.getName());
        
        // 添加含生僻字的姓名
        person.updateNameWithRareCharacter("王大", "王𠄀", "wang da"); 
        assertTrue(person.isRareCharacter());
        assertEquals("王大", person.getDisplayName());
        assertEquals("王𠄀", person.getFullName());
        
        // 再次更新普通姓名应该清除生僻字标记
        person.updateName("赵六");
        assertFalse(person.isRareCharacter());
        assertNull(person.getOriginalName());
        assertEquals("赵六", person.getDisplayName());
        assertEquals("赵六", person.getFullName());
        
        // 添加多个联系方式并测试默认联系方式
        ContactInfo email = ContactInfo.of(null, "zhaoliu@example.com", null, null, null, null);
        ContactInfo workPhone = ContactInfo.of(null, null, "010-12345678", null, null, null);
        ContactInfo wechat = ContactInfo.of(null, null, null, "zhaoliu888", null, null);
        
        person.addContactInfo(email, "电子邮件", false);
        person.addContactInfo(workPhone, "工作电话", true);
        person.addContactInfo(wechat, "微信", false);
        
        assertEquals(workPhone, person.getDefaultContactInfo());
        assertEquals(4, person.getContactInfos().size());
        
        // 添加多个地址并设置默认
        Address homeAddress = Address.ofChina("北京市", "北京市", "海淀区", "清华园街道", "清华园路1号", "100084");
        Address workAddress = Address.ofChina("北京市", "北京市", "朝阳区", "朝阳门街道", "朝阳门外大街甲6号", "100020");
        
        person.addAddress(homeAddress, "家庭地址", true);
        person.addAddress(workAddress, "工作地址", false);
        
        assertEquals(homeAddress, person.getDefaultAddress());
        
        // 切换默认地址然后再次检查
        person.setDefaultAddress(1);
        assertEquals(workAddress, person.getDefaultAddress());
        
        // 添加多个教育经历
        Education highSchool = Education.of(
                "北京四中",
                "普通班",
                EducationLevel.HIGH_SCHOOL,
                LocalDate.of(2006, 9, 1),
                LocalDate.of(2009, 7, 1),
                true,
                "高中毕业证"
        );
        
        Education bachelor = Education.of(
                "清华大学",
                "计算机科学与技术",
                EducationLevel.UNDERGRADUATE,
                LocalDate.of(2009, 9, 1),
                LocalDate.of(2013, 7, 1),
                true,
                "学士学位"
        );
        
        Education master = Education.of(
                "北京大学",
                "人工智能",
                EducationLevel.MASTER,
                LocalDate.of(2013, 9, 1),
                LocalDate.of(2016, 7, 1),
                true,
                "硕士学位"
        );
        
        person.addEducation(highSchool);
        person.addEducation(bachelor);
        person.addEducation(master);
        
        // 验证最高学历
        assertEquals(master, person.getHighestEducation());
        
        // 更新婚姻状况
        person.updateMaritalStatus(MaritalStatus.MARRIED);
        assertEquals(MaritalStatus.MARRIED, person.getMaritalStatus());
        
        // 添加其他证件
        IdentityDocument passport = IdentityDocument.ofPassport(
                "P12345678",
                "北京市出入境管理局",
                LocalDate.now(),
                LocalDate.now().plusYears(10)
        );
        person.addIdentityDocument(passport);
        
        // 更新生物特征
        Biometric biometric = Biometric.of(
                List.of("指纹1", "指纹2"),
                "人脸特征数据",
                List.of("虹膜数据"),
                "掌纹数据"
        );
        person.updateBiometric(biometric);
        
        // 检查状态变更
        person.changeStatus(PersonStatus.DISABLED);
        assertEquals(PersonStatus.DISABLED, person.getStatus());
        
        person.markAsDeleted();
        assertEquals(PersonStatus.DELETED, person.getStatus());
        
        // 验证领域事件总数
        // 1个创建 + 3个姓名更新 + 3个联系方式添加 + 2个地址添加 + 1个默认地址变更
        // + 3个教育经历添加 + 1个婚姻状况更新 + 1个证件添加 + 1个生物特征更新 + 2个状态变更
        assertEquals(19, person.getDomainEvents().size());
    }
    
    @Test
    @DisplayName("测试验证相同证件类型替换逻辑")
    void testIdentityDocumentTypeReplacement() {
        // 创建自然人
        Person person = createTestPerson();
        
        // 获取当前身份证
        IdentityDocument originalIdCard = person.getPrimaryDocument();
        assertEquals(IdentityDocumentType.ID_CARD, originalIdCard.getType());
        
        // 创建相同类型但不同内容的新证件
        IdentityDocument newIdCard = IdentityDocument.ofIdCard(
                "110101199001012345", // 不同的证件号码
                "上海市公安局", // 不同的签发机构
                LocalDate.now().minusDays(10), // 不同的签发日期
                LocalDate.now().plusYears(10) // 不同的有效期
        );
        
        // 添加新证件（应该替换旧证件）
        person.addIdentityDocument(newIdCard);
        
        // 验证证件数量仍然为1
        assertEquals(1, person.getIdentityDocuments().size());
        
        // 验证主要证件已更新为新证件
        IdentityDocument currentPrimary = person.getPrimaryDocument();
        assertEquals("110101199001012345", currentPrimary.getNumber());
        assertEquals("上海市公安局", currentPrimary.getIssuingAuthority());
        
        // 添加不同类型的证件
        IdentityDocument passport = IdentityDocument.ofPassport(
                "P12345678",
                "北京市出入境管理局",
                LocalDate.now(),
                LocalDate.now().plusYears(10)
        );
        
        person.addIdentityDocument(passport);
        
        // 验证证件数量为2
        assertEquals(2, person.getIdentityDocuments().size());
        
        // 验证主要证件仍然是身份证
        assertEquals(IdentityDocumentType.ID_CARD, person.getPrimaryDocument().getType());
        
        // 再次替换护照
        IdentityDocument newPassport = IdentityDocument.ofPassport(
                "E87654321", // 不同的护照号
                "上海市出入境管理局", // 不同的签发机构
                LocalDate.now().minusDays(5),
                LocalDate.now().plusYears(5)
        );
        
        person.addIdentityDocument(newPassport);
        
        // 验证证件数量仍然为2
        assertEquals(2, person.getIdentityDocuments().size());
        
        // 验证护照已更新
        boolean hasUpdatedPassport = person.getIdentityDocuments().stream()
                .anyMatch(doc -> doc.getType() == IdentityDocumentType.PASSPORT && 
                        "E87654321".equals(doc.getNumber()));
        
        assertTrue(hasUpdatedPassport);
    }
    
    @Test
    @DisplayName("测试无证件和无联系方式时的行为")
    void testBehaviorWithoutDocumentsAndContacts() {
        // 创建自然人
        Person person = createTestPerson();
        
        // 清空已有领域事件
        person.clearEvents();
        
        // 移除所有联系方式
        while (!person.getContactInfos().isEmpty()) {
            person.removeContactInfo(0);
        }
        
        // 验证联系方式列表为空
        assertTrue(person.getContactInfos().isEmpty());
        
        // 测试获取默认联系方式应返回null
        assertNull(person.getDefaultContactInfo());
        
        // 创建一个只有ID、姓名、性别、出生日期的最小自然人对象
        PersonId minimalPersonId = PersonId.of(UUID.randomUUID().toString());
        String minimalName = "最小实例";
        Gender minimalGender = Gender.FEMALE;
        LocalDate minimalBirthDate = LocalDate.of(2000, 1, 1);
        
        IdentityDocument minimalDocument = IdentityDocument.ofIdCard(
                "110101200001010123", 
                "北京市公安局",
                LocalDate.now(),
                LocalDate.now().plusYears(20)
        );
        
        ContactInfo minimalContact = ContactInfo.basic("13900139000", null);
        
        Person minimalPerson = Person.create(minimalPersonId, minimalName, minimalGender, 
                minimalBirthDate, minimalDocument, minimalContact);
                
        // 验证地址相关操作
        assertTrue(minimalPerson.getAddresses().isEmpty());
        assertNull(minimalPerson.getDefaultAddress());
        
        // 验证教育经历相关操作
        assertTrue(minimalPerson.getEducations().isEmpty());
        assertNull(minimalPerson.getHighestEducation());
    }
    
    @Test
    @DisplayName("测试边界条件-索引越界异常")
    void testIndexOutOfBoundsExceptions() {
        // 创建自然人
        Person person = createTestPerson();
        
        // 测试地址相关边界条件
        assertTrue(person.getAddresses().isEmpty()); // 初始应为空
        
        // 测试设置默认地址时索引越界
        assertThrows(IllegalArgumentException.class, () -> person.setDefaultAddress(0));
        assertThrows(IllegalArgumentException.class, () -> person.setDefaultAddress(-1));
        
        // 测试删除地址时索引越界
        assertThrows(IllegalArgumentException.class, () -> person.removeAddress(0));
        assertThrows(IllegalArgumentException.class, () -> person.removeAddress(-1));
        
        // 添加一个地址后再测试
        Address address = Address.ofChina("北京市", "北京市", "海淀区", "清华园街道", "清华园路1号", "100084");
        person.addAddress(address, "家庭地址", true);
        
        // 现在索引0有效，但索引1仍然无效
        person.setDefaultAddress(0); // 不应抛出异常
        person.removeAddress(0); // 不应抛出异常
        
        // 地址应该再次为空
        assertTrue(person.getAddresses().isEmpty());
        
        // 联系方式测试（应该有初始联系方式）
        assertEquals(1, person.getContactInfos().size());
        
        // 测试设置默认联系方式时索引越界
        assertThrows(IllegalArgumentException.class, () -> person.setDefaultContactInfo(1));
        assertThrows(IllegalArgumentException.class, () -> person.setDefaultContactInfo(-1));
        
        // 测试删除联系方式时索引越界
        assertThrows(IllegalArgumentException.class, () -> person.removeContactInfo(1));
        assertThrows(IllegalArgumentException.class, () -> person.removeContactInfo(-1));
        
        // 有效索引不应抛出异常
        person.setDefaultContactInfo(0); // 不应抛出异常
        person.removeContactInfo(0); // 不应抛出异常
        
        // 联系方式应该为空
        assertTrue(person.getContactInfos().isEmpty());
    }
} 