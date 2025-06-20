package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;

import java.util.List;

public interface MoneyChangerLocationService {
    List<MoneyChangerResponseDTO> getLocationsByMoneyChanger(Long moneyChangerId);

    void addLocation(Long moneyChangerId, List<String> locationNames, Long createdBy, Long updatedBy);

    void deleteLocation(Long locationId);
    void saveLocations(Long moneyChangerId, List<String> locationNames);

    List<String> getLocationNamesByMoneyChanger(Long moneyChangerId);
}