package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import com.moola.fx.moneychanger.operations.dto.RateDisplayDTO;
import com.moola.fx.moneychanger.operations.service.RateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/view-rates")
public class ViewRatesController {

    private final RateService rateService;

    public ViewRatesController(RateService rateService) {
        this.rateService = rateService;
    }


    @GetMapping

    public RateDisplayDTO getRates() {
        Long moneyChangerId = 1L; //  Temporary hardcoded ID
        String style = "Normal Monitor Style";
        List<RateDTO> rates = rateService.getRatesByMoneyChangerId(moneyChangerId);
        return new RateDisplayDTO(style, rates); // This is correct

    }
}