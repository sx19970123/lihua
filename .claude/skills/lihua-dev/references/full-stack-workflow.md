# 跨端开发流程

## 项目形态

- `lihua/` 是 Spring Boot 服务端。可运行模块是 `lihua-admin`，业务模块主要在 `lihua-biz`，可复用基础能力在 `lihua-base`。
- `lihua-vue/` 是 Web 管理端。业务 API 在 `src/api`，页面在 `src/views`，全局状态在 `src/stores`，公共组件在 `src/components`。
- `lihua-app/` 是 UniApp 移动端。API 在 `src/api`，主包页面在 `src/pages`，分包页面在 `src/subpackages`，状态在 `src/stores`，路由拦截在 `src/router`。
- `res/db/lihua.sql` 是基础数据库脚本。涉及默认菜单、权限、字典、配置或表结构时，以它作为种子数据参考。

## 跨端功能顺序

1. 先定义后端契约。优先沿用已有统一返回、DTO/VO、校验分组、日志注解和安全注解。
2. 保持接口前缀一致：
   - Web 管理端接口通常使用 `system/...`。
   - App 接口通常使用 `app/system/...`，后端实现通常位于 `controller/app`。
3. 在 `lihua-vue/src/api/<domain>/<feature>/` 新增或更新 Web API，并在 `type/` 下维护 TypeScript 类型。
4. 在 `lihua-vue/src/views/<domain>/<feature>/` 新增或更新 Web 页面。静态路由在 `src/router/index.ts`，多数业务菜单通过系统菜单动态配置。
5. 在 `lihua-app/src/api/<domain>/<feature>/` 新增或更新 App API，并在 `type/` 下维护 TypeScript 类型。
6. 在 `lihua-app/src/pages.json` 注册 App 页面。公共页和 tabBar 页放 `src/pages`，业务功能页优先放 `src/subpackages`，除非必须进主包。

## 契约规则

- 两个前端都复用已有全局响应类型和分页响应类型。
- DTO 请求字段应与后端 DTO 对齐；列表/详情展示字段应与后端 VO 对齐。
- 后端接口如果使用 `@PreAuthorize`，要让对应菜单/权限配置可被开发者发现；必要时更新 SQL 菜单种子数据。
- 如果字段由字典驱动，应维护字典数据，并在前端使用现有字典工具或组件，不要硬编码标签。
- 附件相关能力应复用现有附件上传/下载组件和后端附件存储服务。

## 验证命令

- 后端：在 `lihua/` 下运行 `mvn test`；如果只影响某个模块，可运行更窄的 Maven 模块检查。
- Web：在 `lihua-vue/` 下运行 `npm run type-check`；需要验证生产构建时运行 `npm run build`。
- App：在 `lihua-app/` 下运行 `npm run type-check`；只有涉及具体平台时才运行对应 `dev:*` 或 `build:*` 命令。

优先运行能覆盖本次改动的最小检查。如果命令因为 MySQL、Redis、对象存储或其他服务不可用而无法完整运行，要明确说明剩余验证缺口。
