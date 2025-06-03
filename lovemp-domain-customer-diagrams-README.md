# lovemp-domain-customer 模块调用关系图

本目录包含了 `lovemp-domain-customer` 模块的三个不同层次的架构调用关系图，帮助理解模块的内部结构和调用流程。

## 图表概览

### 1. 领域核心层调用关系图
**文件**: `lovemp-domain-customer-module-diagram.svg`

**描述**: 专注于领域核心层的组件关系图，展示了：
- 聚合根：BrandCustomer（品牌顾客）
- 实体：CustomerSharing（顾客共享）
- 值对象：8个核心值对象（CustomerRelationId、CustomerCode等）
- 领域服务：CustomerDomainService
- 领域事件：3个核心事件
- 仓储接口：2个仓储接口

**适用场景**: 
- 理解领域模型设计
- 分析业务规则和约束
- 领域专家和开发者沟通

### 2. 完整六边形架构图
**文件**: `lovemp-domain-customer-full-architecture.svg`

**描述**: 展示完整的六边形架构，包含：
- **入站适配器层**: REST控制器、事件监听器
- **应用层**: 应用服务、查询服务、命令对象
- **领域核心层**: 聚合根、实体、值对象、领域服务、领域事件、仓储接口
- **出站适配器层**: 持久化适配器、事件发布适配器、外部服务适配器
- **外部系统**: MySQL、Redis、消息队列、微服务

**适用场景**:
- 系统架构设计和评审
- 技术选型和集成方案
- 新团队成员架构培训

### 3. 调用流程图
**文件**: `lovemp-domain-customer-call-flow.svg`

**描述**: 以"创建品牌顾客关系"为例，展示典型的调用流程：
- REST API层 → 应用服务层 → 领域核心层 → 基础设施层
- 详细的调用序列和数据流向
- 事务管理和事件发布机制

**适用场景**:
- 理解业务流程执行过程
- 性能分析和优化
- 问题排查和调试

## 架构特点

### 六边形架构原则
1. **依赖倒置**: 所有依赖箭头都指向领域核心
2. **端口适配器**: 领域层定义接口（端口），适配器层实现接口
3. **技术无关**: 领域层不依赖任何框架或外部技术
4. **可测试性**: 通过依赖注入和接口隔离，便于单元测试

### DDD设计模式
1. **聚合根**: BrandCustomer作为聚合边界，确保数据一致性
2. **值对象**: 不可变对象，封装业务概念和验证规则
3. **领域服务**: 处理跨聚合的业务逻辑
4. **领域事件**: 实现松耦合的领域间集成

### 技术实现
1. **Spring Boot 3.4.5**: 应用框架和依赖注入
2. **Spring Data JPA**: 持久化层实现
3. **MapStruct**: 对象映射
4. **Spring Events**: 事件发布和订阅
5. **MySQL + Redis**: 数据存储和缓存
6. **JUnit 5 + JaCoCo**: 测试和覆盖率

## 核心组件说明

### 聚合根 - BrandCustomer
- **职责**: 管理品牌与自然人的顾客关系
- **核心方法**: activate()、freeze()、addPoints()、updateType()
- **业务规则**: 状态流转、积分管理、等级计算
- **事件发布**: 状态变更时发布相应的领域事件

### 实体 - CustomerSharing
- **职责**: 管理跨品牌的顾客数据共享
- **核心方法**: activate()、revoke()、isExpired()、hasPermission()
- **业务规则**: 授权级别控制、有效期管理
- **关联关系**: 属于BrandCustomer聚合

### 领域服务 - CustomerDomainService
- **职责**: 处理跨聚合的复杂业务逻辑
- **核心方法**: 
  - createBrandCustomer(): 创建品牌顾客关系
  - createSharingRelation(): 创建共享关系
  - calculateCustomerLevel(): 等级计算
  - checkVipUpgrade(): VIP升级检查

### 应用服务层
- **BrandCustomerApplicationService**: 品牌顾客用例实现
- **CustomerSharingApplicationService**: 顾客共享用例实现
- **CustomerQueryService**: 查询服务实现
- **事务管理**: 确保用例的原子性和一致性

## 典型调用流程

### 创建品牌顾客关系
```
1. POST /api/customers → BrandCustomerController
2. Controller → BrandCustomerApplicationService.createBrandCustomer()
3. ApplicationService → CustomerDomainService.createBrandCustomer()
4. DomainService → BrandCustomer.create() (工厂方法)
5. BrandCustomer → 发布 BrandCustomerCreatedEvent
6. ApplicationService → BrandCustomerRepository.save()
7. RepositoryImpl → MySQL Database (持久化)
8. ApplicationService → EventPublisher.publish()
9. EventPublisher → Message Queue (异步事件)
10. Controller → 返回 HTTP 201 Created
```

### 激活顾客
```
1. PUT /api/customers/{id}/activate → BrandCustomerController
2. Controller → BrandCustomerApplicationService.activateCustomer()
3. ApplicationService → BrandCustomerRepository.findById()
4. ApplicationService → BrandCustomer.activate()
5. BrandCustomer → 发布 BrandCustomerStatusChangedEvent
6. ApplicationService → BrandCustomerRepository.save()
7. Controller → 返回 HTTP 200 OK
```

## 扩展性考虑

### 新增业务功能
1. **新值对象**: 在 `domain/model/valueobject` 包中添加
2. **新业务方法**: 在聚合根或领域服务中添加
3. **新事件**: 在 `domain/event` 包中定义
4. **新用例**: 在应用服务中实现

### 技术集成
1. **新的持久化方式**: 实现新的仓储适配器
2. **新的消息队列**: 实现新的事件发布适配器
3. **新的外部服务**: 实现新的外部服务适配器
4. **新的API协议**: 实现新的入站适配器

## 质量保证

### 测试策略
1. **单元测试**: 领域模型和业务逻辑测试
2. **集成测试**: 应用服务和仓储测试
3. **端到端测试**: REST API测试
4. **覆盖率要求**: 领域层≥90%，应用层≥85%

### 代码质量
1. **静态分析**: SonarQube代码质量检查
2. **架构守护**: ArchUnit架构规则验证
3. **性能监控**: Micrometer指标收集
4. **日志规范**: 结构化日志和链路追踪

## 相关文档

- [项目技术架构设计方案](../项目技术架构设计方案.md)
- [DDD概念指南](../.cursor/rules/ddd-concepts.mdc)
- [六边形架构实现指南](../.cursor/rules/hexagonal-architecture.mdc)
- [开发流程规范](../.cursor/rules/development-process.mdc)
- [顾客关系域README](./lovemp-domain-customer/README.md) 