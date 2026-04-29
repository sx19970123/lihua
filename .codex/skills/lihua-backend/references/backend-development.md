# 后端开发参考

## 模块

- Maven 父工程：`lihua/pom.xml`。
- 可运行应用：`lihua/lihua-admin`，入口类为 `LiHuaApplication`。
- 业务模块：`lihua/lihua-biz/lihua-system` 和 `lihua/lihua-biz/lihua-monitor`。
- 基础模块：`lihua/lihua-base/lihua-base-*`，包括 attachment、cache、captcha、common、dict、doc、excel、ip、job、log、mybatis、security、sensitive、web、websocket 等能力。
- 启动类使用 `@MapperScan({"com.lihua.**.mapper"})` 和 `@ComponentScan({"com.lihua.**"})`，新增包应保持在 `com.lihua` 下。

## 运行与配置事实

- Java/Maven 编译版本以 `lihua/pom.xml` 为准；当前 `maven.compiler.source` 和 `maven.compiler.target` 为 `25`。README 中的 Java 版本描述只能作为最低环境参考。
- 后端环境配置位于 `lihua/lihua-admin/src/main/resources/application.yml`、`application-dev.yml`、`application-prod.yml`。
- `application.yml` 配置了虚拟线程、multipart 限制、Jackson 忽略 null、MyBatis-Plus 逻辑删除字段 `delFlag`，以及 mapper XML 扫描路径 `classpath*:com/lihua/**/mapper/**/*.xml`。
- 数据源使用 dynamic-datasource；Redis 客户端使用 Redisson，并配置了项目自定义 `TypedJsonJacksonCodec`。
- 验证码配置使用 tianai captcha，包含 Redis key 前缀、过期时间、本地缓存和字体资源。
- Snail Job 默认未启用；如需启用，先检查 `LiHuaApplication` 中的 `@EnableSnailJob` 注释和 `application-*.yml` 中的 `snail-job` 配置。
- 附件上传模式由 `attachment.uploadFileModel` 控制。代码中可见的存储策略组件是 `LOCAL` 和 `ALIYUN-OSS`；不要把配置注释中的 `MINIO` 当作已实现能力，除非先确认代码已有对应策略。

## 新增模块与业务模块

- 父工程 `lihua/pom.xml` 的 `<modules>` 目前包含 `lihua-admin`、`lihua-base`、`lihua-biz`；新增一级 Maven 模块时需要同步维护 modules。
- 业务能力优先放在 `lihua-biz` 下，并在对应 `pom.xml` 中声明依赖；公共基础能力才进入 `lihua-base`。
- 新增可复用 base 模块时，需要在父工程 dependencyManagement 和相关业务模块依赖中保持版本与 artifactId 一致。
- 因启动类已扫描 `com.lihua.**`，新增包名应继续使用 `com.lihua` 命名空间，避免组件、Mapper 或配置类扫描不到。

## 标准业务结构

普通系统功能应参考 `lihua-system` 的结构：

- `entity`：数据库表实体。
- `model/dto`：请求和查询模型。查询、分页或前端入参不要随意污染 Entity，优先新建 DTO。
- `model/vo`：响应模型；多表字段或展示扩展字段可继承 Entity 后追加属性。
- `mapper`：MyBatis-Plus Mapper 接口，继承 `BaseMapper<Entity>`。
- `mapper/xml`：自定义 SQL XML，与 Mapper 包保持同层结构。
- `service`：业务接口。
- `service/impl`：业务实现，常见写法是继承 `ServiceImpl<Mapper, Entity>`。
- `controller`：Web 管理端接口。
- `controller/app`：App 专用接口。

## Controller 规则

- JSON API 优先继承 `ApiResponseController` 并返回 `ApiResponseModel<T>`，方便 SpringDoc 推导返回类型。
- 正常响应使用 `success(...)`，明确错误使用 `error(ResultCodeEnum, message)`。
- 使用 `@Tag` 和 `@Operation` 描述接口文档。
- 需要校验时，在 Controller 和请求体上使用 `@Validated`。
- 角色或权限保护使用 `@PreAuthorize`。现有管理员操作常用 `hasRole('ROLE_admin')`。
- 新增、修改、删除、导入、导出、下载、状态变更等重要操作使用 `@Log(description = ..., type = LogTypeEnum.*)`；密码等敏感参数要排除。
- 文件下载或导出接口应参考现有 `ExcelUtils.export(...)` 和附件流式下载模式，不要返回普通 JSON。

## 数据和持久化规则

- 分页 DTO 应沿用现有基础分页 DTO 模式，并在 Controller 中按已有做法使用 `MaxPageSizeLimit` 等校验分组。
- 分页 Service 返回 `IPage<VO>` 或 `IPage<Entity>`。
- 简单 CRUD 优先使用 MyBatis-Plus Wrapper 和内置能力；只有复杂查询、多表联查或 VO 投影才新增 Mapper XML。
- XML 名称与 Mapper 接口保持一致，例如 `SysUserMapper.java` 对应 `mapper/xml/SysUserMapper.xml`。
- 通用字段、自动填充和基础能力若已由 base 模块处理，不要在每个 Service 中重复手动设置。

## 平台接口

- Web 管理端 Controller 通常使用 `system/user` 这类路径。
- App Controller 放在 `controller/app`，通常使用 `app/system/profile` 这类路径。
- 如果某个领域已经有更窄的 App 专用接口，不要直接让 App 复用 Web 管理端接口。

## 共享能力

- 字典：使用后端字典模块维护字典数据和翻译，并与前端 `dict-tag`/Dict 工具配合。
- 附件：使用现有附件和存储服务处理上传、下载、秒传、分片和存储供应商行为。
- 日志：优先使用 `lihua-base-log` 的注解能力，不要临时写审计逻辑。
- 安全：使用基础安全模块和方法级安全注解；除非现有安全工具要求，不要手写认证判断。
- WebSocket：通知类实时能力优先使用基础 websocket 模块。

## SQL 和种子数据

- 功能涉及默认菜单、权限、字典、配置或表结构时，参考并维护 `res/db/lihua.sql`。
- 新增字典或配置 key 时，要保持后端常量/枚举和 SQL 种子值同步。

## 验证

- 在 `lihua/` 下运行 `mvn test` 做广义验证。
- 如果只影响单个模块，可使用 Maven `-pl <module> -am` 做更快的定向检查。
- 如果 MySQL、Redis、对象存储等服务不可用，仍应运行不依赖这些服务的编译/测试检查，并说明剩余验证缺口。
