package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;

@Entity
@Table(name = "`transaction`")   // back-ticks because “transaction” is SQL-reserved
public class Transaction {

    /* ---------- PK & FKs ---------- */

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;                    // Integer, not int

    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "money_changer_id")
    private Integer moneyChangerId;

    @Column(name = "currency_id")          // ← typo fixed (was current_id)
    private Integer currencyId;

    /* ---------- Core Fields ---------- */

    @Column(name = "transaction_date")
    private Timestamp transactionDate;

    @Column(name = "current_status")
    private String currentStatus;

    private String email;
    private String comments;

    /* ---------- Monetary Values ---------- */
    // BigDecimal is safer than double for money
    @Column(name = "exchange_rate", precision = 18, scale = 8)
    private BigDecimal exchangeRate;

    @Column(name = "foreign_amount", precision = 18, scale = 2)
    private BigDecimal foreignAmount;

    @Column(name = "sgd_amount", precision = 18, scale = 2)
    private BigDecimal sgdAmount;

    @Column(name = "received_cash", precision = 18, scale = 2)
    private BigDecimal receivedCash;

    /* ---------- Audit ---------- */

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @Column(name = "created_by")
    private Integer createdBy;

    @Column(name = "updated_by")
    private Integer updatedBy;

    /* ---------- Getters & Setters ---------- */

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public Timestamp getTransactionDate() { return transactionDate; }
    public void setTransactionDate(Timestamp transactionDate) { this.transactionDate = transactionDate; }

    public Integer getCustomerId() { return customerId; }
    public void setCustomerId(Integer customerId) { this.customerId = customerId; }

    public Integer getMoneyChangerId() { return moneyChangerId; }
    public void setMoneyChangerId(Integer moneyChangerId) { this.moneyChangerId = moneyChangerId; }

    public Integer getCurrencyId() { return currencyId; }
    public void setCurrencyId(Integer currencyId) { this.currencyId = currencyId; }

    public String getCurrentStatus() { return currentStatus; }
    public void setCurrentStatus(String currentStatus) { this.currentStatus = currentStatus; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getComments() { return comments; }
    public void setComments(String comments) { this.comments = comments; }

    public BigDecimal getExchangeRate() { return exchangeRate; }
    public void setExchangeRate(BigDecimal exchangeRate) { this.exchangeRate = exchangeRate; }

    public BigDecimal getForeignAmount() { return foreignAmount; }
    public void setForeignAmount(BigDecimal foreignAmount) { this.foreignAmount = foreignAmount; }

    public BigDecimal getSgdAmount() { return sgdAmount; }
    public void setSgdAmount(BigDecimal sgdAmount) { this.sgdAmount = sgdAmount; }

    public BigDecimal getReceivedCash() { return receivedCash; }
    public void setReceivedCash(BigDecimal receivedCash) { this.receivedCash = receivedCash; }

    public Timestamp getCreatedAt() { return createdAt; }
    public void setCreatedAt(Timestamp createdAt) { this.createdAt = createdAt; }

    public Timestamp getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Timestamp updatedAt) { this.updatedAt = updatedAt; }

    public Integer getCreatedBy() { return createdBy; }
    public void setCreatedBy(Integer createdBy) { this.createdBy = createdBy; }

    public Integer getUpdatedBy() { return updatedBy; }
    public void setUpdatedBy(Integer updatedBy) { this.updatedBy = updatedBy; }
}
