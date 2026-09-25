---
name: lihua-backend
description: 指导 lihua 仓（单体后端，Maven 多模块 Spring Boot）的服务端开发与验证。适用于 Maven 模块、Controller、Service、MyBatis-Plus Mapper、DTO/VO/Entity、字段校验、RBAC 安全、日志、字典、附件、WebSocket、系统配置、数据库 SQL，以及作为契约源头与 lihua-web / lihua-app 兄弟仓的跨端协作、与 lihua-cloud 仓的 base 层双仓同步。
---

# 狸花猫后端开发（lihua 仓）

处理本仓（单体后端）服务端改动时使用。兄弟仓：`lihua-web`（Vue 管理端）、`lihua-app`（UniApp 移动端）、`lihua-cloud`（微服务版后端，其 skill 为自包含版本，微服务差异见其专属章节）。本仓是跨端契约的源头：先定后端契约，再同步两端。「字典与枚举」「附件域」「通用契约」「红线与已否决方案」各节与 lihua-cloud 仓 skill **双仓同基线**——改动任一侧须同步另一侧（同差异地图纪律）。

## 仓库结构

- Maven 父工程：本仓根 `pom.xml`，`<modules>` 为 `lihua-admin`、`lihua-base`、`lihua-websocket`、`lihua-biz`；编译版本 `java.version=25`（README 的 Java 版本描述仅是最低环境参考）。
- 可运行应用：`lihua-admin`（入口 `LiHuaApplication`，`@MapperScan("com.lihua.**.mapper")` + `@ComponentScan("com.lihua.**")`——新增包必须保持 `com.lihua` 命名空间，否则组件/Mapper/配置类扫不到）。
- 业务模块：`lihua-biz/lihua-system`（系统/RBAC/字典/配置/通知等核心业务）、`lihua-biz/lihua-monitor`。业务枚举包在 `lihua-biz/lihua-system/.../com/lihua/enums`；控制器在 `.../com/lihua/controller`（根包只放实际路由控制器，双版本基类放 `controller/base`，App 版放 `controller/app`）。
- 基础模块（`lihua-base/`，14 个）：attachment、cache、captcha、common、dict、doc、excel、job、log、mybatis、security、sensitive、web、**ws**。
- **WS 消息面边界 `lihua-base-ws`**（base 下公共库，业务随手引）：包 `com.lihua.ws.push`（下行投递 `WebSocketPushUtils` + `WsPushMessage`）+ `com.lihua.ws.receive`（上行契约：SPI `WsMessageReceiver` + 回写通道 `WsReply` + 帧实体 `WsClientMessage`——连接层 handleTextMessage 解析帧后按 type 分发；内置心跳参考实现 `receive/impl/HeartbeatWsMessageReceiver`（客户端 30s 一跳 ping→服务端回 pong，不做超时踢线）。**处理器生效范围=连接所在进程**（引 lihua-websocket 的 JVM），业务服务进程注册的处理器收不到调用——跨服务上行业务处理预留 WS 上行 Redis topic 桥接，勿用进程内 event）。业务与连接层 `lihua-websocket` 的中间层，投递唯一入口；依赖 base-cache（Redis 原语）。
- 顶层受控基建库 `lihua-websocket`（不在 base 下，与 cloud 仓同名同位）：WS 连接持有层（/ws-connect 端点、会话登记、Redis pub/sub 订阅器）。**业务模块零依赖**，仅由 `lihua-admin` 装配引入；Java 包名仍是 `com.lihua.websocket`。
- 环境配置：`lihua-admin/src/main/resources/application.yml` / `-dev` / `-prod`（虚拟线程、multipart 限制、Jackson 忽略 null、MyBatis-Plus 逻辑删除 `delFlag`、mapper XML 扫描 `classpath*:com/lihua/**/mapper/**/*.xml`）；数据源 dynamic-datasource，Redis 用 Redisson + 项目自定义 `TypedJsonJacksonCodec`；验证码 tianai captcha（Redis key 前缀/过期/本地缓存/字体资源）；Snail Job 默认未启用（启用先查 `@EnableSnailJob` 注释与 yml `snail-job` 配置）。
- 种子 SQL：`deploy/db/lihua.sql` 保持 2.2.0 基线不动；结构/种子变更走独立幂等脚本（DDL 用 information_schema+PREPARE 判存在，DML 用 WHERE NOT EXISTS 或天然幂等 UPDATE），**脚本按业务名称命名**放在 `deploy/db/` 下（3.0 迁移期统一在 `upgrade-3.0.0.sql`，二开业务如 `upgrade-<业务名>.sql`）；涉及双仓的业务双仓同名同文件同步。
- 附件上传模式由 `attachment.uploadFileModel` 控制，已实现策略只有 `LOCAL` 与 `ALIYUN-OSS`——配置注释里的其他存储不是已实现能力，除非先确认代码已有对应策略组件。
- 仓库根的 `lihua/` 目录是接口测试等杂物，与工程代码无关。

