package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.service.MoneyChangerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/money-changers")
public class MoneyChangerController {

    private final MoneyChangerService moneyChangerService;

    public MoneyChangerController(MoneyChangerService moneyChangerService) {
        this.moneyChangerService = moneyChangerService;
    }

    @GetMapping
    public ResponseEntity<List<MoneyChangerResponseDTO>> getAll() {
        return ResponseEntity.ok(moneyChangerService.getAllMoneyChangers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoneyChangerResponseDTO> getOne(@PathVariable Long id) {
        return ResponseEntity.ok(moneyChangerService.getMoneyChangerById(id));
    }

    @PostMapping
    public ResponseEntity<MoneyChangerResponseDTO> create(@RequestBody MoneyChangerResponseDTO dto) {
        return ResponseEntity.ok(moneyChangerService.createMoneyChanger(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoneyChangerResponseDTO> update(@PathVariable Long id, @RequestBody MoneyChangerResponseDTO dto) {
        return ResponseEntity.ok(moneyChangerService.updateMoneyChanger(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        moneyChangerService.deleteMoneyChanger(id);
        return ResponseEntity.noContent().build();
    }
}
