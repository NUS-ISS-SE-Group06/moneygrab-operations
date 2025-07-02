package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerDTO;

import java.util.List;

public interface MoneyChangerService {
    List<MoneyChangerDTO> listAll();
    MoneyChangerDTO get(Long id);
    MoneyChangerDTO create(MoneyChangerDTO dto);
    MoneyChangerDTO update(Long id, MoneyChangerDTO dto);
    void delete(Long id);
}
