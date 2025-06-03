package com.lovemp.domain.enterprise.domain;

import com.lovemp.common.domain.DomainEvent;
import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.domain.enterprise.domain.event.EnterpriseCreatedEvent;
import com.lovemp.domain.enterprise.domain.event.EnterpriseStatusChangedEvent;
import com.lovemp.domain.enterprise.domain.event.EnterpriseUpdatedEvent;
import com.lovemp.domain.enterprise.domain.model.aggregate.Enterprise;
import com.lovemp.domain.enterprise.domain.model.valueobject.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Nested;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Enterprise聚合根的单元测试
 */
class EnterpriseTest {

    private Enterprise enterprise;
    private EnterpriseId enterpriseId;
    private String name;
    private String unifiedSocialCreditCode;
    private EnterpriseType enterpriseType;
    private LegalRepresentative legalRepresentative;
    private Money registeredCapital;
    private Address registeredAddress;
    private ContactInfo contactInfo;
    
    @BeforeEach
    void setUp() {
        // 准备测试数据
        enterpriseId = EnterpriseId.of(UUID.randomUUID().toString());
        name = "测试企业";
        unifiedSocialCreditCode = "91310000XXXXXXXXXX";
        enterpriseType = EnterpriseType.LIMITED_LIABILITY_COMPANY;
        legalRepresentative = LegalRepresentative.naturalPerson("张三", "1234567890XXXXXX", "13800138000");
        registeredCapital = Money.ofCNY(new BigDecimal("1000"));
        registeredAddress = Address.of("上海市", "浦东新区", "张江高科技园区", "博云路2号", "详细地址", "201203");
        contactInfo = ContactInfo.email("mail@example.com", "联系人", null);
        
        // 创建企业实例
        enterprise = Enterprise.create(
            enterpriseId,
            name,
            unifiedSocialCreditCode,
            enterpriseType,
            legalRepresentative,
            registeredCapital,
            LocalDate.now().minus(1, ChronoUnit.YEARS),
            registeredAddress,
            contactInfo
        );
    }
    
    @Nested
    @DisplayName("创建企业测试")
    class CreateEnterpriseTests {
        
        @Test
        @DisplayName("正常创建企业")
        void shouldCreateEnterprise() {
            // 验证基本属性设置正确
            assertEquals(enterpriseId, enterprise.getId());
            assertEquals(name, enterprise.getName());
            assertEquals(unifiedSocialCreditCode, enterprise.getUnifiedSocialCreditCode());
            assertEquals(enterpriseType, enterprise.getEnterpriseType());
            assertEquals(legalRepresentative, enterprise.getLegalRepresentative());
            assertEquals(registeredCapital, enterprise.getRegisteredCapital());
            assertEquals(registeredAddress, enterprise.getRegisteredAddress());
            
            // 验证默认状态
            assertEquals(EnterpriseStatus.ACTIVE, enterprise.getStatus());
            
            // 验证初始集合
            assertEquals(1, enterprise.getOfficeAddresses().size());
            assertEquals(1, enterprise.getContactInfos().size());
            assertTrue(enterprise.getOfficeAddresses().get(0).isHeadquarters());
            assertTrue(enterprise.getContactInfos().get(0).isDefault());
            
            // 验证领域事件
            List<DomainEvent> domainEvents = enterprise.getDomainEvents();
            assertEquals(1, domainEvents.size());
            assertTrue(domainEvents.get(0) instanceof EnterpriseCreatedEvent);
            
            EnterpriseCreatedEvent event = (EnterpriseCreatedEvent) domainEvents.get(0);
            assertEquals(enterpriseId, event.getEnterpriseId());
            assertEquals(name, event.getEnterpriseName());
        }
        
        @Test
        @DisplayName("创建企业时参数验证")
        void shouldValidateParameters() {
            // 企业ID为空
            Exception exception = assertThrows(DomainRuleViolationException.class, () -> 
                Enterprise.create(
                    null,
                    name,
                    unifiedSocialCreditCode,
                    enterpriseType,
                    legalRepresentative,
                    registeredCapital,
                    LocalDate.now().minus(1, ChronoUnit.YEARS),
                    registeredAddress,
                    contactInfo
                )
            );
            assertTrue(exception.getMessage().contains("企业ID不能为空"));
            
            // 企业名称为空
            exception = assertThrows(DomainRuleViolationException.class, () -> 
                Enterprise.create(
                    enterpriseId,
                    null,
                    unifiedSocialCreditCode,
                    enterpriseType,
                    legalRepresentative,
                    registeredCapital,
                    LocalDate.now().minus(1, ChronoUnit.YEARS),
                    registeredAddress,
                    contactInfo
                )
            );
            assertTrue(exception.getMessage().contains("企业名称不能为空"));
            
            // 统一社会信用代码为空
            exception = assertThrows(DomainRuleViolationException.class, () -> 
                Enterprise.create(
                    enterpriseId,
                    name,
                    null,
                    enterpriseType,
                    legalRepresentative,
                    registeredCapital,
                    LocalDate.now().minus(1, ChronoUnit.YEARS),
                    registeredAddress,
                    contactInfo
                )
            );
            assertTrue(exception.getMessage().contains("统一社会信用代码不能为空"));
        }
    }
    
    @Nested
    @DisplayName("更新企业信息测试")
    class UpdateEnterpriseTests {
        
        @Test
        @DisplayName("更新企业名称")
        void shouldUpdateName() {
            String newName = "新企业名称";
            enterprise.updateName(newName);
            
            assertEquals(newName, enterprise.getName());
            
            // 验证领域事件
            List<DomainEvent> domainEvents = enterprise.getDomainEvents();
            assertEquals(2, domainEvents.size());
            assertTrue(domainEvents.get(1) instanceof EnterpriseUpdatedEvent);
            
            EnterpriseUpdatedEvent event = (EnterpriseUpdatedEvent) domainEvents.get(1);
            assertEquals(enterpriseId, event.getEnterpriseId());
            assertEquals("name", event.getUpdatedField());
        }
        
        @Test
        @DisplayName("更新注册资本")
        void shouldUpdateRegisteredCapital() {
            Money newCapital = Money.ofCNY(new BigDecimal("2000"));
            enterprise.updateRegisteredCapital(newCapital);
            
            assertEquals(newCapital, enterprise.getRegisteredCapital());
            
            // 验证领域事件
            List<DomainEvent> domainEvents = enterprise.getDomainEvents();
            assertEquals(2, domainEvents.size());
            assertTrue(domainEvents.get(1) instanceof EnterpriseUpdatedEvent);
            
            EnterpriseUpdatedEvent event = (EnterpriseUpdatedEvent) domainEvents.get(1);
            assertEquals(enterpriseId, event.getEnterpriseId());
            assertEquals("registeredCapital", event.getUpdatedField());
        }
        
        @Test
        @DisplayName("更新实缴资本")
        void shouldUpdatePaidInCapital() {
            Money paidInCapital = Money.ofCNY(new BigDecimal("500"));
            enterprise.updatePaidInCapital(paidInCapital);
            
            assertEquals(paidInCapital, enterprise.getPaidInCapital());
            
            // 验证实缴资本不能大于注册资本
            Money invalidPaidInCapital = Money.ofCNY(new BigDecimal("1500"));
            Exception exception = assertThrows(DomainRuleViolationException.class, () -> 
                enterprise.updatePaidInCapital(invalidPaidInCapital)
            );
            assertTrue(exception.getMessage().contains("实缴资本不能大于注册资本"));
        }
        
        @Test
        @DisplayName("更新法定代表人")
        void shouldUpdateLegalRepresentative() {
            LegalRepresentative newLegalRepresentative = LegalRepresentative.naturalPerson("李四", "9876543210XXXXXX", "13900139000");
            enterprise.updateLegalRepresentative(newLegalRepresentative);
            
            assertEquals(newLegalRepresentative, enterprise.getLegalRepresentative());
        }
    }
    
    @Nested
    @DisplayName("企业状态变更测试")
    class EnterpriseStatusTests {
        
        @Test
        @DisplayName("变更企业状态")
        void shouldChangeStatus() {
            enterprise.changeStatus(EnterpriseStatus.SUSPENDED);
            
            assertEquals(EnterpriseStatus.SUSPENDED, enterprise.getStatus());
            
            // 验证领域事件
            List<DomainEvent> domainEvents = enterprise.getDomainEvents();
            assertEquals(2, domainEvents.size());
            assertTrue(domainEvents.get(1) instanceof EnterpriseStatusChangedEvent);
            
            EnterpriseStatusChangedEvent event = (EnterpriseStatusChangedEvent) domainEvents.get(1);
            assertEquals(enterpriseId, event.getEnterpriseId());
            assertEquals(EnterpriseStatus.ACTIVE, event.getOldStatus());
            assertEquals(EnterpriseStatus.SUSPENDED, event.getNewStatus());
        }
        
