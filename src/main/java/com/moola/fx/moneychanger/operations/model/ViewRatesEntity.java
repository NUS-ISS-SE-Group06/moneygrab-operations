package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "compute_rates")
public class ViewRatesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "money_changer_id", nullable = false)
    private Integer moneyChangerId;

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @Column(name = "unit", nullable = false)
    private String unit;

    @Column(name = "buy_rate")
    private double buyRate;

    @Column(name = "sell_rate")
    private double sellRate;

    @Column(name = "spread")
    private double spread;

    @Column(name = "ref_bid")
    private double refBid;

    @Column(name = "ref_ask")
    private double refAsk;

    @Column(name = "dp_bid")
    private double dpBid;

    @Column(name = "dp_ask")
    private double dpAsk;

    @Column(name = "mar_bid")
    private double marBid;

    @Column(name = "mar_ask")
    private double marAsk;

    @Column(name = "cb_bid")
    private double cbBid;

    @Column(name = "cb_ask")
    private double cbAsk;

    @Column(name = "cf_bid")
    private double cfBid;

    @Column(name = "cf_ask")
    private double cfAsk;

    @Column(name = "rt_bid")
    private double rtBid;

    @Column(name = "rt_ask")
    private double rtAsk;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}
