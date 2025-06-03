# 顾客关系域模块 (lovemp-domain-customer)

## 概述

顾客关系域模块负责管理自然人与品牌之间的顾客关系，以及跨品牌的顾客数据共享。本模块采用六边形架构和领域驱动设计(DDD)原则实现。

## 核心功能

### 1. 品牌顾客关系管理
- 建立自然人与品牌的顾客关系
- 管理顾客类型（普通、VIP、批发）
- 处理顾客状态（未激活、正常、冻结）
- 积分和等级管理
- 顾客编码生成和管理

### 2. 跨品牌数据共享
- 管理品牌间的顾客数据共享关系
- 支持不同的共享类型（自动、授权、关联）
- 控制授权级别（基础信息、消费信息、全部信息）
- 有效期管理和过期提醒

## 领域模型

### 聚合根
- **BrandCustomer**: 品牌顾客聚合根，管理自然人与品牌的关系

### 实体
- **CustomerSharing**: 顾客共享实体，管理跨品牌数据共享

### 值对象
- **CustomerRelationId**: 顾客关系ID
- **CustomerCode**: 顾客编码
- **CustomerType**: 顾客类型枚举
- **CustomerStatus**: 顾客状态枚举
- **RelationType**: 关系类型枚举
- **SharingId**: 共享ID
- **SharingType**: 共享类型枚举
- **SharingStatus**: 共享状态枚举
- **AuthLevel**: 授权级别枚举

### 领域事件
- **BrandCustomerCreatedEvent**: 品牌顾客创建事件
- **BrandCustomerStatusChangedEvent**: 品牌顾客状态变更事件
- **BrandCustomerUpdatedEvent**: 品牌顾客更新事件

### 领域服务
- **CustomerDomainService**: 顾客领域服务，处理跨聚合的业务逻辑

### 仓储接口
- **BrandCustomerRepository**: 品牌顾客仓储接口
- **CustomerSharingRepository**: 顾客共享仓储接口

## 开发状态

✅ **已完成**:
- 领域模型设计和实现
- 值对象和枚举定义
- 聚合根和实体实现
- 领域事件定义
- 领域服务实现
- 仓储接口定义
- 基础架构搭建

🚧 **待实现**:
- 应用服务层
- REST API适配器
- 持久化适配器
- 单元测试
- 集成测试
- API文档 