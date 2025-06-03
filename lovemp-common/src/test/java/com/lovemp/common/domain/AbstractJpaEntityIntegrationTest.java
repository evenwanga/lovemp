package com.lovemp.common.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * JPA实体抽象基类集成测试
 * 注意：此测试需要在Spring环境中运行，以测试审计功能
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@ContextConfiguration(classes = AbstractJpaEntityIntegrationTest.TestConfig.class)
@ActiveProfiles("test")
@DisplayName("JPA实体抽象基类集成测试")
class AbstractJpaEntityIntegrationTest {

    @Autowired
    private TestEntityManager entityManager;
    
    @Autowired
    private TestEntityRepository repository;
    
    @Test
    @DisplayName("测试审计字段自动填充")
    void testAuditFieldsAutomaticallyFilled() {
        // 准备测试数据
        TestEntity entity = TestEntity.of("test-id", "测试实体");
        
        // 保存实体前记录时间
        LocalDateTime beforeSave = LocalDateTime.now();
        
        // 保存实体
        TestEntity savedEntity = repository.save(entity);
        repository.flush();
        
        // 验证审计字段已自动填充
        assertNotNull(savedEntity.getCreatedAt());
        // 创建时间应该在保存操作之后
        assertFalse(savedEntity.getCreatedAt().isBefore(beforeSave));
        
        // 因为我们配置了测试用的审计提供者，所以这些字段应该有值
        assertNotNull(savedEntity.getCreatedBy());
        assertNotNull(savedEntity.getUpdatedAt());
        assertNotNull(savedEntity.getUpdatedBy());
        assertEquals("test-user", savedEntity.getCreatedBy());
        
        assertEquals(0L, savedEntity.getVersion());
        assertFalse(savedEntity.isDeleted());
    }
    
    @Test
    @DisplayName("测试实体更新时审计字段变化")
    void testAuditFieldsOnUpdate() {
        // 准备并保存测试实体
        TestEntity entity = TestEntity.of("test-update-id", "原始名称");
        TestEntity savedEntity = repository.saveAndFlush(entity);
        
        // 记录创建时的审计信息
        LocalDateTime createdAt = savedEntity.getCreatedAt();
        String createdBy = savedEntity.getCreatedBy();
        
        // 等待一小段时间确保时间戳不同
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            // 忽略中断异常
        }
        
        // 更新实体
        savedEntity.setName("更新后的名称");
        TestEntity updatedEntity = repository.saveAndFlush(savedEntity);
        
        // 验证审计字段
        assertEquals(createdAt, updatedEntity.getCreatedAt()); // 创建时间不变
        assertEquals(createdBy, updatedEntity.getCreatedBy()); // 创建人不变
        
        assertNotNull(updatedEntity.getUpdatedAt());
        assertTrue(updatedEntity.getUpdatedAt().isAfter(createdAt)); // 更新时间晚于创建时间
        assertEquals("test-user", updatedEntity.getUpdatedBy());
        
        assertEquals(1L, updatedEntity.getVersion()); // 版本号增加
    }
    
    @Test
    @DisplayName("测试逻辑删除与数据库交互")
    void testLogicalDeleteWithDatabase() {
        // 准备并保存测试实体
        TestEntity entity = TestEntity.of("test-delete-id", "待删除实体");
        TestEntity savedEntity = repository.saveAndFlush(entity);
        
        // 执行逻辑删除
        savedEntity.markAsDeleted("admin");
        TestEntity deletedEntity = repository.saveAndFlush(savedEntity);
        
        // 验证结果
        assertTrue(deletedEntity.isDeleted());
        assertNotNull(deletedEntity.getDeletedAt());
        assertEquals("admin", deletedEntity.getDeletedBy());
        
        // 从数据库重新加载确认删除状态已保存
        repository.flush();
        entityManager.clear(); // 清除一级缓存
        TestEntity reloadedEntity = repository.findById("test-delete-id").orElse(null);
        
        assertNotNull(reloadedEntity);
        assertTrue(reloadedEntity.isDeleted());
        assertNotNull(reloadedEntity.getDeletedAt());
        assertEquals("admin", reloadedEntity.getDeletedBy());
    }
    
    /**
     * 为测试提供Spring Boot配置和JPA审计配置
     */
    @Configuration
    @EnableJpaAuditing
    @EnableJpaRepositories(basePackages = "com.lovemp.common.domain")
    @EntityScan(basePackages = {"com.lovemp.common.domain"})
    @SpringBootApplication
    static class TestConfig {
        
        /**
         * 提供测试用的审计者信息
         */
        @Bean
        public AuditorAware<String> auditorProvider() {
            return () -> Optional.of("test-user");
        }
    }
} 