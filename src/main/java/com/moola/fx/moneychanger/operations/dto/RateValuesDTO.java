package com.moola.fx.moneychanger.operations.dto;

import lombok.Data;

@Data
public class RateValuesDTO {
    private Double buyRate;
    private Double sellRate;
    private Double spread;
    private Double refBid;
    private Double refAsk;
    private Double dpBid;
    private Double dpAsk;
    private Double marBid;
    private Double marAsk;
    private Double cbBid;
    private Double cbAsk;
    private Double cfBid;
    private Double cfAsk;
    private Double rtBid;
    private Double rtAsk;
}
