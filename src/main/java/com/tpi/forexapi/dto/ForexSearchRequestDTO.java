package com.tpi.forexapi.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ForexSearchRequestDTO {
    private String startDate;
    private String endDate;
    private String currency;
}
