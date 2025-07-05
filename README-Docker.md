# OA办公系统 Docker 部署指南

## 概述

本项目提供了完整的Docker部署方案，包括：
- 多阶段构建的Dockerfile
- Docker Compose编排文件
- 自动化部署脚本
- 生产环境配置

## 系统要求

- Docker 20.10+
- Docker Compose 2.0+
- 至少4GB可用内存
- 至少10GB可用磁盘空间

## 快速开始

### 1. 克隆项目
```bash
git clone <your-repository-url>
cd OA-parent
```

### 2. 使用自动化脚本部署（推荐）

#### Windows用户：
```cmd
build-and-run.bat deploy
```

#### Linux/Mac用户：
```bash
chmod +x build-and-run.sh
./build-and-run.sh deploy
```

### 3. 手动部署

#### 构建镜像
```bash
docker-compose build
```

#### 启动服务
```bash
docker-compose up -d
```

#### 查看服务状态
```bash
docker-compose ps
```

## 服务说明

### 应用服务
- **服务名**: oa-app
- **端口**: 8800
- **访问地址**: http://localhost:8800
- **健康检查**: http://localhost:8800/actuator/health

### 数据库服务
- **服务名**: mysql
- **端口**: 3306
- **数据库名**: guigu-oa
- **用户名**: root
- **密码**: abc&123

### 缓存服务
- **服务名**: redis
- **端口**: 6379
- **密码**: 123456

### 反向代理（可选）
- **服务名**: nginx
- **端口**: 80, 443

## 配置文件

### Docker环境配置
- `application-docker.yml`: Docker环境专用配置
- 支持环境变量覆盖配置

### 环境变量
```bash
# 数据库配置
SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/guigu-oa?serverTimezone=GMT%2B8&useSSL=false&characterEncoding=utf-8
SPRING_DATASOURCE_USERNAME=root
SPRING_DATASOURCE_PASSWORD=abc&123

# Redis配置
SPRING_REDIS_HOST=redis
SPRING_REDIS_PORT=6379
SPRING_REDIS_PASSWORD=123456

# 应用配置
SPRING_PROFILES_ACTIVE=docker
```

## 常用命令

### 服务管理
```bash
# 启动所有服务
docker-compose up -d

# 停止所有服务
docker-compose down

# 重启所有服务
docker-compose restart

# 查看服务状态
docker-compose ps

# 查看日志
docker-compose logs -f oa-app
```

### 镜像管理
```bash
# 重新构建镜像
docker-compose build --no-cache

# 清理未使用的镜像
docker system prune -f

# 查看镜像列表
docker images
```

### 数据管理
```bash
# 备份数据库
docker exec oa-mysql mysqldump -u root -p guigu-oa > backup.sql

# 恢复数据库
docker exec -i oa-mysql mysql -u root -p guigu-oa < backup.sql

# 查看数据卷
docker volume ls
```

## 目录结构

```
OA-parent/
├── Dockerfile                 # 多阶段构建文件
├── docker-compose.yml         # 服务编排文件
├── .dockerignore             # Docker忽略文件
├── build-and-run.sh          # Linux/Mac部署脚本
├── build-and-run.bat         # Windows部署脚本
├── logs/                     # 应用日志目录
├── uploads/                  # 文件上传目录
├── mysql/
│   └── init/                 # 数据库初始化脚本
└── nginx/
    └── conf.d/               # Nginx配置文件
```

## 故障排除

### 1. 端口冲突
如果端口被占用，修改`docker-compose.yml`中的端口映射：
```yaml
ports:
  - "8801:8800"  # 改为8801端口
```

### 2. 内存不足
增加Docker内存限制或优化JVM参数：
```yaml
environment:
  - JAVA_OPTS=-Xmx1g -Xms512m
```

### 3. 数据库连接失败
检查数据库服务是否正常启动：
```bash
docker-compose logs mysql
```

### 4. 应用启动失败
查看应用日志：
```bash
docker-compose logs oa-app
```

## 生产环境部署

### 1. 安全配置
- 修改默认密码
- 配置SSL证书
- 设置防火墙规则
- 启用日志轮转

### 2. 性能优化
- 调整JVM参数
- 配置数据库连接池
- 启用Redis集群
- 配置CDN

### 3. 监控告警
- 配置健康检查
- 设置日志监控
- 配置性能指标
- 设置告警规则

## 备份和恢复

### 数据库备份
```bash
# 创建备份脚本
cat > backup.sh << 'EOF'
#!/bin/bash
DATE=$(date +%Y%m%d_%H%M%S)
docker exec oa-mysql mysqldump -u root -pabc&123 guigu-oa > backup_${DATE}.sql
EOF

chmod +x backup.sh
./backup.sh
```

### 应用数据备份
```bash
# 备份上传文件
tar -czf uploads_backup_$(date +%Y%m%d).tar.gz uploads/

# 备份日志文件
tar -czf logs_backup_$(date +%Y%m%d).tar.gz logs/
```

## 更新部署

### 1. 拉取最新代码
```bash
git pull origin main
```

### 2. 重新构建和部署
```bash
# 使用脚本
./build-and-run.sh deploy

# 或手动操作
docker-compose down
docker-compose build --no-cache
docker-compose up -d
```

## 技术支持

如果遇到问题，请：
1. 查看日志文件
2. 检查配置文件
3. 验证网络连接
4. 联系技术支持

## 许可证

本项目采用 [MIT License](LICENSE) 许可证。 