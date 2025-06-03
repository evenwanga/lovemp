# lovemp-starter 模块

`lovemp-starter` 是整个 `lovemp-ddd-parent` 项目的启动模块。它负责应用的初始化、配置加载、以及将各个领域模块的服务和API整合并暴露出去。

## 主要职责

*   包含应用的**主启动类**（通常是 `LovempApplication.java`），用于启动 Spring Boot 应用。
*   负责全局的应用配置，例如 Web服务器配置、安全性配置（可能集成 Sa-Token）、跨域配置等。
*   聚合各个领域模块的应用服务，并通过 API 控制器（通常在 `api` 包下）将它们暴露为 RESTful 接口或其他形式的端点。
*   处理应用级别的横切关注点，如全局异常处理、请求日志、API文档生成（通过 SpringDoc OpenAPI）等。
*   管理项目的依赖打包和最终的可执行产物（通常是 JAR 或 WAR 文件）。

## 模块结构

该模块的主要代码位于 `src/main/java/com/lovemp` 目录下，其核心结构通常如下：

*   `com.lovemp.LovempApplication.java`: Spring Boot 应用的入口点，包含 `main` 方法并使用 `@SpringBootApplication` 注解。
*   `com.lovemp.api` (或 `com.lovemp.controller`, `com.lovemp.interfaces`):
    *   **API层/接口层**：包含 REST 控制器或其他类型的接口适配器，它们接收外部请求，调用相应领域模块的应用服务，并返回结果。这里是应用与外部世界交互的主要界面。
*   `com.lovemp.config`:
    *   **配置层**：包含应用级别的配置类。例如：
        *   Spring MVC 配置 (WebMvcConfigurer)
        *   Spring Security 配置 (如果使用)
        *   Sa-Token 配置
        *   SpringDoc OpenAPI 配置
        *   全局Bean定义（如 RestTemplate, ObjectMapper 等）
        *   数据库连接池配置（如果不由特定领域模块管理）
*   `com.lovemp.job` (可选): 如果有定时任务，可能会有此包。
*   `com.lovemp.listener` (可选): 如果有事件监听器，可能会有此包。

## 与其他模块的关系

*   **依赖于**：
    *   `lovemp-common`: 获取通用的工具类和基础配置。
    *   `lovemp-domain-person`: 依赖其应用服务来处理与自然人相关的API请求。
    *   `lovemp-domain-brand`: 依赖其应用服务来处理与品牌相关的API请求。
    *   `lovemp-domain-enterprise`: 依赖其应用服务来处理与企业相关的API请求。
    *   (以及 `pom.xml` 中定义的其他 `lovemp-domain-*` 模块)
*   **不被其他业务模块依赖**：作为启动模块，它通常是依赖链的最顶端。

## 如何运行

通常，可以通过执行 `LovempApplication.java` 中的 `main` 方法来启动整个应用程序。如果打包成可执行 JAR 文件，则可以通过 `java -jar lovemp-starter.jar` 命令运行。
