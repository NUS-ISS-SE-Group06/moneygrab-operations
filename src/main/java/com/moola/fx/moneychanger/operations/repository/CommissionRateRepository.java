package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.Scheme;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommissionRateRepository extends JpaRepository<CommissionRate, Integer> {
    boolean existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(CurrencyCode currencyId, Scheme schemeId);
    boolean existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(CurrencyCode currencyId, Scheme schemeId, Integer id);
    List<CommissionRate> findBySchemeIdIdAndIsDeletedFalse(Integer schemeId);
    Optional<CommissionRate> findByIdAndIsDeletedFalse(Integer id);

}