package com.tpi.forexapi.service.impl;

import com.tpi.forexapi.constant.ForexConstants;
import com.tpi.forexapi.constant.OkCodes;
import com.tpi.forexapi.dao.ForexRateRepository;
import com.tpi.forexapi.dto.ForexSearchResponseDTO;
import com.tpi.forexapi.model.ForexRate;
import com.tpi.forexapi.service.ForexService;
import com.tpi.forexapi.util.DateTimeUtils;
import com.tpi.forexapi.util.JsonUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class ForexServiceImpl implements ForexService {

    private final WebClient forexWebClient;
    private final ForexRateRepository forexRateRepository;

    @Override
    public List<ForexRate> fetchForexData() {
        return forexWebClient.get()
                             .retrieve()
                             // 該API回傳json string
                             .bodyToMono(String.class)
                             .flatMapMany(response -> Flux.fromIterable(
                                     Arrays.asList(JsonUtils.toObject(response, Map[].class))))
                             .onErrorResume(error -> {
                                 // 若連線有誤則以log紀錄並回傳空的紀錄
                                 log.error("Forex API error: {}", error.getMessage());
                                 return Flux.empty();
                             })
                             // 處理資料
                             .map(this::processForexData)
                             // 只處理欄位完整的資料
                             .filter(forexRate -> forexRate.getDate() != null && forexRate.getUsd2ntd() != null)
                             .collectList().block();
    }

    @Override
    public void updateForexDB(List<ForexRate> forexRates) {
        for (ForexRate forexRate : forexRates) {
            // 先以日期查詢是否已有存在的紀錄，有的話更新舊資料，沒有則新增
            ForexRate existedRate = forexRateRepository.findByDate(forexRate.getDate());
            ForexRate savedRate = null;
            if (existedRate != null) {
                existedRate.setUsd2ntd(forexRate.getUsd2ntd());
                savedRate = existedRate;
            } else {
                savedRate = forexRate;
            }
            forexRateRepository.save(savedRate);
        }
    }

    /**
     * 針對取得的外部資料做處理、格式檢查，並轉換成符合DB的model
     *
     * @param data
     * @return
     */
    private ForexRate processForexData(Map data) {
        String usd2ntdData = Objects.toString(data.get(ForexConstants.FOREX_RATE_USD_TO_NTD));
        BigDecimal usd2ntd = null;
        if (usd2ntdData != null) {
            try {
                usd2ntd = new BigDecimal(usd2ntdData);
            } catch (NumberFormatException e) {
                log.warn("Data currency corrupted. data={}", data);
            }
        }

        LocalDateTime localDateTime = null;
        try {
            localDateTime = DateTimeUtils.stringToLocalDate(
                                                 Objects.toString(data.get(ForexConstants.FOREX_RATE_DATE)), DateTimeUtils.DATE_FORMAT_YYYYMMDD)
                                         .atStartOfDay();
        } catch (Exception e) {
            log.warn("Data date corrupted. data={}", data);
        }

        log.info("processed forex data={}", data);
        return ForexRate.builder()
                        .usd2ntd(usd2ntd)
                        .date(localDateTime)
                        .build();
    }

    @Override
    public ForexSearchResponseDTO search(String startDate, String endDate, String currency) {
        // 起訖日期
        LocalDate localStartDate = DateTimeUtils.stringToLocalDate(startDate, DateTimeUtils.DATE_FORMAT_YYYYMMDD_SLASH);
        LocalDate localEndDate = DateTimeUtils.stringToLocalDate(endDate, DateTimeUtils.DATE_FORMAT_YYYYMMDD_SLASH);
        // 查詢
        List<ForexRate> forexRateList =
                forexRateRepository.findByDateBetween(localStartDate.atStartOfDay(), localEndDate.atStartOfDay());
        // 包裝回應
        ForexSearchResponseDTO responseDTO = ForexSearchResponseDTO.builder()
                                                                   .error(ForexSearchResponseDTO.Error.builder()
                                                                                                      .code(OkCodes.OK_CODE)
                                                                                                      .message(
                                                                                                              OkCodes.OK_MSG)
                                                                                                      .build())
                                                                   .build();
        if (forexRateList == null || forexRateList.size() == 0) {
            responseDTO.setCurrency(Collections.emptyList());
        } else {
            List<ForexSearchResponseDTO.Currency> currencyList = new ArrayList<>();
            for (ForexRate forexRate : forexRateList) {
                currencyList.add(ForexSearchResponseDTO.Currency.builder()
                                                                .date(forexRate.getDate()
                                                                               .format(DateTimeFormatter.ofPattern(
                                                                                       DateTimeUtils.DATE_FORMAT_YYYYMMDD)))
                                                                .usd(forexRate.getUsd2ntd().toString())
                                                                .build());
            }
            responseDTO.setCurrency(currencyList);
        }
        return responseDTO;
    }

}