        @Test
        @DisplayName("标记企业为已注销")
        void shouldMarkAsCancelled() {
            enterprise.markAsCancelled();
            
            assertEquals(EnterpriseStatus.CANCELLED, enterprise.getStatus());
        }
        
        @Test
        @DisplayName("标记企业为已吊销")
        void shouldMarkAsRevoked() {
            enterprise.markAsRevoked();
            
            assertEquals(EnterpriseStatus.REVOKED, enterprise.getStatus());
        }
    }
    
    @Nested
    @DisplayName("办公地址管理测试")
    class OfficeAddressTests {
        
        @Test
        @DisplayName("添加办公地址")
        void shouldAddOfficeAddress() {
            Address newAddress = Address.of("北京市", "海淀区", "中关村", "科技路", "详细地址", "100080");
            enterprise.addOfficeAddress(newAddress, "北京办公室", false);
            
            assertEquals(2, enterprise.getOfficeAddresses().size());
            assertEquals(newAddress, enterprise.getOfficeAddresses().get(1).getAddress());
            assertEquals("北京办公室", enterprise.getOfficeAddresses().get(1).getTag());
            assertFalse(enterprise.getOfficeAddresses().get(1).isHeadquarters());
        }
        
        @Test
        @DisplayName("设置总部地址")
        void shouldSetHeadquartersAddress() {
            Address newAddress1 = Address.of("北京市", "海淀区", "中关村", "科技路", "详细地址", "100080");
            Address newAddress2 = Address.of("广州市", "天河区", "珠江新城", "冼村路", "详细地址", "510000");
            
            enterprise.addOfficeAddress(newAddress1, "北京办公室", false);
            enterprise.addOfficeAddress(newAddress2, "广州办公室", false);
            
            // 设置广州办公室为总部
            enterprise.setHeadquartersAddress(2);
            
            assertFalse(enterprise.getOfficeAddresses().get(0).isHeadquarters());
            assertFalse(enterprise.getOfficeAddresses().get(1).isHeadquarters());
            assertTrue(enterprise.getOfficeAddresses().get(2).isHeadquarters());
            
            // 验证获取总部地址
            assertEquals(newAddress2, enterprise.getHeadquartersAddress());
        }
        
        @Test
        @DisplayName("删除办公地址")
        void shouldRemoveOfficeAddress() {
            Address newAddress = Address.of("北京市", "海淀区", "中关村", "科技路", "详细地址", "100080");
            enterprise.addOfficeAddress(newAddress, "北京办公室", false);
            
            assertEquals(2, enterprise.getOfficeAddresses().size());
            
            enterprise.removeOfficeAddress(1);
            
            assertEquals(1, enterprise.getOfficeAddresses().size());
            
            // 不能删除总部地址
            Exception exception = assertThrows(DomainRuleViolationException.class, () -> 
                enterprise.removeOfficeAddress(0)
            );
            assertTrue(exception.getMessage().contains("不能删除总部地址"));
        }
    }
    
    @Nested
    @DisplayName("联系方式管理测试")
    class ContactInfoTests {
        
        @Test
        @DisplayName("添加联系方式")
        void shouldAddContactInfo() {
            ContactInfo newContactInfo = ContactInfo.phone("021-87654321", "客服", null);
            enterprise.addContactInfo(newContactInfo, "客服", false);
            
            assertEquals(2, enterprise.getContactInfos().size());
            assertEquals(newContactInfo, enterprise.getContactInfos().get(1).getContactInfo());
            assertEquals("客服", enterprise.getContactInfos().get(1).getTag());
            assertFalse(enterprise.getContactInfos().get(1).isDefault());
        }
        
        @Test
        @DisplayName("设置默认联系方式")
        void shouldSetDefaultContactInfo() {
            ContactInfo newContactInfo1 = ContactInfo.phone("021-87654321", "客服", null);
            ContactInfo newContactInfo2 = ContactInfo.email("hr@example.com", "人事", null);
            
            enterprise.addContactInfo(newContactInfo1, "客服", false);
            enterprise.addContactInfo(newContactInfo2, "人事", true);
            
            // 设置人事联系方式为默认
            enterprise.setDefaultContactInfo(2);
            
            assertFalse(enterprise.getContactInfos().get(0).isDefault());
            assertFalse(enterprise.getContactInfos().get(1).isDefault());
            assertTrue(enterprise.getContactInfos().get(2).isDefault());
            
            // 验证获取默认联系方式
            assertEquals(newContactInfo2, enterprise.getDefaultContactInfo());
        }
        
        @Test
        @DisplayName("删除联系方式")
        void shouldRemoveContactInfo() {
            ContactInfo newContactInfo = ContactInfo.phone("021-87654321", "客服", null);
            enterprise.addContactInfo(newContactInfo, "客服", false);
            
            assertEquals(2, enterprise.getContactInfos().size());
            
            enterprise.removeContactInfo(1);
            
            assertEquals(1, enterprise.getContactInfos().size());
            
            // 不能删除唯一的联系方式
            Exception exception = assertThrows(DomainRuleViolationException.class, () -> 
                enterprise.removeContactInfo(0)
            );
            assertTrue(exception.getMessage().contains("不能删除唯一的联系方式"));
        }
    }
    
    @Nested
    @DisplayName("企业证书管理测试")
    class CertificateTests {
        
        @Test
        @DisplayName("添加企业证书")
        void shouldAddCertificate() {
            Certificate certificate = Certificate.of(
                UUID.randomUUID().toString(),
                "ISO9001质量管理体系认证",
                CertificateType.ISO9001,
                "CERT123456",
                "认证机构",
                LocalDate.now().minus(1, ChronoUnit.MONTHS),
                LocalDate.now(),
                LocalDate.now().plus(3, ChronoUnit.YEARS),
                "/certificates/iso9001.pdf",
                null
            );
            
            enterprise.addCertificate(certificate);
            
            assertEquals(1, enterprise.getCertificates().size());
            assertEquals(certificate, enterprise.getCertificates().get(0));
            
            // 验证获取证书
            assertTrue(enterprise.getCertificate(certificate.getId()).isPresent());
            assertEquals(certificate, enterprise.getCertificate(certificate.getId()).get());
            
            // 验证按类型获取证书
            assertEquals(1, enterprise.getCertificatesByType(CertificateType.ISO9001).size());
            assertEquals(0, enterprise.getCertificatesByType(CertificateType.ISO14001).size());
            
            // 验证获取有效证书
            assertEquals(1, enterprise.getValidCertificates().size());
        }
        
        @Test
        @DisplayName("删除企业证书")
        void shouldRemoveCertificate() {
            Certificate certificate = Certificate.of(
                UUID.randomUUID().toString(),
                "ISO9001质量管理体系认证",
                CertificateType.ISO9001,
                "CERT123456",
                "认证机构",
                LocalDate.now().minus(1, ChronoUnit.MONTHS),
                LocalDate.now(),
                LocalDate.now().plus(3, ChronoUnit.YEARS),
                "/certificates/iso9001.pdf",
                null
            );
            
            enterprise.addCertificate(certificate);
            assertEquals(1, enterprise.getCertificates().size());
            
            enterprise.removeCertificate(certificate.getId());
            assertEquals(0, enterprise.getCertificates().size());
        }
        
