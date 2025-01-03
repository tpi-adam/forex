package com.tpi.forexapi.dto;

import com.tpi.forexapi.annotation.DateStringRequest;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * 查詢請求DTO
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ForexSearchRequestDTO {
    /**
     * 起始日期(日期區間僅限 1 年前~當下日期-1 天)
     */
    @NotBlank
    @DateStringRequest(checkRange = true, rangeStart = -365, rangeEnd = -1)
    private String startDate;
    /**
     * 結束日期(日期區間僅限 1 年前~當下日期-1 天)
     */
    @NotBlank
    @DateStringRequest(checkRange = true, rangeStart = -365, rangeEnd = -1)
    private String endDate;
    /**
     * 幣別
     */
    @NotBlank
    private String currency;
}
