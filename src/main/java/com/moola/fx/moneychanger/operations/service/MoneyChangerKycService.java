package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerKyc;
import java.util.Optional;

public interface MoneyChangerKycService {
    Optional<MoneyChangerKyc> getByMoneyChangerId(Long id);
    void save(Long moneyChangerId, String base64Document, String filename);
}
