package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.service.MoneyChangerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/moneychanger_api/moneychangers")
public class MoneyChangerController {
    private final MoneyChangerService service;

    @Autowired
    public MoneyChangerController(MoneyChangerService service) {
        this.service = service;
    }

    @GetMapping
    public List<MoneyChanger> getAll() {
        return service.getAll();
    }

    @GetMapping("/{id}")
    public MoneyChanger getById(@PathVariable Long id) {
        return service.getById(id);
    }

    @PostMapping
    public MoneyChanger create(@RequestBody MoneyChanger moneyChanger) {
        return service.create(moneyChanger);
    }

    @PutMapping("/{id}")
    public MoneyChanger update(@PathVariable Long id, @RequestBody MoneyChanger moneyChanger) {
        return service.update(id, moneyChanger);
    }

    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id, @RequestParam String role) {
        service.delete(id, role);
        return "Money Changer soft-deleted successfully.";
    }
}