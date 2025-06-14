package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerKyc;
import org.springframework.web.multipart.MultipartFile;

public interface MoneyChangerKycService {

    MoneyChangerKyc getByMoneyChangerId(Long moneyChangerId);

    void saveOrUpdate(Long moneyChangerId, MultipartFile kycFile);
}

