# OA 办公自动化系统

基于 Spring Boot 的企业级办公自动化（OA）系统，提供用户管理、角色权限控制、审批流程管理和微信公众号集成等功能。

## 技术栈

| 技术 | 版本 | 说明 |
|------|------|------|
| Spring Boot | 2.3.6.RELEASE | 基础框架 |
| Spring Security | - | 认证与授权 |
| MyBatis-Plus | 3.4.1 | ORM 持久层框架 |
| Activiti | 7.1.0.M6 | 工作流引擎 |
| MySQL | 8.0.30 | 关系型数据库 |
| Redis | - | 缓存与 Token 管理 |
| JWT | 0.9.1 | 无状态身份认证 |
| Knife4j | 3.0.3 | API 接口文档 |
| Fastjson | 2.0.21 | JSON 序列化 |
| WxJava | 4.1.0 | 微信公众号开发 |
| Lombok | - | 简化 Java 代码 |

## 项目结构

```
OA
├── common                          # 公共模块
│   ├── common-util                 # 通用工具类
│   │   └── src/main/java/com/bangong/common
│   │       ├── jwt/JwtHelper.java          # JWT 令牌工具
│   │       ├── result/Result.java           # 统一响应结果
│   │       ├── result/ResultCodeEnum.java   # 响应状态码枚举
│   │       ├── utils/MD5.java               # MD5 加密工具
│   │       └── utils/ResponseUtil.java      # 响应工具类
│   ├── service-util                # 服务公共配置
│   │   └── src/main/java/com/bangong/common
│   │       ├── config/knife4j/              # Knife4j 文档配置
│   │       ├── config/mp/                   # MyBatis-Plus 配置
│   │       ├── exception/                   # 自定义业务异常
│   │       └── handler/                     # 全局异常处理器
│   └── spring-security             # Spring Security 安全模块
│       └── src/main/java/com/bangong/security
│           ├── config/WebSecurityConfig.java        # 安全配置
│           ├── custom/CustomMd5PasswordEncoder.java # MD5 密码编码器
│           ├── custom/CustomUser.java               # 自定义用户实体
│           ├── custom/LoginUserInfoHelper.java      # 登录用户信息工具
│           ├── custom/UserDetailsService.java       # 用户详情服务
│           ├── filter/TokenLoginFilter.java         # 登录认证过滤器
│           └── filter/TokenAuthenticationFilter.java # Token 认证过滤器
├── model                           # 数据模型模块
│   └── src/main/java/com/bangong
│       ├── model
│       │   ├── base/BaseEntity.java       # 基础实体（id、时间、逻辑删除）
│       │   ├── system/                    # 系统模块实体
│       │   │   ├── SysUser.java           # 系统用户
│       │   │   ├── SysRole.java           # 系统角色
│       │   │   ├── SysMenu.java           # 系统菜单
│       │   │   ├── SysDept.java           # 系统部门
│       │   │   ├── SysPost.java           # 系统岗位
│       │   │   ├── SysRoleMenu.java       # 角色-菜单关联
│       │   │   ├── SysUserRole.java       # 用户-角色关联
│       │   │   ├── SysLoginLog.java       # 登录日志
│       │   │   └── SysOperLog.java        # 操作日志
│       │   ├── process/                   # 审批流程实体
│       │   │   ├── Process.java           # 审批流程
│       │   │   ├── ProcessRecord.java     # 审批记录
│       │   │   ├── ProcessTemplate.java   # 审批模板
│       │   │   └── ProcessType.java       # 审批类型
│       │   └── wechat/Menu.java           # 微信公众号菜单
│       └── vo/                            # 视图对象（View Object）
│           ├── system/                    # 系统模块 VO
│           ├── process/                   # 流程模块 VO
│           └── wechat/                    # 微信模块 VO
└── service-oa                      # OA 主服务模块
    └── src/main/java/com/bangong
        ├── ServiceAuthApplication.java    # 应用启动入口
        ├── auth/                          # 权限管理模块
        │   ├── controller/
        │   │   ├── IndexController.java           # 登录入口
        │   │   ├── SysUserController.java         # 用户管理 API
        │   │   ├── SysRoleController.java         # 角色管理 API
        │   │   └── SysMenuController.java         # 菜单管理 API
        │   ├── service/                           # 业务接口
        │   │   ├── SysUserService.java
        │   │   ├── SysRoleService.java
        │   │   └── SysMenuService.java
        │   ├── service/impl/                      # 业务实现
        │   │   ├── SysUserServiceImpl.java
        │   │   ├── SysRoleServiceImpl.java
        │   │   ├── SysMenuServiceImpl.java
        │   │   └── UserDetailsServiceImpl.java
        │   ├── mapper/                            # 数据访问层
        │   │   ├── SysUserMapper.java
        │   │   ├── SysRoleMapper.java
        │   │   ├── SysMenuMapper.java
        │   │   ├── SysRoleMenuMapper.java
        │   │   └── SysUserRoleMapper.java
        │   ├── mapper/xml/                        # MyBatis XML 映射
        │   └── MenuHelper/MenuHelper.java         # 菜单构建工具
        ├── process/                       # 审批流程模块
        │   ├── controller/
        │   │   ├── ProcessController.java         # 审批管理 API
        │   │   ├── ProcessApiController.java      # 审批流程 API
        │   │   ├── ProcessTemplateController.java # 审批模板管理 API
        │   │   └── ProcessTypeController.java     # 审批类型管理 API
        │   ├── service/
        │   │   ├── ProcessService.java
        │   │   ├── ProcessRecordService.java
        │   │   ├── ProcessTemplateService.java
        │   │   └── ProcessTypeService.java
        │   └── mapper/
        │       ├── ProcessMapper.java
        │       ├── ProcessRecordMapper.java
        │       ├── ProcessTemplateMapper.java
        │       └── ProcessTypeMapper.java
        └── wechat/                        # 微信公众号模块
            ├── controller/MenuController.java
            └── service/MenuService.java
```