## 核心规则

1. **代码落位决策树**：①新增业务功能 → 在 `lihua-biz` 下新建子项目（同步上级聚合 pom 的 `<modules>` 与运行应用 `lihua-admin` 的依赖引入，版本走父工程 dependencyManagement）；②偏公共、需要单独引入某个依赖实现一类能力的组件（如工作流、消息队列）→ 在 `lihua-base` 下新建对应子项目，向外部暴露接口供业务调用（新增 base 模块同步父工程 dependencyManagement 与消费方依赖）；③简单公共实现（如通用字符串校验）→ 在 `lihua-base-common` 的 `utils` 包中添加，或在现有类基础上扩展；④轻量级工具方法**先查 base 有无对应实现**——没有先补齐公共方法再使用，有则直接调用，勿在业务模块手搓 base 已有能力。
2. 遵循标准业务结构：`entity` / `model/dto` / `model/vo` / `mapper` + `mapper/xml` / `service` + `service/impl` / `controller` / `controller/app` / `controller/base`。查询分页入参不污染 Entity，优先 DTO；多表展示字段可继承 Entity 建 VO。
3. 面向前端的 JSON API 继承 `ApiResponseController` 返回 `ApiResponseModel<T>`（正常 `success(...)`、明确错误 `error(ResultCodeEnum, msg)`），配 `@Tag`/`@Operation` 文档注解。
4. 管理端写操作一律 `@PreAuthorize`（现有口径 `hasRole('ROLE_admin')` 粗粒度；细粒度 authorities 通道保留但不消费）+ `@Log(description=..., type=LogTypeEnum.*)`，密码等敏感参数排除出日志。操作日志用 `lihua-base-log` 注解能力，不临时写审计逻辑；实时通知投递用 `WebSocketPushUtils`（lihua-base-ws，Redis pub/sub 扇出，勿直调 WebSocketManager）——`lihua-websocket`（顶层连接层）由 lihua-admin 装配引入，业务模块 pom 零依赖；安全判断用基础安全模块，不手写认证判断。
5. App 专用接口放 `controller/app`，路径 `app/...` 前缀（如 `app/system/profile`）；已有更窄 App 接口的领域不要让 App 复用管理端接口。
6. 持久化用 MyBatis-Plus：分页 `POST /page` + `@Validated(MaxPageSizeLimit.class)` + BaseDTO（pageNum/pageSize 上限 999999/100），Service 返回 `IPage<VO>` 或 `IPage<Entity>`；简单 CRUD 用 Wrapper，只有复杂查询/多表/VO 投影才加 XML，XML 名与 Mapper 接口同名同层（`SysUserMapper.java` ↔ `mapper/xml/SysUserMapper.xml`）。
7. 有逻辑消费的字典值一律经 `DictEnum` 枚举引用，禁止裸字面量与私有常量（详见「字典与枚举」）。
8. 基础能力（审计字段填充、分页、逻辑删除等）已由 base 模块处理的，Service 不要重复手动设置。

## 二开边界与一体感

**边界：脚手架 vs 二开**

- 平台公共层（`lihua-base-*` 各模块、`lihua-admin` 启动装配、SecurityConfig 等安全配置）是脚手架契约——二开**用而不改**；确需改动 = 影响全部模块 + 脚手架升级时的手工合并成本，动前必评估影响面（公共 jar 配置义务、双仓同步纪律见红线/差异地图）。
- 平台既有端点契约（`system/...` 的 URL、DTO/VO 字段、权限标识、种子数据）**只增不改**——它们被 web/app 双端消费且进入升级路径；新能力以新端点/新字段扩展，勿重构平台接口形态。
- 二开业务建自己的 `lihua-biz/<业务模块>`（见代码落位决策树）；`lihua-system`/`lihua-monitor` 是平台自身功能（RBAC/字典/配置/通知/监控），勿混入二开业务。**biz 模块在设计上平级、互不 Maven 依赖——这正是分包边界**：跨业务模块的数据/动作走领域事件解耦（先例：monitor 发布 CacheBlackIpEvent → system 监听刷新缓存）；用户/部门等平台数据一律经登录用户上下文 `LoginUserContext`（base-security）取 `CurrentUser` 会话内容（用户/部门/角色/岗位等），勿跨模块查平台表。
- **配置归属**：biz 模块不自带配置文件，全部配置集中在 `lihua-admin` 的 `application*.yml`；二开模块的配置项也写在那里，用 `@ConfigurationProperties` 绑定（fail-fast 限域、Duration 约定见红线/附件域；敏感值环境变量注入，勿写死仓库）。
- **匿名接口**：免登录端点在 `SecurityConfig`（base-security）的 permitAll 三组清单登记——后台接口组、app 接口组（路径带 `app/` 前缀）、系统基础组（captcha/actuator/ws-connect/swagger）；新免登录端点加进对应组，其余一律 `anyRequest().authenticated()`。

