package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Data;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "compute_rates")
public class ComputeRate {

    @EmbeddedId
    private ComputeRateId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("moneyChangerId") // maps to embeddedId field
    @JoinColumn(name = "money_changer_id", nullable = false, insertable = false, updatable = false)
    private MoneyChanger moneyChanger;

    @Column(length = 50)
    private String unit;

    @Column(name = "trade_type", length = 50)
    private String tradeType;  // BUY_SELL, BUY_ONLY, SELL_ONLY

    @Column(name = "trade_deno", length = 50)
    private String tradeDeno; // ALL, 50, 100, 1000, 10000, 100000

    @Column(name = "trade_round")
    private Integer tradeRound=0; // 0, 1, 2

    @Column(name = "raw_bid", precision = 18, scale = 8)
    private BigDecimal rawBid;

    @Column(name = "raw_ask", precision = 18, scale = 8)
    private BigDecimal rawAsk;

    @Column(precision = 18, scale = 8)
    private BigDecimal spread;

    @Column(precision = 18, scale = 8)
    private BigDecimal skew;

    @Column(name = "ws_bid", precision = 18, scale = 8)
    private BigDecimal wsBid;

    @Column(name = "ws_ask", precision = 18, scale = 8)
    private BigDecimal wsAsk;

    @Column(name = "ref_bid", precision = 18, scale = 8)
    private BigDecimal refBid;

    @Column(name = "dp_bid", precision = 18, scale = 8)
    private BigDecimal dpBid;

    @Column(name = "mar_bid", precision = 18, scale = 8)
    private BigDecimal marBid;

    @Column(name = "cf_bid", precision = 18, scale = 8)
    private BigDecimal cfBid;

    @Column(name = "rt_bid", precision = 18, scale = 8)
    private BigDecimal rtBid;

    @Column(name = "ref_ask", precision = 18, scale = 8)
    private BigDecimal refAsk;

    @Column(name = "dp_ask", precision = 18, scale = 8)
    private BigDecimal dpAsk;

    @Column(name = "mar_ask", precision = 18, scale = 8)
    private BigDecimal marAsk;

    @Column(name = "cf_ask", precision = 18, scale = 8)
    private BigDecimal cfAsk;

    @Column(name = "rt_ask", precision = 18, scale = 8)
    private BigDecimal rtAsk;

    @Column(name = "processed_at", nullable = false)
    private Timestamp processedAt;

    @Column(name = "processed_by")
    private Integer processedBy;

    // Getters and Setters

    @PrePersist
    protected void onCreate() {
        Timestamp now = new Timestamp(System.currentTimeMillis());
        this.processedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.processedAt = new Timestamp(System.currentTimeMillis());
    }
}

