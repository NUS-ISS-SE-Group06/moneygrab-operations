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
    private String unit ="1";

    @Column(name = "trade_type", length = 50)
    private String tradeType ="BUY_SELL";  // BUY_SELL, BUY_ONLY, SELL_ONLY

    @Column(name = "trade_deno", length = 50)
    private String tradeDeno="ALL"; // ALL, 50, 100, 1000, 10000, 100000

    @Column(name = "trade_round")
    private Integer tradeRound=0; // 0, 1, 2

    @Column(name = "raw_bid", precision = 18, scale = 8)
    private BigDecimal rawBid=BigDecimal.ZERO;

    @Column(name = "raw_ask", precision = 18, scale = 8)
    private BigDecimal rawAsk=BigDecimal.ZERO;

    @Column(precision = 18, scale = 8)
    private BigDecimal spread=BigDecimal.ZERO;

    @Column(precision = 18, scale = 8)
    private BigDecimal skew=BigDecimal.ZERO;

    @Column(name = "ws_bid", precision = 18, scale = 8)
    private BigDecimal wsBid=BigDecimal.ZERO;

    @Column(name = "ws_ask", precision = 18, scale = 8)
    private BigDecimal wsAsk=BigDecimal.ZERO;

    @Column(name = "ref_bid", precision = 18, scale = 8)
    private Integer refBid=0;

    @Column(name = "dp_bid", precision = 18, scale = 8)
    private Integer dpBid=4;

    @Column(name = "mar_bid", precision = 18, scale = 8)
    private BigDecimal marBid=BigDecimal.ZERO;

    @Column(name = "cf_bid", precision = 18, scale = 8)
    private BigDecimal cfBid=BigDecimal.ZERO;

    @Column(name = "rt_bid", precision = 18, scale = 8)
    private BigDecimal rtBid=BigDecimal.ZERO;

    @Column(name = "ref_ask", precision = 18, scale = 8)
    private Integer refAsk=0;

    @Column(name = "dp_ask", precision = 18, scale = 8)
    private Integer dpAsk=4;

    @Column(name = "mar_ask", precision = 18, scale = 8)
    private BigDecimal marAsk=BigDecimal.ZERO;

    @Column(name = "cf_ask", precision = 18, scale = 8)
    private BigDecimal cfAsk=BigDecimal.ZERO;

    @Column(name = "rt_ask", precision = 18, scale = 8)
    private BigDecimal rtAsk=BigDecimal.ZERO;

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

