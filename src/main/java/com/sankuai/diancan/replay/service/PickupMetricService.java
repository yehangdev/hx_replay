package com.sankuai.diancan.replay.service;

import com.sankuai.diancan.replay.common.ReplayQueryGuard;
import com.sankuai.diancan.replay.model.PickupOrder;
import com.sankuai.diancan.replay.model.PoiPickupMetrics;
import com.sankuai.diancan.replay.repository.PickupMetricRepository;
import com.sankuai.diancan.replay.repository.VerificationTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PickupMetricService {

    @Autowired
    private PickupMetricRepository pickupMetricRepository;

    @Autowired
    private VerificationTraceRepository verificationTraceRepository;

    public Optional<PoiPickupMetrics> queryPoiMetrics(Long poiId, LocalDate date) {
        LocalDate statDate = date == null ? LocalDate.now().minusDays(1) : date;
        return pickupMetricRepository.findByPoiAndDate(poiId, statDate);
    }

    public List<PickupOrder> queryPickupOrders(
            Long poiId, LocalDate startDate, LocalDate endDate, int limit) {
        LocalDate effectiveEnd = endDate == null ? LocalDate.now() : endDate;
        LocalDate effectiveStart = startDate == null ? effectiveEnd.minusDays(7) : startDate;
        ReplayQueryGuard.checkWindow(effectiveStart, effectiveEnd, "pickup order");
        return verificationTraceRepository.findPickupOrders(poiId, effectiveStart, effectiveEnd, limit);
    }
}
