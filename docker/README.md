# 使用Docker部署应用程序

## 文件结构
```
lihua                 	                                // 项目工程目录
├── docker                		                        // docker 部署目录
│   ├── client                                          // 前端工程目录
│   │   ├── dist	                                    // 前端打包后的dist目录（需自己添加）
│   │   ├── nginx.conf	                                // nginx配置
│   │   ├── dockerfile	                                // 前端构建镜像dockerfile
│   ├── server                                          // 后端服务目录
│   │   ├── lihua-admin-exec.jar	                    // 后端打包后的jar文件（需自己添加）
│   │   ├── dockerfile	             		            // 后端构建镜像dockerfile
│   ├── xxl-job                                         // xxl-job定时任务目录（如果需要）
│   │   ├── xxl-job-admin-2.4.2-SNAPSHOT.jar	        // xxl-job打包后的jar文件（需自己添加）
│   │   ├── dockerfile	                                // xxl-job构建镜像dockerfile
│   ├── document                                        // 开发文档（如果需要，直接从码云下载解压后放到该目录下即可）
│   │   ├── nginx.conf	                                // nginx配置
│   │   ├── lihua-doc	                                // 开发文档（https://gitee.com/yukino_git/lihua-doc）（需自己添加）
│   │   ├── dockerfile	                                // 文档构建镜像dockerfile
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

### document
> client 为开发文档部署目录包含：
> lihua-doc（开发文档）
> nginx.conf（自定义的nginx配置，打包后覆盖镜像原有配置）
> dockerfile（构建镜像）

与client相同，向外部暴露80端口。
文档中内容可根据实际需要进行修改，也可通过docsify本地预览，如不需要可删除对应目录及`compose.yaml`中相应配置

### server
> client 为后端部署目录包含：
> lihua-admin-exec.jar（后端打包后文件，注意切换application.yml 中 active 为 prod）
> dockerfile（构建镜像）

server构建使用openjdk:21，将lihua-admin-exec.jar复制到指定路径下。启动之执行`java -jar` 向外抛出8080端口
`application-prod.yml` 中关键配置读取自变量，在部署时通过`compose.yaml`对环境变量进行配置

### xxl-job
> client 为定时任务部署目录
> xxl-job-admin-2.4.2-SNAPSHOT.jar（xxl-job打包文件）
> dockerfile（构建镜像）

与server相同，向外部抛出8081端口，如不需要可删除对应目录及`compose.yaml`中相应配置
（系统中仅keepHeartbeat方法调用到了定时任务，用于sse定时保活）

### compose.yaml
启动前请根据实际情况完善`compose.yaml`中的配置信息，并将打包好的文件放到对应目录下。

将项目中`docker`目录上传到服务器，在`docker`目录中执行`docker compose -f name.yaml up -d`时默认会启动`lihua-web-server` `lihua-web-client` `lihua-mysql` `lihua-redis` `lihua-xxl-job` `lihua-web-document`六个容器

第一次部署时，容器全部启动后需手动执行sql文件，可使用navicat等工具连接数据库后运行项目下的sql文件（xxl-job的sql文件请参考其官方文档）。

**配置仅包含最基础的项目启动，更多需求请根据项目情况修改dockerfile和compose.yaml**

## 卷映射

> 通过卷映射可以通过连接服务器直接修改docker容器中的文件

> /var/lib/docker/volumes 目录下对应容器卷映射目录

- mysql-conf：mysql配置文件
- mysql-data：mysql数据
- redis-data：redis数据
- server-data：服务器文件（文件上传、文件导出、系统日志）
- jar-resource：启动服务器时的jar包路径
- dist-resource：前端打包dist路径
- xxl-job-resource：启动xxl-job时的jar包路径
- doc-dist-resource：在线文档路径



## 更新版本

- 前端、文档更新时连接到 dist-resource、doc-dist-resource 后，替换_data下的目录即可
- 后端、xxl-job更新时，连接到jar-resource、xxl-job-resource后，替换_data下对应的jar包，重启对应容器即可