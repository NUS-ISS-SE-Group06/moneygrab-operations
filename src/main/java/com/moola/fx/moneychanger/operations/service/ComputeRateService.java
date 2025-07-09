package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.ComputeRate;

import java.util.List;

public interface ComputeRateService {
    List<ComputeRate> listAll();
    ComputeRate get(String currencyCode, Long moneyChangerId);
    List<ComputeRate> saveAll(List<ComputeRate> entities);
    void delete(String currencyCode, Long moneyChangerId);
    List<ComputeRate> findByMoneyChangerId(Long moneyChangerId);
    void deleteOrphanedRates(Long moneyChangerId);
}
