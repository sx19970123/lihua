---
name: lihua-web
description: 指导狸花猫仓库中的 Vue 3 Web 管理端开发。适用于 lihua-vue API、TypeScript 类型、Ant Design Vue 页面、路由、动态菜单、Pinia 状态、权限展示、字典、附件、表格、表单、布局、主题和 Web 端验证。
---

# 狸花猫 Web 管理端开发

处理 `lihua-vue/` 下的改动时，使用本技能。

修改 Web 代码前，先阅读 `references/web-development.md`。如果涉及后端契约，也要参考 `lihua-backend`；跨端功能参考 `lihua-dev`。

## 核心规则

- API 函数放在 `src/api/<domain>/<feature>/`，类型放在对应 `type/` 下。
- 页面放在 `src/views/<domain>/<feature>/`。
- 新增组件前先复用 `src/components` 中已有组件。
- 遵循 Ant Design Vue 和项目已有页面模式。
- 跨页面状态使用 `src/stores`；局部表单/表格状态不要放入全局状态。
- 字典展示使用字典工具和 `dict-tag`，不要硬编码字典标签。

## 验证

在 `lihua-vue/` 下运行：`npm run type-check` 验证类型；需要验证生产打包时运行 `npm run build`。
