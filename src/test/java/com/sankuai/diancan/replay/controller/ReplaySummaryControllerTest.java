package com.sankuai.diancan.replay.controller;

import com.sankuai.diancan.replay.common.ReplayExceptionHandler;
import com.sankuai.diancan.replay.service.ReplaySummaryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReplaySummaryController.class)
@Import(ReplayExceptionHandler.class)
class ReplaySummaryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReplaySummaryService replaySummaryService;

    @Test
    void shouldReturnUnifiedResponseForMissingParameter() throws Exception {
        mockMvc.perform(get("/api/replay/summary/range")
                        .param("poiId", "110100"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.data").doesNotExist());
    }

    @Test
    void shouldReturnUnifiedResponseForInvalidLocalDate() throws Exception {
        mockMvc.perform(get("/api/replay/summary/range")
                        .param("poiId", "110100")
                        .param("startDate", "not-a-date")
                        .param("endDate", "2026-09-01"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.code").value(400))
                .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("invalid request parameter")));
    }

    @Test
    void shouldReturnUnifiedResponseForUnknownPath() throws Exception {
        mockMvc.perform(get("/api/replay/unknown"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.code").value(404));
    }

    @Test
    void shouldReturnUnifiedResponseForUnsupportedMethod() throws Exception {
        mockMvc.perform(post("/api/replay/summary/range")
                        .param("poiId", "110100")
                        .param("startDate", "2026-08-01")
                        .param("endDate", "2026-08-31"))
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.code").value(405));
    }

    @Test
    void shouldReturnUnifiedResponseForUnexpectedError() throws Exception {
        when(replaySummaryService.dailySummaryList(
                any(Long.class), any(LocalDate.class), any(LocalDate.class), any()))
                .thenThrow(new IllegalStateException("boom"));

        mockMvc.perform(get("/api/replay/summary/range")
                        .param("poiId", "110100")
                        .param("startDate", "2026-08-01")
                        .param("endDate", "2026-08-31"))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.code").value(500))
                .andExpect(jsonPath("$.message").value("internal server error"));
    }
}
