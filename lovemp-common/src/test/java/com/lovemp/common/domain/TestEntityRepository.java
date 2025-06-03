package com.lovemp.common.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * 用于测试的TestEntity仓储接口
 */
@Repository
public interface TestEntityRepository extends JpaRepository<TestEntity, String> {
} 