package com.lovemp.domain.auth.domain.model.aggregate;

import com.lovemp.common.domain.AggregateRoot;
import com.lovemp.common.util.Assert;
import com.lovemp.domain.auth.domain.event.*;
import com.lovemp.domain.auth.domain.model.valueobject.AccountId;
import com.lovemp.domain.auth.domain.model.valueobject.AccountStatus;
import com.lovemp.domain.auth.domain.model.valueobject.RoleId;
import com.lovemp.domain.person.domain.model.valueobject.PersonId;

import java.time.LocalDateTime;
import java.util.*;

/**
 * 账号聚合根
 * 
 * <p>管理用户账号信息、认证状态和权限角色</p>
 * 
 * @author lovemp
 * @since 1.0.0
 */
public class Account extends AggregateRoot<AccountId> {
    
    /**
     * 关联的自然人ID
     */
    private PersonId personId;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码（加密后）
     */
    private String password;
    
    /**
     * 邮箱
     */
    private String email;
    
    /**
     * 手机号
     */
    private String phone;
    
    /**
     * 账号状态
     */
    private AccountStatus status;
    
    /**
     * 登录失败次数
     */
    private int failedLoginAttempts;
    
    /**
     * 最大登录失败次数
     */
    private static final int MAX_FAILED_ATTEMPTS = 5;
    
    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginAt;
    
    /**
     * 密码过期时间
     */
    private LocalDateTime passwordExpiresAt;
    
    /**
     * 账号过期时间
     */
    private LocalDateTime accountExpiresAt;
    
    /**
     * 角色ID集合
     */
    private Set<RoleId> roleIds;
    
    /**
     * 创建时间
     */
    private LocalDateTime createdAt;
    
    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
    
    /**
     * 构造账号聚合根
     * 
     * @param id 账号ID
     * @param personId 自然人ID
     * @param username 用户名
     * @param password 密码（已加密）
     * @param email 邮箱
     */
    public Account(AccountId id, PersonId personId, String username, String password, String email) {
        this.id = id;
        Assert.notNull(personId, "自然人ID不能为空");
        Assert.notEmpty(username, "用户名不能为空");
        Assert.notEmpty(password, "密码不能为空");
        Assert.notEmpty(email, "邮箱不能为空");
        
        this.personId = personId;
        this.username = username;
        this.password = password;
        this.email = email;
        this.status = AccountStatus.PENDING;
        this.failedLoginAttempts = 0;
        this.roleIds = new HashSet<>();
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
        
        // 发布账号创建事件
        registerEvent(new AccountCreatedEvent(id, personId, username, email));
    }
    
    /**
     * 创建账号
     * 
     * @param id 账号ID
     * @param personId 自然人ID
     * @param username 用户名
     * @param password 密码（已加密）
     * @param email 邮箱
     * @return 账号聚合根
     */
    public static Account create(AccountId id, PersonId personId, String username, String password, String email) {
        return new Account(id, personId, username, password, email);
    }
    
    /**
     * 激活账号
     */
    public void activate() {
        if (status == AccountStatus.PENDING) {
            AccountStatus oldStatus = this.status;
            this.status = AccountStatus.ACTIVE;
            this.updatedAt = LocalDateTime.now();
            
            registerEvent(new AccountStatusChangedEvent(getId(), oldStatus, AccountStatus.ACTIVE));
        }
    }
    
    /**
     * 禁用账号
     */
    public void disable() {
        AccountStatus oldStatus = this.status;
        this.status = AccountStatus.DISABLED;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(new AccountStatusChangedEvent(getId(), oldStatus, AccountStatus.DISABLED));
    }
    
    /**
     * 锁定账号
     */
    public void lock() {
        AccountStatus oldStatus = this.status;
        this.status = AccountStatus.LOCKED;
        this.updatedAt = LocalDateTime.now();
        
        registerEvent(new AccountLockedEvent(getId(), username, LocalDateTime.now()));
        registerEvent(new AccountStatusChangedEvent(getId(), oldStatus, AccountStatus.LOCKED));
    }
    
    /**
     * 解锁账号
     */
    public void unlock() {
        if (status == AccountStatus.LOCKED) {
            AccountStatus oldStatus = this.status;
            this.status = AccountStatus.ACTIVE;
            this.failedLoginAttempts = 0;
            this.updatedAt = LocalDateTime.now();
            
            registerEvent(new AccountStatusChangedEvent(getId(), oldStatus, AccountStatus.ACTIVE));
        }
    }
    
