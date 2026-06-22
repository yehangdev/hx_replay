package com.sankuai.diancan.replay.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sankuai.diancan.replay.common.ReplayQueryGuard;
import com.sankuai.diancan.replay.model.VerificationTrace;
import com.sankuai.diancan.replay.repository.VerificationTraceRepository;
import com.sankuai.diancan.replay.util.VerificationIdMaskUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class VerificationTraceService {

    private static final String TRACE_CACHE_PREFIX = "diancan:verification:trace:";
    private static final Duration TRACE_CACHE_TTL = Duration.ofMinutes(10);
    private static final TypeReference<List<VerificationTrace>> TRACE_LIST_TYPE =
            new TypeReference<List<VerificationTrace>>() {
            };

    @Autowired
    private VerificationTraceRepository verificationTraceRepository;

    @Autowired
    private ReplayCacheClient replayCacheClient;

    public List<VerificationTrace> queryTrace(
            String verificationCode, LocalDate startDate, LocalDate endDate) {
        if (verificationCode == null || verificationCode.isBlank()) {
            return Collections.emptyList();
        }
        log.info("query verification trace, verificationCode={}",
                VerificationIdMaskUtil.maskVerificationCode(verificationCode));
        LocalDate effectiveEnd = endDate == null ? LocalDate.now() : endDate;
        LocalDate effectiveStart = startDate == null ? effectiveEnd.minusDays(30) : startDate;
        ReplayQueryGuard.checkWindow(effectiveStart, effectiveEnd, "trace");
        String cacheKey = TRACE_CACHE_PREFIX + verificationCode + ":" + effectiveStart + ":" + effectiveEnd;
        Optional<List<VerificationTrace>> cached = replayCacheClient.get(cacheKey, TRACE_LIST_TYPE);
        if (cached.isPresent()) {
            return cached.get();
        }
        List<VerificationTrace> traces = verificationTraceRepository.findByVerificationCode(
                verificationCode, effectiveStart, effectiveEnd, 50);
        replayCacheClient.put(cacheKey, traces, TRACE_CACHE_TTL);
        return traces;
    }

    public List<VerificationTrace> queryByRange(
            Long poiId, LocalDate startDate, LocalDate endDate, String channel, int limit) {
        LocalDate effectiveEnd = endDate == null ? LocalDate.now() : endDate;
        LocalDate effectiveStart = startDate == null ? effectiveEnd.minusDays(7) : startDate;
        ReplayQueryGuard.checkWindow(effectiveStart, effectiveEnd, "query");
        return verificationTraceRepository.findByQuery(poiId, effectiveStart, effectiveEnd, channel, limit);
    }
}
