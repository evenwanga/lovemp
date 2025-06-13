package com.lovemp.domain.auth.domain.model.entity;

import com.lovemp.common.domain.Entity;
import com.lovemp.common.util.Assert;
import com.lovemp.domain.auth.domain.model.valueobject.PermissionId;
import com.lovemp.domain.auth.domain.model.valueobject.RoleId;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 角色实体
 * 
 * <p>表示系统中的角色，管理角色信息和权限集合</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class Role extends Entity<RoleId> {
    
    /**
     * 角色代码，用于程序中的角色判断
     */
    private String code;
    
    /**
     * 角色名称，用于显示
     */
    private String name;
    
    /**
     * 角色描述
     */
    private String description;
    
    /**
     * 角色类型（如：SYSTEM、BUSINESS等）
     */
    private String type;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 是否为系统内置角色（内置角色不可删除）
     */
    private boolean builtin;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 权限ID集合
     */
    private Set<PermissionId> permissionIds;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 构造角色实体
     * 
     * @param id 角色ID
     * @param code 角色代码
     * @param name 角色名称
     * @param type 角色类型
     */
    public Role(RoleId id, String code, String name, String type) {
        this.id = id;
        Assert.notEmpty(code, "角色代码不能为空");
        Assert.notEmpty(name, "角色名称不能为空");
        Assert.notEmpty(type, "角色类型不能为空");
        
        this.code = code;
        this.name = name;
        this.type = type;
        this.enabled = true;
        this.builtin = false;
        this.sortOrder = 0;
        this.permissionIds = new HashSet<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 创建角色实体
     * 
     * @param id 角色ID
     * @param code 角色代码
     * @param name 角色名称
     * @param type 角色类型
     * @return 角色实体
     */
    public static Role create(RoleId id, String code, String name, String type) {
        return new Role(id, code, name, type);
    }
    
    /**
     * 创建系统内置角色
     * 
     * @param id 角色ID
     * @param code 角色代码
     * @param name 角色名称
     * @param type 角色类型
     * @return 系统内置角色实体
     */
    public static Role createBuiltin(RoleId id, String code, String name, String type) {
        Role role = new Role(id, code, name, type);
        role.builtin = true;
        return role;
    }
    
    /**
     * 设置角色描述
     * 
     * @param description 角色描述
     */
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 设置排序号
     * 
     * @param sortOrder 排序号
     */
    public void setSortOrder(Integer sortOrder) {
        this.sortOrder = sortOrder;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 启用角色
     */
    public void enable() {
        this.enabled = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 禁用角色
     */
    public void disable() {
        this.enabled = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 添加权限
     * 
     * @param permissionId 权限ID
     */
    public void addPermission(PermissionId permissionId) {
        Assert.notNull(permissionId, "权限ID不能为空");
        this.permissionIds.add(permissionId);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 移除权限
     * 
     * @param permissionId 权限ID
     */
    public void removePermission(PermissionId permissionId) {
        Assert.notNull(permissionId, "权限ID不能为空");
        this.permissionIds.remove(permissionId);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 批量设置权限
     * 
     * @param permissionIds 权限ID集合
     */
    public void setPermissions(Set<PermissionId> permissionIds) {
        Assert.notNull(permissionIds, "权限ID集合不能为空");
        this.permissionIds.clear();
        this.permissionIds.addAll(permissionIds);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 清空所有权限
     */
    public void clearPermissions() {
        this.permissionIds.clear();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 检查是否拥有指定权限
     * 
     * @param permissionId 权限ID
     * @return 如果拥有权限返回true，否则返回false
     */
    public boolean hasPermission(PermissionId permissionId) {
        return enabled && permissionIds.contains(permissionId);
    }
    
    /**
     * 检查是否可以删除
     * 
     * @return 如果可以删除返回true，否则返回false
     */
    public boolean canDelete() {
        return !builtin;
    }
    
    // Getters
    public String getCode() {
        return code;
    }
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public String getType() {
        return type;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public boolean isBuiltin() {
        return builtin;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public Set<PermissionId> getPermissionIds() {
        return Collections.unmodifiableSet(permissionIds);
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Role role = (Role) o;
        return Objects.equals(getId(), role.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    
    @Override
    public String toString() {
        return "Role{" +
                "id=" + getId() +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", enabled=" + enabled +
                ", builtin=" + builtin +
                '}';
    }
} 