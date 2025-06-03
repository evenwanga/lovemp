# 项目技术架构与DDD设计

## 一、架构概述

### 1. 项目背景

本项目采用领域驱动设计(DDD)思想与六边形架构(Hexagonal Architecture)结合的方式，构建一个模块化、可扩展、易测试的系统。通过清晰的架构边界与依赖关系，使业务逻辑与技术实现解耦，提高系统的可维护性与灵活性。

### 2. 六边形架构简介

六边形架构(又称端口与适配器架构)是一种以领域为中心的架构模式，具有以下特点：

- **领域中心化**：将领域逻辑置于架构中心
- **端口(Ports)**：定义领域与外部世界交互的接口
- **适配器(Adapters)**：实现这些接口，连接外部技术与领域
- **依赖原则**：所有依赖都指向领域核心，确保领域不依赖于外部技术细节

### 3. 六边形架构与DDD的结合

本项目将六边形架构与DDD的战略设计和战术设计相结合：

- **限界上下文**：通过多模块结构隔离不同领域
- **领域模型**：丰富的领域模型位于六边形核心
- **端口定义**：领域服务和仓储接口作为出入站端口
- **适配器实现**：各种技术实现作为适配器连接领域与外部世界

## 二、战略设计

### 1. 领域划分

| 领域名称         | 职责范围                       | 核心概念                 |
|-----------------|--------------------------------|--------------------------|
| **自然人核心域**  | 管理生物特征/法律身份信息       | 自然人、生物识别          |
| **企业领域**      | 管理企业实体及其组织结构        | 企业、法人、组织形式      |
| **品牌产品域**    | 维护各品牌独立属性与配置        | 品牌、服务条款、数据主权   |
| **劳动力资源域**  | 管理所有用工形式及历史轨迹      | 雇佣事件、用工类型        |
| **顾客关系域**    | 处理客户与品牌的多对多关系      | 客户关系、授权共享        |
| **认证授权域**    | 统一身份入口与权限控制          | 账号、角色、访问策略      |

### 2. 限界上下文映射

各限界上下文的交互关系如下：

- 自然人核心域 → 劳动力资源域：提供基础身份
- 自然人核心域 → 顾客关系域：提供基础身份
- 企业领域 → 品牌产品域：提供品牌所有者
- 企业领域 → 劳动力资源域：提供雇佣主体
- 品牌产品域 → 劳动力资源域：配置规则
- 品牌产品域 → 顾客关系域：定义客户策略
- 认证授权域 → 企业领域：鉴权请求
- 认证授权域 → 劳动力资源域：鉴权请求
- 认证授权域 → 顾客关系域：鉴权请求

## 三、多模块项目结构

```
lovemp-ddd-parent/                        # 父项目
├── lovemp-common/                        # 公共模块
├── lovemp-domain-person/                 # 自然人核心域
├── lovemp-domain-enterprise/             # 企业领域
├── lovemp-domain-brand/                  # 品牌产品域  
├── lovemp-domain-labor/                  # 劳动力资源域
├── lovemp-domain-customer/               # 顾客关系域
├── lovemp-domain-auth/                   # 认证授权域
├── lovemp-application/                   # 应用聚合层(可选)
└── lovemp-starter/                       # 启动模块
```

## 四、战术设计

### 1. 核心聚合设计

**聚合根：自然人（Person）**
- **唯一标识**：身份证号/生物特征ID
- **核心属性**：法定姓名、生物特征、不可变身份信息
- **不变性规则**：创建后核心身份信息不可修改

**聚合根：企业（Enterprise）**
- **唯一标识**：企业UUID/统一社会信用代码
- **核心属性**：企业名称、企业类型、注册信息、法定代表人
- **不变性规则**：法律登记信息需审核修改

**聚合根：品牌（Brand）**
- **唯一标识**：品牌UUID
- **核心属性**：品牌名称、所属企业ID、数据主权范围、用工政策版本
- **不变性规则**：品牌必须关联到有效企业

**聚合根：劳动力资源（LaborResource）**
- **唯一标识**：劳动力资源ID
- **核心属性**：关联自然人ID、雇佣历史
- **不变性规则**：同一时间段内不能有重叠的雇佣关系

