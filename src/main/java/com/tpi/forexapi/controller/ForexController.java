package com.tpi.forexapi.controller;

import com.tpi.forexapi.dto.ForexSearchRequestDTO;
import com.tpi.forexapi.dto.ForexSearchResponseDTO;
import com.tpi.forexapi.service.ForexService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/forex")
@Slf4j
@RequiredArgsConstructor
public class ForexController {

    private final ForexService forexService;

    @PostMapping
    public ForexSearchResponseDTO search(@RequestBody ForexSearchRequestDTO request) {
        return forexService.search(request.getStartDate(), request.getEndDate(), request.getCurrency());
    }

}
