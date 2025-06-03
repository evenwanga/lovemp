package com.lovemp.common.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * 用于测试的JPA实体类
 */
@Entity
@Table(name = "test_entity")
@Getter
@Setter
@NoArgsConstructor
public class TestEntity extends AbstractJpaEntity {
    @Id
    private String id;
    
    private String name;
    
    /**
     * 创建测试实体
     * 
     * @param id 实体ID
     * @param name 实体名称
     * @return 测试实体
     */
    public static TestEntity of(String id, String name) {
        TestEntity entity = new TestEntity();
        entity.setId(id);
        entity.setName(name);
        return entity;
    }
} 