**一体感：照先例写代码**

- 落笔前先在仓内找同类先例（最接近的 Controller/Service/Mapper 与前端页面），照其结构写——不引入第二套风格（自造返回包装、自拼分页、绕过字典工具都是破窗）。
- 横切能力一律用平台现成实现，新增写端点配三件套（`@Tag`/`@Operation` 文档 + `@PreAuthorize` 权限 + `@Log` 操作日志）：统一返回 `ApiResponseController`、全局异常 `ServiceException` → `GlobalExceptionHandle`、参数校验 `@Validated` + 分组、分页 BaseDTO + `MaxPageSizeLimit`、登录态 `LoginUserContext`、字典 `DictEnum` + `DictUtils`、附件 base-attachment 全家、实时通知 `WebSocketPushUtils`（lihua-base-ws，Redis pub/sub 扇出）+ `TransactionSendUtils.runAfterCommit`、防重提交 `@PreventDuplicateSubmit`、排序归一化 `SortUtils`、IP 归属地 `WebUtils`。
- 表命名边界：平台表前缀 `sys_`；二开业务表用业务域自己的前缀，勿冒用 `sys_`。

## 字典与枚举

- **建枚举判据**：后端有逻辑消费（if/switch、写入特定语义值）的字典才建枚举；纯展示字典（tag 颜色、下拉选项、Excel label）不建，展示走 `DictUtils` 字典缓存。
- **一个字典类型一个枚举**，实现 `base-common` 的 `DictEnum` 接口（`getType()`/`getValue()`）；读判断与写值都走枚举。按消费方分层：跨模块消费放 `base-common/enums`（如 `SysStatusEnum`，含 `toggle(current)` 收口翻转），单模块消费放该模块 `enums` 包（base-log/base-attachment 属域模块不算公共层）；出现第二个消费模块时下沉 base-common。域字典枚举一律 `implements DictEnum`（getType 返回字典 typeCode）+ 值集对齐种子（Java 侧全量替换、XML 字面量保持+注释锚定）。
- **三通道**：选项集→字典枚举；运行时布尔（"是否"语义字段）→Boolean 化（动契约需前端同步）；结构标记（树根 `"0"`）→常量。禁止跨字典复用通用枚举——`sys_whether`/`sys_status`/`sys_log_status` 值集同为 0/1 但语义不同，各自建枚举。
- **所有权边界**：字典 value（码）归代码所有，不可在字典 UI 改码（Service 层配禁改 guard，如 sys_status save 直接抛异常）；label/tagStyle/sort 归字典 UI 所有，运行时可改。新增字典值 = 枚举加常量 + 同步种子。
- **新建业务字典三件套**：①枚举（消费模块 enums 包，implements DictEnum）②`sys_dict_type`/`sys_dict_data` 种子（双仓 lihua.sql 基线 + 对应业务命名的幂等脚本段）③存量引用点全量替换为枚举（`AppVersionStatusEnum.PUBLISHED.getValue()` 引用形态）。
- **逻辑删除与缓存铁律**：①逻辑删除表删除后还要按已删行取信息（如清缓存要 typeCode）必须**先查后删**——全局 logic-delete 会给后续 selectList 追加 `del_flag='0'`，已删行查不到；②QueryWrapper 勿再手写 `del_flag='0'`（与全局重复）；③缓存回源 SQL 必须带 `ORDER BY sort ASC`，否则缓存内选项顺序不保证。
- **updateStatus 判空两模式**：无缓存模块前置 selectById 判空抛「xx不存在」（防无效 id 静默假成功）；有缓存模块后置判空仅用于跳过缓存清理。
- **校验注解范式**：save 直收实体的模块，varchar 按 DB 列长补 `@Size(max=N)`（消息「xx长度不能超过N个字符」），char(1) 状态字段补 `@Pattern(regexp="^[01]$")`；主键/数字列不加。
- **校验分组陷阱**：`@Validated(XxxGroup.class)` 只评估挂了该组的约束——补新注解时 `groups` 必须跟随字段既有组集合（或按表单适用组挂），否则静默不生效（Default 组约束在组校验下不评估）。
- **同级排序归一化**：业务表带同组排序列的，保存/删除后调 `SortUtils.normalize(mapper, 组键列名, 组键值)`（base-mybatis，实体 `implements SortEntity`）——组内重编 1..N 只写变化行（单条 CASE 批量，不碰审计字段）。接入点三处：insert 后、update 前取原组键后两组各归一、deleteByIds 删前收集组键删后归一。有缓存域 changed>0 才刷缓存；单组上万行再升级模块专用窗口 SQL。
- **SQL 禁 `${}` 列名拼接**：列名动态的需求拆成列名明确的专用 Mapper 方法。
- **登录态模型字段须与查询列对齐**：随接口序列化的模型（如 `CurrentRole`）只保留 XML 实际查询的列，恒 null 字段（status/delFlag/remark）删除——模型字段必须有数据来源。
- **动作端点 URL 范式**：状态开关 `PUT /资源/status/{id}/{currentStatus}`（URL 去动词）；命令类 `POST /资源/动作名`；查询/保存/删除既有约定不动。前端 api 调用点与端点同一微步原子改。