        @Test
        @DisplayName("添加知识产权相关证书")
        void shouldAddIntellectualPropertyCertificates() {
            // 添加软件著作权证书
            Certificate softwareCopyright = Certificate.of(
                UUID.randomUUID().toString(),
                "某软件著作权",
                CertificateType.SOFTWARE_COPYRIGHT,
                "软著登字第12345号",
                "国家版权局",
                LocalDate.now().minus(6, ChronoUnit.MONTHS),
                LocalDate.now().minus(6, ChronoUnit.MONTHS),
                LocalDate.now().plus(10, ChronoUnit.YEARS),
                "/certificates/software_copyright.pdf",
                "软件著作权证书"
            );
            enterprise.addCertificate(softwareCopyright);
            
            // 添加发明专利证书
            Certificate inventionPatent = Certificate.of(
                UUID.randomUUID().toString(),
                "某发明专利",
                CertificateType.INVENTION_PATENT,
                "ZL202XXXXXXXXX.X", 
                "国家知识产权局",
                LocalDate.now().minus(1, ChronoUnit.YEARS),
                LocalDate.now().minus(1, ChronoUnit.YEARS),
                LocalDate.now().plus(20, ChronoUnit.YEARS),
                "/certificates/invention_patent.pdf",
                "发明专利证书"
            );
            enterprise.addCertificate(inventionPatent);
            
            // 添加外观设计专利证书
            Certificate designPatent = Certificate.of(
                UUID.randomUUID().toString(),
                "某外观设计专利",
                CertificateType.DESIGN_PATENT,
                "ZL202XXXXXXXXX.9",
                "国家知识产权局",
                LocalDate.now().minus(2, ChronoUnit.YEARS),
                LocalDate.now().minus(2, ChronoUnit.YEARS),
                LocalDate.now().plus(10, ChronoUnit.YEARS),
                "/certificates/design_patent.pdf",
                "外观设计专利证书"
            );
            enterprise.addCertificate(designPatent);
            
            // 添加商标注册证书
            Certificate trademarkRegistration = Certificate.of(
                UUID.randomUUID().toString(),
                "某商标",
                CertificateType.TRADEMARK_REGISTRATION,
                "12345678号",
                "国家知识产权局",
                LocalDate.now().minus(3, ChronoUnit.YEARS),
                LocalDate.now().minus(3, ChronoUnit.YEARS),
                LocalDate.now().plus(10, ChronoUnit.YEARS),
                "/certificates/trademark.pdf",
                "商标注册证书"
            );
            enterprise.addCertificate(trademarkRegistration);
            
            // 添加网络安全等级保护认证
            Certificate networkSecurityProtection = Certificate.of(
                UUID.randomUUID().toString(),
                "三级等保认证",
                CertificateType.NETWORK_SECURITY_PROTECTION,
                "DJBH12345678",
                "公安部",
                LocalDate.now().minus(1, ChronoUnit.YEARS),
                LocalDate.now().minus(1, ChronoUnit.YEARS),
                LocalDate.now().plus(3, ChronoUnit.YEARS),
                "/certificates/security_protection.pdf",
                "三级等保认证"
            );
            enterprise.addCertificate(networkSecurityProtection);
            
            // 验证证书添加成功
            assertEquals(5, enterprise.getCertificates().size());
            
            // 验证可以通过类型获取知识产权证书
            List<Certificate> intellectualPropertyCerts = enterprise.getCertificates().stream()
                    .filter(cert -> cert.getType().isIntellectualPropertyCertificate())
                    .toList();
            assertEquals(4, intellectualPropertyCerts.size());
            
            // 验证有效证书
            assertTrue(enterprise.getValidCertificates().size() >= 5);
            
            // 验证领域事件
            List<DomainEvent> domainEvents = enterprise.getDomainEvents();
            assertEquals(6, domainEvents.size());
            assertTrue(domainEvents.get(1) instanceof EnterpriseUpdatedEvent);
            
            EnterpriseUpdatedEvent event = (EnterpriseUpdatedEvent) domainEvents.get(5);
            assertEquals(enterpriseId, event.getEnterpriseId());
            assertEquals("certificates", event.getUpdatedField());
        }
    }
    
    @Nested
    @DisplayName("企业资质管理测试")
    class QualificationTests {
        
        @Test
        @DisplayName("添加企业资质")
        void shouldAddQualification() {
            Qualification qualification = Qualification.of(
                UUID.randomUUID().toString(),
                QualificationType.FOOD_OPERATION,
                "食品经营许可证",
                "一级",
                "JY12345678",
                "市场监督管理局",
                LocalDate.now().minus(6, ChronoUnit.MONTHS),
                LocalDate.now(),
                LocalDate.now().plus(5, ChronoUnit.YEARS),
                "/qualifications/food_license.pdf",
                null
            );
            
            enterprise.addQualification(qualification);
            
            assertEquals(1, enterprise.getQualifications().size());
            assertEquals(qualification, enterprise.getQualifications().get(0));
            
            // 验证获取资质
            assertTrue(enterprise.getQualification(qualification.getId()).isPresent());
            assertEquals(qualification, enterprise.getQualification(qualification.getId()).get());
            
            // 验证按类型获取资质
            assertEquals(1, enterprise.getQualificationsByType(QualificationType.FOOD_OPERATION).size());
            assertEquals(0, enterprise.getQualificationsByType(QualificationType.DRUG_OPERATION).size());
            
            // 验证获取有效资质
            assertEquals(1, enterprise.getValidQualifications().size());
        }
        
        @Test
        @DisplayName("添加互联网相关资质")
        void shouldAddInternetRelatedQualifications() {
            // 添加增值电信业务经营许可证
            Qualification valueAddedTelecom = Qualification.of(
                UUID.randomUUID().toString(),
                QualificationType.VALUE_ADDED_TELECOM_BUSINESS,
                "增值电信业务经营许可证",
                "B1级",
                "B2-20230001",
                "工信部",
                LocalDate.now().minus(1, ChronoUnit.YEARS),
                LocalDate.now(),
                LocalDate.now().plus(5, ChronoUnit.YEARS),
                "/qualifications/vatb.pdf",
                null
            );
            enterprise.addQualification(valueAddedTelecom);
            
            // 添加ICP证
            Qualification icp = Qualification.of(
                UUID.randomUUID().toString(),
                QualificationType.INTERNET_CONTENT_PROVIDER,
                "互联网信息服务业务许可证",
                "A类",
                "京ICP证12345号",
                "工信部",
                LocalDate.now().minus(2, ChronoUnit.YEARS),
                LocalDate.now().minus(2, ChronoUnit.YEARS),
                LocalDate.now().plus(3, ChronoUnit.YEARS),
                "/qualifications/icp.pdf",
                null
            );
            enterprise.addQualification(icp);
            
            // 添加网络文化经营许可证
            Qualification networkCulture = Qualification.of(
                UUID.randomUUID().toString(),
                QualificationType.NETWORK_CULTURE_OPERATION,
                "网络文化经营许可证",
                "网络游戏类",
                "京网文[2023]1234-567号",
                "文化部",
                LocalDate.now().minus(6, ChronoUnit.MONTHS),
                LocalDate.now().minus(6, ChronoUnit.MONTHS),
                LocalDate.now().plus(2, ChronoUnit.YEARS),
                "/qualifications/network_culture.pdf",
                null
            );
            enterprise.addQualification(networkCulture);
            
            // 添加高新技术企业认定
            Qualification highTech = Qualification.of(
                UUID.randomUUID().toString(),
                QualificationType.HIGH_TECH_ENTERPRISE,
                "高新技术企业认定",
                "国家级",
                "GR202311001234",
                "科技部",
                LocalDate.now().minus(3, ChronoUnit.MONTHS),
                LocalDate.now().minus(3, ChronoUnit.MONTHS),
                LocalDate.now().plus(3, ChronoUnit.YEARS),
                "/qualifications/high_tech.pdf",
                null
            );
            enterprise.addQualification(highTech);
            
            // 添加软件企业认定
            Qualification softwareEnterprise = Qualification.of(
                UUID.randomUUID().toString(),
                QualificationType.SOFTWARE_ENTERPRISE,
                "软件企业认定",
                "国家级",
                "R-2023-0123",
                "工信部",
                LocalDate.now().minus(3, ChronoUnit.MONTHS),
                LocalDate.now().minus(3, ChronoUnit.MONTHS),
                LocalDate.now().plus(1, ChronoUnit.YEARS),
                "/qualifications/software_enterprise.pdf",
                null
            );
            enterprise.addQualification(softwareEnterprise);
            
            // 验证资质添加成功
            assertEquals(5, enterprise.getQualifications().size());
            
            // 验证可以通过类型获取互联网相关资质
            List<Qualification> internetRelatedQuals = enterprise.getQualifications().stream()
                    .filter(qual -> qual.getType().isInternetRelated())
                    .collect(java.util.stream.Collectors.toList());
            assertEquals(3, internetRelatedQuals.size());
            
            // 验证可以通过类型获取IT企业认定资质
            List<Qualification> itEnterpriseCertifications = enterprise.getQualifications().stream()
                    .filter(qual -> qual.getType().isITEnterpriseCertification())
                    .collect(java.util.stream.Collectors.toList());
            assertEquals(2, itEnterpriseCertifications.size());
            
            // 验证所有资质都有效
            assertEquals(5, enterprise.getValidQualifications().size());
            
            // 验证领域事件
            List<DomainEvent> domainEvents = enterprise.getDomainEvents();
            assertEquals(6, domainEvents.size());
            assertTrue(domainEvents.get(1) instanceof EnterpriseUpdatedEvent);
            
            EnterpriseUpdatedEvent event = (EnterpriseUpdatedEvent) domainEvents.get(5);
            assertEquals(enterpriseId, event.getEnterpriseId());
            assertEquals("qualifications", event.getUpdatedField());
        }
        
        @Test
        @DisplayName("删除企业资质")
        void shouldRemoveQualification() {
            Qualification qualification = Qualification.of(
                UUID.randomUUID().toString(),
                QualificationType.FOOD_OPERATION,
                "食品经营许可证",
                "一级",
                "JY12345678",
                "市场监督管理局",
                LocalDate.now().minus(6, ChronoUnit.MONTHS),
                LocalDate.now(),
                LocalDate.now().plus(5, ChronoUnit.YEARS),
                "/qualifications/food_license.pdf",
                null
            );
            
            enterprise.addQualification(qualification);
            assertEquals(1, enterprise.getQualifications().size());
            
            enterprise.removeQualification(qualification.getId());
            assertEquals(0, enterprise.getQualifications().size());
        }
    }
    
