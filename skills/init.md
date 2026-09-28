---
name: init
description: 狸花猫脚手架（单体版）二次开发初始化流程。当用户下载项目后首次接管、说「init / 初始化项目 / 项目初始化」时使用：项目改名（三档可选）、版本重置 1.0.0、功能裁剪（部门岗位/App端/监控/组件演示页/通知公告）、多仓工作区布局引导。产出改动清单经用户确认后分步执行，每步 commit。
---

# 二次开发初始化（init）

把脚手架变成「用户的项目」：改名、版本重置、功能裁剪、工作区布局。**本质是一次大改，纪律高于速度**——问卷 → 改动清单确认 → 分步执行（每步验证 + commit）→ 报告。

## 角色与编排

- 本文件驱动**全工作区**初始化（lihua 后端 + 兄弟仓客户端）；`lihua-web` / `lihua-app` 仓各有同名 init.md 承接本仓步骤，由本流程按需驱动。
- 用户可能从任一仓发起：先做 Phase 0 工作区发现，再统一问卷、统一计划、逐仓执行。
- 单独初始化客户端仓（无后端在场）时，提示最少组合为「后端 + web」，并只执行该仓 init.md 的本仓部分。
- `lihua` 与 `lihua-cloud` 是二选一的两套后端：同存于一个工作区时，先向用户确认初始化哪套（另一套不纳入本次 init）。

## Phase 0 工作区发现

1. 在当前目录与父目录定位各仓：`lihua`（本仓，单体后端）、`lihua-cloud`（微服务后端）、`lihua-web`（Web 管理端）、`lihua-app`（移动端，可选）。
2. 最少组合 = 后端 + lihua-web。缺仓给出 clone 引导（放入同一父目录，agent 打开父目录即可一同管理），不阻塞已就绪部分的问卷。
3. **生成 agent 入口指针**（init 产物，脚手架仓库不携带）：为每个就绪仓的根目录创建 `AGENTS.md`（列本仓 `skills/` 下各文件与用途、写明「二次开发首次接管对 agent 说『执行 init』」、约定 skill 与代码冲突时以代码为准并回报）与 `CLAUDE.md`（内容仅一行 `@AGENTS.md`）——保证后续会话能发现 `skills/`。
4. 输出工作区地图：仓清单 × 角色 × 将执行的 init 步骤。

## Phase 1 问卷（一次性收集，均给默认值）

1. **项目名称**：新名 slug（用于包名/artifactId/数据库名/存储键前缀）+ 中文名（用于品牌显示）。
2. **改名层级**（三档，向上包含）：
   - **品牌层**：README 标题与描述、web `src/app-info.ts` 应用名与页面标题、登录页文案、app `src/manifest.json` name 与首页徽章文案、compose 项目名。
   - **品牌+标识层**（加）：Maven artifactId 与模块目录名（`lihua-*` → `<slug>-*`，父 pom `<modules>`/依赖坐标联动）、数据库名（`application*.yml` + compose + deploy/db 说明）、web/app `package.json` name、存储键前缀（web/app 内 `lihua_` 前缀，grep 定位）、`deploy/db/lihua.sql` 文件名与 yml 引用。
   - **全量**（加）：Java 包名 `com.lihua` → `com.<slug>`——包目录 `git mv`（两步法：先改临时名再改目标名）+ 全量文本替换（java/xml/yml 中的 import、声明、**@ComponentScan/@MapperScan 字符串**、mapper XML namespace）→ `mvn clean compile` 兜底。规模约 300+ 文件，纯机械可执行。
3. **初始版本**：默认 1.0.0——父 pom `<version>`（模块 pom 随 parent 联动）、web/app `package.json`、app `manifest.json` versionName=1.0.0 / **versionCode=100**（App 版本检查按 versionCode 数值比较，留百位空间）；web `src/views/index/version-record.ts` 首页版本记录重置为「1.0.0 首发」（是否保留脚手架历史条目问用户）。`deploy/db/upgrade-3.0.0.sql` 保留为脚手架迁移史不动，用户后续变更按业务命名新脚本。
4. **功能裁剪**（多选）：部门+岗位 / App 端 / 监控 / 组件演示页 / 通知公告（预设清单见 Phase 3.3）。
5. 复述全部选择，确认后进入 Phase 2。

## Phase 2 改动清单确认

按选择产出全工作区改动清单（仓 × 文件簇 × 动作），**用户确认后才动手**。裁剪类必须先 grep 摸清引用面再列清单——死活判定纪律见 `skills/lihua-backend.md` 红线节（getter/setter 调用形态 grep、禁截断管道、@Override 检查、消费方编译佐证）。

## Phase 3 执行（顺序：改名 → 版本 → 裁剪 → 地图；每步一 commit，每步验证）

### 3.1 改名
- 每档完成立即验证：品牌层 = web/app 构建；标识层 = `mvn clean compile` + 前端 type-check + **旧名残留 grep 计数清零**（有意保留项——如 iconfont 字体族、历史资产文件名——在清单中标注豁免）。
- 包名层：`git mv` 包目录 → 全量替换 → clean compile → 残留 grep 清零。