## 附件域

- **上传面端点一律显式 DTO**，实体不直收（防 id/status/path 经表单注入）；派生字段（originalName/size/type/md5）服务端自文件流派生、uploadMode 端点自证、businessName 缺省回显 businessCode；响应统一 VO（首次访问链接随上传返回）。上传类型限制是可选配置 `attachment.uploadAllowExtensions`（空=不限制，作用全部上传，与附件公开性无关）。
- **契约参数名为 Java 关键字时**：字段名以 `isXxx` 承载 + `@Getter/@Setter(AccessLevel.NONE)` 关闭 Lombok 生成 + 手写 `getXxx/setXxx` 别名访问器——表单（BeanWrapper 按 setter 属性名）与 JSON（Jackson 按属性名）都以契约属性名收参，且 Bean 只暴露单属性不产生双属性。
- **同 md5 多行是常态**（秒传复制行、重复上传各持文件）：按 md5 查找不可任取 `list.get(0)`，判存/秒传统一「任一行物理文件在存即命中」语义（delFlag=0 + status=0 + path 去重逐个 isExists）。**行定位不信任客户端回传 id**（chunk/merge 按 uploadId 查 start 建立的行）。
- **MultipartFile 全链路禁 getBytes**（唯一全量进堆入口）：md5 用 `DigestUtils.md5Hex(InputStream)`（commons-codec，1KB 缓冲流式）；LOCAL 落盘 `FileChannel.transferTo`（零拷贝）；OSS 走 `getInputStream()` 交 SDK——大文件上传内存占用恒定在小缓冲区级别。
- **写侧路径校验三道防线**：落盘路径构造唯一入口 `SysAttachmentStorageServiceImpl#buildUploadFilePath`（拼接 businessCode 段后必经 `FileUtils.checkWritePath`，统一 `/` 分隔）；物理写盘必经 `FileUtils.upload`（LOCAL 策略与分片临时片自动覆盖；OSS putObject 不经 FileUtils，靠构造点校验）。写侧判定=两侧 `toAbsolutePath().normalize()` 后 `Path.startsWith` 目录包含——勿复用下载侧 `checkPath`（它要求目标已存在并做软链解析，首写前不成立）。
- **分片合并原子性与幂等**：合并/生成类落盘一律「唯一临时名 + 校验 + 原子 move（`ATOMIC_MOVE` 降级 `REPLACE_EXISTING`）」，目标只以完整形态出现，异常兜底只清自己的临时文件、**永不删目标**（目标在存=并发另一路的成功产物）。无锁幂等三件套：入口终态短路（行 status 已成功直接返回 VO）+ 失败复查（`isExists` 目标在存按成功收尾，不把成功行覆盖成失败）+ 临时资源清理走 `cleanChunks`（幂等/不抛错；OSS abortMultipartUpload 是计费资源必须正确中止）。md5 复用与秒传同信任模型（声明 md5 + 物理在存即复用命中行 path）；复用行的 originalName/extensionName 跟随本行声明、不复制命中行。
- **HMAC 签名链接**：自签自验短时效链接一律「明文参数 + HMAC 签名」（`SignedUrlUtils`：`<expireMs>.<Base64Url(path)>.<hmacHex(path::expireMs)>`）——**禁止用对称加密伪装防伪**（AES key 随 jar 发布=公开可伪造，加密≠认证）。签名密钥外置 `attachment.download-sign-key`，@PostConstruct fail-fast（缺失启动失败、禁止默认值回退），生产经环境变量注入（yml 占位符 `${ATTACHMENT_DOWNLOAD_SIGN_KEY}` 无默认值即强制注入）；签名比对必须 `MessageDigest.isEqual` 常量时间（防时序侧信道）；验签失败不区分格式/签名原因（不为探测提供信息），非法→`PARAMS_ERROR(400)`。时效收口单漏斗：`attachment.downloadExpireTime`/`downloadMaxExpireTime` 缺省 1h/30d、默认≤上限启动校验——策略类参数用「代码缺省值+可覆盖」而非硬编码常量；**配置时长一律 `Duration` + `@DurationUnit(ChronoUnit.MINUTES)`**（yml 写 `1h`/`30d` 带单位、裸数字按分钟兜底），勿用裸 int 分钟数（第三方/框架属性类不受此约束）。
- **下载单路由与数据面分层**：唯一端点 `download` 双参二选一（`key=`私密验签免查库 / `fullPath=`公开行校验），permitAll 链接即凭证；**签发与存储模式无关**（一律 entry 相对链，对象存储绝对 URL 禁止直发客户端）。公开性判定只认行级 `is_public`（Caffeine path→is_public 长 TTL——不可变标记免失效 + 索引兜底查行），目录白名单模式已废弃。数据面 `getDownloadRedirectUrl`：OSS=302 现场短时效预签名（附件字节不过网关，302 缓存必须短于预签时效）、LOCAL=null 直读 File 流式。
- **头像 URL**：统一走 `SysUserService.getAvatarUrl(userId)` / `resolveAvatarUrl(avatarJson)`，勿自行拼 URL——`sys_user.avatar` 是前端 AvatarType JSON 串（value=path、type∈image/icon/text），仅 `type=image` 才有附件 URL（其余/解析失败返回 null）；entry URL 组装一律 `AttachmentUrlUtils.resolvePublicUrl(path, urlBasePath)`（纯函数在 base-common/utils/url，无查库；反代前缀经 `attachment.url-base-path`，服务端不持有部署前缀）；avatarUrl 填充在 CurrentUser 对象上随 userInfo 下发（不单列响应顶层字段，落库与缓存均不依赖）。

