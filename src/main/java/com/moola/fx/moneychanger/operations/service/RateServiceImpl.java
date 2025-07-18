package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import com.moola.fx.moneychanger.operations.repository.ViewRatesRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RateServiceImpl implements RateService {

    private final ViewRatesRepository viewRatesRepository;

    public RateServiceImpl(ViewRatesRepository viewRatesRepository) {
        this.viewRatesRepository = viewRatesRepository;
    }

    @Override
    public List<RateDTO> getRatesByMoneyChangerId(Long moneyChangerId) {
        //return viewRatesRepository.findRatesByMoneyChangerId(moneyChangerId);
        List<RateDTO> dtos = viewRatesRepository.findRatesByMoneyChangerId(moneyChangerId);

        for (RateDTO dto : dtos) {
            dto.setBuyRate(dto.getRtBid());   // Fix here: assign rtBid to buyRate
            dto.setSellRate(dto.getRtAsk());  // Fix here: assign rtAsk to sellRate
        }

        return dtos;

    }

    @Override
    public String getStyleForMoneyChanger(Long moneyChangerId) {
        // TODO: Replace with DB lookup in the future
        return "Extended Monitor Style"; // Hardcoded for now
    }
}