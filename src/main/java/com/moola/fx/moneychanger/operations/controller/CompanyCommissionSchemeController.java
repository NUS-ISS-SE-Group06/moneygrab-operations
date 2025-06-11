package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.service.CompanyCommissionSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/company-commission-schemes")
public class CompanyCommissionSchemeController {
    private final CompanyCommissionSchemeService service;

    @Autowired
    public CompanyCommissionSchemeController(CompanyCommissionSchemeService service) {
        this.service = service;
    }

    @GetMapping
    public List<CompanyCommissionScheme> list() { return service.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyCommissionScheme> get(@PathVariable Integer id) {
        CompanyCommissionScheme item = service.get(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public CompanyCommissionScheme create(@RequestBody CompanyCommissionScheme item) {
        return service.save(item);
    }

    @PutMapping("/{id}")
    public CompanyCommissionScheme update(@PathVariable Integer id, @RequestBody CompanyCommissionScheme item) {
        item.setId(id);
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) { service.delete(id); }
}