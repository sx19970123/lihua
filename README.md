# 狸花猫后台管理系统

> 一个基于 **Spring Boot + Vue + UniApp** 的现代化 RBAC 权限管理系统

[![Gitee Stars](https://gitee.com/yukino_git/lihua/badge/star.svg?theme=dark)](https://gitee.com/yukino_git/lihua/stargazers)[![GitHub Stars](https://img.shields.io/github/stars/sx19970123/lihua)](https://github.com/sx19970123/lihua)<a href="https://gitcode.com/weixin_44118742/lihua" target="_blank"><img src="https://gitcode.com/weixin_44118742/lihua/star/badge.svg" alt="AtomGit Star"/></a>



## 🚀 在线体验

> 为完整体验功能，请自行注册账号

👉 web体验地址： https://lihua.xyz

👉 安卓apk下载：[狸花猫 APP 下载](https://gitee.com/yukino_git/lihua-app/releases/download/1.0.0/狸花猫APP.apk)



## 📚 文档

- 📖 开发文档：https://doc.lihua.xyz/
- 🤖 AI 文档（DeepWiki）：https://deepwiki.com/sx19970123/lihua
- 🎥 功能介绍视频：https://www.bilibili.com/video/BV14Z1oY8EKh/



## 🧩 项目仓库

- Gitee：https://gitee.com/yukino_git/lihua
- GitHub：https://github.com/sx19970123/lihua
- GitCode：https://gitcode.com/weixin_44118742/lihua



## 💬 交流反馈

- 欢迎提交 Issues（功能建议 / Bug / 优化建议）
- QQ交流群：850464676



## 🛠 主要技术栈

- Java 21+
- Spring Boot 4
- Vue 3
- MySQL 8+
- Redis
- Node.js 22+



## 目录结构

为方便使用，将app代码合并到主仓库中。

如不需要app端，直接删除即可，后端服务将app专用接口进行了拆分，可将controller/app包中接口一并删除。

``` bash
lihua-cloud/
├── lihua-app/              # app 客户端
├── lihua-vue/              # web 客户端
├── lihua/                  # spring boot 服务端
├── res/                    # 项目静态资源（数据库脚本、nacos配置、docker部署compose）
├── LICENSE
└── README.md
```



## 🔐 数据库脚本默认账号

| 账号 | 密码 |
|------|------|
| admin | admin123 |



## 🧠 项目简介

**狸花猫** 是一套完整的 RBAC 权限管理系统，适用于中后台管理系统快速开发。

### ✨ Web端核心功能

#### 🔑 权限系统（RBAC）
- 用户管理
- 角色管理
- 菜单权限控制
- 多部门支持

#### 🏢 组织架构
- 部门管理
- 岗位管理
- 用户多部门归属

#### 📚 字典系统
- 普通字典 / 树形字典
- 后端统一工具类
- 前端 `dict-tag` 自动标签展示

#### 📢 通知公告
- tinymce 富文本编辑
- websocket 实时消息推送

#### 📎 附件管理

- 支持附件上传、文件秒传、分片上传、断点续传
- attachment-upload 组件开箱即用
- 支持本地和阿里云OSS，支持灵活适配其他对象存储服务

#### 👤 个人中心
- 个性化主题配置
- 布局 / 导航自定义

#### ⚙ 系统配置
- 默认密码设置
- 密码定期修改
- 登录限制
- 注册开关
- 验证码开关
- IP 黑名单
- 灰色模式

#### 📊 系统监控
- 在线用户
- 操作日志
- 登录日志
- 缓存监控
- 服务监控
- 定时任务

### ✨ App端核心功能

#### 🔐 注册登录

- Web 端统一控制注册开关

#### 🧠 验证码

- 集成 tianai 验证码
- Web 端统一配置启用状态

#### 👤 个人中心

- 头像、昵称等基础信息与后端保持一致

#### 🛡️ 权限体系

- 支持角色、权限、部门标识，`user store` 可直接获取

#### 🔔 通知公告

- WebSocket 实时消息推送，App 支持原生通知提醒

#### 🌗 暗色模式

- App支持手动切换，微信小程序可跟随系统



## 📸 WEB端系统截图

### 首页
![首页](https://gitee.com/yukino_git/lihua-assets/raw/master/web/首页.jpg)

![暗色模式](https://gitee.com/yukino_git/lihua-assets/raw/master/web/首页-暗色.jpg)

![锁屏](https://gitee.com/yukino_git/lihua-assets/raw/master/web/锁屏.png)

### 登录与注册
![登录页](https://gitee.com/yukino_git/lihua-assets/raw/master/web/登录页.jpg)

![注册页](https://gitee.com/yukino_git/lihua-assets/raw/master/web/注册页.jpg)

### 个人中心

![个人中心](https://gitee.com/yukino_git/lihua-assets/raw/master/web/个人中心.jpg)

![修改头像](https://gitee.com/yukino_git/lihua-assets/raw/master/web/修改头像.jpg)

### 系统管理
![用户管理](https://gitee.com/yukino_git/lihua-assets/raw/master/web/用户管理.jpg)

![角色管理](https://gitee.com/yukino_git/lihua-assets/raw/master/web/角色管理.jpg)

![菜单管理](https://gitee.com/yukino_git/lihua-assets/raw/master/web/菜单管理.jpg)

![字典管理](https://gitee.com/yukino_git/lihua-assets/raw/master/web/字典管理·.jpg)

![部门管理](https://gitee.com/yukino_git/lihua-assets/raw/master/web/部门管理.jpg)

![岗位管理](https://gitee.com/yukino_git/lihua-assets/raw/master/web/岗位管理.jpg)

![通知公告](https://gitee.com/yukino_git/lihua-assets/raw/master/web/通知公告.jpg)

![附件管理](https://gitee.com/yukino_git/lihua-assets/raw/master/web/附件管理.png)

### 系统监控
![在线用户](https://gitee.com/yukino_git/lihua-assets/raw/master/web/在线用户.jpg)

![操作日志](https://gitee.com/yukino_git/lihua-assets/raw/master/web/操作日志.jpg)

![登录日志](https://gitee.com/yukino_git/lihua-assets/raw/master/web/登录日志.jpg)

![缓存监控](https://gitee.com/yukino_git/lihua-assets/raw/master/web/缓存监控.jpg)

![服务监控](https://gitee.com/yukino_git/lihua-assets/raw/master/web/服务监控.jpg)

### 系统设置
![设置页](https://gitee.com/yukino_git/lihua-assets/raw/master/web/设置页.jpg)



## 📱APP端系统截图

<div style="display:flex; flex-wrap:wrap; gap:8px;">
	<img src="https://gitee.com/yukino_git/lihua-assets/raw/master/app/IMG_1917.png" width="32%" />
	<img src="https://gitee.com/yukino_git/lihua-assets/raw/master/app/IMG_1918.png" width="32%" />
	<img src="https://gitee.com/yukino_git/lihua-assets/raw/master/app/IMG_1919.png" width="32%" />
	<img src="https://gitee.com/yukino_git/lihua-assets/raw/master/app/IMG_1916.png" width="32%" />
	<img src="https://gitee.com/yukino_git/lihua-assets/raw/master/app/IMG_1925.png" width="32%" />
	<img src="https://gitee.com/yukino_git/lihua-assets/raw/master/app/IMG_1926.png" width="32%" />
</div>
