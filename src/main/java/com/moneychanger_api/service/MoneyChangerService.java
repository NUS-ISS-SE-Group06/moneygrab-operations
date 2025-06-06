package com.moneychanger_api.service;

import com.moneychanger_api.model.MoneyChanger;

import java.util.List;

public interface MoneyChangerService {
    List<MoneyChanger> getAll();
    MoneyChanger getById(Long id);
    MoneyChanger create(MoneyChanger moneyChanger);
    MoneyChanger update(Long id, MoneyChanger moneyChanger);
    void delete(Long id, String role);
}