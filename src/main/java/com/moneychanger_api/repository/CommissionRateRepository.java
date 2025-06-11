package com.moneychanger_api.repository;

import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.model.CurrencyCode;
import com.moneychanger_api.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommissionRateRepository extends JpaRepository<CommissionRate, Integer> {
    boolean existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(CurrencyCode currencyId, Scheme schemeId);
    boolean existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(CurrencyCode currencyId, Scheme schemeId, Integer id);
    List<CommissionRate> findBySchemeIdIdAndIsDeletedFalse(Integer schemeId);
    Optional<CommissionRate> findByIdAndIsDeletedFalse(Integer id);

}