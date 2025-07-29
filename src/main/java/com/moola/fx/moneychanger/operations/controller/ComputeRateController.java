package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.ComputeRateDTO;
import com.moola.fx.moneychanger.operations.mapper.ComputeRateMapper;
import com.moola.fx.moneychanger.operations.model.ComputeRate;
import com.moola.fx.moneychanger.operations.service.ComputeRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/compute-rates")
public class ComputeRateController {
    private final ComputeRateService service;
    private final ComputeRateMapper mapper;

    @Autowired
    public ComputeRateController(ComputeRateService service, ComputeRateMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<ComputeRateDTO>> list(@RequestParam(value = "moneyChangerId", required = false) Long moneyChangerId) {
        List<ComputeRate> list = (moneyChangerId != null)
                ? service.findByMoneyChangerId(moneyChangerId)
                : service.listAll();

        List<ComputeRateDTO> result = list.stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{currencyCode}/{moneyChangerId}")
    public ResponseEntity<ComputeRateDTO> get(@PathVariable("currencyCode") String currencyCode,
                                              @PathVariable("moneyChangerId") Long moneyChangerId) {
        ComputeRateDTO dto = mapper.toDTO(service.get(currencyCode, moneyChangerId));
        return ResponseEntity.ok(dto);
    }


    @PostMapping("/batch")
    public ResponseEntity<List<ComputeRateDTO>> batchCreate(@RequestBody List<ComputeRateDTO> dtos) {
        List<ComputeRate> entities = dtos.stream().map(mapper::toEntity).toList();
        List<ComputeRate> saved = service.saveAll(entities);
        List<ComputeRateDTO> result = saved.stream().map(mapper::toDTO).toList();
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{currencyCode}/{moneyChangerId}")
    public ResponseEntity<Void> delete(@PathVariable("currencyCode") String currencyCode,
                                       @PathVariable("moneyChangerId") Long moneyChangerId) {
        service.delete(currencyCode, moneyChangerId);
        return ResponseEntity.noContent().build();
    }
}
