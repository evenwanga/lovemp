package com.lovemp.domain.auth.domain.repository;

import com.lovemp.domain.auth.domain.model.entity.Role;
import com.lovemp.domain.auth.domain.model.valueobject.RoleId;

import java.util.List;
import java.util.Optional;

/**
 * 角色仓储接口
 * 
 * <p>定义角色实体的持久化操作</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public interface RoleRepository {
    
    /**
     * 根据ID查找角色
     * 
     * @param roleId 角色ID
     * @return 角色实体，如果不存在则返回空
     */
    Optional<Role> findById(RoleId roleId);
    
    /**
     * 根据角色代码查找角色
     * 
     * @param code 角色代码
     * @return 角色实体，如果不存在则返回空
     */
    Optional<Role> findByCode(String code);
    
    /**
     * 查找所有角色
     * 
     * @return 角色列表
     */
    List<Role> findAll();
    
    /**
     * 查找启用的角色
     * 
     * @return 启用的角色列表
     */
    List<Role> findAllEnabled();
    
    /**
     * 保存角色
     * 
     * @param role 角色实体
     */
    void save(Role role);
    
    /**
     * 删除角色
     * 
     * @param roleId 角色ID
     */
    void deleteById(RoleId roleId);
    
    /**
     * 检查角色代码是否存在
     * 
     * @param code 角色代码
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByCode(String code);
} 