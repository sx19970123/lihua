# 狸花猫后台管理系统（Lihua）

> 一套基于 **Spring Boot / Spring Cloud + Vue3 + UniApp** 的现代化 RBAC 权限管理系统

[![Gitee Stars](https://gitee.com/yukino_git/lihua/badge/star.svg?theme=dark)](https://gitee.com/yukino_git/lihua/stargazers) [![GitHub Stars](https://img.shields.io/github/stars/sx19970123/lihua)](https://github.com/sx19970123/lihua) <a href="https://gitcode.com/weixin_44118742/lihua" target="_blank"><img src="https://gitcode.com/weixin_44118742/lihua/star/badge.svg" alt="GitCode Star"/></a>

## 🧩 项目仓库

**3.0 起项目按端拆分为四个独立仓库**，共用统一的账号、权限与数据模型，可按需组合使用：

| 仓库 | 说明 | 地址 |
|------|------|------|
| lihua | 后端 · Spring Boot 单体版（本仓库） | https://gitee.com/yukino_git/lihua |
| lihua-cloud | 后端 · Spring Cloud 微服务版 | https://gitee.com/yukino_git/lihua-cloud |
| lihua-web | 前端 · Vue3 管理端（Antdv Next 组件库） | https://gitee.com/yukino_git/lihua-web |
| lihua-app | 移动端 · UniApp（Android / iOS / 鸿蒙 / 微信小程序） | https://gitee.com/yukino_git/lihua-app |

> Web 端与移动端可同时对接单体版与微服务版后端，接口契约保持一致。

## 📚 文档

- 📖 开发文档：https://doc.lihua.xyz（含 1.0 / 2.0 / 3.0 全版本）
- 🤖 AI 文档（DeepWiki）：https://deepwiki.com/sx19970123/lihua
- 🎥 功能介绍视频：https://www.bilibili.com/video/BV14Z1oY8EKh/

## 🚀 在线体验

👉 web 体验地址：https://lihua.xyz

## 💬 交流反馈

- 欢迎提交 Issues（功能建议 / Bug / 优化建议）
- QQ 交流群：850464676

## 🛠 主要技术栈

- Java 25（虚拟线程默认开启）
- Spring Boot 4.1.1
- MyBatis-Plus 3.5.17
- MySQL 8+
- Redis（Redisson + Caffeine 二级缓存）
- Maven 3.6+

## 📁 目录结构

``` bash
lihua/
├── lihua-admin/            # 应用启动与打包入口
├── lihua-base/             # 基础能力层（attachment / cache / captcha / common / dict / doc / excel / job / log / mybatis / security / sensitive / web / ws）
├── lihua-websocket/        # WebSocket 连接层（受控基建库，随 lihua-admin 装配）
├── lihua-biz/              # 业务层（lihua-system 系统业务 / lihua-monitor 系统监控）
├── deploy/                 # 数据库脚本、docker 部署编排
├── LICENSE
└── README.md
```

## 🚀 快速开始

1. 准备环境：JDK 25、MySQL 8.0+、Redis
2. 导入数据库脚本 `deploy/db/lihua.sql`（升级场景执行 `deploy/db/upgrade-3.0.0.sql`）
3. 修改 `lihua-admin/src/main/resources/application-dev.yml` 中的数据库与 Redis 连接
4. 启动 `lihua-admin` 模块下的 `LiHuaApplication`，控制台打印启动成功即可
5. 配合 [lihua-web](https://gitee.com/yukino_git/lihua-web) 访问 `http://localhost:90`

### 🔐 数据库脚本默认账号

| 账号 | 密码 |
|------|------|
| admin | admin123 |

## 🧠 项目简介

**狸花猫** 是一套完整的 RBAC 权限管理系统，适用于中后台管理系统快速开发。本仓库为单体版后端，开箱即用；如需微服务形态请使用 [lihua-cloud](https://gitee.com/yukino_git/lihua-cloud)。

### ✨ 核心能力

#### 🔑 权限系统（RBAC）

- 用户管理、角色管理、菜单权限控制
- 部门管理、岗位管理、用户多部门归属与默认部门
- 接口级权限校验（`@PreAuthorize`）

#### 📚 字典系统

- 普通字典 / 树形字典
- 后端 `DictUtils` 统一工具类与多级缓存
- 前端 `dict-tag` 自动标签展示

#### 📢 通知公告

- TinyMCE 富文本编辑
- WebSocket 实时消息推送（事务提交后推送）
- 公告已读 / 未读 / 星标管理

#### 📎 附件管理

- 支持附件上传、文件秒传、分片上传、断点续传
- 下载链 HMAC 签名与 Range 断点协商
- 支持本地存储与阿里云 OSS，可灵活适配其他对象存储

#### 🛡️ 安全与稳定

- 接口限流 `@RateLimit`、防重复提交 `@PreventDuplicateSubmit`
- 登录失败锁定（账号 + IP 双维度，默认关闭）
- 权限变更「数据更新」红点提醒，替代旧的强制下线
- 操作日志 / 登录日志注解化记录，TraceId 全链路贯穿

#### ⚙ 系统配置

- 默认密码设置、密码定期修改
- 同账号登录限制、注册开关、验证码开关
- IP 黑名单、灰色模式

#### 📊 系统监控

- 在线用户、缓存监控、服务监控
- 操作日志、登录日志、定时任务
