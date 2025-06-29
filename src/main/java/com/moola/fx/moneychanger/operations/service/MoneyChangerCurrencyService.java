package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerCurrency;

import java.util.List;

public interface MoneyChangerCurrencyService {
    List<MoneyChangerCurrency> listAll();
    MoneyChangerCurrency get(Integer id);
    MoneyChangerCurrency save(MoneyChangerCurrency entity);
    void delete(Integer id, Integer userId);
    List<MoneyChangerCurrency> findByMoneyChangerId(Integer id);
}
