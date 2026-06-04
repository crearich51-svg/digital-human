# 数字人智能讲解员

一个基于 Java Web + React 的数字人智能讲解员项目，支持定制数字人形象、语音合成、一键生成讲解视频。

## 项目结构

```
digital-human/
├── frontend/                    # 前端项目（React + Vite）
│   ├── src/
│   │   ├── pages/               # 页面组件
│   │   ├── services/            # API 服务
│   │   ├── components/          # 公共组件
│   │   └── types/               # TypeScript 类型定义
│   ├── package.json
│   └── vite.config.ts
├── src/main/java/com/example/digitalhuman/
│   ├── config/                  # 数据库配置
│   ├── dao/                     # 数据访问层
│   ├── entity/                  # 实体类
│   ├── servlet/                 # Servlet 控制器
│   └── util/                    # 工具类
├── src/main/webapp/
│   └── WEB-INF/web.xml          # Web 应用配置
├── pom.xml                      # Maven 配置
└── mvnw / mvnw.cmd              # Maven Wrapper
```

## 环境要求

- **JDK** 11 或更高版本
- **Maven** 3.8+（项目已包含 Maven Wrapper，无需单独安装）
- **Node.js** 18+
- **npm** 9+

## 快速启动

### 方式一：前后端分别启动（开发推荐）

#### 1. 启动后端服务

```bash
# 给 mvnw 添加执行权限（仅首次）
chmod +x mvnw

# 启动 Jetty 服务器（端口 8080）
./mvnw jetty:run
```

#### 2. 启动前端服务

```bash
# 进入前端目录
cd frontend

# 安装依赖（仅首次）
npm install

# 启动开发服务器（端口 5173）
npm run dev
```

#### 3. 访问应用

| 服务 | 地址 |
|------|------|
| 前端 | http://localhost:5173 |
| 后端 API | http://localhost:8080 |

### 方式二：使用系统 Maven

```bash
# 后端
mvn jetty:run

# 前端（另开终端）
cd frontend && npm run dev
```

### 方式三：打包后部署到 Tomcat

```bash
# 1. 打包前端
cd frontend
npm run build

# 2. 打包后端（会自动包含前端构建产物）
cd ..
./mvnw clean package

# 3. 将 target/digital-human-1.0-SNAPSHOT.war 部署到 Tomcat 的 webapps 目录
```

## 数据库说明

项目使用 **H2 内存数据库**，无需额外安装：

- 驱动：`org.h2.Driver`
- 连接地址：`jdbc:h2:mem:digital_human;DB_CLOSE_DELAY=-1;MODE=MySQL`
- 用户名：`sa`
- 密码：空
- 特点：内存模式，重启后数据清空；兼容 MySQL 语法

默认测试账号：
- 用户名：`admin`
- 密码：`123456`

## 功能模块

- ✅ **形象定制**：支持多种风格、肤色、发色、服装颜色定制
- ✅ **语音合成**：男女声自由切换
- ✅ **视频生成**：一键合成讲解视频
- ✅ **作品管理**：查看、播放、删除已生成的作品

## 停止服务器

在运行终端按 `Ctrl + C` 停止服务器。

## 技术栈

### 前端
- React 19
- TypeScript
- Vite
- Ant Design 6
- Tailwind CSS 4
- React Router 7

### 后端
- Java 11
- Jakarta Servlet 5.0
- H2 Database（内存数据库）
- Apache Commons DBCP2（连接池）
- Jetty 11（嵌入式服务器）
- Maven 3.8
