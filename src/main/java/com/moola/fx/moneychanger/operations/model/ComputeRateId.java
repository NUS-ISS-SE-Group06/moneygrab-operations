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

    @Column(name = "money_changer_id", nullable = false)
    private Long moneyChangerId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ComputeRateId)) return false;
        ComputeRateId that = (ComputeRateId) o;
        return Objects.equals(currencyCode, that.currencyCode) &&
                Objects.equals(moneyChangerId, that.moneyChangerId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(currencyCode, moneyChangerId);
    }
}
