package com.moneychanger_api.repository;

import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.model.CurrencyCode;
import com.moneychanger_api.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommissionRateRepository extends JpaRepository<CommissionRate, Integer> {
    boolean existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(CurrencyCode currencyId, Scheme schemeId);

}