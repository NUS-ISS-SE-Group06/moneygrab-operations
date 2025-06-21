package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import java.util.Optional;

public interface MoneyChangerPhotoService {
    Optional<MoneyChangerPhoto> getByMoneyChangerId(Long moneyChangerId);
    void saveOrUpdate(Long moneyChangerId, String base64Image, String filename);
}
