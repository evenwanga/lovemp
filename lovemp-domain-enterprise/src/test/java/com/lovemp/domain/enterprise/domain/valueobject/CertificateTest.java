package com.lovemp.domain.enterprise.domain.valueobject;

import com.lovemp.common.exception.DomainRuleViolationException;
import com.lovemp.domain.enterprise.domain.model.valueobject.Certificate;
import com.lovemp.domain.enterprise.domain.model.valueobject.CertificateType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Certificate值对象的单元测试
 */
@DisplayName("证书值对象测试")
class CertificateTest {

    @Test
    @DisplayName("正常创建证书")
    void shouldCreateCertificate() {
        String id = UUID.randomUUID().toString();
        String name = "ISO9001质量管理体系认证";
        CertificateType type = CertificateType.ISO9001;
        String certificateNumber = "CERT123456";
        String issuingAuthority = "认证机构";
        LocalDate issueDate = LocalDate.now().minus(1, ChronoUnit.MONTHS);
        LocalDate validFromDate = LocalDate.now();
        LocalDate validToDate = LocalDate.now().plus(3, ChronoUnit.YEARS);
        String filePath = "/certificates/iso9001.pdf";
        
        Certificate certificate = Certificate.of(
            id, 
            name, 
            type, 
            certificateNumber, 
            issuingAuthority, 
            issueDate, 
            validFromDate, 
            validToDate, 
            filePath, 
            null
        );
        
        assertEquals(id, certificate.getId());
        assertEquals(name, certificate.getName());
        assertEquals(type, certificate.getType());
        assertEquals(certificateNumber, certificate.getCertificateNumber());
        assertEquals(issuingAuthority, certificate.getIssuingAuthority());
        assertEquals(issueDate, certificate.getIssueDate());
        assertEquals(validFromDate, certificate.getValidFromDate());
        assertEquals(validToDate, certificate.getValidToDate());
        assertEquals(filePath, certificate.getFilePath());
        assertNull(certificate.getRemark());
    }
    
    @Test
    @DisplayName("创建长期有效证书")
    void shouldCreatePermanentCertificate() {
        String id = UUID.randomUUID().toString();
        String name = "ISO9001质量管理体系认证";
        CertificateType type = CertificateType.ISO9001;
        String certificateNumber = "CERT123456";
        String issuingAuthority = "认证机构";
        LocalDate issueDate = LocalDate.now().minus(1, ChronoUnit.MONTHS);
        LocalDate validFromDate = LocalDate.now();
        
        Certificate certificate = Certificate.of(
            id, 
            name, 
            type, 
            certificateNumber, 
            issuingAuthority, 
            issueDate, 
            validFromDate, 
            null,  // 无结束日期表示长期有效
            "/certificates/iso9001.pdf", 
            "长期有效证书"
        );
        
        assertNull(certificate.getValidToDate());
        assertEquals("长期有效证书", certificate.getRemark());
    }
    
    @Test
    @DisplayName("创建证书参数验证")
    void shouldValidateParameters() {
        String id = UUID.randomUUID().toString();
        String name = "ISO9001质量管理体系认证";
        CertificateType type = CertificateType.ISO9001;
        String certificateNumber = "CERT123456";
        String issuingAuthority = "认证机构";
        LocalDate issueDate = LocalDate.now().minus(1, ChronoUnit.MONTHS);
        LocalDate validFromDate = LocalDate.now();
        LocalDate validToDate = LocalDate.now().plus(3, ChronoUnit.YEARS);
        String filePath = "/certificates/iso9001.pdf";
        
        // 证书名称为空
        Exception exception = assertThrows(DomainRuleViolationException.class, () ->
            Certificate.of(
                id, 
                null, 
                type, 
                certificateNumber, 
                issuingAuthority, 
                issueDate, 
                validFromDate, 
                validToDate, 
                filePath, 
                null
            )
        );
        assertTrue(exception.getMessage().contains("证书名称不能为空"));
        
        // 证书类型为空
        exception = assertThrows(DomainRuleViolationException.class, () ->
            Certificate.of(
                id, 
                name, 
                null, 
                certificateNumber, 
                issuingAuthority, 
                issueDate, 
                validFromDate, 
                validToDate, 
                filePath, 
                null
            )
        );
        assertTrue(exception.getMessage().contains("证书类型不能为空"));
        
        // 证书编号为空
        exception = assertThrows(DomainRuleViolationException.class, () ->
            Certificate.of(
                id, 
                name, 
                type, 
                null, 
                issuingAuthority, 
                issueDate, 
                validFromDate, 
                validToDate, 
                filePath, 
                null
            )
        );
        assertTrue(exception.getMessage().contains("证书编号不能为空"));
    }
    
