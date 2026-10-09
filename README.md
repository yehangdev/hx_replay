# 到店核销链路查询服务

到店餐饮核销与秒提取餐链路的只读查询服务，主要供研发和运营按核销码、POI、日期排查核销差异、出餐延迟和取餐状态问题。

## 使用场景

1. 按 verificationCode 回溯核销记录和关键时间点。
2. 查询 POI 的秒提取餐订单及汇总指标。
3. 按 POI、日期和通道查看核销量、出餐和取餐统计。
4. 按通道查看 GROUPON / MTSECONDPICKUP / DP 等来源的核销异常与缺取餐分布。

核销码查询结果使用共享 Redis 做 10 分钟缓存，Redis 异常时自动回退 MySQL 查询。

## 查询约束

- 按 verificationCode 查询默认覆盖最近 30 天，也可显式传入 startDate / endDate。
- trace、pickup orders 与汇总查询的单次日期窗口最多 31 个自然日。
- 本服务只读，不提供写库、重放、补跑或告警操作入口。

## 相关文档

- [部署与数据源配置](docs/deployment.md)
- [Redis 连通性检查](scripts/check-redis-connection.sh)
- [MySQL 初始化表](scripts/init-mysql-tables.sql)

## 更新记录

版本变化见 [CHANGELOG.md](CHANGELOG.md)。

## 内部来源

本仓库为公司内部只读工作区的审阅快照，内部 Git 域为 `dev.sankuai.com`。
