package com.tpi.forexapi.service;

import com.tpi.forexapi.dto.ForexSearchResponseDTO;
import com.tpi.forexapi.model.ForexRate;

import java.util.List;

/**
 * Forex API service
 */
public interface ForexService {
    /**
     * 透過外部API取得Forex資料
     *
     * @return
     */
    List<ForexRate> fetchForexData();

    /**
     * 更新Forex DB資料
     *
     * @param forexRates
     */
    void updateForexDB(List<ForexRate> forexRates);

    /**
     * 查詢Forex資料
     *
     * @param startDate
     * @param endDate
     * @param currency
     * @return
     */
    ForexSearchResponseDTO search(String startDate, String endDate, String currency);
}
