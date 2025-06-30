package com.moola.fx.moneychanger.operations.dto;

import lombok.Data; 

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
public class CommissionRateDTO {
    private Integer id;
    private Integer currencyId;
    private String currency;
    private Integer schemeId;
    private String nameTag;
    private BigDecimal rate;
    private Timestamp createdAt;
    private Timestamp updatedAt;
    private Integer createdBy;
    private Integer updatedBy;
    private Boolean isDeleted; 
}
/*
Testing CICD


*/