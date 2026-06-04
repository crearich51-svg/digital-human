# 数字人智能讲解员

一个基于 Java Web 的数字人智能讲解员项目。

## 项目结构

```
digital-human/
├── src/main/java/com/example/digitalhuman/
│   └── HelloServlet.java       # Servlet 示例
├── src/main/webapp/
│   ├── WEB-INF/
│   │   └── web.xml             # Web 应用配置
│   ├── index.html              # 首页
│   ├── create.html             # 创作页
│   ├── works.html              # 我的作品页
│   └── index.jsp               # JSP 示例
├── pom.xml                     # Maven 配置
└── mvnw / mvnw.cmd             # Maven Wrapper
```

## 环境要求

- JDK 11 或更高版本
- Maven 3.8+（项目已包含 Maven Wrapper，无需单独安装）

## 运行方式

### 方式一：使用 Maven Wrapper（推荐）

```bash
# 1. 给 mvnw 添加执行权限（仅首次）
chmod +x mvnw

# 2. 设置 JAVA_HOME（如果系统没有默认配置）
export JAVA_HOME="/path/to/your/jdk"

# 3. 启动 Jetty 服务器
./mvnw jetty:run
```

### 方式二：使用系统 Maven

```bash
mvn jetty:run
```

### 方式三：打包后部署到 Tomcat

```bash
# 打包生成 WAR 文件
./mvnw clean package

# 将 target/digital-human-1.0-SNAPSHOT.war 部署到 Tomcat 的 webapps 目录
```

## 访问地址

服务器启动后，可访问以下地址：

| 页面 | 地址 |
|------|------|
| 首页 | http://localhost:8080/index.html |
| 创作页 | http://localhost:8080/create.html |
| 我的作品 | http://localhost:8080/works.html |
| HelloServlet | http://localhost:8080/hello-servlet |
| JSP 示例 | http://localhost:8080/index.jsp |

## 停止服务器

在运行终端按 `Ctrl + C` 停止服务器。

## 技术栈

- Java 11
- Jakarta Servlet 5.0
- Jetty 11（嵌入式服务器）
- Maven 3.8