## 通用契约

- **响应码比较必须 equals**：`ResultCodeEnum.code` 与 `ApiResponseModel.code` 均为 Integer——字面量 `200 == resp.getCode()` 走拆箱值比较合法，但换成枚举常量 `SUCCESS.getCode() == ...` 后变为引用比较（200 超出 Integer 缓存 −128~127 必不相等，编译器不报错、运行期静默判否）——远程响应码判断一律 `ResultCodeEnum.SUCCESS.getCode().equals(resp.getCode())`。
- **校验链下沉 Service 判据**：控制器 `error(ResultCodeEnum.ERROR, msg)` 改抛 `ServiceException(msg)` 的前提是全局异常处理器（`GlobalExceptionHandle` 的 BaseException 通道）返回同构响应（HTTP 200 + 业务码 + msg），前端 `resp.code !== 200` 分支行为等价；副作用为业务失败多记一条 error 日志（全库既有模式，可接受）。
- **save 接口 Entity 直收 body 是全库既定口径**（dict/post/dept/role/menu/setting + @Validated/分组校验），BaseEntity 字段（delFlag/审计字段）经 body 可注入的面已接受为已知口径——后续体检不再提议收窄 DTO；notice/profile/log/user/attachment 的 DTO 形态是各自模块历史定稿，非全库规范。
- **双版本控制器去重**：管理版与 App 版（`controller/app`）方法体逐行相同时，共有端点收抽象基类（形态如 `BaseSysAttachmentStorageController`：无 @RestController/@RequestMapping/@Tag，`@Resource protected` 注入 Service，方法注解随方法留基类，Spring 继承扫描自动注册路由；**基类统一落 `controller/base` 子包**——根包只留实际路由控制器，与 controller/app 端侧分组对称）。子类只挂各自 `@RequestMapping` 前缀与 `@Tag` 分组；管理版独有端点族由管理版子类声明、App 子类可为空壳；**对外行为双版本一致是去重前提**，端点集差异必须保留在子类侧。推广判据：仅 `@Operation` summary 措辞差异视为可抽；端侧逻辑差异端点一律留子类、不引入钩子方法；注解差异经拍板统一=接受行为对齐；**同路径 HTTP 动词分叉（如 App POST vs Web GET）不可抽**——同一基类方法无法双动词，强抽必改一端契约；子类/基类跨包必须显式 import（漏 import 编译期才暴露）。
- **异步上下文接力唯一通道 = ContextCopyTaskDecorator**（base-web）：@Async 等经 Spring executor 提交的任务自动被装饰（提交时快照 MDC+SecurityContext、执行时覆盖恢复、finally 清理——池化执行器不串不漏，正确性与执行器形态解耦；写返回值的 @Async 方法同样被装饰）。**MODE_INHERITABLETHREADLOCAL 策略已下线勿恢复**——其正确性依赖「每任务新线程」形态，关虚拟线程回池化 executor 即从正确变串（静默数据错误）。**非 executor 线程（手动 new Thread/commonPool/parallelStream/@Scheduled/reactor 调度）明确无上下文**（SecurityContext 读到 null）——需要上下文时经 executor 提交或显式传参。setTaskDecorator 单槽（新载荷进 decorator 三段式各加一行，不做平行 decorator）。
- **token/IP 取值唯一源头**：取 token/IP 一律经 `WebUtils.getToken/getIpAddress`（base-web，web 域唯一源头，getRegion 归属地查询也在此）或 `LoginUserContext`（security 上下文，兜底链已指向 WebUtils），任何新代码不得自行读 Authorization/Request-IP 头解析。base-ip 模块已整体并入 base-web、IpUtils 类已退休；mono 无网关头，IP 由 IpResolveUtils 三级直解。**防双击写接口挂 `@PreventDuplicateSubmit`**（base-web，唯一参数 `interval` 秒默认 5；幂等键=同会话 token（匿名退 IP）+URI+参数摘要，SET NX PX 占键、窗口期 TTL 自然过期不删键；重复抛 DuplicateSubmitException → REPEAT_SUBMIT_ERROR(511)「操作过于频繁，请稍后再试」）——参数变化生成新键不误伤修正重提，多端登录 token 不同互不影响。
- **新表设计默认口径**：业务表继承 `BaseEntity`（base-mybatis，审计字段 + 逻辑删除 `delFlag`）；带状态语义的字段用字典 + 枚举承载（见「字典与枚举」）；有同组排序需求加 `sort` 列且实体 `implements SortEntity`（配套 SortUtils 归一化，见上）；char(1) 状态列配 `@Pattern`、varchar 按 DB 列长配 `@Size`（校验注解范式见「字典与枚举」）。
- **跨模块解耦用领域事件**：`ApplicationEventPublisher` 发事件、监听器消费，勿跨模块直调内部实现。**事件载体统一放 `base-common` 的 `com.lihua.common.model.event/<业务域>/` 子包、类名统一 `Event` 后缀**——载体可携带数据（`event/log/LogEvent`，日志落库传参）也可为纯信号空类（`event/setting/CacheBlackIpEvent`，IP 黑名单缓存刷新）；同一业务域多个事件在域包内并列分类。先例：操作日志 = HandleRecodeLog 发布 LogEvent → SysLogService 监听落库。原「权限更新三件套」的进程内事件腿（PermissionUpdateEvent + PermissionUpdateEventListener）已退役：WS 实时推送统一 `WebSocketPushUtils.push`（lihua-base-ws，Redis pub/sub 扇出）；`PermissionUpdateUtils`（base-security）保留 Redis 标记（红点事实源，markChanged/hasChanged/clear）不变。事件不跨实例传播——红点等判定必须有独立事实源。事件监听侧需要异步时经 executor 提交（走 ContextCopyTaskDecorator 通道，见上条）。
- **事务后执行用 `TransactionSendUtils.runAfterCommit`**（base-common `utils/spring`，自 base-websocket 下沉）：当前存在活动事务则挂 afterCommit 执行、否则立即执行——事务内直接推送会先于数据提交到达，客户端收到消息立即回拉时读不到关联数据；凡「写库 + 推送/通知」组合一律包 runAfterCommit（**在发布点包裹，勿嵌套**——afterCommit 回调内事务同步仍激活，二次注册不会执行）。

