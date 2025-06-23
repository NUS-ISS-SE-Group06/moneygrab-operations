package com.moola.fx.moneychanger.operations.dto;

import lombok.Data;

import java.sql.Timestamp;

@Data
public class MoneyChangerCurrencyDTO {
    private Integer id;
    private Integer moneyChangerId;
    private String companyName;           // From MoneyChanger
    private Integer currencyId;
    private String currency;           // From CurrencyCode
    private String currencyDescription;    // From CurrencyCode
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer createdBy;
    private Integer updatedBy;
    private Boolean isDeleted;
}



