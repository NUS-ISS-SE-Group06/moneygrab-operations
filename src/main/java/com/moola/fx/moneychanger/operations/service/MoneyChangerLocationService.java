package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerDTO;

import java.util.List;

public interface MoneyChangerLocationService {
    List<MoneyChangerDTO> getLocationsByMoneyChangerId(Long id);
    void add(Long moneyChangerId, List<Integer> locationNames, Long createdBy, Long updatedBy);
    void delete(Long locationId);
    void save(Long moneyChangerId, List<Integer> locationNames);
    List<Integer> getLocationNamesByMoneyChangerId(Long id);
}