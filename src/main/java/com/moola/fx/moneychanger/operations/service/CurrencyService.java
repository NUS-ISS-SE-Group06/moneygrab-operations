package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.CurrencyCode;

import java.util.List;

public interface CurrencyService {
    List<CurrencyCode> listAll();
    CurrencyCode get(Integer id);
    CurrencyCode getByCurrency(String currency);
}
