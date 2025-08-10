package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.ApplicationSettingDTO;
import com.moola.fx.moneychanger.operations.service.ApplicationSettingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/v1/application-settings")
public class ApplicationSettingController {

    private final ApplicationSettingService service;

    public ApplicationSettingController(ApplicationSettingService service) {
        this.service = service;
    }

    // GET – Retrieve all records.
    @GetMapping
    public ResponseEntity<List<ApplicationSettingDTO>> listAll() {
        return ResponseEntity.ok(service.listAll());
    }

    // GET /category/{category} – Retrieve records filtered by category.
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ApplicationSettingDTO>> listByCategory(@PathVariable String category) {
        return ResponseEntity.ok(service.listByCategory(category));
    }

    // GET /category/{category}/key/{settingKey} – Retrieve one by category+key.
    @GetMapping("/category/{category}/key/{settingKey}")
    public ResponseEntity<ApplicationSettingDTO> getByCategoryAndKey(@PathVariable String category,
                                                                     @PathVariable String settingKey) {
        return ResponseEntity.ok(service.getByCategoryAndKey(category, settingKey));
    }

    // (Full CRUD) Get by id
    @GetMapping("/{id}")
    public ResponseEntity<ApplicationSettingDTO> get(@PathVariable Long id) {
        return ResponseEntity.ok(service.get(id));
    }

    // Create
    @PostMapping
    public ResponseEntity<ApplicationSettingDTO> create(@RequestBody ApplicationSettingDTO dto) {
        ApplicationSettingDTO created = service.create(dto);
        return ResponseEntity.created(URI.create("/v1/application-settings/" + created.getId())).body(created);
    }

    // Update
    @PutMapping("/{id}")
    public ResponseEntity<ApplicationSettingDTO> update(@PathVariable Long id,
                                                        @RequestBody ApplicationSettingDTO dto) {
        return ResponseEntity.ok(service.update(id, dto));
    }

    // Delete
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
