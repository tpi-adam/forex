package com.tpi.forexapi.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ExceptionDTO {
    private String message;
}
