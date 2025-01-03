package com.tpi.forexapi.dto;

import lombok.*;

/**
 * 例外DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExceptionDTO {
    private String message;
}
