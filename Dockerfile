# 多阶段构建Dockerfile
# 第一阶段：构建阶段
FROM maven:3.8.4-openjdk-8 AS builder

# 设置工作目录
WORKDIR /app

# 复制Maven配置文件
COPY pom.xml .
COPY common/pom.xml common/
COPY model/pom.xml model/
COPY service-oa/pom.xml service-oa/

# 下载依赖（利用Docker缓存层）
RUN mvn dependency:go-offline -B

# 复制源代码
COPY . .

# 构建项目
RUN mvn clean package -DskipTests -pl service-oa

# 第二阶段：运行阶段
FROM openjdk:8-jre-alpine

# 设置工作目录
WORKDIR /app

# 安装必要的工具
RUN apk add --no-cache curl

# 创建非root用户
RUN addgroup -g 1001 -S appgroup && \
    adduser -u 1001 -S appuser -G appgroup

# 从构建阶段复制jar文件
COPY --from=builder /app/service-oa/target/service-oa-1.0-SNAPSHOT.jar app.jar

# 创建配置文件目录
RUN mkdir -p /app/config

# 复制配置文件（如果需要）
COPY service-oa/src/main/resources/application.yml /app/config/
COPY service-oa/src/main/resources/application-dev.yml /app/config/

# 设置文件权限
RUN chown -R appuser:appgroup /app

# 切换到非root用户
USER appuser

# 暴露端口
EXPOSE 8800

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 \
  CMD curl -f http://localhost:8800/actuator/health || exit 1

# 启动应用
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.config.location=classpath:/,file:/app/config/"] 