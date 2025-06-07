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
    @JoinColumn(name = "money_changer_id", referencedColumnName = "id")
    private MoneyChanger moneyChangerId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "commission_rate_id", referencedColumnName = "id")
    private CommissionRate commissionRateId;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    @Column(name = "is_deleted")
    private Boolean isDeleted;

    // Getters and Setters
}