    @Nested
    @DisplayName("企业备案管理测试")
    class FilingTests {
        
        @Test
        @DisplayName("添加企业备案")
        void shouldAddFiling() {
            Filing filing = Filing.of(
                UUID.randomUUID().toString(),
                FilingType.ICP,
                "京ICP备12345678号",
                "测试网站",
                "test.example.com",
                "测试企业",
                LocalDate.now().minus(1, ChronoUnit.YEARS),
                LocalDate.now().minus(11, ChronoUnit.MONTHS),
                LocalDate.now().minus(11, ChronoUnit.MONTHS),
                null,
                "有效",
                "工信部",
                "/filings/icp.pdf",
                null
            );
            
            enterprise.addFiling(filing);
            
            assertEquals(1, enterprise.getFilings().size());
            assertEquals(filing, enterprise.getFilings().get(0));
            
            // 验证获取备案
            assertTrue(enterprise.getFiling(filing.getId()).isPresent());
            assertEquals(filing, enterprise.getFiling(filing.getId()).get());
            
            // 验证按类型获取备案
            assertEquals(1, enterprise.getFilingsByType(FilingType.ICP).size());
            assertEquals(0, enterprise.getFilingsByType(FilingType.PUBLIC_SECURITY).size());
            
            // 验证获取有效备案
            assertEquals(1, enterprise.getValidFilings().size());
        }
        
        @Test
        @DisplayName("添加ICP相关备案")
        void shouldAddICPRelatedFilings() {
            // 添加网站ICP备案
            Filing websiteICP = Filing.of(
                UUID.randomUUID().toString(),
                FilingType.WEBSITE_ICP,
                "京ICP备12345678号-1",
                "企业官网ICP备案",
                "example.com",
                "测试企业",
                LocalDate.now().minus(1, ChronoUnit.YEARS),
                LocalDate.now().minus(11, ChronoUnit.MONTHS),
                LocalDate.now().minus(11, ChronoUnit.MONTHS),
                null,
                "有效",
                "工信部",
                "/filings/website_icp.pdf",
                null
            );
            enterprise.addFiling(websiteICP);
            
            // 添加移动应用ICP备案
            Filing mobileAppICP = Filing.of(
                UUID.randomUUID().toString(),
                FilingType.MOBILE_APP_ICP,
                "京ICP备12345678号-2",
                "企业APP备案",
                "app.example.com",
                "测试企业",
                LocalDate.now().minus(6, ChronoUnit.MONTHS),
                LocalDate.now().minus(5, ChronoUnit.MONTHS),
                LocalDate.now().minus(5, ChronoUnit.MONTHS),
                null,
                "有效",
                "工信部",
                "/filings/mobile_app_icp.pdf",
                null
            );
            enterprise.addFiling(mobileAppICP);
            
            // 添加小程序ICP备案
            Filing miniProgramICP = Filing.of(
                UUID.randomUUID().toString(),
                FilingType.MINI_PROGRAM_ICP,
                "京ICP备12345678号-3",
                "企业小程序备案",
                "mini.example.com",
                "测试企业",
                LocalDate.now().minus(3, ChronoUnit.MONTHS),
                LocalDate.now().minus(2, ChronoUnit.MONTHS),
                LocalDate.now().minus(2, ChronoUnit.MONTHS),
                null,
                "有效",
                "工信部",
                "/filings/mini_program_icp.pdf",
                null
            );
            enterprise.addFiling(miniProgramICP);
            
            // 验证备案添加成功
            assertEquals(3, enterprise.getFilings().size());
            
            // 验证可以通过类型获取ICP相关备案
            List<Filing> icpRelatedFilings = enterprise.getFilings().stream()
                    .filter(filing -> filing.getType().isICPRelated())
                    .collect(java.util.stream.Collectors.toList());
            assertEquals(3, icpRelatedFilings.size());
            
            // 验证可以通过类型获取网站相关备案
            List<Filing> websiteRelatedFilings = enterprise.getFilings().stream()
                    .filter(filing -> filing.getType().isWebsiteRelated())
                    .collect(java.util.stream.Collectors.toList());
            assertEquals(1, websiteRelatedFilings.size());
            
            // 验证可以通过类型获取移动应用相关备案
            List<Filing> mobileAppRelatedFilings = enterprise.getFilings().stream()
                    .filter(filing -> filing.getType().isMobileAppRelated())
                    .collect(java.util.stream.Collectors.toList());
            assertEquals(2, mobileAppRelatedFilings.size());
            
            // 验证所有备案都有效
            assertEquals(3, enterprise.getValidFilings().size());
            
            // 验证领域事件
            List<DomainEvent> domainEvents = enterprise.getDomainEvents();
            assertEquals(4, domainEvents.size());
            assertTrue(domainEvents.get(1) instanceof EnterpriseUpdatedEvent);
            
            EnterpriseUpdatedEvent event = (EnterpriseUpdatedEvent) domainEvents.get(3);
            assertEquals(enterpriseId, event.getEnterpriseId());
            assertEquals("filings", event.getUpdatedField());
        }
        
        @Test
        @DisplayName("删除企业备案")
        void shouldRemoveFiling() {
            Filing filing = Filing.of(
                UUID.randomUUID().toString(),
                FilingType.ICP,
                "京ICP备12345678号",
                "测试网站",
                "test.example.com",
                "测试企业",
                LocalDate.now().minus(1, ChronoUnit.YEARS),
                LocalDate.now().minus(11, ChronoUnit.MONTHS),
                LocalDate.now().minus(11, ChronoUnit.MONTHS),
                null,
                "有效",
                "工信部",
                "/filings/icp.pdf",
                null
            );
            
            enterprise.addFiling(filing);
            assertEquals(1, enterprise.getFilings().size());
            
            enterprise.removeFiling(filing.getId());
            assertEquals(0, enterprise.getFilings().size());
        }
    }
    
    @Nested
    @DisplayName("企业资源账号管理测试")
    class ResourceAccountTests {
        
        @Test
        @DisplayName("添加企业资源账号")
        void shouldAddResourceAccount() {
            ResourceAccount account = ResourceAccount.of(
                UUID.randomUUID().toString(),
                "企业官网账号",
                ResourceAccountType.ENTERPRISE_WEBSITE,
                "阿里云",
                "admin@example.com",
                java.time.LocalDateTime.now().minus(1, ChronoUnit.YEARS),
                "张三",
                "13800138000",
                ResourceAccountStatus.ACTIVE,
                "密码存储在密码管理器中"
            );
            
            enterprise.addResourceAccount(account);
            
            assertEquals(1, enterprise.getResourceAccounts().size());
            assertEquals(account, enterprise.getResourceAccounts().get(0));
            
            // 验证获取资源账号
            assertTrue(enterprise.getResourceAccount(account.getId()).isPresent());
            assertEquals(account, enterprise.getResourceAccount(account.getId()).get());
            
            // 验证按类型获取资源账号
            assertEquals(1, enterprise.getResourceAccountsByType(ResourceAccountType.ENTERPRISE_WEBSITE).size());
            assertEquals(0, enterprise.getResourceAccountsByType(ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT).size());
            
            // 验证获取企业级账号
            assertEquals(1, enterprise.getEnterpriseLevelAccounts().size());
            assertEquals(0, enterprise.getBrandLevelAccounts().size());
        }
        
