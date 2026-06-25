package com.sankuai.diancan.replay.service;

import com.sankuai.diancan.replay.model.ChannelAbnormalStat;
import com.sankuai.diancan.replay.repository.VerificationTraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ChannelStatsService {

    @Autowired
    private VerificationTraceRepository verificationTraceRepository;

    public List<ChannelAbnormalStat> stats(LocalDate date, String channel) {
        return verificationTraceRepository.channelStats(date, channel);
    }
}
