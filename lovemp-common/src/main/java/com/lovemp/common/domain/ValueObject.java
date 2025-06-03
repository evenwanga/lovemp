package com.lovemp.common.domain;

/**
 * 值对象标记接口
 * 
 * 值对象特点：
 * 1. 不变性：一旦创建，状态不可变
 * 2. 无标识：通过属性整体判断相等性，而非ID
 * 3. 可替换性：可以替换为具有相同属性的另一个实例
 */
public interface ValueObject {
    
    /**
     * 值对象必须实现equals方法，基于属性比较
     */
    @Override
    boolean equals(Object obj);
    
    /**
     * 值对象必须实现hashCode方法，基于属性计算
     */
    @Override
    int hashCode();
} 