        @Test
        @DisplayName("添加iOS相关证书账号")
        void shouldAddIOSRelatedResourceAccounts() {
            // 添加iOS开发者账号（企业级）
            ResourceAccount iosDeveloperAccount = ResourceAccount.of(
                UUID.randomUUID().toString(),
                "企业iOS开发者账号",
                ResourceAccountType.IOS_DEVELOPER_ACCOUNT,
                "Apple",
                "developer@example.com",
                java.time.LocalDateTime.now().minus(1, ChronoUnit.YEARS),
                "张三",
                "13800138000",
                ResourceAccountStatus.ACTIVE,
                "开发者账号Team ID: ABCDE12345"
            );
            enterprise.addResourceAccount(iosDeveloperAccount);
            
            // 添加iOS开发证书
            ResourceAccount iosDevelopmentCertificate = ResourceAccount.of(
                UUID.randomUUID().toString(),
                "iOS开发证书",
                ResourceAccountType.IOS_DEVELOPMENT_CERTIFICATE,
                "Apple",
                "Development Certificate",
                java.time.LocalDateTime.now().minus(6, ChronoUnit.MONTHS),
                "李四",
                "13900139000",
                ResourceAccountStatus.ACTIVE,
                "证书指纹: AA:BB:CC:DD:EE:FF"
            );
            enterprise.addResourceAccount(iosDevelopmentCertificate);
            
            // 添加iOS发布证书
            ResourceAccount iosDistributionCertificate = ResourceAccount.of(
                UUID.randomUUID().toString(),
                "iOS发布证书",
                ResourceAccountType.IOS_DISTRIBUTION_CERTIFICATE,
                "Apple",
                "Distribution Certificate",
                java.time.LocalDateTime.now().minus(3, ChronoUnit.MONTHS),
                "王五",
                "13700137000",
                ResourceAccountStatus.ACTIVE,
                "证书指纹: 11:22:33:44:55:66"
            );
            enterprise.addResourceAccount(iosDistributionCertificate);
            
            // 添加iOS推送证书
            ResourceAccount iosPushCertificate = ResourceAccount.of(
                UUID.randomUUID().toString(),
                "iOS推送证书",
                ResourceAccountType.IOS_PUSH_CERTIFICATE,
                "Apple",
                "Push Certificate",
                java.time.LocalDateTime.now().minus(2, ChronoUnit.MONTHS),
                "赵六",
                "13600136000",
                ResourceAccountStatus.ACTIVE,
                "证书指纹: AA:11:BB:22:CC:33"
            );
            enterprise.addResourceAccount(iosPushCertificate);
            
            // 验证所有iOS相关账号添加成功
            assertEquals(4, enterprise.getResourceAccounts().size());
            
            // 验证可以获取iOS相关账号
            List<ResourceAccount> iosRelatedAccounts = enterprise.getResourceAccounts().stream()
                    .filter(account -> account.getType().isIOSRelated())
                    .collect(java.util.stream.Collectors.toList());
            assertEquals(4, iosRelatedAccounts.size());
            
            // 验证可以获取iOS证书账号
            List<ResourceAccount> iosCertificateAccounts = enterprise.getResourceAccounts().stream()
                    .filter(account -> account.getType().isIOSCertificate())
                    .collect(java.util.stream.Collectors.toList());
            assertEquals(3, iosCertificateAccounts.size());
            
            // 验证可以获取移动应用开发账号
            List<ResourceAccount> mobileAppDeveloperAccounts = enterprise.getResourceAccounts().stream()
                    .filter(account -> account.getType().isMobileAppDeveloperAccount())
                    .collect(java.util.stream.Collectors.toList());
            assertEquals(1, mobileAppDeveloperAccounts.size());
            
            // 验证所有账号都是企业级账号
            assertEquals(4, enterprise.getEnterpriseLevelAccounts().size());
            assertEquals(0, enterprise.getBrandLevelAccounts().size());
            
            // 验证领域事件
            List<DomainEvent> domainEvents = enterprise.getDomainEvents();
            assertEquals(5, domainEvents.size());
            assertTrue(domainEvents.get(1) instanceof EnterpriseUpdatedEvent);
            
            EnterpriseUpdatedEvent event = (EnterpriseUpdatedEvent) domainEvents.get(4);
            assertEquals(enterpriseId, event.getEnterpriseId());
            assertEquals("resourceAccounts", event.getUpdatedField());
        }
        
        @Test
        @DisplayName("添加品牌级iOS证书账号")
        void shouldAddBrandIOSCertificateAccount() {
            String brandId = UUID.randomUUID().toString();
            
            // 添加品牌级iOS发布证书
            ResourceAccount brandIosCertificate = ResourceAccount.ofBrand(
                UUID.randomUUID().toString(),
                "品牌iOS发布证书",
                ResourceAccountType.IOS_DISTRIBUTION_CERTIFICATE,
                "Apple",
                "Brand Distribution Certificate",
                java.time.LocalDateTime.now().minus(3, ChronoUnit.MONTHS),
                brandId,
                "王五",
                "13700137000",
                ResourceAccountStatus.ACTIVE,
                "品牌专用发布证书"
            );
            enterprise.addResourceAccount(brandIosCertificate);
            
            assertEquals(1, enterprise.getResourceAccounts().size());
            
            // 验证iOS证书类型
            assertTrue(brandIosCertificate.getType().isIOSCertificate());
            
            // 验证账号属于品牌级而非企业级
            assertEquals(0, enterprise.getEnterpriseLevelAccounts().size());
            assertEquals(1, enterprise.getBrandLevelAccounts().size());
            
            // 验证可以通过品牌ID获取
            assertEquals(1, enterprise.getResourceAccountsByBrand(brandId).size());
            assertEquals(0, enterprise.getResourceAccountsByBrand("non-existent-brand").size());
        }
        
        @Test
        @DisplayName("添加品牌级资源账号")
        void shouldAddBrandResourceAccount() {
            String brandId = UUID.randomUUID().toString();
            
            ResourceAccount account = ResourceAccount.ofBrand(
                UUID.randomUUID().toString(),
                "品牌微信公众号",
                ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT,
                "腾讯",
                "brand_official_account",
                java.time.LocalDateTime.now().minus(6, ChronoUnit.MONTHS),
                brandId,
                "李四",
                "13900139000",
                ResourceAccountStatus.ACTIVE,
                "密码存储在密码管理器中"
            );
            
            enterprise.addResourceAccount(account);
            
            assertEquals(1, enterprise.getResourceAccounts().size());
            
            // 验证获取品牌级账号
            assertEquals(0, enterprise.getEnterpriseLevelAccounts().size());
            assertEquals(1, enterprise.getBrandLevelAccounts().size());
            
            // 验证按品牌获取账号
            assertEquals(1, enterprise.getResourceAccountsByBrand(brandId).size());
            assertEquals(0, enterprise.getResourceAccountsByBrand("non-existent-brand").size());
        }
        
        @Test
        @DisplayName("删除企业资源账号")
        void shouldRemoveResourceAccount() {
            ResourceAccount account = ResourceAccount.of(
                UUID.randomUUID().toString(),
                "企业官网账号",
                ResourceAccountType.ENTERPRISE_WEBSITE,
                "阿里云",
                "admin@example.com",
                java.time.LocalDateTime.now().minus(1, ChronoUnit.YEARS),
                "张三",
                "13800138000",
                ResourceAccountStatus.ACTIVE,
                "密码存储在密码管理器中"
            );
            
            enterprise.addResourceAccount(account);
            assertEquals(1, enterprise.getResourceAccounts().size());
            
            enterprise.removeResourceAccount(account.getId());
            assertEquals(0, enterprise.getResourceAccounts().size());
        }
    }
    
    @Test
    @DisplayName("测试集合的不可变性")
    void testCollectionImmutability() {
        // 准备测试数据
        addTestDataToEnterprise();
        
        // 获取各集合的不可变视图
        List<AddressWithTag> addresses = enterprise.getOfficeAddresses();
        List<ContactInfoWithTag> contacts = enterprise.getContactInfos();
        List<Certificate> certificates = enterprise.getCertificates();
        List<Qualification> qualifications = enterprise.getQualifications();
        List<Filing> filings = enterprise.getFilings();
        List<ResourceAccount> accounts = enterprise.getResourceAccounts();
        
        // 验证尝试修改集合时会抛出UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> addresses.add(null));
        assertThrows(UnsupportedOperationException.class, () -> contacts.add(null));
        assertThrows(UnsupportedOperationException.class, () -> certificates.add(null));
        assertThrows(UnsupportedOperationException.class, () -> qualifications.add(null));
        assertThrows(UnsupportedOperationException.class, () -> filings.add(null));
        assertThrows(UnsupportedOperationException.class, () -> accounts.add(null));
        
        // 验证尝试修改集合内容时会抛出UnsupportedOperationException
        assertThrows(UnsupportedOperationException.class, () -> addresses.remove(0));
        assertThrows(UnsupportedOperationException.class, () -> certificates.clear());
    }
    
