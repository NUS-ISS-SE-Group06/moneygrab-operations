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

    //@GetMapping
    //public List<RateDTO> getRates(@RequestParam("moneyChangerId") Integer moneyChangerId) {
    //    return rateService.getRatesByMoneyChangerId(moneyChangerId);
    //}
    @GetMapping
    //public List<RateDTO> getRates() {
    public RateDisplayDTO getRates() {
        Long moneyChangerId = 1L; //  Temporary hardcoded ID
        //String style = rateService.getStyleForMoneyChanger(moneyChangerId); // placeholder
        //return rateService.getRatesByMoneyChangerId(moneyChangerId);

        // ► in the real version you’ll fetch style from DB
        String style = "Normal Monitor Style";
        List<RateDTO> rates = rateService.getRatesByMoneyChangerId(moneyChangerId);
        return new RateDisplayDTO(style, rates); // This is correct

    }
}