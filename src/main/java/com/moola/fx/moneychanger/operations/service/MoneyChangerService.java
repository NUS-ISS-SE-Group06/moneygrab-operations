package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChanger;

import java.util.List;

public interface MoneyChangerService {
    List<MoneyChanger> getAll();
    MoneyChanger getById(Long id);
    MoneyChanger create(MoneyChanger moneyChanger);
    MoneyChanger update(Long id, MoneyChanger moneyChanger);

    void delete(Long id);
    MoneyChanger getOne(Long id);
}