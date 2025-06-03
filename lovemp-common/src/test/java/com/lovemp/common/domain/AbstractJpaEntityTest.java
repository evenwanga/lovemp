package com.lovemp.common.domain;

import com.lovemp.common.test.fixtures.AbstractJpaEntityFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JPA实体抽象基类测试
 */
@DisplayName("JPA实体抽象基类测试")
class AbstractJpaEntityTest {

    @Test
    @DisplayName("测试逻辑删除功能")
    void testMarkAsDeleted() {
        // 准备数据
        TestEntity entity = AbstractJpaEntityFixture.createTestEntity();
        assertFalse(entity.isDeleted());
        assertNull(entity.getDeletedAt());
        assertNull(entity.getDeletedBy());
        
        // 执行方法
        String deletedBy = "admin-user";
        entity.markAsDeleted(deletedBy);
        
        // 验证结果
        assertTrue(entity.isDeleted());
        assertNotNull(entity.getDeletedAt());
        assertEquals(deletedBy, entity.getDeletedBy());
    }
    
    @Test
    @DisplayName("测试预创建的已删除实体")
    void testPreCreatedDeletedEntity() {
        // 使用测试工厂创建已删除的实体
        TestEntity deletedEntity = AbstractJpaEntityFixture.createDeletedTestEntity();
        
        // 验证实体状态
        assertTrue(deletedEntity.isDeleted());
        assertNotNull(deletedEntity.getDeletedAt());
        assertEquals("test-admin", deletedEntity.getDeletedBy());
    }
    
    @Test
    @DisplayName("测试审计字段行为")
    void testAuditFields() {
        // 准备数据 - 使用测试工厂
        TestEntity entity = AbstractJpaEntityFixture.createTestEntityWithAuditInfo();
        
        // 验证结果
        assertNotNull(entity.getCreatedAt());
        assertEquals("test-user", entity.getCreatedBy());
        assertEquals(0L, entity.getVersion());
        
        // 获取原始创建时间以便后续比较
        LocalDateTime originalCreatedAt = entity.getCreatedAt();
        
        // 模拟更新
        LocalDateTime updateTime = LocalDateTime.now().plusHours(1);
        String updateUser = "update-user";
        ReflectionTestUtils.setField(entity, "updatedAt", updateTime);
        ReflectionTestUtils.setField(entity, "updatedBy", updateUser);
        ReflectionTestUtils.setField(entity, "version", 1L);
        
        // 验证更新后结果
        assertEquals(originalCreatedAt, entity.getCreatedAt()); // 创建时间不变
        assertEquals("test-user", entity.getCreatedBy()); // 创建人不变
        assertEquals(updateTime, entity.getUpdatedAt());
        assertEquals(updateUser, entity.getUpdatedBy());
        assertEquals(1L, entity.getVersion());
    }
    
    @Test
    @DisplayName("测试已更新实体的审计信息")
    void testUpdatedEntityAuditInfo() {
        // 使用测试工厂创建已更新的实体
        TestEntity updatedEntity = AbstractJpaEntityFixture.createUpdatedTestEntity();
        
        // 验证实体状态
        assertNotNull(updatedEntity.getCreatedAt());
        assertNotNull(updatedEntity.getUpdatedAt());
        assertTrue(updatedEntity.getUpdatedAt().isAfter(updatedEntity.getCreatedAt()));
        assertEquals("test-user", updatedEntity.getCreatedBy());
        assertEquals("update-user", updatedEntity.getUpdatedBy());
        assertEquals(1L, updatedEntity.getVersion());
    }
    
    @Test
    @DisplayName("测试创建新实体时审计字段为空")
    void testNewEntityHasNullFields() {
        // 准备数据 - 创建全新实体
        TestEntity entity = new TestEntity();
        
        // 验证审计字段初始为null
        assertNull(entity.getVersion());
        assertNull(entity.getCreatedAt());
        assertNull(entity.getCreatedBy());
        assertNull(entity.getUpdatedAt());
        assertNull(entity.getUpdatedBy());
        assertFalse(entity.isDeleted());
    }
    
    @Test
    @DisplayName("测试实体删除再恢复")
    void testEntityDeleteAndRestore() {
        // 创建实体并删除
        TestEntity entity = AbstractJpaEntityFixture.createTestEntityWithAuditInfo();
        entity.markAsDeleted("admin");
        assertTrue(entity.isDeleted());
        
        // 模拟恢复操作 (通过反射，因为实际类中没有恢复方法)
        ReflectionTestUtils.setField(entity, "deleted", false);
        ReflectionTestUtils.setField(entity, "deletedAt", null);
        ReflectionTestUtils.setField(entity, "deletedBy", null);
        
        // 验证恢复结果
        assertFalse(entity.isDeleted());
        assertNull(entity.getDeletedAt());
        assertNull(entity.getDeletedBy());
    }
} 