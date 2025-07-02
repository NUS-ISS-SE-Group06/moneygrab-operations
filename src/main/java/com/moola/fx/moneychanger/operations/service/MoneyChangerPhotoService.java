package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import java.util.Optional;

public interface MoneyChangerPhotoService {
    Optional<MoneyChangerPhoto> getByMoneyChangerId(Long id);
    void save(Long moneyChangerId, String base64Image, String filename);
}
