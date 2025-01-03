package com.tpi.forexapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 匯率
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document
public class ForexRate {
    /**
     * id
     */
    @Id
    private String id;
    /**
     * 日期
     */
    private LocalDateTime date;
    /**
     * usd to ntd 匯率
     */
    private BigDecimal usd2ntd;
}
