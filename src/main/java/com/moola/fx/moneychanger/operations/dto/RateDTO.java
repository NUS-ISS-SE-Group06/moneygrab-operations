package com.moola.fx.moneychanger.operations.dto;

import lombok.Data;

@Data
public class RateDTO {
    private String currencyCode;
    private String unit;
    private RateValuesDTO rateValues;
}