package com.tpi.forexapi.service;

import com.tpi.forexapi.dto.ForexSearchResponseDTO;
import com.tpi.forexapi.model.ForexRate;

import java.util.List;

public interface ForexService {
    List<ForexRate> fetchForexData();

    void updateForexDB(List<ForexRate> forexRates);

    ForexSearchResponseDTO search(String startDate, String endDate, String currency);
}
