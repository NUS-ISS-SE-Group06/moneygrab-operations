package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import org.springframework.web.multipart.MultipartFile;

public interface MoneyChangerPhotoService {

    MoneyChangerPhoto getByMoneyChangerId(Long moneyChangerId);

    void saveOrUpdate(Long moneyChangerId, MultipartFile photoFile);
}
