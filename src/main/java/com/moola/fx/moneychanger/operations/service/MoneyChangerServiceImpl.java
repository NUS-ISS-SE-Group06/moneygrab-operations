package com.moola.fx.moneychanger.operations.service;


import com.moola.fx.moneychanger.operations.dto.MoneyChangerDTO;
import com.moola.fx.moneychanger.operations.model.*;
import com.moola.fx.moneychanger.operations.repository.*;
import org.apache.tika.Tika;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Base64;
import java.util.List;
import java.time.LocalDateTime;

@Service
public class MoneyChangerServiceImpl implements MoneyChangerService {

    private final MoneyChangerRepository moneyChangerRepository;
    private final MoneyChangerLocationRepository locationRepository;
    private final MoneyChangerPhotoRepository photoRepository;
    private final MoneyChangerKycRepository kycRepository;

    public MoneyChangerServiceImpl(
            MoneyChangerRepository moneyChangerRepository,
            MoneyChangerLocationRepository locationRepository,
            MoneyChangerPhotoRepository photoRepository,
            MoneyChangerKycRepository kycRepository) {
        this.moneyChangerRepository = moneyChangerRepository;
        this.locationRepository = locationRepository;
        this.photoRepository = photoRepository;
        this.kycRepository = kycRepository;
    }

    @Override
    public List<MoneyChangerDTO> listAll() {
            return moneyChangerRepository.findAll().stream()
                    .filter(mc -> Boolean.FALSE.equals(mc.getIsDeleted()))
                    .map(this::toDTO)
                    .toList();
    }



