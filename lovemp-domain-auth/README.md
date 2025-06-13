# 认证授权域 (lovemp-domain-auth)

## 概述

认证授权域是Lovemp项目的核心安全模块，负责管理用户账号、角色权限和身份认证。本模块基于领域驱动设计(DDD)和六边形架构实现，确保安全逻辑与技术实现的分离。

## 领域模型

### 聚合根

#### Account（账号聚合根）
- **职责**：管理用户账号信息、认证状态和权限角色
- **核心属性**：
  - `AccountId`：账号唯一标识
  - `PersonId`：关联的自然人ID
  - `username`：用户名
  - `password`：加密后的密码
  - `email`：邮箱地址
  - `phone`：手机号码
  - `status`：账号状态（激活、禁用、锁定、过期、待激活）
  - `roleIds`：角色ID集合
- **核心行为**：
  - 账号创建和激活
  - 密码验证和更新
  - 登录失败控制和自动锁定
  - 角色管理
  - 状态变更

### 实体

#### Role（角色实体）
- **职责**：管理角色信息和权限集合
- **核心属性**：
  - `RoleId`：角色唯一标识
  - `code`：角色代码
  - `name`：角色名称
  - `type`：角色类型
  - `permissionIds`：权限ID集合
  - `builtin`：是否为系统内置角色
- **核心行为**：
  - 权限的添加和移除
  - 权限验证
  - 角色启用/禁用

#### Permission（权限实体）
- **职责**：定义系统权限和访问控制规则
- **核心属性**：
  - `PermissionId`：权限唯一标识
  - `code`：权限代码
  - `name`：权限名称
  - `type`：权限类型（MENU、BUTTON、API等）
  - `resourcePath`：资源路径
  - `action`：操作动作
- **核心行为**：
  - 权限匹配验证
  - 权限启用/禁用

### 值对象

#### AccountId
- 账号标识符值对象，确保账号的唯一性

#### RoleId
- 角色标识符值对象，确保角色的唯一性

#### PermissionId
- 权限标识符值对象，确保权限的唯一性

#### AccountStatus
- 账号状态枚举：
  - `ACTIVE`：激活状态
  - `DISABLED`：禁用状态
  - `LOCKED`：锁定状态
  - `EXPIRED`：过期状态
  - `PENDING`：待激活状态

## 核心业务规则

### 账号安全规则
1. **登录失败锁定**：连续5次登录失败自动锁定账号
2. **密码过期**：密码90天后自动过期
3. **账号状态控制**：只有激活状态的账号才能正常使用
4. **唯一性约束**：用户名和邮箱在系统中必须唯一

### 权限控制规则
1. **角色权限继承**：账号通过角色获得权限
2. **权限匹配**：支持精确匹配和模式匹配
3. **内置角色保护**：系统内置角色不可删除
4. **权限启用控制**：只有启用的权限才生效

## 领域事件

### AccountCreatedEvent
- **触发时机**：新账号创建时
- **用途**：通知其他领域进行相关初始化

### AccountStatusChangedEvent
- **触发时机**：账号状态变更时
- **用途**：记录状态变更历史，触发相关业务流程

### AccountLockedEvent
- **触发时机**：账号被锁定时
- **用途**：安全告警，记录安全日志

### LoginSuccessEvent
- **触发时机**：用户成功登录时
- **用途**：记录登录日志，更新最后登录时间

### LoginFailedEvent
- **触发时机**：用户登录失败时
- **用途**：安全监控，记录失败尝试

## 仓储接口

### AccountRepository
- 账号聚合根的持久化接口
- 支持按ID、用户名、邮箱、自然人ID查询

### RoleRepository
- 角色实体的持久化接口
- 支持按ID、代码查询，支持查询启用角色

### PermissionRepository
- 权限实体的持久化接口
- 支持按ID、代码、类型查询，支持查询启用权限

## 技术特性

### 安全性
- 密码加密存储
- 登录失败自动锁定
- 密码过期机制
- 权限精确控制

### 可扩展性
- 基于六边形架构，易于扩展
- 事件驱动设计，支持异步处理
- 角色权限模型，支持复杂权限场景

### 可维护性
- 领域模型清晰，业务逻辑集中
- 完整的单元测试覆盖
- 详细的文档说明

## 使用示例

### 创建账号
```java
// 创建账号
PersonId personId = PersonId.of("person-123");
AccountId accountId = AccountId.of("account-456");
Account account = Account.create(
    accountId, 
    personId, 
    "john_doe", 
    "encrypted_password", 
    "john@example.com"
);

// 激活账号
account.activate();

// 添加角色
RoleId adminRole = RoleId.of("role-admin");
account.addRole(adminRole);
```

### 验证登录
```java
// 验证密码
boolean isValid = account.verifyPassword("raw_password", "encrypted_password");
if (isValid) {
    // 登录成功
    System.out.println("登录成功");
} else {
    // 登录失败，可能触发账号锁定
    System.out.println("登录失败，失败次数：" + account.getFailedLoginAttempts());
}
```

### 权限验证
```java
// 创建权限
PermissionId permissionId = PermissionId.of("perm-123");
Permission permission = Permission.create(
    permissionId, 
    "user:read", 
    "用户查看权限", 
    "API"
);
permission.setResourcePath("/api/users");
permission.setAction("GET");

// 验证权限匹配
boolean matches = permission.matches("/api/users", "GET");
```

## 后续扩展

### 计划功能
1. **多因子认证**：支持短信、邮箱验证码
2. **单点登录**：集成OAuth2/OIDC协议
3. **权限缓存**：Redis缓存权限信息
4. **审计日志**：完整的操作审计记录
5. **密码策略**：可配置的密码复杂度要求

### 集成方向
1. **Sa-Token集成**：在适配器层集成Sa-Token框架
2. **Spring Security集成**：与Spring Security深度集成
3. **分布式会话**：支持分布式环境下的会话管理
4. **权限同步**：与外部权限系统的数据同步

## 依赖关系

- **lovemp-common**：共享内核，提供基础抽象
- **lovemp-domain-person**：自然人核心域，用于关联Person实体

## 测试策略

- **单元测试**：覆盖所有领域模型和业务逻辑
- **集成测试**：验证仓储接口和事件发布
- **安全测试**：验证密码加密、权限控制等安全功能 