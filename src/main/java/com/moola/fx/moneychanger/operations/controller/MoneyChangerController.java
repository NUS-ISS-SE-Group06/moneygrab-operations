package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerDTO;
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
    public ResponseEntity<List<MoneyChangerDTO>> getAll() {
        return ResponseEntity.ok(moneyChangerService.listAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<MoneyChangerDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(moneyChangerService.get(id));
    }

    @PostMapping
    public ResponseEntity<MoneyChangerDTO> create(@RequestBody MoneyChangerDTO dto) {
        return ResponseEntity.ok(moneyChangerService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MoneyChangerDTO> update(@PathVariable Long id, @RequestBody MoneyChangerDTO dto) {
        return ResponseEntity.ok(moneyChangerService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        moneyChangerService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