        @Override
    public MoneyChangerDTO get(Long id) {
        MoneyChanger entity = moneyChangerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("MoneyChanger not found"));
        return toDTO(entity);
    }

    @Override
    public MoneyChangerDTO create(MoneyChangerDTO dto) {
        MoneyChanger entity = new MoneyChanger();
        toEntity(dto, entity);
        entity.setCreatedAt(LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        entity.setIsDeleted(false);
        MoneyChanger saved = moneyChangerRepository.save(entity);
        saveLocations(dto, saved.getId());
        savePhoto(dto, saved.getId());
        saveKyc(dto, saved.getId());
        return toDTO(saved);
    }

    @Override
    public MoneyChangerDTO update(Long id, MoneyChangerDTO dto) {
        MoneyChanger entity = moneyChangerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MoneyChanger not found"));
        LocalDateTime originalCreatedAt = entity.getCreatedAt();
        toEntity(dto, entity);
        // Restore original createdAt and set updatedAt
        // Restore original createdAt and set updatedAt
        entity.setCreatedAt(originalCreatedAt != null ? originalCreatedAt : LocalDateTime.now());
        entity.setUpdatedAt(LocalDateTime.now());
        MoneyChanger updated = moneyChangerRepository.save(entity);
        saveLocations(dto, updated.getId());
        savePhoto(dto, updated.getId());
        saveKyc(dto, updated.getId());
        return toDTO(updated);
    }

    @Override
    public void delete(Long id) {
        moneyChangerRepository.findById(id).ifPresent(mc -> {
            mc.setIsDeleted(true);
            moneyChangerRepository.save(mc);

        });
    }

    private MoneyChangerDTO toDTO(MoneyChanger entity) {
        MoneyChangerDTO dto = new MoneyChangerDTO();
        dto.setId(entity.getId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setPostalCode(entity.getPostalCode());
        dto.setNotes(entity.getNotes());
        dto.setCountry(entity.getCountry());
        dto.setUen(entity.getUen());
        dto.setSchemeId(entity.getSchemeId());
        dto.setIsDeleted(entity.getIsDeleted() != null && entity.getIsDeleted() ? 1 : 0);
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        entity.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : null);
        entity.setUpdatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : null);
        // Convert date string to LocalDate
        if (entity.getDateOfIncorporation() != null && !entity.getDateOfIncorporation().isEmpty()) {
            dto.setDateOfIncorporation(LocalDate.parse(entity.getDateOfIncorporation()));
        }

        List<MoneyChangerLocation> locations = locationRepository.findByMoneyChangerIdAndIsDeletedFalse(entity.getId());
        dto.setLocations(locations.stream().map(MoneyChangerLocation::getLocationName).toList());

        photoRepository.findByMoneyChangerIdAndIsDeletedFalse(entity.getId()).ifPresent(photo -> {        dto.setPhotoMimetype(photo.getPhotoMimetype());
            dto.setLogoBase64(Base64.getEncoder().encodeToString(photo.getPhotoData()));
        });

        kycRepository.findByMoneyChangerIdAndIsDeletedFalse(entity.getId()).ifPresent(kyc -> {
            dto.setKycFilename(kyc.getDocumentFilename());
            dto.setDocumentMimetype(kyc.getDocumentMimetype());
            dto.setKycBase64(Base64.getEncoder().encodeToString(kyc.getDocumentData()));
        });

        return dto;
    }

    private void toEntity(MoneyChangerDTO dto, MoneyChanger entity) {
        entity.setCompanyName(dto.getCompanyName());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setPostalCode(dto.getPostalCode());
        entity.setNotes(dto.getNotes());
        entity.setCountry(dto.getCountry());
        entity.setUen(dto.getUen());
        entity.setSchemeId(dto.getSchemeId());
        dto.setIsDeleted(entity.getIsDeleted() != null && entity.getIsDeleted() ? 1 : 0);
        entity.setCreatedAt(dto.getCreatedAt());
        entity.setUpdatedAt(dto.getUpdatedAt());
        entity.setCreatedBy(dto.getCreatedBy() != null ? dto.getCreatedBy() : null);
        entity.setUpdatedBy(dto.getUpdatedBy() != null ? dto.getUpdatedBy() : null);

        if (dto.getDateOfIncorporation() != null) {
            entity.setDateOfIncorporation(dto.getDateOfIncorporation().toString());
        }
    }

    private void saveLocations(MoneyChangerDTO dto, Long moneyChangerId) {
        List<MoneyChangerLocation> existing = locationRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId);
        for (MoneyChangerLocation loc : existing) {
            loc.setIsDeleted(true);
        }
        locationRepository.saveAll(existing);

        if (dto.getLocations() != null) {
            List<MoneyChangerLocation> newLocs = dto.getLocations().stream().map(name -> {
                MoneyChangerLocation loc = new MoneyChangerLocation();
                loc.setLocationName(name);
                MoneyChanger mcRef = new MoneyChanger();
                mcRef.setId(moneyChangerId);
                loc.setMoneyChanger(mcRef);
                return loc;
            }).toList();
            locationRepository.saveAll(newLocs);
        }
    }

    private void savePhoto(MoneyChangerDTO dto, Long moneyChangerId) {
        if (dto.getLogoBase64() == null || dto.getLogoBase64().trim().isEmpty()) {
            return; // No new photo provided, keep existing record untouched
        }

        photoRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId).ifPresent(existing -> {
            existing.setIsDeleted(true);
            photoRepository.save(existing);
        });
        if (dto.getLogoBase64() != null && !dto.getLogoBase64().isEmpty()) {

            MoneyChangerPhoto photo = new MoneyChangerPhoto();
            photo.setMoneyChangerId(moneyChangerId);
            String base64Data = dto.getLogoBase64().contains(",")
                    ? dto.getLogoBase64().substring(dto.getLogoBase64().indexOf(',') + 1) // strip prefix
                    : dto.getLogoBase64();

            byte[] decoded = Base64.getDecoder().decode(base64Data);
            photo.setPhotoData(decoded);               // GOOD data
            photo.setPhotoFilename(dto.getLogoFilename());
            photo.setPhotoMimetype(detectMimeType(decoded));
            photoRepository.save(photo);
        }
    }

    private void saveKyc(MoneyChangerDTO dto, Long moneyChangerId) {

        if (dto.getKycBase64() == null || dto.getKycBase64().trim().isEmpty()) {
            return; // No new KYC provided, keep existing record untouched
        }
        kycRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId).ifPresent(existing -> {
            existing.setIsDeleted(true);
            kycRepository.save(existing);
        });

        if (dto.getKycBase64() != null && !dto.getKycBase64().isEmpty()) {

            MoneyChangerKyc kyc = new MoneyChangerKyc();
            kyc.setMoneyChangerId(moneyChangerId);
            String base64Data =dto.getKycBase64().contains(",")
                    ? dto.getKycBase64().substring(dto.getKycBase64().indexOf(',') + 1) // strip prefix
                    : dto.getKycBase64();
            byte[] decoded = Base64.getDecoder().decode(base64Data);
            kyc.setDocumentData(decoded);               // GOOD data
            kyc.setDocumentFilename(dto.getKycFilename());
            kyc.setDocumentMimetype(detectMimeType(decoded));
            kycRepository.save(kyc);
        }
    }

    private String detectMimeType(byte[] bytes) {
        try {
            return new Tika().detect(bytes);
        } catch (Exception e) {
            return "application/octet-stream";
        }
    }
}
