package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import com.moola.fx.moneychanger.operations.dto.RateValuesDTO;
import com.moola.fx.moneychanger.operations.model.ViewRatesEntity;
import com.moola.fx.moneychanger.operations.repository.ViewRatesRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RateServiceImpl implements RateService {

    private final ViewRatesRepository viewRatesRepository;

    @Override
    public List<RateDTO> getRatesByMoneyChangerId(Long moneyChangerId) {
        List<ViewRatesEntity> entities = viewRatesRepository.findByMoneyChangerId(moneyChangerId);
        List<RateDTO> dtos = new ArrayList<>();

        for (ViewRatesEntity entity : entities) {
            RateValuesDTO values = new RateValuesDTO();
            values.setRefBid(entity.getRefBid());
            values.setRefAsk(entity.getRefAsk());
            values.setDpBid(entity.getDpBid());
            values.setDpAsk(entity.getDpAsk());
            values.setMarBid(entity.getMarBid());
            values.setMarAsk(entity.getMarAsk());
            values.setCbBid(entity.getCbBid());
            values.setCbAsk(entity.getCbAsk());
            values.setCfBid(entity.getCfBid());
            values.setCfAsk(entity.getCfAsk());
            values.setRtBid(entity.getRtBid());
            values.setRtAsk(entity.getRtAsk());
            values.setSpread(entity.getSpread());

            values.setBuyRate(defaultIfNull(entity.getRtBid()));
            values.setSellRate(defaultIfNull(entity.getRtAsk()));

            RateDTO dto = new RateDTO();
            dto.setCurrencyCode(entity.getCurrencyCode());
            dto.setUnit(entity.getUnit());
            dto.setRateValues(values);

            dtos.add(dto);
        }

        return dtos;
    }

    @Override
    public String getStyleForMoneyChanger(Long moneyChangerId) {

        return "Normal Monitor Style";
    }

    private Double defaultIfNull(Double value) {
        return value != null ? value : 0.0;
    }
}