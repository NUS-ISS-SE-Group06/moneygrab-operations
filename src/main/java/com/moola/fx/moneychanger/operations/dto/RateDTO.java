package com.moola.fx.moneychanger.operations.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RateDTO {

    private String currencyCode;
    private String unit;
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

    // Required for JPQL SELECT NEW query
    public RateDTO(String currencyCode, String unit,
                   Double buyRate, Double sellRate, Double spread,
                   Double refBid, Double refAsk,
                   Double dpBid, Double dpAsk,
                   Double marBid, Double marAsk,
                   Double cbBid, Double cbAsk,
                   Double cfBid, Double cfAsk,
                   Double rtBid, Double rtAsk) {
        this.currencyCode = currencyCode;
        this.unit = unit;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
        this.spread = spread;
        this.refBid = refBid;
        this.refAsk = refAsk;
        this.dpBid = dpBid;
        this.dpAsk = dpAsk;
        this.marBid = marBid;
        this.marAsk = marAsk;
        this.cbBid = cbBid;
        this.cbAsk = cbAsk;
        this.cfBid = cfBid;
        this.cfAsk = cfAsk;
        this.rtBid = rtBid;
        this.rtAsk = rtAsk;
    }

    // SonarQube-compliant static factory method
    public static RateDTO of(String currencyCode, String unit,
                             Double buyRate, Double sellRate, Double spread,
                             Double refBid, Double refAsk,
                             Double dpBid, Double dpAsk,
                             Double marBid, Double marAsk,
                             Double cbBid, Double cbAsk,
                             Double cfBid, Double cfAsk,
                             Double rtBid, Double rtAsk) {
        return new RateDTO(currencyCode, unit,
                buyRate, sellRate, spread,
                refBid, refAsk,
                dpBid, dpAsk,
                marBid, marAsk,
                cbBid, cbAsk,
                cfBid, cfAsk,
                rtBid, rtAsk);
    }
}
