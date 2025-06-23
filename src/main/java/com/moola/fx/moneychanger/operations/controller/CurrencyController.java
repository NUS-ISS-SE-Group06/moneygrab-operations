package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.service.CurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/currencies") 
public class CurrencyController {

    private final CurrencyService service;

    @Autowired
    public CurrencyController(CurrencyService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<CurrencyCode>> list() {
        List<CurrencyCode> currencies = service.listAll();
        return ResponseEntity.ok(currencies);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CurrencyCode> get(@PathVariable("id") Integer id) {
        CurrencyCode existing = service.get(id);
        return ResponseEntity.ok(existing);
    }

    @GetMapping("/code/{currency}")
    public ResponseEntity<CurrencyCode> getByCurrency(@PathVariable("currency") String currency) {
        CurrencyCode existing = service.getByCurrency(currency);
        return ResponseEntity.ok(existing);
    }
}
