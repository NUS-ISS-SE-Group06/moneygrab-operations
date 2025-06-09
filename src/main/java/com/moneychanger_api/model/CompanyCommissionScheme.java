package com.moneychanger_api.model;

import lombok.Data;
import jakarta.persistence.*;
import java.sql.Timestamp;

@Data
@Entity
@Table(name = "company_commission_scheme", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"money_changer_id", "commission_rate_id"})
})
public class CompanyCommissionScheme {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "money_changer_id", nullable = false)
    private MoneyChanger moneyChangerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_rate_id", nullable = false)
    private CommissionRate commissionRateId;

    @Column(name = "created_at", nullable = false, updatable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Timestamp createdAt;

    @Column(name = "updated_at", nullable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private Timestamp updatedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "is_deleted", nullable = false, columnDefinition = "TINYINT(1) DEFAULT 0")
    private Boolean isDeleted=false;

    // Getters and Setters
}