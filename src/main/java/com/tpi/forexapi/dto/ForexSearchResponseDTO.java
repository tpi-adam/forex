package com.tpi.forexapi.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.List;

/**
 * 查詢回應DTO
 */
@Data
@SuperBuilder
@ToString
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ForexSearchResponseDTO extends BaseResponseDTO {
    /**
     * 匯率清單
     */
    private List<Currency> currency;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Currency {
        /**
         * 日期
         */
        private String date;
        /**
         * usd匯率
         */
        private String usd;
    }
}
