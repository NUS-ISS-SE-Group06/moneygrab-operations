package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@IdClass(ViewRatesKey.class)
@Table(name = "compute_rates")
public class ViewRatesEntity {

    @Id
    @Column(name = "money_changer_id", nullable = false)
    private Integer moneyChangerId;

    @Id
    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "buy_rate")
    private Double buyRate;

    @Column(name = "sell_rate")
    private Double sellRate;

    @Column(name = "spread")
    private Double spread;

    @Column(name = "ref_bid")
    private Double refBid;

    @Column(name = "ref_ask")
    private Double refAsk;

    @Column(name = "dp_bid")
    private Double dpBid;

    @Column(name = "dp_ask")
    private Double dpAsk;

    @Column(name = "mar_bid")
    private Double marBid;

    @Column(name = "mar_ask")
    private Double marAsk;

    @Column(name = "cb_bid")
    private Double cbBid;

    @Column(name = "cb_ask")
    private Double cbAsk;

    @Column(name = "cf_bid")
    private Double cfBid;

    @Column(name = "cf_ask")
    private Double cfAsk;

    @Column(name = "rt_bid")
    private Double rtBid;

    @Column(name = "rt_ask")
    private Double rtAsk;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
