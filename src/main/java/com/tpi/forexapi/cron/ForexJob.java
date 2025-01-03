package com.tpi.forexapi.cron;

import com.tpi.forexapi.model.ForexRate;
import com.tpi.forexapi.service.ForexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 排程作業
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class ForexJob {

    private final ForexService forexService;

    /**
     * 透過API取得Forex資料並存入DB
     */
    @Scheduled(cron = "${forex.cron}")
    public void fetchForexJob() {
        log.info("start fetching forex job. Date: {}", LocalDateTime.now());
        // 取得資料
        List<ForexRate> forexRateList = forexService.fetchForexData();
        if (CollectionUtils.isEmpty(forexRateList)) {
            log.info("No forex rates found");
        }
        // 更新DB
        forexService.updateForexDB(forexRateList);
        log.info("finish fetching forex job. Date: {}", LocalDateTime.now());
    }

}
