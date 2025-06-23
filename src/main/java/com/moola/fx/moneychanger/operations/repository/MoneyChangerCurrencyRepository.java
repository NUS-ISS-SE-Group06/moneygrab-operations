package com.moola.fx.moneychanger.operations.repository;

import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerCurrency;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MoneyChangerCurrencyRepository extends JpaRepository<MoneyChangerCurrency, Integer> {
    List<MoneyChangerCurrency> findByMoneyChangerId_idAndIsDeletedFalse(Long id);
    boolean existsByMoneyChangerIdAndCurrencyIdAndIsDeletedFalse(MoneyChanger moneyChargerId, CurrencyCode currencyId);
    boolean existsByMoneyChangerIdAndCurrencyIdAndIdNotAndIsDeletedFalse(MoneyChanger moneyChangerId, CurrencyCode currencyId, Integer id);
    Optional<MoneyChangerCurrency> findByIdAndIsDeletedFalse(Integer id);
    List<MoneyChangerCurrency> findByIsDeletedFalse();
}
