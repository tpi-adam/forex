package com.tpi.forexapi.controller;

import com.tpi.forexapi.dto.ForexSearchRequestDTO;
import com.tpi.forexapi.dto.ForexSearchResponseDTO;
import com.tpi.forexapi.service.ForexService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Forex API controller
 */
@RestController
@RequestMapping("/forex")
@Slf4j
@RequiredArgsConstructor
@Validated
public class ForexController {

    private final ForexService forexService;

    /**
     * 查詢存在DB的Forex資料
     *
     * @param request
     * @return
     */
    @PostMapping
    public ForexSearchResponseDTO search(@Valid @RequestBody ForexSearchRequestDTO request) {
        return forexService.search(request.getStartDate(), request.getEndDate(), request.getCurrency());
    }

}
