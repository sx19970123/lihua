---
name: lihua-app
description: 指导狸花猫仓库中的 UniApp 移动端开发。适用于 lihua-app API、TypeScript 类型、pages.json、页面、分包、sard-uniapp 组件、Pinia 状态、路由守卫、App 专用后端接口、字典、附件、通知、主题和 App 端验证。
---

# 狸花猫 App 开发

处理 `lihua-app/` 下的改动时，使用本技能。

修改 App 代码前，先阅读 `references/app-development.md`。如果涉及后端 App 接口，也要参考 `lihua-backend`；跨端功能参考 `lihua-dev`。

## 核心规则

- API 函数放在 `src/api/<domain>/<feature>/`，类型放在对应 `type/` 下。
- 调用 App 专用后端接口时使用 `app/system/...` 这类前缀。
- 页面必须在 `src/pages.json` 注册。
- 公共页或 tabBar 页放在 `src/pages`；业务功能页优先放在 `src/subpackages`，除非必须进入主包。
- 需要经过路由守卫的跳转，使用 `@/router/router` 中的 `router`，不要直接用 `uni.navigateTo`。
- 新增 UI 前先复用 Sard UI 和项目已有组件。

## 验证

在 `lihua-app/` 下运行：`npm run type-check` 验证 TypeScript/Vue 正确性。只有涉及具体平台时，才运行对应平台的构建脚本。
