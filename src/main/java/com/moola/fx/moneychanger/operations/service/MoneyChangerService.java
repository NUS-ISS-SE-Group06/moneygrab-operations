package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;

import java.util.List;

public interface MoneyChangerService {
    List<MoneyChangerResponseDTO> getAllMoneyChangers();
    MoneyChangerResponseDTO getMoneyChangerById(Long id);
    MoneyChangerResponseDTO createMoneyChanger(MoneyChangerResponseDTO dto);
    MoneyChangerResponseDTO updateMoneyChanger(MoneyChangerResponseDTO dto);
    void deleteMoneyChanger(Long id);
}
