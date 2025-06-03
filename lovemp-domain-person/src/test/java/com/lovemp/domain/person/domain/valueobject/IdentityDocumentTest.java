package com.lovemp.domain.person.domain.valueobject;

import com.lovemp.domain.person.domain.model.valueobject.IdentityDocument;
import com.lovemp.domain.person.domain.model.valueobject.IdentityDocumentStatus;
import com.lovemp.domain.person.domain.model.valueobject.IdentityDocumentType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("证件信息值对象测试")
class IdentityDocumentTest {

    @Test
    @DisplayName("测试创建身份证信息")
    void testCreateIdCard() {
        // 创建身份证信息
        IdentityDocument idCard = IdentityDocument.ofIdCard(
                "110101199001011234",
                "北京市公安局",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2040, 1, 1)
        );
        
        // 验证结果
        assertEquals(IdentityDocumentType.ID_CARD, idCard.getType());
        assertEquals("110101199001011234", idCard.getNumber());
        assertEquals("北京市公安局", idCard.getIssuingAuthority());
        assertEquals(LocalDate.of(2020, 1, 1), idCard.getValidFrom());
        assertEquals(LocalDate.of(2040, 1, 1), idCard.getValidUntil());
        assertEquals(IdentityDocumentStatus.VALID, idCard.getStatus());
        assertNull(idCard.getFrontImageUrl());
        assertNull(idCard.getBackImageUrl());
    }
    
    @Test
    @DisplayName("测试创建带影像链接的身份证信息")
    void testCreateIdCardWithImages() {
        // 创建带影像链接的身份证信息
        IdentityDocument idCard = IdentityDocument.ofIdCardWithImages(
                "110101199001011234",
                "北京市公安局",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2040, 1, 1),
                "https://example.com/front.jpg",
                "https://example.com/back.jpg"
        );
        
        // 验证结果
        assertEquals(IdentityDocumentType.ID_CARD, idCard.getType());
        assertEquals("110101199001011234", idCard.getNumber());
        assertEquals("北京市公安局", idCard.getIssuingAuthority());
        assertEquals(LocalDate.of(2020, 1, 1), idCard.getValidFrom());
        assertEquals(LocalDate.of(2040, 1, 1), idCard.getValidUntil());
        assertEquals(IdentityDocumentStatus.VALID, idCard.getStatus());
        assertEquals("https://example.com/front.jpg", idCard.getFrontImageUrl());
        assertEquals("https://example.com/back.jpg", idCard.getBackImageUrl());
    }
    
    @Test
    @DisplayName("测试创建护照信息")
    void testCreatePassport() {
        // 创建护照信息
        IdentityDocument passport = IdentityDocument.ofPassport(
                "P12345678",
                "北京市出入境管理局",
                LocalDate.of(2022, 1, 1),
                LocalDate.of(2032, 1, 1)
        );
        
        // 验证结果
        assertEquals(IdentityDocumentType.PASSPORT, passport.getType());
        assertEquals("P12345678", passport.getNumber());
        assertEquals("北京市出入境管理局", passport.getIssuingAuthority());
        assertEquals(LocalDate.of(2022, 1, 1), passport.getValidFrom());
        assertEquals(LocalDate.of(2032, 1, 1), passport.getValidUntil());
        assertEquals(IdentityDocumentStatus.VALID, passport.getStatus());
        assertNull(passport.getFrontImageUrl());
        assertNull(passport.getBackImageUrl());
    }
    
    @Test
    @DisplayName("测试证件有效性检查")
    void testIsValid() {
        // 创建有效证件
        LocalDate now = LocalDate.now();
        IdentityDocument validDoc = IdentityDocument.ofIdCard(
                "110101199001011234",
                "北京市公安局",
                now.minusYears(1),
                now.plusYears(1)
        );
        
        // 创建过期证件
        IdentityDocument expiredDoc = IdentityDocument.ofIdCard(
                "110101199001011234",
                "北京市公安局",
                now.minusYears(2),
                now.minusYears(1)
        );
        
        // 创建未生效证件
        IdentityDocument futureDoc = IdentityDocument.ofIdCard(
                "110101199001011234",
                "北京市公安局",
                now.plusYears(1),
                now.plusYears(2)
        );
        
        // 创建已作废证件
        IdentityDocument cancelledDoc = IdentityDocument.ofIdCard(
                "110101199001011234",
                "北京市公安局",
                now.minusYears(1),
                now.plusYears(1)
        ).withStatus(IdentityDocumentStatus.CANCELLED);
        
        // 验证结果
        assertTrue(validDoc.isValid());
        assertFalse(expiredDoc.isValid());
        assertFalse(futureDoc.isValid());
        assertFalse(cancelledDoc.isValid());
    }
    
    @Test
    @DisplayName("测试更新证件状态")
    void testUpdateStatus() {
        // 创建证件
        IdentityDocument doc = IdentityDocument.ofIdCard(
                "110101199001011234",
                "北京市公安局",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2040, 1, 1)
        );
        
        // 更新状态
        IdentityDocument cancelledDoc = doc.withStatus(IdentityDocumentStatus.CANCELLED);
        
        // 验证结果
        assertEquals(IdentityDocumentStatus.VALID, doc.getStatus()); // 原对象不变
        assertEquals(IdentityDocumentStatus.CANCELLED, cancelledDoc.getStatus());
        
        // 其他属性保持不变
        assertEquals(doc.getType(), cancelledDoc.getType());
        assertEquals(doc.getNumber(), cancelledDoc.getNumber());
        assertEquals(doc.getIssuingAuthority(), cancelledDoc.getIssuingAuthority());
        assertEquals(doc.getValidFrom(), cancelledDoc.getValidFrom());
        assertEquals(doc.getValidUntil(), cancelledDoc.getValidUntil());
    }
    
    @Test
    @DisplayName("测试更新证件有效期")
    void testUpdateValidUntil() {
        // 创建证件
        IdentityDocument doc = IdentityDocument.ofIdCard(
                "110101199001011234",
                "北京市公安局",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2040, 1, 1)
        );
        
        // 更新有效期
        LocalDate newValidUntil = LocalDate.of(2050, 1, 1);
        IdentityDocument updatedDoc = doc.withValidUntil(newValidUntil);
        
        // 验证结果
        assertEquals(LocalDate.of(2040, 1, 1), doc.getValidUntil()); // 原对象不变
        assertEquals(newValidUntil, updatedDoc.getValidUntil());
    }
    
    @Test
    @DisplayName("测试更新证件影像链接")
    void testUpdateImages() {
        // 创建不带影像的证件
        IdentityDocument doc = IdentityDocument.ofIdCard(
                "110101199001011234",
                "北京市公安局",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2040, 1, 1)
        );
        assertNull(doc.getFrontImageUrl());
        assertNull(doc.getBackImageUrl());
        
        // 1. 同时更新两个影像链接
        IdentityDocument withImages = doc.withImages(
                "https://example.com/front.jpg",
                "https://example.com/back.jpg"
        );
        
        // 验证结果
        assertEquals("https://example.com/front.jpg", withImages.getFrontImageUrl());
        assertEquals("https://example.com/back.jpg", withImages.getBackImageUrl());
        
        // 2. 只更新正面影像
        IdentityDocument withFrontOnly = doc.withFrontImage("https://example.com/new-front.jpg");
        
        // 验证结果
        assertEquals("https://example.com/new-front.jpg", withFrontOnly.getFrontImageUrl());
        assertNull(withFrontOnly.getBackImageUrl());
        
        // 3. 只更新反面影像
        IdentityDocument withBackOnly = doc.withBackImage("https://example.com/new-back.jpg");
        
        // 验证结果
        assertNull(withBackOnly.getFrontImageUrl());
        assertEquals("https://example.com/new-back.jpg", withBackOnly.getBackImageUrl());
        
        // 4. 链式更新
        IdentityDocument chainedUpdate = doc
                .withFrontImage("https://example.com/front-chained.jpg")
                .withBackImage("https://example.com/back-chained.jpg");
        
        // 验证结果
        assertEquals("https://example.com/front-chained.jpg", chainedUpdate.getFrontImageUrl());
        assertEquals("https://example.com/back-chained.jpg", chainedUpdate.getBackImageUrl());
    }
    
    @Test
    @DisplayName("测试值对象相等性")
    void testEquality() {
        // 创建两个相同的身份证对象
        IdentityDocument doc1 = IdentityDocument.ofIdCardWithImages(
                "110101199001011234",
                "北京市公安局",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2040, 1, 1),
                "https://example.com/front.jpg",
                "https://example.com/back.jpg"
        );
        
        IdentityDocument doc2 = IdentityDocument.ofIdCardWithImages(
                "110101199001011234",
                "北京市公安局",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2040, 1, 1),
                "https://example.com/front.jpg",
                "https://example.com/back.jpg"
        );
        
        // 验证相等性
        assertEquals(doc1, doc2);
        assertEquals(doc1.hashCode(), doc2.hashCode());
        
        // 创建一个属性不同的身份证对象
        IdentityDocument doc3 = IdentityDocument.ofIdCardWithImages(
                "110101199001011234",
                "北京市公安局",
                LocalDate.of(2020, 1, 1),
                LocalDate.of(2040, 1, 1),
                "https://example.com/front.jpg",
                "https://example.com/different-back.jpg"
        );
        
        // 验证不相等
        assertNotEquals(doc1, doc3);
    }
} 