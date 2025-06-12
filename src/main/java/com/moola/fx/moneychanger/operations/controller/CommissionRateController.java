package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.CommissionRateDTO;
import com.moola.fx.moneychanger.operations.mapper.CommissionRateMapper;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.service.CommissionRateService;
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
    public ResponseEntity<List<CommissionRateDTO>> list(@RequestParam(value ="schemeId", required = false ) Integer schemeId) {
        List<CommissionRate> list = (schemeId != null)
                ? service.findBySchemeId(schemeId)
                : service.listAll();

        List<CommissionRateDTO> result= list.stream().map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CommissionRateDTO> get(@PathVariable("id") Integer id) {
        CommissionRateDTO dto = mapper.toDTO(service.get(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CommissionRateDTO> create(@RequestBody CommissionRateDTO dto) {
        CommissionRate entity = mapper.toEntity(dto);

        entity.setUpdatedBy(dto.getCreatedBy());

        CommissionRate saved = service.save(entity);
        return ResponseEntity.ok(mapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CommissionRateDTO> update(@PathVariable("id") Integer id, @RequestBody CommissionRateDTO dto) {
        CommissionRate existing = service.get(id);

        if (dto.getRate() != null) {
            existing.setRate(dto.getRate());
        }

        existing.setUpdatedBy(dto.getUpdatedBy());

        CommissionRate updated = service.save(existing);
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") Integer id, @RequestParam("userId") Integer userId) {
        service.delete(id,userId);
        return ResponseEntity.noContent().build();
    }
}