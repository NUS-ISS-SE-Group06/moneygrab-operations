package com.moola.fx.moneychanger.operations.model;

import java.io.Serializable;
import java.util.Objects;

public class ViewRatesKey implements Serializable {

    private Integer moneyChangerId;
    private String currencyCode;

    public ViewRatesKey() {}

    public ViewRatesKey(Integer moneyChangerId, String currencyCode) {
        this.moneyChangerId = moneyChangerId;
        this.currencyCode = currencyCode;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ViewRatesKey)) return false;
        ViewRatesKey that = (ViewRatesKey) o;
        return Objects.equals(moneyChangerId, that.moneyChangerId) &&
               Objects.equals(currencyCode, that.currencyCode);
    }

    @Override
    public int hashCode() {
        return Objects.hash(moneyChangerId, currencyCode);
    }
}
