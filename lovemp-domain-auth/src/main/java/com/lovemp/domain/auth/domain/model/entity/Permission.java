package com.lovemp.domain.auth.domain.model.entity;

import com.lovemp.common.domain.Entity;
import com.lovemp.common.util.Assert;
import com.lovemp.domain.auth.domain.model.valueobject.PermissionId;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * 权限实体
 * 
 * <p>表示系统中的权限，包含权限的基本信息和权限验证逻辑</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class Permission extends Entity<PermissionId> {
    
    /**
     * 权限代码，用于程序中的权限判断
     */
    private String code;
    
    /**
     * 权限名称，用于显示
     */
    private String name;
    
    /**
     * 权限描述
     */
    private String description;
    
    /**
     * 权限类型（如：MENU、BUTTON、API等）
     */
    private String type;
    
    /**
     * 资源路径（如：URL路径、菜单路径等）
     */
    private String resourcePath;
    
    /**
     * 操作动作（如：READ、WRITE、DELETE等）
     */
    private String action;
    
    /**
     * 父权限ID，用于构建权限树
     */
    private PermissionId parentId;
    
    /**
     * 排序号
     */
    private Integer sortOrder;
    
    /**
     * 是否启用
     */
    private boolean enabled;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 构造权限实体
     * 
     * @param id 权限ID
     * @param code 权限代码
     * @param name 权限名称
     * @param type 权限类型
     */
    public Permission(PermissionId id, String code, String name, String type) {
        this.id = id;
        Assert.notEmpty(code, "权限代码不能为空");
        Assert.notEmpty(name, "权限名称不能为空");
        Assert.notEmpty(type, "权限类型不能为空");
        
        this.code = code;
        this.name = name;
        this.type = type;
        this.enabled = true;
        this.sortOrder = 0;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 创建权限实体
     * 
     * @param id 权限ID
     * @param code 权限代码
     * @param name 权限名称
     * @param type 权限类型
     * @return 权限实体
     */
    public static Permission create(PermissionId id, String code, String name, String type) {
        return new Permission(id, code, name, type);
    }
    
    /**
     * 设置权限描述
     * 
     * @param description 权限描述
     */
    public void setDescription(String description) {
        this.description = description;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 设置资源路径
     * 
     * @param resourcePath 资源路径
     */
    public void setResourcePath(String resourcePath) {
        this.resourcePath = resourcePath;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 设置操作动作
     * 
     * @param action 操作动作
     */
    public void setAction(String action) {
        this.action = action;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 设置父权限
     * 
     * @param parentId 父权限ID
     */
    public void setParentId(PermissionId parentId) {
        this.parentId = parentId;
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
     * 启用权限
     */
    public void enable() {
        this.enabled = true;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 禁用权限
     */
    public void disable() {
        this.enabled = false;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 检查权限是否匹配指定的资源和动作
     * 
     * @param resourcePath 资源路径
     * @param action 操作动作
     * @return 如果匹配返回true，否则返回false
     */
    public boolean matches(String resourcePath, String action) {
        if (!enabled) {
            return false;
        }
        
        boolean resourceMatches = this.resourcePath == null || 
                                 this.resourcePath.equals(resourcePath) ||
                                 resourcePath.startsWith(this.resourcePath);
        
        boolean actionMatches = this.action == null || 
                               this.action.equals(action) ||
                               "*".equals(this.action);
        
        return resourceMatches && actionMatches;
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
    
    public String getResourcePath() {
        return resourcePath;
    }
    
    public String getAction() {
        return action;
    }
    
    public PermissionId getParentId() {
        return parentId;
    }
    
    public Integer getSortOrder() {
        return sortOrder;
    }
    
    public boolean isEnabled() {
        return enabled;
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
        Permission that = (Permission) o;
        return Objects.equals(getId(), that.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    
    @Override
    public String toString() {
        return "Permission{" +
                "id=" + getId() +
                ", code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", type='" + type + '\'' +
                ", enabled=" + enabled +
                '}';
    }
} 