#!/bin/bash

# OA办公系统 Docker 构建和运行脚本

set -e

echo "=== OA办公系统 Docker 部署脚本 ==="

# 颜色定义
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 函数：打印带颜色的消息
print_message() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 检查Docker是否安装
check_docker() {
    if ! command -v docker &> /dev/null; then
        print_error "Docker 未安装，请先安装 Docker"
        exit 1
    fi
    
    if ! command -v docker-compose &> /dev/null; then
        print_error "Docker Compose 未安装，请先安装 Docker Compose"
        exit 1
    fi
    
    print_message "Docker 环境检查通过"
}

# 创建必要的目录
create_directories() {
    print_message "创建必要的目录..."
    mkdir -p logs uploads mysql/init nginx/conf.d ssl
}

# 构建镜像
build_image() {
    print_message "开始构建 Docker 镜像..."
    docker-compose build --no-cache
    print_message "镜像构建完成"
}

# 启动服务
start_services() {
    print_message "启动服务..."
    docker-compose up -d
    
    print_message "等待服务启动..."
    sleep 30
    
    # 检查服务状态
    docker-compose ps
}

# 停止服务
stop_services() {
    print_message "停止服务..."
    docker-compose down
}

# 重启服务
restart_services() {
    print_message "重启服务..."
    docker-compose restart
}

# 查看日志
view_logs() {
    print_message "查看应用日志..."
    docker-compose logs -f oa-app
}

# 清理资源
cleanup() {
    print_message "清理 Docker 资源..."
    docker-compose down -v
    docker system prune -f
}

# 显示帮助信息
show_help() {
    echo "用法: $0 [选项]"
    echo ""
    echo "选项:"
    echo "  build     构建 Docker 镜像"
    echo "  start     启动所有服务"
    echo "  stop      停止所有服务"
    echo "  restart   重启所有服务"
    echo "  logs      查看应用日志"
    echo "  cleanup   清理 Docker 资源"
    echo "  deploy    完整部署（构建 + 启动）"
    echo "  help      显示此帮助信息"
    echo ""
    echo "示例:"
    echo "  $0 deploy    # 完整部署"
    echo "  $0 logs      # 查看日志"
    echo "  $0 stop      # 停止服务"
}

# 主函数
main() {
    case "${1:-help}" in
        "build")
            check_docker
            create_directories
            build_image
            ;;
        "start")
            check_docker
            start_services
            ;;
        "stop")
            stop_services
            ;;
        "restart")
            restart_services
            ;;
        "logs")
            view_logs
            ;;
        "cleanup")
            cleanup
            ;;
        "deploy")
            check_docker
            create_directories
            build_image
            start_services
            print_message "部署完成！"
            print_message "应用访问地址: http://localhost:8800"
            print_message "数据库端口: 3306"
            print_message "Redis端口: 6379"
            ;;
        "help"|*)
            show_help
            ;;
    esac
}

# 执行主函数
main "$@" 