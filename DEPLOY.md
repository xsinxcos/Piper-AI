# Docker 部署说明

## 环境要求
- Docker 20.10.0+
- Docker Compose 2.0.0+
- 至少 2GB 可用内存

## 部署步骤

1. 克隆代码库
```bash
git clone [your-repository-url]
cd Piper-AI
```

2. 构建并启动服务
```bash
docker-compose up -d
```

3. 查看服务状态
```bash
docker-compose ps
```

4. 查看日志
```bash
docker-compose logs -f
```

## 常用命令

- 停止服务
```bash
docker-compose down
```

- 重启服务
```bash
docker-compose restart
```

- 查看服务日志
```bash
docker-compose logs -f
```

- 进入容器
```bash
docker-compose exec piper-ai bash
```

## 配置说明

### 端口映射
- 应用服务端口：8080
- 映射到宿主机端口：8080

### 环境变量
- SPRING_PROFILES_ACTIVE=prod：使用生产环境配置

### 数据卷
- 日志目录：./logs:/app/logs

## 健康检查
服务包含健康检查机制，每30秒检查一次服务状态。可以通过以下命令查看健康状态：
```bash
docker inspect --format='{{json .State.Health}}' piper-ai
```

## 注意事项
1. 首次部署时，构建镜像可能需要较长时间
2. 确保 8080 端口未被其他服务占用
3. 生产环境部署时，建议修改默认配置
4. 日志文件会保存在宿主机的 ./logs 目录下

## 故障排查
1. 如果服务无法启动，检查日志：
```bash
docker-compose logs -f
```

2. 如果端口冲突，修改 docker-compose.yml 中的端口映射

3. 如果内存不足，可以调整 JVM 参数：
在 docker-compose.yml 中添加环境变量：
```yaml
environment:
  - JAVA_OPTS=-Xmx512m -Xms256m
``` 