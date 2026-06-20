package com.sankuai.diancan.replay.repository;

import com.sankuai.diancan.replay.model.ChannelAbnormalStat;
import com.sankuai.diancan.replay.model.PickupOrder;
import com.sankuai.diancan.replay.model.PoiDailySummary;
import com.sankuai.diancan.replay.model.TraceEvent;
import com.sankuai.diancan.replay.model.VerificationTrace;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
public class VerificationTraceRepository {

    private static final int MAX_TRACE_LIMIT = 500;

    @Autowired
    @Qualifier("replayJdbcTemplate")
    private JdbcTemplate replayJdbcTemplate;

    public List<VerificationTrace> findByVerificationCode(
            String verificationCode, LocalDate startDate, LocalDate endDate, int limit) {
        return replayJdbcTemplate.query(
                "SELECT verification_code, deal_group_id, poi_id, channel, pickup_code, "
                        + "cabinet_no, merchant_ready_at, picked_at, prep_completed_offset, "
                        + "verification_status, source_system, create_time, dt "
                        + "FROM dwd_diancan_verification_trace "
                        + "WHERE verification_code = ? AND dt >= ? AND dt <= ? "
                        + "ORDER BY create_time LIMIT ?",
                new Object[]{verificationCode, startDate, endDate, normalizeLimit(limit)},
                new BeanPropertyRowMapper<>(VerificationTrace.class));
    }

    public List<PickupOrder> findPickupOrders(
            Long poiId, LocalDate startDate, LocalDate endDate, int limit) {
        return replayJdbcTemplate.query(
                "SELECT pickup_code, order_no, poi_id, merchant_id, cabinet_no, source_system, "
                        + "order_create_time, merchant_ready_at, picked_at, channel, dt "
                        + "FROM dwd_poi_seconds_pickup_order "
                        + "WHERE poi_id = ? AND dt >= ? AND dt <= ? "
                        + "ORDER BY order_create_time DESC LIMIT ?",
                new Object[]{poiId, startDate, endDate, normalizeLimit(limit)},
                new BeanPropertyRowMapper<>(PickupOrder.class));
    }

    public List<VerificationTrace> findByQuery(
            Long poiId, LocalDate startDate, LocalDate endDate, String channel, int limit) {
        StringBuilder sql = new StringBuilder(
                "SELECT verification_code, deal_group_id, poi_id, channel, pickup_code, "
                        + "cabinet_no, merchant_ready_at, picked_at, prep_completed_offset, "
                        + "verification_status, source_system, create_time, dt "
                        + "FROM dwd_diancan_verification_trace WHERE 1=1 ");
        List<Object> params = new ArrayList<>();
        if (poiId != null) {
            sql.append("AND poi_id = ? ");
            params.add(poiId);
        }
        if (startDate != null) {
            sql.append("AND dt >= ? ");
            params.add(startDate);
        }
        if (endDate != null) {
            sql.append("AND dt <= ? ");
            params.add(endDate);
        }
        if (channel != null && !channel.isBlank()) {
            sql.append("AND channel = ? ");
            params.add(channel);
        }
        sql.append("ORDER BY create_time DESC LIMIT ?");
        params.add(normalizeLimit(limit));
        return replayJdbcTemplate.query(
                sql.toString(), params.toArray(), new BeanPropertyRowMapper<>(VerificationTrace.class));
    }

    public List<TraceEvent> findTimelineEvents(
            String verificationCode, LocalDate startDate, LocalDate endDate, int limit) {
        return replayJdbcTemplate.query(
                "SELECT event_id, event_type, event_name, event_time, offset_seconds, detail "
                        + "FROM dwd_diancan_verification_event "
                        + "WHERE verification_code = ? AND dt >= ? AND dt <= ? "
                        + "ORDER BY event_time LIMIT ?",
                new Object[]{verificationCode, startDate, endDate, normalizeLimit(limit)},
                (rs, rowNum) -> mapTimelineEvent(rs));
    }

