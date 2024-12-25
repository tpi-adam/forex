package com.tpi.forexapi.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Document
public class ForexRate {
    @Id
    private String id;
    private LocalDateTime date;
    private BigDecimal usd2ntd;
}
