CREATE TABLE IF NOT EXISTS `dwd_diancan_verification_trace` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `verification_code` VARCHAR(64) NOT NULL COMMENT '核销码',
    `poi_id` BIGINT NOT NULL COMMENT '门店 POI',
    `channel` VARCHAR(32) NOT NULL COMMENT '通道',
    `dt` DATE NOT NULL COMMENT '业务日期',
    `deal_group_id` VARCHAR(64) DEFAULT NULL COMMENT '团购套餐组',
    `pickup_code` VARCHAR(64) DEFAULT NULL COMMENT '秒提取餐码',
    `cabinet_no` VARCHAR(32) DEFAULT NULL COMMENT '取餐柜编号',
    `merchant_ready_at` DATETIME DEFAULT NULL COMMENT '商家标记出餐',
    `picked_at` DATETIME DEFAULT NULL COMMENT '用户取餐',
    `prep_completed_offset` INT DEFAULT NULL COMMENT '出餐完成偏移',
    `verification_status` TINYINT DEFAULT NULL COMMENT '核销状态',
    `source_system` VARCHAR(32) DEFAULT NULL COMMENT '来源系统',
    `create_time` DATETIME NOT NULL COMMENT '创建时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_trace_business` (`verification_code`, `poi_id`, `channel`, `dt`),
    KEY `idx_trace_dt_create` (`dt`, `create_time`),
    KEY `idx_trace_poi_dt` (`poi_id`, `dt`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='核销链路明细';

CREATE TABLE IF NOT EXISTS `dwd_poi_seconds_pickup_order` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `poi_id` BIGINT NOT NULL,
    `dt` DATE NOT NULL,
    `pickup_code` VARCHAR(64) NOT NULL,
    `order_no` VARCHAR(64) NOT NULL,
    `merchant_id` VARCHAR(64) DEFAULT NULL,
    `cabinet_no` VARCHAR(32) DEFAULT NULL,
    `source_system` VARCHAR(32) DEFAULT NULL,
    `order_create_time` DATETIME NOT NULL,
    `merchant_ready_at` DATETIME DEFAULT NULL,
    `picked_at` DATETIME DEFAULT NULL,
    `channel` VARCHAR(32) DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_pickup_order` (`poi_id`, `dt`, `pickup_code`, `order_no`),
    KEY `idx_pickup_poi_time` (`poi_id`, `order_create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='秒提取餐订单';

CREATE TABLE IF NOT EXISTS `dws_poi_pickup_metrics` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `poi_id` BIGINT NOT NULL,
    `stat_date` DATE NOT NULL,
    `etl_version` INT NOT NULL,
    `total_orders` INT NOT NULL DEFAULT 0,
    `completed_pickup` INT NOT NULL DEFAULT 0,
    `abandoned_pickup` INT NOT NULL DEFAULT 0,
    `avg_ready_offset_minutes` DECIMAL(10, 2) DEFAULT NULL,
    `avg_pickup_gap_minutes` DECIMAL(10, 2) DEFAULT NULL,
    `no_show_count` INT NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_poi_metrics_version` (`poi_id`, `stat_date`, `etl_version`),
    KEY `idx_metrics_date` (`stat_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='POI 秒提汇总指标';

CREATE TABLE IF NOT EXISTS `dwd_diancan_verification_event` (
    `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
    `verification_code` VARCHAR(64) NOT NULL COMMENT '核销码',
    `dt` DATE NOT NULL COMMENT '业务日期',
    `event_id` VARCHAR(64) NOT NULL COMMENT '事件唯一 ID',
    `poi_id` BIGINT NOT NULL COMMENT '门店 POI',
    `channel` VARCHAR(32) NOT NULL COMMENT '通道',
    `event_type` VARCHAR(32) NOT NULL COMMENT '事件类型',
    `event_name` VARCHAR(64) DEFAULT NULL COMMENT '事件名称',
    `event_time` DATETIME NOT NULL COMMENT '事件时间',
    `offset_seconds` INT DEFAULT NULL COMMENT '相对核销创建偏移',
    `detail` VARCHAR(512) DEFAULT NULL COMMENT '事件详情',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_trace_event` (`verification_code`, `dt`, `event_id`, `poi_id`),
    KEY `idx_event_trace_time` (`verification_code`, `dt`, `event_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='核销链路事件';
