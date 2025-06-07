package com.moneychanger_api.controller;

import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.service.CommissionRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commission-rates")
public class CommissionRateController {
    @Autowired
    private CommissionRateService service;

    @GetMapping
    public List<CommissionRate> list() { return service.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<CommissionRate> get(@PathVariable Integer id) {
        CommissionRate item = service.get(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public CommissionRate create(@RequestBody CommissionRate item) { return service.save(item); }

    @PutMapping("/{id}")
    public CommissionRate update(@PathVariable Integer id, @RequestBody CommissionRate item) {
        item.setId(id);
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) { service.delete(id); }
}