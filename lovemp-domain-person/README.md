# lovemp-domain-person 模块

`lovemp-domain-person` 是项目中负责处理“自然人”或“个人”核心领域相关业务逻辑的模块。根据项目总体概述，此领域可能管理人员基本信息和生物特征等。

## 主要职责

*   封装与“自然人”相关的核心业务规则和数据。
*   提供对自然人信息的增删改查等应用服务。
*   管理自然人实体及其生命周期。
*   定义与自然人领域相关的仓储接口和领域服务。

## 模块结构

该模块遵循领域驱动设计 (DDD) 和六边形架构（端口与适配器）的原则，其主要代码位于 `src/main/java/com/lovemp/domain/person` 目录下。核心包结构通常如下：

*   `com.lovemp.domain.person.domain`:
    *   **核心领域层**：包含该领域的实体（Entities）、值对象（Value Objects）、聚合根（Aggregates）、领域事件（Domain Events）、以及领域服务（Domain Services）和仓储接口（Repository Interfaces）。这是模块业务逻辑的核心。
*   `com.lovemp.domain.person.application`:
    *   **应用层**：包含应用服务（Application Services），这些服务编排领域对象和领域服务来执行具体的业务用例。它们是外部世界（如API或其他模块）与领域逻辑交互的入口点。
*   `com.lovemp.domain.person.adapter`:
    *   **适配器层**：实现与外部世界的交互。这可能包括：
        *   **输入适配器 (Driving Adapters)**：例如，REST 控制器（如果API直接在此模块）、消息监听器等，它们将外部请求转换为对应用服务的调用。
        *   **输出适配器 (Driven Adapters)**：例如，数据库仓储的具体实现（JPA实现）、调用外部服务的客户端等，它们是领域层仓储接口或领域服务所需外部依赖的具体实现。
*   `com.lovemp.domain.person.config`:
    *   **配置**：包含该领域模块特有的配置信息，例如Bean的配置、特定的安全配置或与其他基础设施集成的配置。

## 与其他模块的关系

*   **依赖于**：
    *   `lovemp-common`: 获取通用的工具类、基础领域对象和异常处理等。
*   **可能被依赖于**：
    *   `lovemp-starter`: 通过此模块的应用服务暴露API或启动相关服务。
    *   其他领域模块：如果其他领域需要自然人领域的信息或服务。
