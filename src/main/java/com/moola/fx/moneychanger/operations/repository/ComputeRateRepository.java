package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.ComputeRate;
import com.moola.fx.moneychanger.operations.model.ComputeRateId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ComputeRateRepository extends JpaRepository<ComputeRate, ComputeRateId> {
    Optional<ComputeRate> findByIdCurrencyCodeAndIdMoneyChangerId(String currencyCode, Long moneyChangerId);
    List<ComputeRate> findByIdMoneyChangerId(Long moneyChangerId);
    boolean existsByIdCurrencyCodeAndIdMoneyChangerId(String currencyCode, Long moneyChangerId);
}