    /**
     * 验证密码
     * 
     * @param rawPassword 原始密码
     * @param encodedPassword 加密后的密码（用于比较）
     * @return 如果密码正确返回true，否则返回false
     */
    public boolean verifyPassword(String rawPassword, String encodedPassword) {
        Assert.notEmpty(rawPassword, "密码不能为空");
        Assert.notEmpty(encodedPassword, "加密密码不能为空");
        
        boolean isValid = this.password.equals(encodedPassword);
        
        if (isValid) {
            // 密码正确，重置失败次数
            this.failedLoginAttempts = 0;
            this.lastLoginAt = LocalDateTime.now();
            this.updatedAt = LocalDateTime.now();
            
            registerEvent(new LoginSuccessEvent(getId(), username, LocalDateTime.now()));
        } else {
            // 密码错误，增加失败次数
            this.failedLoginAttempts++;
            this.updatedAt = LocalDateTime.now();
            
            registerEvent(new LoginFailedEvent(getId(), username, failedLoginAttempts, LocalDateTime.now()));
            
            // 如果失败次数达到上限，锁定账号
            if (this.failedLoginAttempts >= MAX_FAILED_ATTEMPTS) {
                lock();
            }
        }
        
        return isValid;
    }
    
    /**
     * 更新密码
     * 
     * @param newPassword 新密码（已加密）
     */
    public void updatePassword(String newPassword) {
        Assert.notEmpty(newPassword, "新密码不能为空");
        this.password = newPassword;
        this.passwordExpiresAt = LocalDateTime.now().plusDays(90); // 密码90天后过期
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 设置手机号
     * 
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 设置账号过期时间
     * 
     * @param accountExpiresAt 账号过期时间
     */
    public void setAccountExpiresAt(LocalDateTime accountExpiresAt) {
        this.accountExpiresAt = accountExpiresAt;
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 添加角色
     * 
     * @param roleId 角色ID
     */
    public void addRole(RoleId roleId) {
        Assert.notNull(roleId, "角色ID不能为空");
        this.roleIds.add(roleId);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 移除角色
     * 
     * @param roleId 角色ID
     */
    public void removeRole(RoleId roleId) {
        Assert.notNull(roleId, "角色ID不能为空");
        this.roleIds.remove(roleId);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 批量设置角色
     * 
     * @param roleIds 角色ID集合
     */
    public void setRoles(Set<RoleId> roleIds) {
        Assert.notNull(roleIds, "角色ID集合不能为空");
        this.roleIds.clear();
        this.roleIds.addAll(roleIds);
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 清空所有角色
     */
    public void clearRoles() {
        this.roleIds.clear();
        this.updatedAt = LocalDateTime.now();
    }
    
    /**
     * 检查是否拥有指定角色
     * 
     * @param roleId 角色ID
     * @return 如果拥有角色返回true，否则返回false
     */
    public boolean hasRole(RoleId roleId) {
        return roleIds.contains(roleId);
    }
    
    /**
     * 检查账号是否可用
     * 
     * @return 如果账号可用返回true，否则返回false
     */
    public boolean isAvailable() {
        return status.isActive() && !isExpired();
    }
    
    /**
     * 检查账号是否已过期
     * 
     * @return 如果账号已过期返回true，否则返回false
     */
    public boolean isExpired() {
        return accountExpiresAt != null && LocalDateTime.now().isAfter(accountExpiresAt);
    }
    
    /**
     * 检查密码是否已过期
     * 
     * @return 如果密码已过期返回true，否则返回false
     */
    public boolean isPasswordExpired() {
        return passwordExpiresAt != null && LocalDateTime.now().isAfter(passwordExpiresAt);
    }
    
    // Getters
    public PersonId getPersonId() {
        return personId;
    }
    
    public String getUsername() {
        return username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public String getEmail() {
        return email;
    }
    
    public String getPhone() {
        return phone;
    }
    
    public AccountStatus getStatus() {
        return status;
    }
    
    public int getFailedLoginAttempts() {
        return failedLoginAttempts;
    }
    
    public LocalDateTime getLastLoginAt() {
        return lastLoginAt;
    }
    
    public LocalDateTime getPasswordExpiresAt() {
        return passwordExpiresAt;
    }
    
    public LocalDateTime getAccountExpiresAt() {
        return accountExpiresAt;
    }
    
    public Set<RoleId> getRoleIds() {
        return Collections.unmodifiableSet(roleIds);
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
        Account account = (Account) o;
        return Objects.equals(getId(), account.getId());
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
    
    @Override
    public String toString() {
        return "Account{" +
                "id=" + getId() +
                ", personId=" + personId +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", status=" + status +
                '}';
    }
} 