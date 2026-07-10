package com.sankuai.diancan.replay.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.sankuai.diancan.replay.model.VerificationTrace;
import com.sankuai.diancan.replay.repository.VerificationTraceRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.ArgumentMatchers;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VerificationTraceServiceCacheTest {

    @Mock
    private VerificationTraceRepository verificationTraceRepository;

    @Mock
    private ReplayCacheClient replayCacheClient;

    @InjectMocks
    private VerificationTraceService verificationTraceService;

    @Test
    void shouldUseCachedTraceResult() {
        VerificationTrace trace = new VerificationTrace();
        trace.setVerificationCode("ABC123456789");
        when(replayCacheClient.get(anyString(),
                ArgumentMatchers.<TypeReference<List<VerificationTrace>>>any()))
                .thenReturn(Optional.of(List.of(trace)));

        List<VerificationTrace> result = verificationTraceService.queryTrace(
                "ABC123456789", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 7));

        assertEquals(List.of(trace), result);
        verifyNoInteractions(verificationTraceRepository);
    }

    @Test
    void shouldCacheRepositoryResultOnMiss() {
        VerificationTrace trace = new VerificationTrace();
        trace.setVerificationCode("ABC123456789");
        when(replayCacheClient.get(anyString(),
                ArgumentMatchers.<TypeReference<List<VerificationTrace>>>any()))
                .thenReturn(Optional.empty());
        when(verificationTraceRepository.findByVerificationCode(
                eq("ABC123456789"),
                eq(LocalDate.of(2026, 9, 1)),
                eq(LocalDate.of(2026, 9, 7)),
                eq(50)))
                .thenReturn(List.of(trace));

        List<VerificationTrace> result = verificationTraceService.queryTrace(
                "ABC123456789", LocalDate.of(2026, 9, 1), LocalDate.of(2026, 9, 7));

        assertEquals(List.of(trace), result);
        verify(replayCacheClient).put(anyString(), eq(result), eq(Duration.ofMinutes(10)));
    }
}
