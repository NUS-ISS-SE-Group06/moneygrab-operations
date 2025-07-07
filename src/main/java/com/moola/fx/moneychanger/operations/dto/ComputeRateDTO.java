package com.moola.fx.moneychanger.operations.dto;

import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class ComputeRateDTO {

    private String currencyCode;
    private Long moneyChangerId;

    private String unit;

    private BigDecimal rawBid;
    private BigDecimal rawAsk;
    private BigDecimal spread;
    private BigDecimal skew;
    private BigDecimal wsBid;
    private BigDecimal wsAsk;

    private BigDecimal refBid;
    private BigDecimal dpBid;
    private BigDecimal marBid;
    private BigDecimal cfBid;
    private BigDecimal rtBid;

    private BigDecimal refAsk;
    private BigDecimal dpAsk;
    private BigDecimal marAsk;
    private BigDecimal cfAsk;
    private BigDecimal rtAsk;

    private Timestamp processedAt;
    private Integer processedBy;
}

