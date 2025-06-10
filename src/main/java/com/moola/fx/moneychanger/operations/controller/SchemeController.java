package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/schemes")
public class SchemeController {
    private final SchemeService service;

    @Autowired
    public SchemeController(SchemeService service) {
        this.service = service;
    }

    @GetMapping
    public List<Scheme> list() { return service.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Scheme> get(@PathVariable Integer id) {
        Scheme item = service.get(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public Scheme create(@RequestBody Scheme item) {
        item.setCreatedBy(1);
        item.setUpdatedBy(1);
        return service.save(item);
    }

    @PutMapping("/{id}")
    public Scheme update(@PathVariable Integer id, @RequestBody Scheme item) {
        item.setId(id);
        item.setUpdatedBy(1);
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) {
        service.delete(id,1);
    }
}