package com.lovemp.domain.auth.domain.repository;

import com.lovemp.domain.auth.domain.model.entity.Permission;
import com.lovemp.domain.auth.domain.model.valueobject.PermissionId;

import java.util.List;
import java.util.Optional;

/**
 * 权限仓储接口
 * 
 * <p>定义权限实体的持久化操作</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public interface PermissionRepository {
    
    /**
     * 根据ID查找权限
     * 
     * @param permissionId 权限ID
     * @return 权限实体，如果不存在则返回空
     */
    Optional<Permission> findById(PermissionId permissionId);
    
    /**
     * 根据权限代码查找权限
     * 
     * @param code 权限代码
     * @return 权限实体，如果不存在则返回空
     */
    Optional<Permission> findByCode(String code);
    
    /**
     * 查找所有权限
     * 
     * @return 权限列表
     */
    List<Permission> findAll();
    
    /**
     * 查找启用的权限
     * 
     * @return 启用的权限列表
     */
    List<Permission> findAllEnabled();
    
    /**
     * 根据权限类型查找权限
     * 
     * @param type 权限类型
     * @return 权限列表
     */
    List<Permission> findByType(String type);
    
    /**
     * 保存权限
     * 
     * @param permission 权限实体
     */
    void save(Permission permission);
    
    /**
     * 删除权限
     * 
     * @param permissionId 权限ID
     */
    void deleteById(PermissionId permissionId);
    
    /**
     * 检查权限代码是否存在
     * 
     * @param code 权限代码
     * @return 如果存在返回true，否则返回false
     */
    boolean existsByCode(String code);
} 