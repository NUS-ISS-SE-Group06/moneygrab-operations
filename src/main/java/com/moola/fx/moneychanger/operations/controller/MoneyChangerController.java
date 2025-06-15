package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.service.MoneyChangerLocationService;
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

    private final MoneyChangerLocationService moneyChangerLocationService;
    private final ObjectMapper objectMapper;

    public MoneyChangerController(
            MoneyChangerService moneyChangerService,
            MoneyChangerPhotoService moneyChangerPhotoService,
            MoneyChangerKycService moneyChangerKycService,
            MoneyChangerLocationService moneyChangerLocationService,
            ObjectMapper objectMapper) {
        this.moneyChangerService = moneyChangerService;
        this.moneyChangerPhotoService = moneyChangerPhotoService;
        this.moneyChangerKycService = moneyChangerKycService;
        this.moneyChangerLocationService = moneyChangerLocationService;
        this.objectMapper = objectMapper;

    }

    // GET ALL MoneyChangers
    @GetMapping
    public ResponseEntity<List<MoneyChangerResponseDTO>> getAllMoneyChangers() {
        List<MoneyChangerResponseDTO> moneyChangers = moneyChangerService.getAllMoneyChangers();
        return ResponseEntity.ok(moneyChangers);
    }
    // GET ONE MoneyChanger with full info
    @GetMapping("/{id}")
    public ResponseEntity<MoneyChangerResponseDTO> getMoneyChangerById(@PathVariable Long id) {
        MoneyChangerResponseDTO dto = moneyChangerService.getMoneyChangerById(id);

        // Enrich with photo
        MoneyChangerPhoto photo = moneyChangerPhotoService.getByMoneyChangerId(id);
        if (photo != null) {
            dto.setPhotoData(photo.getPhotoData());
            dto.setPhotoFilename(photo.getPhotoFilename());
            dto.setPhotoMimetype(photo.getPhotoMimetype());
        }

        // Enrich with KYC
        MoneyChangerKyc kyc = moneyChangerKycService.getByMoneyChangerId(id);
        if (kyc != null) {
            dto.setKycDocumentData(kyc.getDocumentData());
            dto.setKycDocumentFilename(kyc.getDocumentFilename());
            dto.setKycDocumentMimetype(kyc.getDocumentMimetype());
        }

        // Enrich with locations
        dto.setLocations(moneyChangerLocationService.getLocationNamesByMoneyChanger(id));

        return ResponseEntity.ok(dto);
    }




    @PutMapping("/{id}")
    public ResponseEntity<MoneyChangerResponseDTO> updateMoneyChanger(
            @PathVariable Long id,
            @RequestPart("moneyChanger") MoneyChangerResponseDTO dto,
            @RequestPart(value = "photo", required = false) MultipartFile photo,
            @RequestPart(value = "kycDoc", required = false) MultipartFile kycDoc
    ) {
        MoneyChangerResponseDTO updated = moneyChangerService.updateMoneyChanger(dto);

        return ResponseEntity.ok(updated);
    }

    // POST create MoneyChanger with optional photo and KYC
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MoneyChangerResponseDTO> createMoneyChanger(
            @RequestPart("moneyChanger") String moneyChangerJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile,
            @RequestPart(value = "kycDoc", required = false) MultipartFile kycFile
    ) throws Exception {

        //  Convert incoming JSON to MoneyChangerResponseDTO
        MoneyChangerResponseDTO dto = objectMapper.readValue(moneyChangerJson, MoneyChangerResponseDTO.class);

        //  Call the correct service method
        MoneyChangerResponseDTO created = moneyChangerService.createMoneyChanger(dto);

        //  Save photo and KYC using returned ID
        if (photoFile != null && !photoFile.isEmpty()) {
            moneyChangerPhotoService.saveOrUpdate(created.getId(), photoFile);
        }

        if (kycFile != null && !kycFile.isEmpty()) {
            moneyChangerKycService.saveOrUpdate(created.getId(), kycFile);
        }

        // Enrich response with uploaded photo/KYC
        MoneyChangerPhoto photo = moneyChangerPhotoService.getByMoneyChangerId(created.getId());
        MoneyChangerKyc kyc = moneyChangerKycService.getByMoneyChangerId(created.getId());

        if (photo != null) {
            created.setPhotoData(photo.getPhotoData());
            created.setPhotoFilename(photo.getPhotoFilename());
            created.setPhotoMimetype(photo.getPhotoMimetype());
        }

        if (kyc != null) {
            created.setKycDocumentData(kyc.getDocumentData());
            created.setKycDocumentFilename(kyc.getDocumentFilename());
            created.setKycDocumentMimetype(kyc.getDocumentMimetype());
        }

        // THIS LINE IS MISSING — Add this!
        moneyChangerLocationService.saveLocations(created.getId(), dto.getLocations());
        created.setLocations(moneyChangerLocationService.getLocationNamesByMoneyChanger(created.getId()));

        return ResponseEntity.ok(created);
    }


    @PutMapping(path = "/{id}", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<MoneyChangerResponseDTO> updateMoneyChanger(
            @PathVariable Long id,
            @RequestPart("moneyChanger") String moneyChangerJson,
            @RequestPart(value = "photo", required = false) MultipartFile photoFile,
            @RequestPart(value = "kycDoc", required = false) MultipartFile kycFile
    ) throws Exception {

        //  Convert JSON into DTO
        MoneyChangerResponseDTO dto = objectMapper.readValue(moneyChangerJson, MoneyChangerResponseDTO.class);
        dto.setId(id);

        // Call the correct service method
        MoneyChangerResponseDTO updated = moneyChangerService.updateMoneyChanger(dto);
        moneyChangerLocationService.saveLocations(id, dto.getLocations());
        //  Save photo/KYC if provided
        if (photoFile != null && !photoFile.isEmpty()) {
            moneyChangerPhotoService.saveOrUpdate(id, photoFile);
        }

        if (kycFile != null && !kycFile.isEmpty()) {
            moneyChangerKycService.saveOrUpdate(id, kycFile);
        }

        // Enrich with stored photo/kyc
        MoneyChangerPhoto photo = moneyChangerPhotoService.getByMoneyChangerId(id);
        MoneyChangerKyc kyc = moneyChangerKycService.getByMoneyChangerId(id);

        if (photo != null) {
            updated.setPhotoData(photo.getPhotoData());
            updated.setPhotoFilename(photo.getPhotoFilename());
            updated.setPhotoMimetype(photo.getPhotoMimetype());
        }

        if (kyc != null) {
            updated.setKycDocumentData(kyc.getDocumentData());
            updated.setKycDocumentFilename(kyc.getDocumentFilename());
            updated.setKycDocumentMimetype(kyc.getDocumentMimetype());
        }

        MoneyChangerResponseDTO finalDto = moneyChangerService.getMoneyChangerById(id); // Re-fetch latest full state

        return ResponseEntity.ok(updated);
    }

    // DELETE MoneyChanger
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMoneyChanger(@PathVariable Long id) {
        moneyChangerService.deleteMoneyChanger(id);
        return ResponseEntity.ok().build();
    }
}