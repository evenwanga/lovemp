package com.lovemp.common.test.fixtures;

import com.lovemp.common.domain.TestEntity;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * AbstractJpaEntity测试数据工厂
 */
public class AbstractJpaEntityFixture {

    /**
     * 创建用于测试的具体实体实现
     */
    public static TestEntity createTestEntity() {
        return TestEntity.of(
            UUID.randomUUID().toString(),
            "测试实体"
        );
    }
    
    /**
     * 创建带有审计信息的测试实体
     */
    public static TestEntity createTestEntityWithAuditInfo() {
        TestEntity entity = createTestEntity();
        LocalDateTime now = LocalDateTime.now();
        String testUser = "test-user";
        
        // 由于实际环境中依赖Spring的AuditingEntityListener，
        // 在测试中手动设置审计字段用于测试
        ReflectionTestUtils.setField(entity, "createdAt", now);
        ReflectionTestUtils.setField(entity, "createdBy", testUser);
        ReflectionTestUtils.setField(entity, "version", 0L);
        
        return entity;
    }
    
    /**
     * 创建已删除的测试实体
     */
    public static TestEntity createDeletedTestEntity() {
        TestEntity entity = createTestEntityWithAuditInfo();
        entity.markAsDeleted("test-admin");
        return entity;
    }
    
    /**
     * 创建已更新的测试实体
     */
    public static TestEntity createUpdatedTestEntity() {
        TestEntity entity = createTestEntityWithAuditInfo();
        LocalDateTime updateTime = LocalDateTime.now().plusHours(1);
        String updateUser = "update-user";
        
        ReflectionTestUtils.setField(entity, "updatedAt", updateTime);
        ReflectionTestUtils.setField(entity, "updatedBy", updateUser);
        ReflectionTestUtils.setField(entity, "version", 1L);
        
        return entity;
    }
} 