### 3.2 版本重置
Phase 1.3 的全部版本落点一步完成，构建验证。

### 3.3 裁剪预设（每个预设：先出细清单 → 确认 → 删 → compile + type-check + grep 审计）

**A. 部门 + 岗位**（联动面最大；本清单是**耦合类别清单**——类别必须穷尽，文件级清单由运行时 grep 生成后经确认删除）
- 域本体：SysDept/SysPost 实体、controller（含 app 版）、service、mapper/XML、DTO/VO、user_dept 关联（SysUserDept）、相关字典。
- **用户管理消费点**：SysUserDTO/SysUserVO 的部门岗位字段、用户保存/编辑的部门岗位分配与用户列表部门列（SystemUser）。
- **系统设置消费点**：自助注册配置（SysSettingDTO 的 sign-up 段含**注册默认部门**）——设置页注册设置项同步移除。
- **注册链路**：register 的默认部门分配逻辑（SysAuthenticationServiceImpl）。
- **默认部门切换消费点**：profile 域 setDefaultDept 端点（Base/App/Web 三态）；web 顶栏部门切换器（`layout/head/components/dept`）与 `stores/user.ts` 部门状态；app 设置页修改默认部门（`subpackages/system/setting/user/SaveDefaultDept` + 入口）。
- **登录后向导**：postLoginCheck 默认部门策略 + 向导默认部门步（web/app 的 `components/user-setup/UserSetupDefaultDept`）。
- SQL 种子：dept/post 菜单与按钮权限、role_menu 关联（进按业务命名的新幂等脚本）。
- web 承接：`views/system/dept|post`、`api/system/dept|post`、`components/default-dept-select` 组件本体、SystemSetting 注册默认部门项、SystemUser 部门岗位列/表单/筛选。
- app 承接：向导默认部门步、SaveDefaultDept 页与设置入口、Profile 部门岗位展示、相关 api（profile/dept）。
- 回归点：登录 → 顶栏无部门切换器 → 用户管理无部门岗位 → 设置无注册默认部门项 → 注册链路正常（无部门分配）→ postLoginCheck 无部门步。

**B. App 端**（用户确认不需要移动端）
- 后端：删除全部 `controller/app` 包；**Base 控制器下放**——6 个双版本去重基类（Notice/DictData/Profile/AttachmentStorage/Authentication/Setting，位于 `controller/base`）的端点方法并入对应管理版子类（基类仅因 Web/App 去重而存在）；SecurityConfig permitAll 的 app 组（`/app/...`）清理；app-version 域整体裁剪（纯 App 服务域：表/字典/菜单/枚举/controller）。**`BaseResponseController`（base-common 统一返回父类）不在合并范围**。getClientType 等 app 分支逻辑无害保留。
- app 仓：不再使用（删除与否由用户定，属仓库操作）。
- 回归点：web 全量登录 + 六个被合并域（通知/字典/个人中心/附件/认证/设置）回归。

**C. 监控**
- 删 `lihua-biz/lihua-monitor` 模块（父 pom `<modules>` + lihua-admin 依赖联动）+ monitor 菜单种子 + web `src/views/monitor` 与对应 api。
- 回归点：compile + 管理端无监控菜单。

**D. 组件演示页**
- web：`src/views/component` 演示页与演示路由/菜单种子；app：`subpackages/system` 内演示性质页面与仅被演示消费的样板引用（公共组件本体保留）。
- 回归点：type-check + 演示入口不可见。

**E. 通知公告**（联动面大）
- 后端：notice 域（SysNotice/user_notice 关联/notice_type 字典/通知策略）+ WS 未读红点链路（unreadCount/红点）；**裁剪后若无其他 WS 消费方，向用户确认是否连带裁 WS**。
- web：通知页 + 头部通知组件 + 角标；app：通知中心 + 红点 + 推送横幅资产。
- SQL：notice 菜单/字典/欢迎公告种子。
- 回归点：登录链路正常、头部无通知入口、WS 状态按裁剪结果确认。

### 3.4 INIT-REPORT 落盘
写入后端仓根（用户可指定他处）：替换映射表（旧名→新名逐类）、裁剪清单、豁免项、后续建议。被裁域对应的 skill 规则段落保留不删（不再触发即可）；改名波及 skills/ 内路径字样（如 lihua.sql、包名）时同步更新 skill 文本（skill 是活文档）。

## Phase 4 验证与报告

- 后端 `mvn clean compile`；web/app `npm run type-check`（按需 build）；旧名与被裁域残留 grep 审计。
- 人工回归清单：登录、首页、用户管理、设置、被裁功能不可见确认。
- 报告：各步 commit 清单、INIT-REPORT 位置、建议首次打 tag `v1.0.0`。

## 纪律

- 在干净 clone 上执行；**每步 commit**（用户项目历史从 init 开始），**不 push**。
- 所有删除先列清单经用户确认；删码执行 lihua-backend.md 红线节的死活判定纪律。
- 跨仓操作一律绝对路径，rm/批量改后立即 grep 验证目标仓。
