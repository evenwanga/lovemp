package com.lovemp.common.domain;

import java.util.Objects;

/**
 * 领域实体基类
 * 
 * @param <ID> 实体标识类型
 */
public abstract class Entity<ID> {
    
    protected ID id;
    
    /**
     * 获取实体ID
     * 
     * @return 实体ID
     */
    public ID getId() {
        return id;
    }
    
    /**
     * 判断实体是否相等，基于实体ID比较
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Entity<?> entity = (Entity<?>) o;
        return Objects.equals(id, entity.id);
    }
    
    /**
     * 生成实体的哈希码，基于实体ID
     */
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
} 