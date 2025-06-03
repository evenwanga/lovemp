# lovemp-domain-labor 模块

`lovemp-domain-labor` 是项目中负责"劳动力资源域"的核心领域模块，专注于处理劳动力资源和雇佣关系的管理。

## 主要职责

* 管理自然人的所有用工形式及其历史轨迹
* 维护雇佣关系的生命周期（创建、入职、离职等）
* 确保雇佣关系的有效性与合规性
* 支持多种用工类型（全职、兼职、临时工、个体工商户等）

## 领域模型

本模块遵循领域驱动设计(DDD)和六边形架构的原则，核心领域模型包括：

### 聚合根

* **LaborResource（劳动力资源）**：与自然人相关联的劳动力资源实体，管理所有雇佣关系及其状态变化

### 实体

* **EmploymentEvent（雇佣事件）**：记录雇佣关系的各种状态变化事件，形成完整的雇佣历史轨迹

### 值对象

* **LaborResourceId**：劳动力资源唯一标识
* **EmploymentEventId**：雇佣事件唯一标识
* **EmploymentType**：雇佣类型（全职、兼职、临时工等）
* **EmploymentStatus**：雇佣状态（待入职、在职、离职中、已离职等）
* **TimeRange**：时间范围，用于表示雇佣关系的有效期间

### 领域事件

* **LaborResourceCreatedEvent**：劳动力资源创建事件
* **EmploymentCreatedEvent**：雇佣关系创建事件
* **EmploymentOnboardedEvent**：入职事件
* **EmploymentLeavingInitiatedEvent**：发起离职事件
* **EmploymentTerminatedEvent**：完成离职事件
* **EmploymentCanceledEvent**：雇佣取消事件

## 核心业务规则

1. 一个自然人只能有一个劳动力资源记录
2. 一个劳动力资源可以有多个雇佣事件，形成完整的雇佣历史
3. 同一时间段内，不允许有重叠的活跃雇佣关系
4. 雇佣状态的转换遵循特定的生命周期（如：待入职→在职→离职中→已离职）
5. 雇佣关系必须关联到特定企业和品牌
6. 个体工商户等特殊用工类型有专门的处理逻辑

## 模块结构

```
lovemp-domain-labor/
├── src/main/java/com/lovemp/domain/labor/
│   ├── domain/                     # 领域核心（六边形中心）
│   │   ├── model/                  # 领域模型
│   │   │   ├── aggregate/          # 聚合根
│   │   │   │   └── LaborResource.java
│   │   │   ├── entity/             # 实体
│   │   │   │   └── EmploymentEvent.java
│   │   │   └── valueobject/        # 值对象
│   │   │       ├── LaborResourceId.java
│   │   │       ├── EmploymentEventId.java
│   │   │       ├── EmploymentType.java
│   │   │       ├── EmploymentStatus.java
│   │   │       └── TimeRange.java
│   │   ├── event/                  # 领域事件
│   │   │   ├── LaborResourceCreatedEvent.java
│   │   │   ├── EmploymentCreatedEvent.java
│   │   │   ├── EmploymentOnboardedEvent.java
│   │   │   ├── EmploymentLeavingInitiatedEvent.java
│   │   │   ├── EmploymentTerminatedEvent.java
│   │   │   └── EmploymentCanceledEvent.java
│   │   ├── service/                # 领域服务
│   │   │   ├── LaborDomainService.java
│   │   │   └── LaborDomainServiceImpl.java
│   │   └── port/                   # 端口（接口）
│   │       ├── incoming/           # 入站端口
│   │       └── repository/         # 仓储端口
│   │           └── LaborResourceRepository.java
│   ├── application/                # 应用层
│   │   ├── service/                # 应用服务
│   │   ├── command/                # 命令
│   │   └── query/                  # 查询
│   ├── adapter/                    # 适配器层
│   │   ├── incoming/               # 入站适配器
│   │   └── outgoing/               # 出站适配器
│   └── config/                     # 配置
```

## 与其他模块的关系

* **依赖于**：
  * `lovemp-common`：获取通用工具类和基础设施
  * `lovemp-domain-person`：获取自然人相关的领域模型
  * `lovemp-domain-enterprise`：获取企业相关的领域模型
  * `lovemp-domain-brand`：获取品牌和用工政策相关的领域模型

* **被依赖于**：
  * `lovemp-starter`：通过应用服务与适配器整合到应用程序中
  * `lovemp-domain-customer`：可能需要查询雇佣关系以确定客户身份

## 主要用例

1. 创建劳动力资源
2. 建立雇佣关系
3. 处理雇佣生命周期（入职、离职等）
4. 查询雇佣历史和当前状态
5. 验证雇佣关系有效性