**聚合根：品牌客户关系（BrandCustomer）**
- **唯一标识**：客户关系ID
- **核心属性**：品牌ID、自然人ID、关系类型
- **不变性规则**：同一客户与同一品牌只能有一个活跃关系

**聚合根：账号（Account）**
- **唯一标识**：账号ID
- **核心属性**：用户名、密码哈希、角色列表
- **不变性规则**：账号与自然人或企业关联后不可更改关联

### 2. 关键实体设计

| 实体                | 所属聚合根   | 业务规则                                 |
|--------------------|--------------|------------------------------------------|
| 雇佣事件            | 劳动力资源   | 每次雇佣关系变更创建新记录                |
| (EmploymentEvent)   |              | 时间线不允许重叠                         |
| 客户关系            | 品牌产品     | 同一自然人可建立多个品牌关系              |
| (BrandCustomer)     |              | 授权共享需显式同意                       |
| 用工策略版本        | 品牌产品     | 雇佣事件需关联生效策略版本                |
| (LaborPolicy)       |              |                                          |
| 组织部门            | 企业         | 每个部门必须属于一个企业                  |
| (Department)        |              | 可以形成层级结构                          |

### 3. 值对象设计

| 值对象              | 包含属性                         | 验证规则                    |
|--------------------|----------------------------------|------------------------------|
| 生物特征            | 指纹哈希/面部特征向量            | 符合ISO/IEC 19794标准        |
| 时间范围            | 开始日期、结束日期、时区         | 结束日期>=开始日期           |
| 权限描述符          | 资源类型、操作列表、生效条件     | 需通过RBAC引擎验证           |
| 企业类型            | 类型编码、描述、法律类别         | 必须是有效的法定企业形式     |
| 营业执照信息        | 执照编号、经营范围、有效期       | 必须在有效期内               |

### 4. 领域间关系

**企业领域与品牌产品域关系**：
- 企业是品牌的所有者，一个企业可以拥有多个品牌
- 品牌通过企业ID引用企业信息，而不是直接包含企业实体
- 企业状态变更影响关联品牌的可用性（如企业注销导致品牌停用）
- 企业可以授权其他企业使用其品牌（如特许经营）

## 五、六边形架构分层结构

每个领域模块内部采用六边形架构，具有以下分层：

### 1. 领域核心(Domain Core)

位于六边形中心，包含纯粹的业务逻辑，不依赖任何外部技术。

- **领域模型**：聚合根、实体和值对象
- **领域服务**：处理跨实体业务逻辑
- **领域事件**：表达领域状态变化
- **端口接口**：定义与外部世界交互的接口

### 2. 应用层(Application Layer)

协调领域活动，实现用例。是入站端口的主要实现者。

- **应用服务**：实现用例，编排领域操作
- **命令/查询对象**：封装用例输入
- **事务管理**：确保用例的原子性

### 3. 适配器层(Adapters Layer)

连接外部世界与领域核心，分为入站和出站适配器。

- **入站适配器**：接收外部请求，调用应用服务
  - REST控制器、消息消费者、定时任务等
- **出站适配器**：实现领域定义的出站端口
  - 持久化实现、消息发布、外部服务集成等

## 六、领域模块详细结构

以下是基于六边形架构的领域模块典型结构(以企业领域为例)：

