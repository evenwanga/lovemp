# LoveMP DDD Parent 项目

本项目是一个基于六边形架构的领域驱动设计（DDD）项目，使用 Java 和 Spring Boot 构建。

## 项目描述

根据 `pom.xml` 文件，项目名称为 `lovemp-ddd-parent`，其核心目标是实践和应用领域驱动设计的原则与六边形架构模式，构建一个模块化、高内聚、低耦合的应用程序。

项目采用 Java 17 和 Spring Boot 3.4.5，并集成了一系列现代化技术栈，包括：

*   **Spring Cloud**: 用于构建分布式系统。
*   **Sa-Token**: 提供简单易用的权限认证方案。
*   **MapStruct**: 高效的 Java Bean 映射工具。
*   **SpringDoc OpenAPI**: 自动生成 API 文档。
*   **Resilience4j**: 实现熔断、限流等弹性能力。
*   **Lombok**: 简化 Java 代码编写。
*   **Testcontainers & JUnit 5**: 支持集成测试。
*   **JaCoCo**: 代码覆盖率分析。

## 详细文档

项目内已包含详细的规划和设计文档，存放于 `doc` 目录下。强烈建议查阅这些文档以深入了解项目：

*   **[项目概述](./doc/0、项目概述.md)**: 包含项目简介、核心领域、项目架构、关键模块、技术栈和开发阶段等详细信息。
*   **[项目文档索引](./doc/README.md)**: `doc` 目录内所有文档的索引页。

## 主要模块

本项目由以下主要子模块构成：

*   `lovemp-common`: 通用工具和基础代码模块。
*   `lovemp-domain-person`: 负责人事或个人领域相关的业务逻辑。
*   `lovemp-domain-brand`: 负责品牌领域相关的业务逻辑。
*   `lovemp-domain-enterprise`: 负责企业领域相关的业务逻辑。
*   `lovemp-starter`: 项目的启动和配置模块，通常包含应用主类。

后续将为每个主要子模块生成其专属的 `README.md` 文件，以说明其具体功能和结构。
