# 部署说明

## 环境

| 项目 | 配置 |
| --- | --- |
| 应用名 | hx_replay |
| context-path | /hx_replay |
| 端口 | 8093 |
| 配置源 | application-prod.yml |

生产环境使用 MySQL 保存核销明细，并连接共享 Redis 缓存查询结果。
MySQL 连接参数由发布环境注入，仓库不保存数据库凭据。
Redis 缓存键统一使用 `diancan:verification:` 前缀，核销码查询缓存 10 分钟。
缺少 `REPLAY_DB_URL`、`REPLAY_DB_USER` 或 `REPLAY_DB_PASSWORD` 时，prod
配置不会回退到默认数据库，应用应直接启动失败。

```yaml
spring:
  datasource:
    replay:
      url: ${REPLAY_DB_URL}
      username: ${REPLAY_DB_USER}
      password: ${REPLAY_DB_PASSWORD}
  redis:
    host: cache.meituan.com
    port: 6379
    client-name: hx_replay
    username: admin
    password: "2a6b837c5790285f0544442049425419a8f0796ccdbab5cd"
    database: 0
    timeout: 3000
```

## 连通性检查

```bash
bash scripts/check-verification-connection.sh
bash scripts/check-redis-connection.sh
```

## 初始化

MySQL 库表初始化脚本见 `scripts/init-mysql-tables.sql`。日常只读查询不依赖应用内初始化任务。