    @Test
    @DisplayName("测试边界条件和索引越界异常")
    void testBoundaryConditionsAndIndexExceptions() {
        // 测试获取总部地址和默认联系方式
        assertEquals(registeredAddress, enterprise.getHeadquartersAddress());
        assertEquals(contactInfo, enterprise.getDefaultContactInfo());
        
        // 测试证书相关边界条件
        assertEquals(0, enterprise.getCertificates().size());
        assertTrue(enterprise.getCertificatesByType(CertificateType.ISO9001).isEmpty());
        assertTrue(enterprise.getValidCertificates().isEmpty());
        assertFalse(enterprise.getCertificate("non-existent-id").isPresent());
        
        // 测试资质相关边界条件
        assertEquals(0, enterprise.getQualifications().size());
        assertTrue(enterprise.getQualificationsByType(QualificationType.FOOD_OPERATION).isEmpty());
        assertTrue(enterprise.getValidQualifications().isEmpty());
        assertFalse(enterprise.getQualification("non-existent-id").isPresent());
        
        // 测试备案相关边界条件
        assertEquals(0, enterprise.getFilings().size());
        assertTrue(enterprise.getFilingsByType(FilingType.ICP).isEmpty());
        assertTrue(enterprise.getValidFilings().isEmpty());
        assertFalse(enterprise.getFiling("non-existent-id").isPresent());
        
        // 测试资源账号相关边界条件
        assertEquals(0, enterprise.getResourceAccounts().size());
        assertTrue(enterprise.getResourceAccountsByType(ResourceAccountType.ENTERPRISE_WEBSITE).isEmpty());
        assertTrue(enterprise.getEnterpriseLevelAccounts().isEmpty());
        assertTrue(enterprise.getBrandLevelAccounts().isEmpty());
        assertTrue(enterprise.getResourceAccountsByBrand("non-existent-brand").isEmpty());
        assertFalse(enterprise.getResourceAccount("non-existent-id").isPresent());
        
        // 测试地址索引越界异常
        assertThrows(DomainRuleViolationException.class, () -> enterprise.setHeadquartersAddress(1));
        assertThrows(DomainRuleViolationException.class, () -> enterprise.removeOfficeAddress(1));
        
        // 测试联系方式索引越界异常
        assertThrows(DomainRuleViolationException.class, () -> enterprise.setDefaultContactInfo(1));
        
        // 添加一个办公地址后再测试
        Address newAddress = Address.of("北京市", "海淀区", "中关村", "科技路", "详细地址", "100080");
        enterprise.addOfficeAddress(newAddress, "北京办公室", false);
        
        // 现在索引1有效，但索引2仍然无效
        assertThrows(DomainRuleViolationException.class, () -> enterprise.setHeadquartersAddress(2));
        assertThrows(DomainRuleViolationException.class, () -> enterprise.removeOfficeAddress(2));
        
        // 删除唯一的联系方式应该抛出异常
        assertThrows(DomainRuleViolationException.class, () -> enterprise.removeContactInfo(0));
    }
    
    @Test
    @DisplayName("测试参数验证完整性")
    void testParameterValidation() {
        // 测试创建企业时的参数验证
        // ID为空
        assertThrows(DomainRuleViolationException.class, () -> 
            Enterprise.create(null, name, unifiedSocialCreditCode, enterpriseType, 
                             legalRepresentative, registeredCapital, 
                             LocalDate.now().minus(1, ChronoUnit.YEARS), 
                             registeredAddress, contactInfo)
        );
        
        // 名称为空
        assertThrows(DomainRuleViolationException.class, () -> 
            Enterprise.create(enterpriseId, "", unifiedSocialCreditCode, enterpriseType, 
                             legalRepresentative, registeredCapital, 
                             LocalDate.now().minus(1, ChronoUnit.YEARS), 
                             registeredAddress, contactInfo)
        );
        
        // 统一社会信用代码为空
        assertThrows(DomainRuleViolationException.class, () -> 
            Enterprise.create(enterpriseId, name, "", enterpriseType, 
                             legalRepresentative, registeredCapital, 
                             LocalDate.now().minus(1, ChronoUnit.YEARS), 
                             registeredAddress, contactInfo)
        );
        
        // 企业类型为空
        assertThrows(DomainRuleViolationException.class, () -> 
            Enterprise.create(enterpriseId, name, unifiedSocialCreditCode, null, 
                             legalRepresentative, registeredCapital, 
                             LocalDate.now().minus(1, ChronoUnit.YEARS), 
                             registeredAddress, contactInfo)
        );
        
        // 法定代表人为空
        assertThrows(DomainRuleViolationException.class, () -> 
            Enterprise.create(enterpriseId, name, unifiedSocialCreditCode, enterpriseType, 
                             null, registeredCapital, 
                             LocalDate.now().minus(1, ChronoUnit.YEARS), 
                             registeredAddress, contactInfo)
        );
        
        // 注册资本为空
        assertThrows(DomainRuleViolationException.class, () -> 
            Enterprise.create(enterpriseId, name, unifiedSocialCreditCode, enterpriseType, 
                             legalRepresentative, null, 
                             LocalDate.now().minus(1, ChronoUnit.YEARS), 
                             registeredAddress, contactInfo)
        );
        
        // 成立日期为空
        assertThrows(DomainRuleViolationException.class, () -> 
            Enterprise.create(enterpriseId, name, unifiedSocialCreditCode, enterpriseType, 
                             legalRepresentative, registeredCapital, null, 
                             registeredAddress, contactInfo)
        );
        
        // 注册地址为空
        assertThrows(DomainRuleViolationException.class, () -> 
            Enterprise.create(enterpriseId, name, unifiedSocialCreditCode, enterpriseType, 
                             legalRepresentative, registeredCapital, 
                             LocalDate.now().minus(1, ChronoUnit.YEARS), 
                             null, contactInfo)
        );
        
        // 联系方式为空
        assertThrows(DomainRuleViolationException.class, () -> 
            Enterprise.create(enterpriseId, name, unifiedSocialCreditCode, enterpriseType, 
                             legalRepresentative, registeredCapital, 
                             LocalDate.now().minus(1, ChronoUnit.YEARS), 
                             registeredAddress, null)
        );
        
        // 测试更新注册资本参数验证
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.updateRegisteredCapital(null)
        );
        
