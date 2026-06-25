package com.sankuai.diancan.replay.service;

import com.sankuai.diancan.replay.common.ReplayQueryGuard;
import com.sankuai.diancan.replay.model.PoiDailySummary;
import com.sankuai.diancan.replay.repository.VerificationTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ReplaySummaryService {

    @Autowired
    private VerificationTraceRepository verificationTraceRepository;

    public List<PoiDailySummary> dailySummaryList(
            Long poiId, LocalDate startDate, LocalDate endDate, String channel) {
        ReplayQueryGuard.checkWindow(startDate, endDate, "summary");
        return verificationTraceRepository.findPoiDailySummaries(poiId, startDate, endDate, channel);
    }
}
