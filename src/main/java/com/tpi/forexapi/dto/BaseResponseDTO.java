package com.tpi.forexapi.dto;

import lombok.*;
import lombok.experimental.SuperBuilder;

/**
 * 回應DTO的基礎類
 */
@Data
@ToString
@SuperBuilder
public class BaseResponseDTO {
    private Error error;

    @Data
    @Builder
    @AllArgsConstructor
    @NoArgsConstructor
    @ToString
    public static class Error {
        public String code;
        public String message;
    }
}
