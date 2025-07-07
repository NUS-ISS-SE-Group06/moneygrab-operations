package com.moola.fx.moneychanger.operations.model;

import jakarta.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.Objects;

@Data
@Embeddable
public class ComputeRateId implements Serializable {

    @Column(name = "currency_code", length = 3, nullable = false)
    private String currencyCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "money_changer_id", nullable = false, foreignKey = @ForeignKey(name = "fk_compute_rates_money_changer"))
    private transient MoneyChanger moneyChanger;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComputeRateId)) return false;
        ComputeRateId that = (ComputeRateId) o;
        return Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(getMoneyChangerId(), that.getMoneyChangerId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, getMoneyChangerId());
    }

    private Long getMoneyChangerId() {
        return moneyChanger != null ? moneyChanger.getId() : null;
    }
}
