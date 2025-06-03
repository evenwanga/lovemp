package com.lovemp.common.domain;

import java.io.Serializable;
import java.util.Objects;

/**
 * 标识符值对象基类
 * 用于实现复杂领域标识符
 *
 * @param <T> 标识符的值类型
 */
public abstract class Identifier<T extends Serializable> implements ValueObject, Serializable {

    private static final long serialVersionUID = 1L;

    private final T value;

    /**
     * 构造函数
     *
     * @param value 标识符值
     */
    protected Identifier(T value) {
        this.value = Objects.requireNonNull(value, "标识符值不能为空");
    }

    /**
     * 获取标识符的值
     *
     * @return 标识符值
     */
    public T getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        
        Identifier<?> that = (Identifier<?>) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
} 