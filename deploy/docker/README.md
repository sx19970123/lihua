# 使用Docker部署应用程序

## 文件结构
```
lihua                 	                                // 项目工程目录
├── docker                		                // docker 部署目录
│   ├── client                                          // 前端工程目录
│   │   ├── dist	                                // 前端打包后的dist目录（需自己添加）
│   │   ├── nginx.conf	                                // nginx配置
│   │   ├── dockerfile	                                // 前端构建镜像dockerfile
│   ├── server                                          // 后端服务目录
│   │   ├── lihua-admin-exec.jar	                // 后端打包后的jar文件（需自己添加）
│   │   ├── dockerfile	             		        // 后端构建镜像dockerfile
│   ├── docker-compose.yml                              // docker编排文件
......        
```
## 部署说明
**请确保服务器中已安装docker**

### client
> client 为前端部署目录包含：
> dist（lihua-vue打包目录）
> nginx.conf（自定义的nginx配置，打包后覆盖镜像原有配置）
> dockerfile（构建镜像）

构建镜像时会将dist和nginx.conf复制到镜像指定路径下，并向外部暴露80端口
生产发包时，前端打包后替换掉旧版本的dist目录即可

### server
> client 为后端部署目录包含：
> lihua-admin-exec.jar（后端打包后文件，注意切换application.yml 中 active 为 prod）
> dockerfile（构建镜像）

server构建使用`eclipse-temurin:25.0.4_7-jre-noble`（与 Java 25 编译目标匹配；镜像内含 fontconfig 与 DejaVu 字体，满足验证码字体渲染），将lihua-admin-exec.jar复制到指定路径下。启动之执行`java -jar` 向外抛出8080端口
`application-prod.yml` 中关键配置读取自变量，在部署时通过`compose.yaml`对环境变量进行配置


### compose.yaml
启动前请根据实际情况完善`compose.yaml`中的配置信息，并将打包好的文件放到对应目录下。

将项目中`docker`目录上传到服务器，在`docker`目录中执行`docker compose -f name.yaml up -d`时默认会启动`lihua-web-server` `lihua-web-client` `lihua-mysql` `lihua-redis` 四个容器

第一次部署时，容器全部启动后需手动执行sql文件，可使用navicat等工具连接数据库后运行项目下的sql文件。

**配置仅包含最基础的项目启动，更多需求请根据项目情况修改dockerfile和compose.yaml**

## 容器健康与自愈

后端服务引入 actuator 探针（`management.server.port=9090` 独立端口，`application.yml` 的 `management` 段配置）——不占用业务端口、不映射宿主机，仅容器内网可达。compose 据此为每个容器配置了体检与自愈：

- **healthcheck 定时体检**：后端服务探 `9090/actuator/health`（聚合数据库/Redis 连通性）；mysql/redis 用各自官方命令探活。`docker compose ps` 的 STATUS 列显示 `(healthy)` 即体检通过。
- **depends_on 启动排序**：后端服务等 mysql/redis 全部 `(healthy)` 后才启动；前端等后端。
- **restart: unless-stopped 宿主机重启自愈**：服务器重启后 Docker 自动拉起全部容器（手动 `docker compose stop` 停掉的不会被拉起）。
- **资源与日志**：容器已设 `mem_limit`（JVM 堆经 `-XX:MaxRAMPercentage=75.0` 跟随容器限额），日志统一 json-file 轮转（单文件 10MB × 3 份）。
- **OOM 行为**：JVM 内存溢出时异常栈进容器日志（`docker compose logs` 可见），堆快照 dump 到 server 数据卷（`java_pid*.hprof`，约等于堆大小——排查 OOM 的现场材料，低频事件手动清理），随后进程立即退出交由 restart 自愈。

后端服务开启优雅停机（`server.shutdown=graceful`，收尾超时 30s，compose `stop_grace_period=35s` 兜底）：容器停止时先拒新请求、等待在途请求完成——大文件上传/流式下载等长请求超 30s 仍会被截断，更新版本安排在低峰期。

## 卷映射

> 通过卷映射可以通过连接服务器直接修改docker容器中的文件

> /var/lib/docker/volumes 目录下对应容器卷映射目录

- mysql-conf：mysql配置文件
- mysql-data：mysql数据
- redis-data：redis数据
- server-data：服务器文件（文件上传、系统日志）
- jar-resource：启动服务器时的jar包路径
- dist-resource：前端打包dist路径

## 更新版本

- 前端 进入到 dist-resource 后，替换_data下的目录即可
- 后端 进入到 jar-resource 后，替换_data下对应的jar包，重启对应容器即可

## 3.0 升级部署必读

1. **先执行数据库升级**：导入本仓库 `deploy/db/upgrade-3.0.0.sql`（幂等可重跑），再启动服务；基线新装直接用 `lihua.sql`（含全部变更）。
2. **下载链接签名密钥必配**：3.0 起 `attachment.download-sign-key` 缺失服务启动失败（fail-fast）。docker 部署在 compose 同目录 `.env` 提供：
   ```
   ATTACHMENT_DOWNLOAD_SIGN_KEY=<openssl rand -hex 32 生成，至少 16 字符>
   ```
   （compose 已透传该变量；mono 生产 yml 与 cloud Nacos lihua-file.yaml 均以 `${ATTACHMENT_DOWNLOAD_SIGN_KEY}` 占位。）
3. **Nacos 配置重导入（cloud）**：`deploy/nacos/nacos_config_export.zip` 已更新（附件 attachment 段 3.0 形态、附件路由超时 10m、路由显式 order、uploadFilePath 指向数据卷），升级后需在 Nacos 重新导入并发布。
4. **附件存储卷（cloud）**：lihua-file 的 `uploadFilePath` 已指向 `/lihua-file/data/upload/`（落在 `file-server-data` 卷）；请勿改回相对路径，否则容器重建附件丢失。
