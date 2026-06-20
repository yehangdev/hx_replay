package com.sankuai.diancan.replay.repository;

import com.sankuai.diancan.replay.model.PoiPickupMetrics;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public class PickupMetricRepository {

    @Autowired
    @Qualifier("replayJdbcTemplate")
    private JdbcTemplate replayJdbcTemplate;

    public Optional<PoiPickupMetrics> findByPoiAndDate(Long poiId, LocalDate statDate) {
        List<PoiPickupMetrics> rows = replayJdbcTemplate.query(
                "SELECT poi_id, stat_date, etl_version, total_orders, completed_pickup, abandoned_pickup, "
                        + "avg_ready_offset_minutes, avg_pickup_gap_minutes, no_show_count "
                        + "FROM dws_poi_pickup_metrics WHERE poi_id = ? AND stat_date = ? "
                        + "ORDER BY etl_version DESC LIMIT 1",
                new Object[]{poiId, statDate},
                (rs, rowNum) -> map(rs));
        return rows.stream().findFirst();
    }

    private PoiPickupMetrics map(ResultSet rs) throws SQLException {
        PoiPickupMetrics metrics = new PoiPickupMetrics();
        metrics.setPoiId(rs.getLong("poi_id"));
        metrics.setStatDate(rs.getDate("stat_date").toLocalDate());
        metrics.setEtlVersion(rs.getInt("etl_version"));
        metrics.setTotalOrders(rs.getInt("total_orders"));
        metrics.setCompletedPickup(rs.getInt("completed_pickup"));
        metrics.setAbandonedPickup(rs.getInt("abandoned_pickup"));
        metrics.setAvgReadyOffsetMinutes(rs.getBigDecimal("avg_ready_offset_minutes"));
        metrics.setAvgPickupGapMinutes(rs.getBigDecimal("avg_pickup_gap_minutes"));
        metrics.setNoShowCount(rs.getInt("no_show_count"));
        return metrics;
    }
}
