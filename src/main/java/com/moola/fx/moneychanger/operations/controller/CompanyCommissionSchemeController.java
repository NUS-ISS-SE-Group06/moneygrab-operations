package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.CompanyCommissionSchemeDTO;
import com.moola.fx.moneychanger.operations.mapper.CompanyCommissionSchemeMapper;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.service.CompanyCommissionSchemeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/company-commission-schemes")
public class CompanyCommissionSchemeController {
    private final CompanyCommissionSchemeService service;
    private final CompanyCommissionSchemeMapper mapper;

    @Autowired
    public CompanyCommissionSchemeController(CompanyCommissionSchemeService service, CompanyCommissionSchemeMapper mapper) {
        this.service = service;
        this.mapper = mapper;
    }

    @GetMapping
    public ResponseEntity<List<CompanyCommissionSchemeDTO>> list(@RequestParam(value ="schemeId", required = false ) Integer schemeId) {
        List<CompanyCommissionScheme> list = (schemeId != null)
                ? service.findBySchemeId(schemeId)
                : service.listAll();

        List<CompanyCommissionSchemeDTO> result= list.stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompanyCommissionSchemeDTO> get(@PathVariable("id") Integer id) {
        CompanyCommissionSchemeDTO dto = mapper.toDTO(service.get(id));
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<CompanyCommissionSchemeDTO> create(@RequestBody CompanyCommissionSchemeDTO dto) {
        CompanyCommissionScheme entity = mapper.toEntity(dto);
        entity.setUpdatedBy(dto.getCreatedBy());
        CompanyCommissionScheme saved = service.save(entity);
        return ResponseEntity.ok(mapper.toDTO(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompanyCommissionSchemeDTO> update(@PathVariable("id") Integer id, @RequestBody CompanyCommissionSchemeDTO dto) {
        CompanyCommissionScheme updated = service.save(dto);
        return ResponseEntity.ok(mapper.toDTO(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void>  delete(@PathVariable("id") Integer id, @RequestParam("userId") Integer userId) {
        service.delete(id,userId);
        return ResponseEntity.noContent().build();
    }


}