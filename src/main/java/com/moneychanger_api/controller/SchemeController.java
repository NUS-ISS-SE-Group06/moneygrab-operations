package com.moneychanger_api.controller;

import com.moneychanger_api.model.Scheme;
import com.moneychanger_api.service.SchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/v1/schemes")
public class SchemeController {
    @Autowired
    private SchemeService service;

    @GetMapping
    public List<Scheme> list() { return service.listAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Scheme> get(@PathVariable Integer id) {
        Scheme item = service.get(id);
        return ResponseEntity.ok(item);
    }

    @PostMapping
    public Scheme create(@RequestBody Scheme item) { return service.save(item); }

    @PutMapping("/{id}")
    public Scheme update(@PathVariable Integer id, @RequestBody Scheme item) {
        item.setId(id);
        return service.save(item);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) { service.delete(id); }
}