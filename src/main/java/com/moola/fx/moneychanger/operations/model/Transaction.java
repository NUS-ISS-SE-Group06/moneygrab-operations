package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "transactions") // optional but recommended
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "transaction_date")
    private Timestamp transactionDate;

    @Column(name = "customer_id")
    private int customerId;

    @Column(name = "money_changer_id")
    private int money_changer_id;

    @Column(name = "current_status")
    private String currentStatus;

    private String email;

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Timestamp getTransactionDate() {
        return transactionDate;
    }

    public void setTransactionDate(Timestamp transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }
     public int getMoneyChangerId() {
        return money_changer_id;
    }
   public void setMoneyChangerId(int money_changer_id) {
        this.money_changer_id = money_changer_id;
    }
    public String getCurrentStatus() {
        return currentStatus;
    }

    public void setCurrentStatus(String currentStatus) {
        this.currentStatus = currentStatus;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
