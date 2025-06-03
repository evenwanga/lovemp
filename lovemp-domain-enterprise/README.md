# lovemp-domain-enterprise 模块

`lovemp-domain-enterprise` 是项目中负责处理“企业”核心领域相关业务逻辑的模块。此领域可能涉及企业基本信息、组织架构、企业认证等方面。

## 主要职责

*   封装与“企业”相关的核心业务规则和数据。
*   提供对企业信息的增删改查以及相关的业务操作（如企业注册、认证等）的应用服务。
*   管理企业实体及其生命周期，可能包括企业的部门、员工（关联自然人领域）等聚合关系。
*   定义与企业领域相关的仓储接口和领域服务。

## 模块结构

该模块遵循领域驱动设计 (DDD) 和六边形架构（端口与适配器）的原则，其主要代码位于 `src/main/java/com/lovemp/domain/enterprise` 目录下。核心包结构通常如下：

*   `com.lovemp.domain.enterprise.domain`:
    *   **核心领域层**：包含该领域的实体（如 Enterprise, Department）、值对象、聚合根、领域事件，以及领域服务和仓储接口。
*   `com.lovemp.domain.enterprise.application`:
    *   **应用层**：包含应用服务，用于编排领域对象和领域服务来执行具体的业务用例（如注册新企业、更新企业信息等）。
*   `com.lovemp.domain.enterprise.adapter`:
    *   **适配器层**：实现与外部世界的交互。包括输入适配器（如API控制器）和输出适配器（如数据库实现、第三方企业信息服务客户端）。
*   `com.lovemp.domain.enterprise.config`:
    *   **配置**：包含该领域模块特有的配置信息。

## 与其他模块的关系

*   **依赖于**：
    *   `lovemp-common`: 获取通用的工具类、基础领域对象和异常处理等。
    *   `lovemp-domain-person`: 企业可能关联员工（自然人）。
    *   `lovemp-domain-brand`: 企业可能拥有或管理多个品牌。
*   **可能被依赖于**：
    *   `lovemp-starter`: 通过此模块的应用服务暴露API或启动相关服务。
    *   其他需要企业信息的领域模块。
