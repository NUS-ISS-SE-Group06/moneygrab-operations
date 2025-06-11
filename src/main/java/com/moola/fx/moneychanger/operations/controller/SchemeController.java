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
    public ResponseEntity<List<Scheme>> list() {
        List<Scheme> schemes = service.listAll();
        return ResponseEntity.ok(schemes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Scheme> get(@PathVariable Integer id) {
        Scheme item = service.get(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public ResponseEntity<Scheme>  create(@RequestBody Scheme item) {
        Scheme saved = service.save(item);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Scheme>  update(@PathVariable Integer id, @RequestBody Scheme item) {
        Scheme existing = service.get(id);

        existing.setUpdatedBy(item.getUpdatedBy());
        existing.setDescription(item.getDescription());
        existing.setIsDefault(item.getIsDefault());

        Scheme updated = service.save(existing);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id, @RequestParam("userId") Integer userId) {
        service.delete(id,userId);
        return ResponseEntity.noContent().build();
    }

}