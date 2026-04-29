# Web 管理端开发参考

## 项目形态

- 根目录：`lihua-vue/`。
- API 层：`src/api`，全局响应类型在 `src/api/global/type.ts`。
- 页面：`src/views`。
- 组件：`src/components`。
- 路由：`src/router/index.ts`。
- 状态：`src/stores`。
- 请求封装：`src/utils/request.ts`。
- 环境 API 地址：`.env.development` 和 `.env.production`。

## API 规则

- 每个后端交互都定义为导出函数，返回 `request<T>(...)` 或 `blobRequest(...)`。
- 从 `@/utils/request.ts` 引入请求封装。
- 业务类型放在 `src/api/<domain>/<feature>/type/`。
- 分页响应使用 `PageResponseType<T>`；普通响应通过 request 包装后的 `ResponseType<T>`。
- URL、HTTP 方法、请求体和响应泛型必须与后端 Controller 精确对齐。
- Excel 导出、文件下载、模板下载使用 `blobRequest`。
- 上传接口在 API 函数内构造 `FormData`，并按现有 API 写法设置 multipart headers。

## 路由和菜单规则

- 静态路由位于 `src/router/index.ts`。
- 大多数业务菜单通过后端菜单系统动态配置。普通管理页面不要随意新增静态路由，除非该功能已有同类静态路由模式。
- 静态路由 `meta` 支持标题、图标、显示隐藏、页签、缓存、链接行为、匿名访问和角色限制。
- 如果 `meta.cache` 为 true，路由必须有稳定且唯一的 `name`，组件名也应唯一。
- 角色限制使用 `ROLE_admin` 等角色编码；无权限用户应进入现有 403 行为。

## 应用初始化与权限链路

- `src/permission.ts` 注册全局路由守卫，处理 token、匿名访问、登录页跳转、403、WebSocket 连接和主题重置。
- `src/app-init.ts` 是登录后应用初始化入口：初始化用户信息、系统主题、动态路由、菜单、view-tabs、字典缓存和 keep-alive 缓存。
- 动态路由和菜单主要由 `src/stores/permission.ts` 处理；变更菜单结构或路由元信息时，要检查动态菜单和静态路由组合后的行为。
- `src/stores/view-tabs.ts` 管理多任务页签、缓存组件和最近访问组件；新增可缓存页面时要同时考虑路由 `name`、组件名和 view-tab 行为。
- 登录状态下首次进入页面会连接 WebSocket；退出或 token 失效会关闭连接并清空用户状态。
- 主题初始化依赖用户信息中的主题配置；改动布局或主题时要检查 `theme` store 和相关 layout 组件。

## 权限指令

- `v-hasRole` 来自 `src/directive/has-role.ts`，参数必须是角色编码数组，例如 `v-hasRole="['ROLE_admin']"`。
- `v-hasPermission` 来自 `src/directive/has-permission.ts`，参数必须是权限编码数组，例如 `v-hasPermission="['system:user:save']"`。
- 指令会在 mounted 阶段移除无权限 DOM；不要把它当作后端权限校验的替代，后端仍需 `@PreAuthorize`。

## 页面和组件规则

- 业务页面放在 `src/views/<domain>/<feature>/`。
- 页面组件名应全局唯一，建议使用“领域 + 功能”的大驼峰命名，保证 keep-alive 行为可靠。
- 表格查询、弹窗/抽屉、保存流程、导入导出、状态切换等，优先参考附近的 `src/views/system/*` 或 `src/views/monitor/*` 页面模式。
- 只有多个功能都会复用时，才在 `src/components` 下新增全局组件。
- 只服务当前功能的组件应靠近功能页面放置。
- 新增 UI 前优先复用附件上传、字典标签、图标选择、用户选择、表格设置、富文本编辑器、图片裁剪、树选择等内置组件。

## 状态、权限和字典

- 已有 store 包括 `user`、`permission`、`setting`、`theme`、`dict`、`view-tabs`。
- 用户身份、角色、权限、部门、岗位、菜单和页签信息来自 user store。
- 字典值使用字典工具和 `dict-tag`。按字典编码初始化字典时，要注意 TypeScript 中响应式 ref 的 `.value`。
- 字典驱动的字段不要硬编码标签或颜色，应由后端字典控制。

## 验证

- 修改 TypeScript、API 类型、路由或组件后运行 `npm run type-check`。
- 较大的 UI、路由、环境或依赖变更后运行 `npm run build`。
- 如果需要后端接口做手动验证但后端未运行，至少完成类型/构建验证，并说明运行时验证缺口。