## 红线与已否决方案

- **跨模块字段/方法「死活」判定**：判 VO/Entity 字段可删前按 **getter/setter 调用形态**全仓 grep（`xx.getUserId()`、`SysPostVO::getUserId`），不能只搜类型名——消费方常以变量名调用（`post.getUserId()`），按类型名匹配会漏判并引发跨模块编译错误。删 Mapper 方法前按方法名 grep 双仓库（含测试）。**grep 消费点禁止 `head` 等截断管道**——跨模块删除判定必须看全量输出，并以宽模式（函数名裸 grep 全仓）二次复核。删 public 方法/字段前先看是否 @Override 框架接口（UserDetails/InitializingBean 等）——接口多态调用（如 DaoAuthenticationProvider 调 `UserDetails.getPassword()`）静态 grep 不可见，登录链功能字段可能「零 grep 消费」。
- **枚举/常量/配置键死活**：grep 必须**「符号名 + 值字符串」双模式**（消费代码常只引用枚举名，值字符串零命中 ≠ 零引用）；「零引用」结论必须以**消费方模块编译（`-am` 全链）**佐证——只编定义模块测不出引用断裂。删除类改动 commit 前双核一遍。
- **依赖死活**：`mvn dependency:analyze` 报的 "Unused declared dependencies" 几乎全是运行时依赖误报，逐类核对勿直接删：①starter 族（spring-boot-starter-*/redisson/springdoc/snail-job client）——自动装配运行时需要，编译期无 import 是常态；②驱动与自动装配类（mysql-connector-j、mybatis-plus、dynamic-datasource、resilience4j starter）——JDBC 驱动/装配入口永不直接 import；③内部聚合模块——由 lihua-admin 聚合打包消费或经传递路径供给。真死依赖画像：**非 starter/驱动/聚合的工具库**，analyze 报 Unused 且按包名全仓 grep import 零命中才可判死。根 pom 版本属性核查按 `${属性名}` grep 全部模块 pom（双仓库都查）。
- **公共 jar 配置义务边界**：被宿主 `@ComponentScan("com.lihua.**")` 全量注册的 base-* jar，其中 fail-fast 校验与无默认值 `@Value` 会把配置义务强加给所有引入方——①fail-fast 与必填占位符限定在「声明了该域角色的服务」内生效（先例：AttachmentProperties 以 `upload-file-model` 非空判定存储角色才校验）；排查占位符勿只看配置类——jar 内任意 @Component 自带的无默认值 `@Value` 同样被宿主解析；Class 型共享配置绑定会加载整个继承链，条目须在全部消费方 classpath 可解析且父类无 servlet/security 栈依赖。②对外下发的 URL/链接一律相对链（部署反代前缀由各端 base 拼接），服务端不持有前缀配置——前缀是部署态，多处维护必漂移。③跨服务消费纯函数优先下沉 base-common，勿让消费方依赖域 jar——域 jar 会拖进该域全部 bean 与配置义务；域 jar 内做角色门控只是防御而非豁免。
- **依赖升级两铁律**：版本核对必须直接查 Central `maven-metadata.xml`（`versions:display-dependency-updates` 不穿透 BOM import）；验证必须 `mvn clean compile/test`（增量编译按源码时间戳判断，依赖版本变更不触发重编，普通 `mvn compile` 假绿）。
- **已否决勿再提议**：save 接口收窄 DTO；恢复 MODE_INHERITABLETHREADLOCAL。
- **跨双仓 Bash 坑**：复合命令 cd 与相对路径组合易发生 cwd 漂移（曾在错误仓库执行 sed/perl/rm）——跨仓操作一律绝对路径或 `mvn -f` 指定 pom，rm/批量改后立即 grep 验证目标仓库。
- **不强制统一项（已拍板，勿再提议立规）**：单元测试（项目无单元测试）、`@Transactional` 事务边界、定时任务（Snail Job）接入、Excel 导入、`ResultCodeEnum` 错误码扩展——均不做脚手架级强制规范，按所在模块既有写法随项目走。