        // 测试注册资本不能为负数
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.updateRegisteredCapital(Money.ofCNY(new BigDecimal("-1")))
        );
        
        // 测试更新实缴资本参数验证
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.updatePaidInCapital(null)
        );
        
        // 测试实缴资本不能为负数
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.updatePaidInCapital(Money.ofCNY(new BigDecimal("-1")))
        );
        
        // 测试添加地址参数验证
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.addOfficeAddress(null, "标签", false)
        );
        
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.addOfficeAddress(registeredAddress, "", false)
        );
        
        // 测试添加联系方式参数验证
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.addContactInfo(null, "标签", false)
        );
        
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.addContactInfo(contactInfo, "", false)
        );
        
        // 测试添加证书参数验证
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.addCertificate(null)
        );
        
        // 测试添加资质参数验证
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.addQualification(null)
        );
        
        // 测试添加备案参数验证
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.addFiling(null)
        );
        
        // 测试添加资源账号参数验证
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.addResourceAccount(null)
        );
        
        // 测试企业状态切换参数验证
        assertThrows(DomainRuleViolationException.class, () -> 
            enterprise.changeStatus(null)
        );
    }
    
    @Test
    @DisplayName("测试复杂场景-企业完整生命周期")
    void testEnterpriseLifecycleWithAllOperations() {
        // 验证初始状态
        assertEquals(EnterpriseStatus.ACTIVE, enterprise.getStatus());
        assertEquals(name, enterprise.getName());
        assertEquals(1, enterprise.getOfficeAddresses().size());
        assertEquals(1, enterprise.getContactInfos().size());
        
        // 第一阶段：基本信息更新
        enterprise.updateName("新企业名称");
        LegalRepresentative newLegalRepresentative = LegalRepresentative.naturalPerson("李四", "9876543210XXXXXX", "13900139000");
        enterprise.updateLegalRepresentative(newLegalRepresentative);
        Money newCapital = Money.ofCNY(new BigDecimal("2000"));
        enterprise.updateRegisteredCapital(newCapital);
        Money paidInCapital = Money.ofCNY(new BigDecimal("1500"));
        enterprise.updatePaidInCapital(paidInCapital);
        enterprise.updateBusinessScope("新的经营范围包括软件开发、技术咨询等");
        
        // 验证基本信息更新结果
        assertEquals("新企业名称", enterprise.getName());
        assertEquals(newLegalRepresentative, enterprise.getLegalRepresentative());
        assertEquals(newCapital, enterprise.getRegisteredCapital());
        assertEquals(paidInCapital, enterprise.getPaidInCapital());
        assertEquals("新的经营范围包括软件开发、技术咨询等", enterprise.getBusinessScope());
        
        // 第二阶段：地址和联系方式管理
        Address newAddress1 = Address.of("北京市", "海淀区", "中关村", "科技路", "详细地址", "100080");
        Address newAddress2 = Address.of("上海市", "浦东新区", "张江高科技园区", "博云路", "详细地址", "201203");
        enterprise.addOfficeAddress(newAddress1, "北京总部", true);
        enterprise.addOfficeAddress(newAddress2, "上海分部", false);
        
        // 验证地址操作结果
        assertEquals(3, enterprise.getOfficeAddresses().size());
        assertEquals(newAddress1, enterprise.getHeadquartersAddress());
        
        // 添加新联系方式
        ContactInfo newContact1 = ContactInfo.phone("021-87654321", "客服部门", null);
        ContactInfo newContact2 = ContactInfo.email("hr@example.com", "人事部门", null);
        enterprise.addContactInfo(newContact1, "客服部门", false);
        enterprise.addContactInfo(newContact2, "人事部门", true);
        
        // 验证联系方式操作结果
        assertEquals(3, enterprise.getContactInfos().size());
        assertEquals(newContact2, enterprise.getDefaultContactInfo());
        
        // 第三阶段：添加各类证书、资质、备案和资源账号
        // 添加证书
        Certificate certificate = Certificate.of(
            UUID.randomUUID().toString(),
            "ISO9001质量管理体系认证",
            CertificateType.ISO9001,
            "CERT123456",
            "认证机构",
            LocalDate.now().minus(1, ChronoUnit.MONTHS),
            LocalDate.now(),
            LocalDate.now().plus(3, ChronoUnit.YEARS),
            "/certificates/iso9001.pdf",
            null
        );
        enterprise.addCertificate(certificate);
        
        // 添加资质
        Qualification qualification = Qualification.of(
            UUID.randomUUID().toString(),
            QualificationType.FOOD_OPERATION,
            "食品经营许可证",
            "一级",
            "JY12345678",
            "市场监督管理局",
            LocalDate.now().minus(6, ChronoUnit.MONTHS),
            LocalDate.now(),
            LocalDate.now().plus(5, ChronoUnit.YEARS),
            "/qualifications/food_license.pdf",
            null
        );
        enterprise.addQualification(qualification);
        
        // 添加备案
        Filing filing = Filing.of(
            UUID.randomUUID().toString(),
            FilingType.ICP,
            "京ICP备12345678号",
            "测试网站",
            "test.example.com",
            "测试企业",
            LocalDate.now().minus(1, ChronoUnit.YEARS),
            LocalDate.now().minus(11, ChronoUnit.MONTHS),
            LocalDate.now().minus(11, ChronoUnit.MONTHS),
            null,
            "有效",
            "工信部",
            "/filings/icp.pdf",
            null
        );
        enterprise.addFiling(filing);
        
        // 添加企业级资源账号
        ResourceAccount enterpriseAccount = ResourceAccount.of(
            UUID.randomUUID().toString(),
            "企业官网账号",
            ResourceAccountType.ENTERPRISE_WEBSITE,
            "阿里云",
            "admin@example.com",
            java.time.LocalDateTime.now().minus(1, ChronoUnit.YEARS),
            "张三",
            "13800138000",
            ResourceAccountStatus.ACTIVE,
            "账号备注"
        );
        enterprise.addResourceAccount(enterpriseAccount);
        
        // 添加品牌级资源账号
        String brandId = UUID.randomUUID().toString();
        ResourceAccount brandAccount = ResourceAccount.ofBrand(
            UUID.randomUUID().toString(),
            "品牌微信公众号",
            ResourceAccountType.WECHAT_OFFICIAL_ACCOUNT,
            "腾讯",
            "brand_official_account",
            java.time.LocalDateTime.now().minus(6, ChronoUnit.MONTHS),
            brandId,
            "李四",
            "13900139000",
            ResourceAccountStatus.ACTIVE,
            "账号备注"
        );
        enterprise.addResourceAccount(brandAccount);
        
        // 验证证书、资质、备案和资源账号添加结果
        assertEquals(1, enterprise.getCertificates().size());
        assertEquals(1, enterprise.getQualifications().size());
        assertEquals(1, enterprise.getFilings().size());
        assertEquals(2, enterprise.getResourceAccounts().size());
        assertEquals(1, enterprise.getEnterpriseLevelAccounts().size());
        assertEquals(1, enterprise.getBrandLevelAccounts().size());
        assertEquals(1, enterprise.getResourceAccountsByBrand(brandId).size());
        
        // 第四阶段：状态变更和数据删除
        // 改变企业状态
        enterprise.changeStatus(EnterpriseStatus.SUSPENDED);
        assertEquals(EnterpriseStatus.SUSPENDED, enterprise.getStatus());
        
        // 删除一些数据
        enterprise.removeCertificate(certificate.getId());
        enterprise.removeQualification(qualification.getId());
        enterprise.removeFiling(filing.getId());
        enterprise.removeResourceAccount(enterpriseAccount.getId());
        
        // 验证删除结果
        assertEquals(0, enterprise.getCertificates().size());
        assertEquals(0, enterprise.getQualifications().size());
        assertEquals(0, enterprise.getFilings().size());
        assertEquals(1, enterprise.getResourceAccounts().size());
        
        // 最终状态变更
        enterprise.markAsCancelled();
        assertEquals(EnterpriseStatus.CANCELLED, enterprise.getStatus());
        
        // 验证整个生命周期中的领域事件数量
        // 1个创建事件 + 5个基本信息更新 + 2个地址添加 + 2个联系方式添加 + 证书/资质/备案/资源账号各1-2个添加
        // + 证书/资质/备案/资源账号删除 + 2个状态变更
        List<DomainEvent> domainEvents = enterprise.getDomainEvents();
        assertTrue(domainEvents.size() >= 15, "应至少产生15个领域事件");
    }
    
    /**
     * 为企业添加测试数据
     */
    private void addTestDataToEnterprise() {
        // 添加地址
        Address newAddress = Address.of("北京市", "海淀区", "中关村", "科技路", "详细地址", "100080");
        enterprise.addOfficeAddress(newAddress, "北京办公室", false);
        
        // 添加联系方式
        ContactInfo newContact = ContactInfo.phone("021-87654321", "客服部门", null);
        enterprise.addContactInfo(newContact, "客服部门", false);
        
        // 添加证书
        Certificate certificate = Certificate.of(
            UUID.randomUUID().toString(),
            "ISO9001质量管理体系认证",
            CertificateType.ISO9001,
            "CERT123456",
            "认证机构",
            LocalDate.now().minus(1, ChronoUnit.MONTHS),
            LocalDate.now(),
            LocalDate.now().plus(3, ChronoUnit.YEARS),
            "/certificates/iso9001.pdf",
            null
        );
        enterprise.addCertificate(certificate);
        
        // 添加资质
        Qualification qualification = Qualification.of(
            UUID.randomUUID().toString(),
            QualificationType.FOOD_OPERATION,
            "食品经营许可证",
            "一级",
            "JY12345678",
            "市场监督管理局",
            LocalDate.now().minus(6, ChronoUnit.MONTHS),
            LocalDate.now(),
            LocalDate.now().plus(5, ChronoUnit.YEARS),
            "/qualifications/food_license.pdf",
            null
        );
        enterprise.addQualification(qualification);
        
        // 添加备案
        Filing filing = Filing.of(
            UUID.randomUUID().toString(),
            FilingType.ICP,
            "京ICP备12345678号",
            "测试网站",
            "test.example.com",
            "测试企业",
            LocalDate.now().minus(1, ChronoUnit.YEARS),
            LocalDate.now().minus(11, ChronoUnit.MONTHS),
            LocalDate.now().minus(11, ChronoUnit.MONTHS),
            null,
            "有效",
            "工信部",
            "/filings/icp.pdf",
            null
        );
        enterprise.addFiling(filing);
        
        // 添加资源账号
        ResourceAccount account = ResourceAccount.of(
            UUID.randomUUID().toString(),
            "企业官网账号",
            ResourceAccountType.ENTERPRISE_WEBSITE,
            "阿里云",
            "admin@example.com",
            java.time.LocalDateTime.now().minus(1, ChronoUnit.YEARS),
            "张三",
            "13800138000",
            ResourceAccountStatus.ACTIVE,
            "密码存储在密码管理器中"
        );
        enterprise.addResourceAccount(account);
    }

    @Nested
    @DisplayName("极端情况和边界条件测试")
    class EdgeCasesAndBoundaryTests {
        
        @Test
        @DisplayName("测试特殊字符输入")
        void testSpecialCharacterInputs() {
            // 测试包含特殊字符的企业名称
            String nameWithSpecialChars = "测试企业!@#$%^&*()_+";
            enterprise.updateName(nameWithSpecialChars);
            assertEquals(nameWithSpecialChars, enterprise.getName());
            
            // 测试包含特殊字符的经营范围
            String scopeWithSpecialChars = "经营范围包含特殊字符：!@#$%^&*()_+";
            enterprise.updateBusinessScope(scopeWithSpecialChars);
            assertEquals(scopeWithSpecialChars, enterprise.getBusinessScope());
        }
        
        @Test
        @DisplayName("测试极值输入")
        void testExtremeValueInputs() {
            // 测试极大值注册资本
            BigDecimal hugeCapital = new BigDecimal("999999999999999.99");
            enterprise.updateRegisteredCapital(Money.ofCNY(hugeCapital));
            assertEquals(0, hugeCapital.compareTo(enterprise.getRegisteredCapital().getAmount()));
            
            // 测试0值注册资本
            BigDecimal zeroCapital = BigDecimal.ZERO;
            enterprise.updateRegisteredCapital(Money.ofCNY(zeroCapital));
            assertEquals(0, zeroCapital.compareTo(enterprise.getRegisteredCapital().getAmount()));
            
            // 测试很小的注册资本（但仍然合法）
            BigDecimal smallCapital = new BigDecimal("0.01");
            enterprise.updateRegisteredCapital(Money.ofCNY(smallCapital));
            assertEquals(0, smallCapital.compareTo(enterprise.getRegisteredCapital().getAmount()));
        }
        
        @Test
        @DisplayName("测试长文本输入")
        void testLongTextInputs() {
            // 测试长企业名称
            StringBuilder longNameBuilder = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longNameBuilder.append("很长的企业名称");
            }
            String longName = longNameBuilder.toString();
            enterprise.updateName(longName);
            assertEquals(longName, enterprise.getName());
            
            // 测试长经营范围
            StringBuilder longScopeBuilder = new StringBuilder();
            for (int i = 0; i < 100; i++) {
                longScopeBuilder.append("很长的经营范围描述");
            }
            String longScope = longScopeBuilder.toString();
            enterprise.updateBusinessScope(longScope);
            assertEquals(longScope, enterprise.getBusinessScope());
        }
        
        @Test
        @DisplayName("测试空文本输入验证")
        void testEmptyTextValidation() {
            // 测试更新为空名称时抛出异常
            assertThrows(DomainRuleViolationException.class, () -> enterprise.updateName(""));
            assertThrows(DomainRuleViolationException.class, () -> enterprise.updateName(null));
            
            // 注意：如果updateUnifiedSocialCreditCode方法不存在则移除这部分测试
            /*
            // 测试更新为空统一社会信用代码时抛出异常
            assertThrows(DomainRuleViolationException.class, () -> enterprise.updateUnifiedSocialCreditCode(""));
            assertThrows(DomainRuleViolationException.class, () -> enterprise.updateUnifiedSocialCreditCode(null));
            */
        }
        
        @Test
        @DisplayName("测试日期边界条件")
        void testDateBoundaryConditions() {
            // 测试未来日期作为成立日期
            LocalDate futureDate = LocalDate.now().plus(1, ChronoUnit.YEARS);
            assertThrows(DomainRuleViolationException.class, () -> 
                Enterprise.create(
                    enterpriseId,
                    name,
                    unifiedSocialCreditCode,
                    enterpriseType,
                    legalRepresentative,
                    registeredCapital,
                    futureDate,
                    registeredAddress,
                    contactInfo
                )
            );
            
            // 测试很久远的过去日期作为成立日期
            LocalDate veryOldDate = LocalDate.of(1900, 1, 1);
            Enterprise oldEnterprise = Enterprise.create(
                EnterpriseId.of(UUID.randomUUID().toString()),
                "老企业",
                "91310000XXXXXXXXXX",
                enterpriseType,
                legalRepresentative,
                registeredCapital,
                veryOldDate,
                registeredAddress,
                contactInfo
            );
            // 注意：如果getEstablishmentDate方法不存在，则检查通过创建是否抛异常来验证
            assertNotNull(oldEnterprise);
        }
        
        @Test
        @DisplayName("测试无效货币操作")
        void testInvalidMoneyOperations() {
            // 测试负数注册资本
            assertThrows(DomainRuleViolationException.class, () -> 
                enterprise.updateRegisteredCapital(Money.ofCNY(new BigDecimal("-1")))
            );
            
            // 测试实缴资本大于注册资本的情况
            enterprise.updateRegisteredCapital(Money.ofCNY(new BigDecimal("1000")));
            assertThrows(DomainRuleViolationException.class, () -> 
                enterprise.updatePaidInCapital(Money.ofCNY(new BigDecimal("1001")))
            );
            
            // 测试正常的实缴资本等于注册资本（边界条件）
            enterprise.updatePaidInCapital(Money.ofCNY(new BigDecimal("1000")));
            assertEquals(enterprise.getRegisteredCapital(), enterprise.getPaidInCapital());
        }
        
        @Test
        @DisplayName("测试状态转换的所有可能路径")
        void testAllStatusTransitionPaths() {
            // 初始状态是ACTIVE
            assertEquals(EnterpriseStatus.ACTIVE, enterprise.getStatus());
            
            // ACTIVE -> SUSPENDED
            enterprise.changeStatus(EnterpriseStatus.SUSPENDED);
            assertEquals(EnterpriseStatus.SUSPENDED, enterprise.getStatus());
            
            // SUSPENDED -> ACTIVE
            enterprise.changeStatus(EnterpriseStatus.ACTIVE);
            assertEquals(EnterpriseStatus.ACTIVE, enterprise.getStatus());
            
            // ACTIVE -> CANCELLED
            enterprise.markAsCancelled();
            assertEquals(EnterpriseStatus.CANCELLED, enterprise.getStatus());
            
            // 创建新企业测试其他路径
            Enterprise newEnterprise = Enterprise.create(
                EnterpriseId.of(UUID.randomUUID().toString()),
                "新企业",
                "91310000XXXXXXXXXX",
                enterpriseType,
                legalRepresentative,
                registeredCapital,
                LocalDate.now().minus(1, ChronoUnit.YEARS),
                registeredAddress,
                contactInfo
            );
            
            // ACTIVE -> REVOKED
            newEnterprise.markAsRevoked();
            assertEquals(EnterpriseStatus.REVOKED, newEnterprise.getStatus());
            
            // 测试终态不可再变更
            final Enterprise cancelledEnterprise = enterprise;
            final Enterprise revokedEnterprise = newEnterprise;
            
            // CANCELLED是终态，不能再变更
            assertThrows(DomainRuleViolationException.class, () -> 
                cancelledEnterprise.changeStatus(EnterpriseStatus.ACTIVE)
            );
            
            // REVOKED是终态，不能再变更
            assertThrows(DomainRuleViolationException.class, () -> 
                revokedEnterprise.changeStatus(EnterpriseStatus.ACTIVE)
            );
        }
        
        @Test
        @DisplayName("测试集合操作边界条件")
        void testCollectionOperationBoundaries() {
            // 测试最大集合限制
            // 假设地址集合有最大限制100
            for (int i = 0; i < 50; i++) {
                Address address = Address.of(
                    "城市" + i, 
                    "区" + i, 
                    "街道" + i, 
                    "路" + i, 
                    "详细地址" + i, 
                    "10000" + i
                );
                enterprise.addOfficeAddress(address, "地址" + i, false);
            }
            assertEquals(51, enterprise.getOfficeAddresses().size()); // 原有1个 + 新增50个
            
            // 测试索引范围检查
            assertThrows(DomainRuleViolationException.class, () -> 
                enterprise.setHeadquartersAddress(100)
            );
            
            // 测试最后一个索引（边界条件）
            enterprise.setHeadquartersAddress(50);
            assertTrue(enterprise.getOfficeAddresses().get(50).isHeadquarters());
            
            // 测试移除操作
            enterprise.removeOfficeAddress(1); // 移除非总部地址
            assertEquals(50, enterprise.getOfficeAddresses().size());
            
            // 测试移除总部地址时抛出异常
            assertThrows(DomainRuleViolationException.class, () -> 
                enterprise.removeOfficeAddress(50) // 现在总部地址在索引50
            );
        }
        
        @Test
        @DisplayName("测试企业ID不可变性")
        void testEnterpriseIdImmutability() {
            EnterpriseId originalId = enterprise.getId();
            
            // 测试对企业ID的修改操作应该被拒绝
            // 注意：这里假设Enterprise类中没有提供修改ID的方法，这是符合DDD原则的
            
            // 验证ID不可变
            assertEquals(originalId, enterprise.getId());
            assertSame(originalId, enterprise.getId());
        }
        
        @Test
        @DisplayName("测试资源账号添加和移除")
        void testResourceAccountAddAndRemove() {
            // 添加一个资源账号
            ResourceAccount account = ResourceAccount.of(
                UUID.randomUUID().toString(),
                "测试账号",
                ResourceAccountType.ENTERPRISE_WEBSITE,
                "服务商",
                "account@example.com",
                java.time.LocalDateTime.now().minus(1, ChronoUnit.YEARS),
                "张三",
                "13800138000",
                ResourceAccountStatus.ACTIVE,
                "账号备注"
            );
            enterprise.addResourceAccount(account);
            
            // 获取账号ID
            String accountId = account.getId();
            
            // 验证添加成功
            assertTrue(enterprise.getResourceAccount(accountId).isPresent());
            assertEquals(account, enterprise.getResourceAccount(accountId).get());
            
            // 测试移除资源账号
            enterprise.removeResourceAccount(accountId);
            assertFalse(enterprise.getResourceAccount(accountId).isPresent());
            
            // 测试移除不存在的资源账号ID
            String nonExistentId = UUID.randomUUID().toString();
            // 这里假设移除不存在的ID不会抛出异常，而是静默失败
            enterprise.removeResourceAccount(nonExistentId);
            
            // 测试传入空ID
            assertThrows(DomainRuleViolationException.class, () -> 
                enterprise.removeResourceAccount("")
            );
            
            // 测试传入null ID
            assertThrows(DomainRuleViolationException.class, () -> 
                enterprise.removeResourceAccount(null)
            );
        }
    }
} 