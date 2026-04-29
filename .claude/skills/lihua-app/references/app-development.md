# App 开发参考

## 项目形态

- 根目录：`lihua-app/`。
- API 层：`src/api`，全局响应类型在 `src/api/global/type.ts`。
- 请求封装：`src/utils/request.ts`。
- 页面注册：`src/pages.json`。
- 主包页面：`src/pages`。
- 分包页面：`src/subpackages`。
- 状态：`src/stores`。
- 路由守卫：`src/router/router.ts`。
- 根级应用逻辑：`src/App.vue` 和 `src/AppRoot.vue`。

## 启动与路由守卫

- `src/App.vue` 在 `onLaunch` 中初始化主题，并注册 WebSocket 通知监听。
- `src/AppRoot.vue` 承载 `uni-ku/root` 根节点能力，同时挂载 `sar-notify-agent`、`sar-dialog-agent`、`sar-crop-image-agent` 和轻量通知组件。
- `src/router/router.ts` 基于 `sard-uniapp` 的 `Router` 实现前置守卫；需要经过守卫的跳转必须从 `@/router/router` 导入 router。
- 路由公开白名单包含 splash、登录、注册、隐私政策和用户协议；新增匿名页时要同步检查白名单。
- 有 token 但用户信息未初始化时，路由守卫会初始化用户信息、连接 WebSocket、刷新未读通知数，并预加载 `sys_notice_type` 字典。
- 无 token 时会关闭 WebSocket；访问非公开页面会 `uni.reLaunch` 到登录页。

## API 规则

- 每个后端交互都定义为导出函数，并使用 `@/utils/request` 中的统一请求封装。
- 业务类型放在 `src/api/<domain>/<feature>/type/`。
- 使用 `app/system/profile` 这类 App 专用接口前缀；除非后端明确允许 App 复用，否则不要调用 Web 管理端接口。
- 请求封装会在存在 token 时附加 bearer auth，并统一处理登录过期等响应码。
- HTTP 方法必须与后端保持一致。App 代码中部分文件使用大写的 `GET`/`POST`/`DELETE`，按相邻文件风格书写。

## 页面和分包

- 所有可跳转页面都必须声明在 `src/pages.json`。
- tabBar、首屏和公共页面放在 `src/pages`。
- 业务功能页面放在 `src/subpackages/<domain>/...`，避免主包体积继续增长。
- 只服务当前模块的组件放在对应分包内。
- 全局复用的移动端组件放在 `src/components`。

## 路由

- 需要经过守卫的跳转，应 `import router from '@/router/router'`，再调用 `router.navigateTo`、`router.redirectTo` 等方法。
- 路由守卫维护了匿名白名单，例如 splash、登录、注册页面。新增公开页面要谨慎加入白名单。
- webview 类跳转需要对 URL 参数进行编码。

## 状态和全局数据

- 已有 store 包括 `user`、`dict`、`notice`、`root`、`theme`。
- 用户 id、昵称、用户名、头像、角色、权限、部门和岗位信息来自 user store。
- App store 默认不持久化；除非显式添加持久化，不要假设进程重启后状态仍存在。
- UniApp 没有天然的 Vue 根组件语义，涉及全局应用行为时使用 root store/root component 模式。

## UI、字典、附件和通知

- 新增 UI 前优先使用 `sard-uniapp` 组件和项目已有组件。
- 字典值使用 App 字典工具和 `dict-tag` 组件。TypeScript 中字典选项 ref 需要 `.value`。
- 附件功能使用现有附件上传组件和附件工具。
- 通知类能力使用现有 notice store 和通知组件。
- 主题行为要与 `src/stores/theme.ts` 和 `pages.json` 中的主题变量保持一致。

## 验证

- 修改 API、类型、页面、store 或组件后运行 `npm run type-check`。
- H5 验证使用 `npm run dev:h5` 或 `npm run build:h5`。
- 只有功能目标包含对应平台时，才运行 `dev:mp-weixin`、`build:mp-weixin` 等平台脚本。
