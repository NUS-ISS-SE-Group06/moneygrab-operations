package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerCurrencyDTO;
import com.moola.fx.moneychanger.operations.mapper.MoneyChangerCurrencyMapper;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.MoneyChangerCurrency;
import com.moola.fx.moneychanger.operations.service.MoneyChangerCurrencyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/money-changers-currencies")
public class MoneyChangerCurrencyController {
    private final MoneyChangerCurrencyService service;
    private final MoneyChangerCurrencyMapper mapper;

    @Autowired
    public MoneyChangerCurrencyController(MoneyChangerCurrencyService service, MoneyChangerCurrencyMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<MoneyChangerCurrencyDTO>> list(@RequestParam(value ="moneyChangerId", required = false ) Integer moneyChangerId) {
        List<MoneyChangerCurrency> list = (moneyChangerId != null)
                ? service.findByMoneyChangerId(moneyChangerId)
                : service.listAll();

        List<MoneyChangerCurrencyDTO> result= list.stream().map(mapper::toDTO).toList();

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoneyChangerCurrencyDTO> get(@PathVariable("id") Integer id) {
        MoneyChangerCurrencyDTO dto = mapper.toDTO(service.get(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<MoneyChangerCurrencyDTO> create(@RequestBody MoneyChangerCurrencyDTO dto) {
        MoneyChangerCurrency entity = mapper.toEntity(dto);

        entity.setUpdatedBy(dto.getCreatedBy());

        MoneyChangerCurrency saved = service.save(entity);
        return ResponseEntity.ok(mapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoneyChangerCurrencyDTO> update(@PathVariable("id") Integer id, @RequestBody MoneyChangerCurrencyDTO dto) {
        MoneyChangerCurrency existing = service.get(id);

        if (existing == null) {
            return ResponseEntity.notFound().build();
        }

        if (dto.getCurrencyId() != null) {
            CurrencyCode currency= new CurrencyCode();
            currency.setId(dto.getCurrencyId());
            currency.setCurrency(dto.getCurrency());
            currency.setDescription(dto.getCurrencyDescription());
            existing.setCurrencyId(currency);
        }

        existing.setUpdatedBy(dto.getUpdatedBy());

        MoneyChangerCurrency updated = service.save(existing);

        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id, @RequestParam("userId") Integer userId) {
        service.delete(id,userId);
        return ResponseEntity.noContent().build();
    }


}