    @Test
    @DisplayName("判断证书是否过期")
    void shouldCheckExpiration() {
        String id = UUID.randomUUID().toString();
        
        // 有效证书
        Certificate validCertificate = Certificate.of(
            id, 
            "有效证书", 
            CertificateType.ISO9001, 
            "CERT123456", 
            "认证机构", 
            LocalDate.now().minus(2, ChronoUnit.MONTHS), 
            LocalDate.now().minus(1, ChronoUnit.MONTHS), 
            LocalDate.now().plus(1, ChronoUnit.YEARS), 
            "/certificates/valid.pdf", 
            null
        );
        
        assertFalse(validCertificate.isExpired());
        assertTrue(validCertificate.isValid());
        
        // 过期证书
        Certificate expiredCertificate = Certificate.of(
            id, 
            "过期证书", 
            CertificateType.ISO9001, 
            "CERT654321", 
            "认证机构", 
            LocalDate.now().minus(3, ChronoUnit.YEARS), 
            LocalDate.now().minus(3, ChronoUnit.YEARS), 
            LocalDate.now().minus(1, ChronoUnit.DAYS), 
            "/certificates/expired.pdf", 
            null
        );
        
        assertTrue(expiredCertificate.isExpired());
        assertFalse(expiredCertificate.isValid());
        
        // 长期有效证书
        Certificate permanentCertificate = Certificate.of(
            id, 
            "长期有效证书", 
            CertificateType.ISO9001, 
            "CERT789012", 
            "认证机构", 
            LocalDate.now().minus(1, ChronoUnit.YEARS), 
            LocalDate.now().minus(1, ChronoUnit.YEARS), 
            null, 
            "/certificates/permanent.pdf", 
            null
        );
        
        assertFalse(permanentCertificate.isExpired());
        assertTrue(permanentCertificate.isValid());
        assertEquals(Integer.MAX_VALUE, permanentCertificate.getRemainingDays());
    }
    
    @Test
    @DisplayName("判断证书是否即将过期")
    void shouldCheckExpiringsSoon() {
        String id = UUID.randomUUID().toString();
        
        // 即将过期证书（30天内）
        Certificate expiringSoonCertificate = Certificate.of(
            id, 
            "即将过期证书", 
            CertificateType.ISO9001, 
            "CERT123456", 
            "认证机构", 
            LocalDate.now().minus(2, ChronoUnit.YEARS), 
            LocalDate.now().minus(2, ChronoUnit.YEARS), 
            LocalDate.now().plus(15, ChronoUnit.DAYS), 
            "/certificates/expiring.pdf", 
            null
        );
        
        assertTrue(expiringSoonCertificate.isExpiringsSoon(30));
        assertFalse(expiringSoonCertificate.isExpiringsSoon(10));
        
        // 长期有效证书
        Certificate permanentCertificate = Certificate.of(
            id, 
            "长期有效证书", 
            CertificateType.ISO9001, 
            "CERT789012", 
            "认证机构", 
            LocalDate.now().minus(1, ChronoUnit.YEARS), 
            LocalDate.now().minus(1, ChronoUnit.YEARS), 
            null, 
            "/certificates/permanent.pdf", 
            null
        );
        
        assertFalse(permanentCertificate.isExpiringsSoon(30));
    }
} 