## 双仓库差异地图（lihua ↔ lihua-cloud）

> 本节与 lihua-cloud 仓 `skills/lihua-cloud-backend.md` 的同名章节同文；**改任一处须同步另一处**。改 base 层/横切逻辑前必读；两仓 base 层改动遵循双同步原则（同 commit 粒度、行为一致）。

- **直接复制即可**（源码级一致，仅包名差异）：base 的 attachment/captcha/doc/job/mybatis/sensitive/ws 全部 + 顶层 `lihua-websocket`（双仓同名同位的受控基建库）；`lihua-system` 绝大多数文件逐字节一致（@Log/@PreAuthorize/@Sensitive 用法同构）。cloud 多一个 `lihua-base-client`（远程调用基建），mono 无。
- **需 cloud 特有处理的分叉点**：
  1. 认证/注册/验证码：mono 在 lihua-system，cloud 拆到独立 lihua-auth 服务且走 RPC（`lihua-api-system` 的 SysUserAuthClient + facade）；AuthenticationManager Bean 位置不同（mono 在 SecurityConfig，cloud 在 auth 服务 LoginConfig）。
  2. 附件：cloud 归 lihua-file 服务，配置在 nacos `lihua-file.yaml`（上传模式/100MB 限制/下载链接过期）。
  3. 操作日志落库：mono 用 ApplicationEventPublisher 发事件（事件模型 LogEvent，`common/model/event/log`），cloud 走 `LogClient` 双实现——本服务 `LogClientLocalImpl`（@Primary 本地直写）、跨服务 `SysLogClientFacade` RPC（base-log 依赖 lihua-api-system）。
  4. IP 黑名单：mono 是 Servlet 拦截器（RequestIpInterceptor + Ip2region），cloud 上移到 gateway 过滤器并把 IP 写入 `Request-IP` header 透传下游。
  5. SecurityConfig：cloud 多 3 条内部端点 permitAll（log insert / user auth / setting）。
  6. TokenEnum 位置：cloud 在 base-common，mono 在 base-security（值相同，硬编码 JWT 密钥）。