```
lovemp-domain-enterprise/
├── src/main/java/com/lovemp/domain/enterprise/
│   ├── domain/                       # 领域核心（六边形中心）
│   │   ├── model/                    # 领域模型
│   │   │   ├── aggregate/            # 聚合根
│   │   │   │   └── Enterprise.java   # 企业聚合根
│   │   │   ├── entity/               # 实体
│   │   │   │   └── Department.java   # 部门实体
│   │   │   └── valueobject/          # 值对象
│   │   │       ├── EnterpriseType.java # 企业类型值对象
│   │   │       └── EnterpriseId.java # 企业标识值对象
│   │   ├── service/                  # 领域服务
│   │   │   └── EnterpriseDomainService.java # 领域服务实现
│   │   ├── event/                    # 领域事件
│   │   │   └── EnterpriseCreatedEvent.java  # 企业创建事件
│   │   └── port/                     # 端口（接口）
│   │       ├── incoming/             # 入站端口
│   │       │   └── EnterpriseManagementService.java  # 企业管理服务接口
│   │       └── outgoing/             # 出站端口
│   │           ├── EnterpriseRepository.java  # 仓储接口
│   │           └── EnterpriseEventPublisher.java  # 事件发布接口
│   ├── application/                  # 应用层
│   │   ├── service/                  # 应用服务
│   │   │   └── EnterpriseApplicationService.java  # 企业应用服务
│   │   ├── command/                  # 命令
│   │   │   ├── CreateEnterpriseCommand.java  # 创建企业命令
│   │   │   └── UpdateEnterpriseCommand.java  # 更新企业命令 
│   │   └── query/                    # 查询
│   │       └── EnterpriseQuery.java  # 企业查询
│   ├── adapter/                      # 适配器层
│   │   ├── incoming/                 # 入站适配器
│   │   │   ├── rest/                 # REST API适配器
│   │   │   │   ├── EnterpriseController.java  # 企业控制器
│   │   │   │   └── dto/              # 数据传输对象
│   │   │   │       └── EnterpriseDTO.java  # 企业DTO
│   │   │   └── messaging/            # 消息消费适配器
│   │   │       └── EnterpriseEventConsumer.java  # 企业事件消费者
│   │   └── outgoing/                 # 出站适配器
│   │       ├── persistence/          # 持久化适配器
│   │       │   ├── EnterpriseRepositoryImpl.java  # 仓储实现
│   │       │   ├── mapper/           # 对象映射器
│   │   │   │   └── EnterpriseMapper.java  # 企业映射器
│   │       │   └── entity/           # 持久化实体
│   │       │       └── EnterprisePO.java  # 企业持久化对象
│   │       └── messaging/            # 消息发布适配器
│   │           └── EnterpriseEventPublisherImpl.java  # 事件发布实现
│   └── config/                       # 配置
│       ├── EnterpriseDomainConfig.java   # 领域配置
│       └── EnterprisePersistenceConfig.java  # 持久化配置
```

## 七、业务流程实现

### 1. 雇佣生命周期管理

雇佣生命周期的状态转换流程：
- 待入职 → 在职：完成入职手续
- 在职 → 离职中：发起离职流程
- 离职中 → 已离职：交接完成
- 已离职 → 在职：重新入职(新建事件)

### 2. 企业-品牌关系管理流程

1. 企业创建后可以申请注册一个或多个品牌
2. 品牌创建时必须关联到一个有效企业
3. 企业可以授权其他企业使用其品牌（特许经营模式）
4. 企业状态变更时触发品牌状态联动变更

### 3. 跨品牌客户授权流程

1. 品牌A客户访问品牌B服务
2. 系统检查`brand_customer_sharing`授权记录
3. 若未授权，触发动态授权协议签署
4. 创建临时客户关系（标记为"衍生关系"）

### 4. 个体工商户用工特殊处理

- **用工类型**：标记为`contractor`
- **关联实体**：营业执照副本（加密存储）
- **结算规则**：关联独立结算策略聚合

## 八、技术栈

### 1. 核心框架

- **Spring Boot**: 应用基础框架，版本 3.4.5
- **Spring MVC**: Web 层框架
- **Spring Security**: 安全框架
- **Spring Data JPA**: 持久化层框架
- **Spring Cloud**: 微服务框架 (可选)
- **Sa-Token**: 轻量级Java权限认证框架

### 2. 数据库相关

- **MySQL/PostgreSQL**: 关系型数据库
- **Redis**: 缓存和分布式锁
- **Elasticsearch**: 搜索引擎 (可选)

### 3. 消息和事件

- **Spring Cloud Stream**: 消息驱动
- **Kafka/RabbitMQ**: 消息中间件
- **Spring Events**: 领域事件发布订阅

### 4. 其他工具

- **Lombok**: 简化代码
- **MapStruct**: 对象映射
- **Validation**: 参数校验
- **Swagger/OpenAPI**: API文档
- **Resilience4j**: 熔断和限流

## 九、六边形架构实施指南

### 1. 依赖注入配置

