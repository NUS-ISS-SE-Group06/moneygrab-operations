package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import java.util.List;

public interface RateService {
    List<RateDTO> getRatesByMoneyChangerId(Long moneyChangerId);

    String getStyleForMoneyChanger(Long moneyChangerId);
}
