package com.lovemp.common.domain;

import java.util.List;
import java.util.Optional;

/**
 * 仓储接口
 * 定义领域对象仓储的基本操作
 *
 * @param <T> 聚合根类型
 * @param <ID> 聚合根标识类型
 */
public interface Repository<T extends AggregateRoot<ID>, ID> {

    /**
     * 保存聚合根
     *
     * @param aggregateRoot 聚合根对象
     * @return 保存后的聚合根
     */
    T save(T aggregateRoot);

    /**
     * 根据ID查找聚合根
     *
     * @param id 聚合根ID
     * @return 聚合根对象，如不存在则返回空
     */
    Optional<T> findById(ID id);

    /**
     * 判断指定ID的聚合根是否存在
     *
     * @param id 聚合根ID
     * @return 是否存在
     */
    boolean existsById(ID id);

    /**
     * 删除聚合根
     *
     * @param aggregateRoot 待删除的聚合根
     */
    void delete(T aggregateRoot);

    /**
     * 根据ID删除聚合根
     *
     * @param id 聚合根ID
     */
    void deleteById(ID id);

    /**
     * 查询所有聚合根
     *
     * @return 聚合根列表
     */
    List<T> findAll();
} 