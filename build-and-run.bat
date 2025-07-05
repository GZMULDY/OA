@echo off
chcp 65001 >nul
setlocal enabledelayedexpansion

echo === OA办公系统 Docker 部署脚本 ===

:: 颜色定义
set "GREEN=[92m"
set "YELLOW=[93m"
set "RED=[91m"
set "NC=[0m"

:: 函数：打印带颜色的消息
:print_message
echo %GREEN%[INFO]%NC% %~1
goto :eof

:print_warning
echo %YELLOW%[WARNING]%NC% %~1
goto :eof

:print_error
echo %RED%[ERROR]%NC% %~1
goto :eof

:: 检查Docker是否安装
:check_docker
docker --version >nul 2>&1
if errorlevel 1 (
    call :print_error "Docker 未安装，请先安装 Docker"
    exit /b 1
)

docker-compose --version >nul 2>&1
if errorlevel 1 (
    call :print_error "Docker Compose 未安装，请先安装 Docker Compose"
    exit /b 1
)

call :print_message "Docker 环境检查通过"
goto :eof

:: 创建必要的目录
:create_directories
call :print_message "创建必要的目录..."
if not exist "logs" mkdir logs
if not exist "uploads" mkdir uploads
if not exist "mysql\init" mkdir mysql\init
if not exist "nginx\conf.d" mkdir nginx\conf.d
if not exist "ssl" mkdir ssl
goto :eof

:: 构建镜像
:build_image
call :print_message "开始构建 Docker 镜像..."
docker-compose build --no-cache
if errorlevel 1 (
    call :print_error "镜像构建失败"
    exit /b 1
)
call :print_message "镜像构建完成"
goto :eof

:: 启动服务
:start_services
call :print_message "启动服务..."
docker-compose up -d
if errorlevel 1 (
    call :print_error "服务启动失败"
    exit /b 1
)

call :print_message "等待服务启动..."
timeout /t 30 /nobreak >nul

call :print_message "检查服务状态..."
docker-compose ps
goto :eof

:: 停止服务
:stop_services
call :print_message "停止服务..."
docker-compose down
goto :eof

:: 重启服务
:restart_services
call :print_message "重启服务..."
docker-compose restart
goto :eof

:: 查看日志
:view_logs
call :print_message "查看应用日志..."
docker-compose logs -f oa-app
goto :eof

:: 清理资源
:cleanup
call :print_message "清理 Docker 资源..."
docker-compose down -v
docker system prune -f
goto :eof

:: 显示帮助信息
:show_help
echo 用法: %0 [选项]
echo.
echo 选项:
echo   build     构建 Docker 镜像
echo   start     启动所有服务
echo   stop      停止所有服务
echo   restart   重启所有服务
echo   logs      查看应用日志
echo   cleanup   清理 Docker 资源
echo   deploy    完整部署（构建 + 启动）
echo   help      显示此帮助信息
echo.
echo 示例:
echo   %0 deploy    # 完整部署
echo   %0 logs      # 查看日志
echo   %0 stop      # 停止服务
goto :eof

:: 主函数
:main
if "%1"=="" goto show_help
if "%1"=="help" goto show_help
if "%1"=="build" goto do_build
if "%1"=="start" goto do_start
if "%1"=="stop" goto do_stop
if "%1"=="restart" goto do_restart
if "%1"=="logs" goto do_logs
if "%1"=="cleanup" goto do_cleanup
if "%1"=="deploy" goto do_deploy
goto show_help

:do_build
call :check_docker
if errorlevel 1 exit /b 1
call :create_directories
call :build_image
goto :eof

:do_start
call :check_docker
if errorlevel 1 exit /b 1
call :start_services
goto :eof

:do_stop
call :stop_services
goto :eof

:do_restart
call :restart_services
goto :eof

:do_logs
call :view_logs
goto :eof

:do_cleanup
call :cleanup
goto :eof

:do_deploy
call :check_docker
if errorlevel 1 exit /b 1
call :create_directories
call :build_image
if errorlevel 1 exit /b 1
call :start_services
if errorlevel 1 exit /b 1
call :print_message "部署完成！"
call :print_message "应用访问地址: http://localhost:8800"
call :print_message "数据库端口: 3306"
call :print_message "Redis端口: 6379"
goto :eof

:: 执行主函数
call :main %* 