package com.moola.fx.moneychanger.operations.model;

import lombok.Data;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "commission_rate", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"currency_id", "scheme_id"})
})
public class CommissionRate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "currency_id", nullable = false)
    private CurrencyCode currencyId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scheme_id", nullable = false)
    private Scheme schemeId;

    @Column(nullable = false, precision = 5, scale = 4)
    private BigDecimal rate;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "is_deleted", nullable = false, columnDefinition ="TINYINT(1) DEFAULT 0" )
    private Boolean isDeleted=false;

    // Getters and Setters
}