    public List<PoiDailySummary> findPoiDailySummaries(
            Long poiId, LocalDate startDate, LocalDate endDate, String channel) {
        StringBuilder sql = new StringBuilder(
                "SELECT poi_id, dt AS stat_date, channel, "
                        + "COUNT(*) AS total_orders, "
                        + "SUM(CASE WHEN verification_status = 1 THEN 1 ELSE 0 END) AS verified_orders, "
                        + "SUM(CASE WHEN picked_at IS NOT NULL THEN 1 ELSE 0 END) AS pickup_completed, "
                        + "SUM(CASE WHEN picked_at IS NULL OR prep_completed_offset < 0 "
                        + "THEN 1 ELSE 0 END) AS abnormal_count, "
                        + "COALESCE(AVG(prep_completed_offset) / 60.0, 0) AS avg_ready_offset_minutes, "
                        + "COALESCE(AVG(TIMESTAMPDIFF(SECOND, create_time, picked_at)) / 60.0, 0) "
                        + "AS avg_pickup_gap_minutes "
                        + "FROM dwd_diancan_verification_trace "
                        + "WHERE poi_id = ? AND dt >= ? AND dt <= ? ");
        List<Object> params = new ArrayList<>();
        params.add(poiId);
        params.add(startDate);
        params.add(endDate);
        if (channel != null && !channel.isBlank()) {
            sql.append("AND channel = ? ");
            params.add(channel);
        }
        sql.append("GROUP BY poi_id, dt, channel ORDER BY dt, channel");
        return replayJdbcTemplate.query(
                sql.toString(), params.toArray(), (rs, rowNum) -> mapDailySummary(rs));
    }

    public List<ChannelAbnormalStat> channelStats(LocalDate statDate, String channel) {
        StringBuilder sql = new StringBuilder(
                "SELECT channel, "
                        + "COUNT(*) AS total_verification, "
                        + "SUM(CASE WHEN verification_status IS NULL OR verification_status <> 1 "
                        + "THEN 1 ELSE 0 END) AS abnormal_count, "
                        + "SUM(CASE WHEN merchant_ready_at IS NOT NULL AND picked_at IS NULL "
                        + "THEN 1 ELSE 0 END) AS missing_pickup_count "
                        + "FROM dwd_diancan_verification_trace WHERE dt = ? ");
        List<Object> params = new ArrayList<>();
        params.add(statDate);
        if (channel != null && !channel.isBlank()) {
            sql.append("AND channel = ? ");
            params.add(channel);
        }
        sql.append("GROUP BY channel ORDER BY channel");
        return replayJdbcTemplate.query(
                sql.toString(), params.toArray(), (rs, rowNum) -> mapChannelAbnormalStat(rs, statDate));
    }

    private int normalizeLimit(int limit) {
        return Math.min(Math.max(limit, 1), MAX_TRACE_LIMIT);
    }

    private PoiDailySummary mapDailySummary(ResultSet rs) throws SQLException {
        return PoiDailySummary.builder()
                .poiId(rs.getLong("poi_id"))
                .statDate(rs.getDate("stat_date").toLocalDate())
                .channel(rs.getString("channel"))
                .totalOrders(rs.getInt("total_orders"))
                .verifiedOrders(rs.getInt("verified_orders"))
                .pickupCompleted(rs.getInt("pickup_completed"))
                .abnormalCount(rs.getInt("abnormal_count"))
                .avgReadyOffsetMinutes(rs.getBigDecimal("avg_ready_offset_minutes"))
                .avgPickupGapMinutes(rs.getBigDecimal("avg_pickup_gap_minutes"))
                .build();
    }

    private ChannelAbnormalStat mapChannelAbnormalStat(ResultSet rs, LocalDate statDate) throws SQLException {
        return ChannelAbnormalStat.builder()
                .statDate(statDate)
                .channel(rs.getString("channel"))
                .totalVerification(rs.getLong("total_verification"))
                .abnormalCount(rs.getLong("abnormal_count"))
                .missingPickupCount(rs.getLong("missing_pickup_count"))
                .build();
    }

    private TraceEvent mapTimelineEvent(ResultSet rs) throws SQLException {
        return TraceEvent.builder()
                .eventId(rs.getString("event_id"))
                .eventType(rs.getString("event_type"))
                .eventName(rs.getString("event_name"))
                .eventTime(rs.getTimestamp("event_time").toLocalDateTime())
                .offsetSeconds(rs.getInt("offset_seconds"))
                .detail(rs.getString("detail"))
                .build();
    }
}