## 核心功能

### 1. 用户认证与权限管理
- 基于 **JWT** 的无状态身份认证，Token 有效期 365 天
- 采用 **Spring Security** 实现接口级别的权限控制
- 密码使用 **MD5 加盐**加密存储
- 支持 **RBAC**（基于角色的访问控制）：用户 → 角色 → 菜单权限
- 支持 `@PreAuthorize` 注解方式控制方法级权限

### 2. 审批流程管理
- 基于 **Activiti 7** 工作流引擎实现流程审批
- 支持审批类型管理（请假、报销、用章等）
- 支持审批模板管理
- 支持审批记录追踪
- 流程历史数据完整保存（`history-level: full`）

### 3. 微信公众号集成
- 微信公众号自定义菜单管理
- 支持微信用户绑定手机号

### 4. 系统管理
- 用户管理：增删改查、状态启用/禁用
- 角色管理：角色分配与权限设置
- 菜单管理：动态菜单树构建
- 部门管理、岗位管理
- 登录日志与操作日志记录

### 5. API 文档
- 集成 **Knife4j**，提供可视化 API 接口文档
- 访问路径：`/doc.html`

## 快速开始

### 环境要求

- JDK 1.8+
- Maven 3.6+
- MySQL 8.0+
- Redis

### 数据库初始化

1. 创建 MySQL 数据库：
```sql
CREATE DATABASE `guigu-oa` DEFAULT CHARACTER SET utf8mb4;
```

2. 配置 `service-oa/src/main/resources/application-dev.yml` 中的数据库连接信息和 Redis 连接信息。

### 启动项目

```bash
# 克隆项目
git clone https://github.com/GZMULDY/OA.git
cd OA

# 编译打包
mvn clean install -DskipTests

# 启动服务
cd service-oa
mvn spring-boot:run
```

服务默认运行在 **8800** 端口。

### API 文档

启动后访问：`http://localhost:8800/doc.html`

## 安全配置

- 登录接口 `/admin/system/index/login` 无需认证
- Swagger/Knife4j 相关路径无需认证
- 其他所有接口需要携带 JWT Token 访问
- 禁用 Session，采用无状态 Token 认证
- 开启 CORS 跨域支持
- 关闭 CSRF 防护

## 许可证

本项目仅用于学习交流目的。