- 采用构造器注入，避免字段注入
- 通过Spring配置确保依赖方向永远指向领域
- 领域层不应依赖框架注解

```java
@Configuration
public class EnterpriseDomainConfig {
    @Bean
    public EnterpriseDomainService enterpriseDomainService(EnterpriseRepository enterpriseRepository) {
        return new EnterpriseDomainService(enterpriseRepository);
    }
}
```

### 2. 端口与适配器示例

**入站端口定义**：
```java
// 领域层中的入站端口
public interface EnterpriseManagementService {
    EnterpriseId createEnterprise(String name, EnterpriseType type, String creditCode);
    void updateEnterprise(EnterpriseId enterpriseId, String name);
}
```

**入站适配器实现**：
```java
// 应用层中的端口实现
@Service
public class EnterpriseApplicationService implements EnterpriseManagementService {
    private final EnterpriseRepository enterpriseRepository;
    
    public EnterpriseApplicationService(EnterpriseRepository enterpriseRepository) {
        this.enterpriseRepository = enterpriseRepository;
    }
    
    @Override
    public EnterpriseId createEnterprise(String name, EnterpriseType type, String creditCode) {
        Enterprise enterprise = Enterprise.create(EnterpriseId.of(UUID.randomUUID().toString()), 
                                                 name, type, creditCode);
        enterpriseRepository.save(enterprise);
        return enterprise.getId();
    }
    
    @Override
    public void updateEnterprise(EnterpriseId enterpriseId, String name) {
        Enterprise enterprise = enterpriseRepository.findById(enterpriseId)
            .orElseThrow(() -> new EntityNotFoundException("Enterprise not found"));
        enterprise.updateName(name);
        enterpriseRepository.save(enterprise);
    }
}
```

**出站端口定义**：
```java
// 领域层中的出站端口
public interface EnterpriseRepository {
    Optional<Enterprise> findById(EnterpriseId enterpriseId);
    Optional<Enterprise> findByCreditCode(String creditCode);
    void save(Enterprise enterprise);
}
```

**出站适配器实现**：
```java
// 基础设施层中的适配器实现
@Repository
public class EnterpriseRepositoryImpl implements EnterpriseRepository {
    private final JpaEnterpriseRepository jpaRepository;
    private final EnterpriseMapper mapper;
    
    public EnterpriseRepositoryImpl(JpaEnterpriseRepository jpaRepository, EnterpriseMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }
    
    @Override
    public Optional<Enterprise> findById(EnterpriseId enterpriseId) {
        return jpaRepository.findById(enterpriseId.getValue())
            .map(mapper::toDomain);
    }
    
    @Override
    public Optional<Enterprise> findByCreditCode(String creditCode) {
        return jpaRepository.findByCreditCode(creditCode)
            .map(mapper::toDomain);
    }
    
    @Override
    public void save(Enterprise enterprise) {
        EnterprisePO po = mapper.toPersistence(enterprise);
        jpaRepository.save(po);
    }
}
```

## 十、合规性设计

### 1. 数据主权控制矩阵
| 数据类型          | 所有者       | 共享策略                  |
|-------------------|--------------|---------------------------|
| 生物特征          | 自然人       | 不可共享                  |
| 雇佣绩效          | 品牌产品     | 需匿名化后集团内共享      |
| 客户消费习惯      | 品牌产品     | 可付费购买使用权          |
| 企业基本信息      | 企业         | 可对外公开                |
| 企业财务信息      | 企业         | 仅内部可见                |

### 2. 审计日志要求
- 所有企业信息变更记录操作者身份和时间
- 所有雇佣状态变更记录时点快照
- 客户关系变更记录操作者IP与设备指纹
- 数据共享操作记录目的与使用期限

## 十一、扩展性设计

### 1. 未来扩展点
- **企业集团关系**：增加集团管理能力
- **新增用工类型**：通过`LaborPolicy`扩展枚举值
- **跨境雇佣**：增加税务实体关联
- **AI用工分析**：基于历史事件的预测模型

### 2. 性能优化预备
- 企业数据按地区分片存储
- 雇佣事件按品牌分片存储
- 客户关系建立异步索引
- 生物特征单独加密存储 