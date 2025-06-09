package com.moneychanger_api.controller;

import com.moneychanger_api.dto.CommissionRateDTO;
import com.moneychanger_api.mapper.CommissionRateMapper;
import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.service.CommissionRateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/commission-rates")
public class CommissionRateController {
    private final CommissionRateService service;
    private final CommissionRateMapper mapper;

    @Autowired
    public CommissionRateController(CommissionRateService service, CommissionRateMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public List<CommissionRateDTO> list() {
        return service.listAll().stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommissionRateDTO> get(@PathVariable Integer id) {
        CommissionRateDTO dto = mapper.toDTO(service.get(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CommissionRateDTO> create(@RequestBody CommissionRateDTO item) {
        CommissionRate commissionRate = mapper.toEntity(item);
        CommissionRate saved = service.save(commissionRate);
        return ResponseEntity.ok(mapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommissionRateDTO> update(@PathVariable Integer id, @RequestBody CommissionRateDTO item) {
        CommissionRate commissionRate = mapper.toEntity(item);
        item.setId(id);
        CommissionRate saved = service.save(commissionRate);
        return ResponseEntity.ok(mapper.toDTO(saved));
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Integer id) { service.delete(id); }
}