package com.tpi.forexapi.service.impl;

import com.tpi.forexapi.constant.ErrorCodes;
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
                             .bodyToMono(String.class)
                             .flatMapMany(response -> Flux.fromIterable(
                                     Arrays.asList(JsonUtils.toObject(response, Map[].class))))
                             .onErrorResume(error -> {
                                 log.error("Forex API error: {}", error.getMessage());
                                 return Flux.empty();
                             })
                             .map(this::processForexData)
                             .filter(forexRate -> forexRate.getDate() != null && forexRate.getUsd2ntd() != null)
                             .collectList().block();
    }

    @Override
    public void updateForexDB(List<ForexRate> forexRates) {
        for (ForexRate forexRate : forexRates) {
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
        LocalDate localStartDate = DateTimeUtils.stringToLocalDate(startDate, DateTimeUtils.DATE_FORMAT_YYYYMMDD_SLASH);
        LocalDate localEndDate = DateTimeUtils.stringToLocalDate(endDate, DateTimeUtils.DATE_FORMAT_YYYYMMDD_SLASH);

        // 日期區間僅限 1 年前~當下日期-1 天，若日期區間不符規則， response 需回 error code E001
        LocalDate today = LocalDate.now();
        LocalDate lastYear = today.minusYears(1);
        LocalDate yesterday = today.minusDays(1);
        if (localStartDate.isBefore(lastYear) || localStartDate.isAfter(yesterday) || localEndDate.isBefore(lastYear) ||
            localEndDate.isAfter(yesterday)) {
            return ForexSearchResponseDTO.builder()
                                         .error(ForexSearchResponseDTO.Error.builder()
                                                                            .code(ErrorCodes.INVALID_DATE_FORMAT_CODE)
                                                                            .message(ErrorCodes.INVALID_DATE_FORMAT_MSG)
                                                                            .build())
                                         .build();
        }

        List<ForexRate> forexRateList =
                forexRateRepository.findByDateBetween(localStartDate.atStartOfDay(), localEndDate.atStartOfDay());

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
