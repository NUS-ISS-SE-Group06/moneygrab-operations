package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionRateRepository extends JpaRepository<CommissionRate, Integer> {
    boolean existsByCurrencyIdAndSchemeId(CurrencyCode currencyId, Scheme schemeId);

}