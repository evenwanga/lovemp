package com.lovemp.common.domain;

/**
 * 规约接口
 * 用于封装领域规则和业务规则
 *
 * @param <T> 规约应用的对象类型
 */
public interface Specification<T> {

    /**
     * 判断对象是否满足规约
     *
     * @param candidate 待判断对象
     * @return 是否满足规约
     */
    boolean isSatisfiedBy(T candidate);

    /**
     * 与操作，两个规约必须同时满足
     *
     * @param other 另一个规约
     * @return 组合后的规约
     */
    default Specification<T> and(Specification<T> other) {
        return candidate -> isSatisfiedBy(candidate) && other.isSatisfiedBy(candidate);
    }

    /**
     * 或操作，两个规约满足其一即可
     *
     * @param other 另一个规约
     * @return 组合后的规约
     */
    default Specification<T> or(Specification<T> other) {
        return candidate -> isSatisfiedBy(candidate) || other.isSatisfiedBy(candidate);
    }

    /**
     * 非操作，对规约结果取反
     *
     * @return 取反后的规约
     */
    default Specification<T> not() {
        return candidate -> !isSatisfiedBy(candidate);
    }
} 