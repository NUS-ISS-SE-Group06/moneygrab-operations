package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerKyc;
import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import com.moola.fx.moneychanger.operations.service.MoneyChangerKycService;
import com.moola.fx.moneychanger.operations.service.MoneyChangerPhotoService;
import com.moola.fx.moneychanger.operations.service.MoneyChangerService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/money-changers")
public class MoneyChangerController {

    private final MoneyChangerService moneyChangerService;
    private final MoneyChangerPhotoService moneyChangerPhotoService;
    private final MoneyChangerKycService moneyChangerKycService;
    private final ObjectMapper objectMapper;

    public MoneyChangerController(
            MoneyChangerService moneyChangerService,
            MoneyChangerPhotoService moneyChangerPhotoService,
            MoneyChangerKycService moneyChangerKycService,
            ObjectMapper objectMapper) {
        this.moneyChangerService = moneyChangerService;
        this.moneyChangerPhotoService = moneyChangerPhotoService;
        this.moneyChangerKycService = moneyChangerKycService;
        this.objectMapper = objectMapper;
    }

    // GET ALL MoneyChangers
    @GetMapping
    public ResponseEntity<List<MoneyChanger>> getAllMoneyChangers() {
        List<MoneyChanger> moneyChangers = moneyChangerService.getAll();
        return ResponseEntity.ok(moneyChangers);
    }

    // GET ONE MoneyChanger with full info
    @GetMapping("/{id}")
    public ResponseEntity<MoneyChangerResponseDTO> getMoneyChangerById(@PathVariable Long id) {
        MoneyChanger mc = moneyChangerService.getById(id);
        MoneyChangerPhoto photo = moneyChangerPhotoService.getByMoneyChangerId(id);
        MoneyChangerKyc kyc = moneyChangerKycService.getByMoneyChangerId(id);

        MoneyChangerResponseDTO dto = new MoneyChangerResponseDTO();
        dto.setId(mc.getId());
        dto.setCompanyName(mc.getCompanyName());
        dto.setEmail(mc.getEmail());
        dto.setAddress(mc.getAddress());
        dto.setPostalCode(mc.getPostalCode());
        dto.setNotes(mc.getNotes());
        dto.setDateOfIncorporation(mc.getDateOfIncorporation());
        dto.setCountry(mc.getCountry());
        dto.setUen(mc.getUen());
        dto.setSchemeId(mc.getSchemeId());

        if (photo != null) {
            dto.setPhotoData(photo.getPhotoData());
            dto.setPhotoFilename(photo.getPhotoFilename());
            dto.setPhotoMimetype(photo.getPhotoMimetype());
        }

        if (kyc != null) {
            dto.setKycDocumentData(kyc.getDocumentData());
            dto.setKycDocumentFilename(kyc.getDocumentFilename());
            dto.setKycDocumentMimetype(kyc.getDocumentMimetype());
        }

        return ResponseEntity.ok(dto);
    }

    // POST create MoneyChanger with optional photo and KYC
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MoneyChangerResponseDTO> createMoneyChanger(
            @RequestPart("moneyChanger") String moneyChangerJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile,
            @RequestPart(value = "kycDoc", required = false) MultipartFile kycFile
    ) throws Exception {

        MoneyChanger moneyChanger = objectMapper.readValue(moneyChangerJson, MoneyChanger.class);
        MoneyChanger created = moneyChangerService.create(moneyChanger);

        if (photoFile != null && !photoFile.isEmpty()) {
            moneyChangerPhotoService.saveOrUpdate(created.getId(), photoFile);
        }

        if (kycFile != null && !kycFile.isEmpty()) {
            moneyChangerKycService.saveOrUpdate(created.getId(), kycFile);
        }

        MoneyChangerPhoto photo = moneyChangerPhotoService.getByMoneyChangerId(created.getId());
        MoneyChangerKyc kyc = moneyChangerKycService.getByMoneyChangerId(created.getId());

        MoneyChangerResponseDTO responseDTO = new MoneyChangerResponseDTO();
        responseDTO.setId(created.getId());
        responseDTO.setCompanyName(created.getCompanyName());
        responseDTO.setEmail(created.getEmail());
        responseDTO.setAddress(created.getAddress());
        responseDTO.setPostalCode(created.getPostalCode());
        responseDTO.setNotes(created.getNotes());
        responseDTO.setDateOfIncorporation(created.getDateOfIncorporation());
        responseDTO.setCountry(created.getCountry());
        responseDTO.setUen(created.getUen());
        responseDTO.setSchemeId(created.getSchemeId());

        if (photo != null) {
            responseDTO.setPhotoData(photo.getPhotoData());
            responseDTO.setPhotoFilename(photo.getPhotoFilename());
            responseDTO.setPhotoMimetype(photo.getPhotoMimetype());
        }

        if (kyc != null) {
            responseDTO.setKycDocumentData(kyc.getDocumentData());
            responseDTO.setKycDocumentFilename(kyc.getDocumentFilename());
            responseDTO.setKycDocumentMimetype(kyc.getDocumentMimetype());
        }

        return ResponseEntity.ok(responseDTO);
    }

    // PUT update MoneyChanger with optional new photo and KYC
    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MoneyChangerResponseDTO> updateMoneyChanger(
            @PathVariable Long id,
            @RequestPart("moneyChanger") String moneyChangerJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile,
            @RequestPart(value = "kycDoc", required = false) MultipartFile kycFile
    ) throws Exception {

        MoneyChanger updatedMoneyChanger = objectMapper.readValue(moneyChangerJson, MoneyChanger.class);
        MoneyChanger updated = moneyChangerService.update(id, updatedMoneyChanger);

        if (photoFile != null && !photoFile.isEmpty()) {
            moneyChangerPhotoService.saveOrUpdate(id, photoFile);
        }

        if (kycFile != null && !kycFile.isEmpty()) {
            moneyChangerKycService.saveOrUpdate(id, kycFile);
        }

        MoneyChangerPhoto photo = moneyChangerPhotoService.getByMoneyChangerId(id);
        MoneyChangerKyc kyc = moneyChangerKycService.getByMoneyChangerId(id);

        MoneyChangerResponseDTO responseDTO = new MoneyChangerResponseDTO();
        responseDTO.setId(updated.getId());
        responseDTO.setCompanyName(updated.getCompanyName());
        responseDTO.setEmail(updated.getEmail());
        responseDTO.setAddress(updated.getAddress());
        responseDTO.setPostalCode(updated.getPostalCode());
        responseDTO.setNotes(updated.getNotes());
        responseDTO.setDateOfIncorporation(updated.getDateOfIncorporation());
        responseDTO.setCountry(updated.getCountry());
        responseDTO.setUen(updated.getUen());
        responseDTO.setSchemeId(updated.getSchemeId());

        if (photo != null) {
            responseDTO.setPhotoData(photo.getPhotoData());
            responseDTO.setPhotoFilename(photo.getPhotoFilename());
            responseDTO.setPhotoMimetype(photo.getPhotoMimetype());
        }

        if (kyc != null) {
            responseDTO.setKycDocumentData(kyc.getDocumentData());
            responseDTO.setKycDocumentFilename(kyc.getDocumentFilename());
            responseDTO.setKycDocumentMimetype(kyc.getDocumentMimetype());
        }

        return ResponseEntity.ok(responseDTO);
    }

    // DELETE MoneyChanger
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoneyChanger(@PathVariable Long id) {
        moneyChangerService.delete(id);
        return ResponseEntity.ok().build();
    }
}