- **配置双轨**：mono 用 application-dev/prod.yml，cloud 用 nacos（仓库导出 `deploy/nacos/nacos_config_export.zip`，目录=group）；token 参数唯一来源是 nacos `lihua-common.yaml`。
- **WS 部署形态**：mono 单 jar WS 嵌 admin 进程（顶层 `lihua-websocket` 由 lihua-admin 显式引入，业务模块零依赖；推送投递经 Redis pub/sub 回本进程订阅器推送，mono 部署多份天然扇出）/ cloud 独立 `lihua-ws` 服务（无库、可多实例；gateway 路由 `/ws-connect/**` 指向 lihua-ws；system 已卸除 WS 连接层依赖）。两形态同构：`lihua-websocket`（连接持有方，与 cloud 的 `lihua-api` 同类的受控基建库）与业务模块完全隔离、仅经 Redis 交互（WS_PUSH 投递/订阅 + once token 握手鉴权读写）；业务侧投递一律 `WebSocketPushUtils.push`（lihua-base-ws），禁止依赖 lihua-websocket/直调 WebSocketManager。
- **范式约定**：分页 `POST /page` + `@Validated(MaxPageSizeLimit.class)` + BaseDTO；权限维持 `hasRole('ROLE_admin')` 粗粒度（细粒度 authorities 通道保留但不消费，项目定位类若依脚手架）。

## 跨端协作（契约源头）

1. **先定后端契约**：统一返回、DTO/VO、校验分组、日志/安全注解先行确认，再同步前端。
2. **接口前缀**：管理端 `system/...`；App 端 `app/system/...`（实现在 `controller/app`）。App 已有更窄专用接口的领域，不让 App 复用管理端接口。
3. **DTO/VO 对齐**：请求字段对齐后端 DTO，列表/详情展示字段对齐 VO；两个前端复用全局响应/分页类型。
4. **权限联动**：`@PreAuthorize` 的权限标识要让对应菜单/权限配置可被开发者发现，必要时更新菜单种子 SQL。
5. **字典联动**：字典驱动字段维护字典数据，前端用字典工具/dict-tag，不硬编码标签。
6. **附件联动**：复用现有附件上传/下载组件与后端附件存储服务。
7. **同步顺序**：后端契约 → `lihua-web` 的 `src/api/<domain>/<feature>/`（+`type/`）与页面（静态路由在 `src/router/index.ts`，多数业务菜单走动态配置）→ `lihua-app` 的 API 与页面（pages.json 注册、分包优先）。
8. **兄弟仓验证**：lihua-web `npm run type-check`（生产打包 `npm run build`）；lihua-app `npm run type-check`（涉及具体平台才跑对应构建脚本）。
9. 优先运行能覆盖本次改动的最小检查；兄弟仓改动以各自仓 skill 为准。

## 部署

- Dockerfile 基础镜像必须与根 pom 编译主版本匹配（Java 25 = class major 69，低版 JRE 容器 `java -jar` 即 `UnsupportedClassVersionError`）；镜像选 **Ubuntu 系变体（noble/jammy），禁用 alpine**——精简镜像无 fontconfig/系统字体，验证码字体（base-captcha 中文 TTF，经 `Font.createFont` + Java2D 渲染）加载报错；temurin Ubuntu 系镜像自带 fontconfig + fonts-dejavu。tag 用精确 pin（如 `eclipse-temurin:25.0.4_7-jre-noble`）；README 镜像口径随 dockerfile 同步更正。
- 换/升镜像容器内实测三件套（勿只看 tag 名）：① `java -version` 对主版本；② `fc-list`/`dpkg -l` 查 fontconfig 与 dejavu 字体在位；③ 挂 captcha-font 真实 TTF 跑 `Font.createFont` + `drawString` 中文渲染探针（非空白像素 >1000 且 `canDisplay('狸')` 为真才判字体链路真通）。

## 验证

- 定向优先：`mvn -pl <module> -am compile/test`（如 `mvn -pl lihua-biz/lihua-system -am test`）；改动共享模块或公共契约时跑更广检查；广义 `mvn test`。
- MySQL/Redis/对象存储不可用时，仍运行不依赖这些服务的编译/测试检查，并说明剩余验证缺口。
