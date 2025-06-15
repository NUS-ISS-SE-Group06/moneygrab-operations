package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerLocation;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerLocationRepository;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MoneyChangerServiceImpl implements MoneyChangerService {

    private final MoneyChangerRepository moneyChangerRepository;
    private final MoneyChangerLocationRepository locationRepository;

    public MoneyChangerServiceImpl(MoneyChangerRepository moneyChangerRepository,
                                   MoneyChangerLocationRepository locationRepository) {
        this.moneyChangerRepository = moneyChangerRepository;
        this.locationRepository = locationRepository;
    }

    @Override
    public List<MoneyChangerResponseDTO> getAllMoneyChangers() {
        return moneyChangerRepository.findAll().stream()
                .filter(m -> Boolean.FALSE.equals(m.getIsDeleted()))
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Override
    /*
    public MoneyChangerResponseDTO getMoneyChangerById(Long id) {
        MoneyChanger entity = moneyChangerRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("MoneyChanger not found"));
        return mapToDto(entity);
    }
  */
    public MoneyChangerResponseDTO getMoneyChangerById(Long id) {
        MoneyChanger entity = moneyChangerRepository.findByIdAndIsDeletedFalse(id)
                .orElseThrow(() -> new IllegalArgumentException("MoneyChanger not found or deleted"));
        return mapToDto(entity);
    }

    @Override
    public MoneyChangerResponseDTO createMoneyChanger(MoneyChangerResponseDTO dto) {
        MoneyChanger entity = new MoneyChanger();
        entity.setCompanyName(dto.getCompanyName());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setPostalCode(dto.getPostalCode());
        entity.setNotes(dto.getNotes());
        entity.setDateOfIncorporation(dto.getDateOfIncorporation());
        entity.setCountry(dto.getCountry());
        entity.setUen(dto.getUen());
        entity.setSchemeId(dto.getSchemeId());
        entity.setIsDeleted(false);
        entity.setCreatedAt(java.time.LocalDateTime.now());
        entity.setIsDeleted(false);
        entity.setUpdatedAt(java.time.LocalDateTime.now());
        MoneyChanger saved = moneyChangerRepository.save(entity);
        return mapToDto(saved);
    }

    @Override
    public MoneyChangerResponseDTO updateMoneyChanger(MoneyChangerResponseDTO dto) {
        //MoneyChanger entity = moneyChangerRepository.findById(dto.getId())
         //       .orElseThrow(() -> new IllegalArgumentException("MoneyChanger not found"));

        // Find the existing entity by ID
        MoneyChanger entity = moneyChangerRepository.findById(dto.getId())
                .orElseThrow(() -> new IllegalArgumentException("MoneyChanger not found"));

        // Check if the email is used by another record (exclude current ID)
        Optional<MoneyChanger> existingWithEmail = moneyChangerRepository.findByEmail(dto.getEmail());
        if (existingWithEmail.isPresent() && !existingWithEmail.get().getId().equals(dto.getId())) {
            throw new IllegalArgumentException("Email already in use by another money changer");
        }

        entity.setCompanyName(dto.getCompanyName());
        entity.setEmail(dto.getEmail());
        entity.setAddress(dto.getAddress());
        entity.setPostalCode(dto.getPostalCode());
        entity.setNotes(dto.getNotes());
        entity.setDateOfIncorporation(dto.getDateOfIncorporation());
        entity.setCountry(dto.getCountry());
        entity.setUen(dto.getUen());
        entity.setSchemeId(dto.getSchemeId());
        entity.setUpdatedAt(java.time.LocalDateTime.now());

        MoneyChanger updated = moneyChangerRepository.save(entity);
        return mapToDto(updated);
    }

    @Override
    public void deleteMoneyChanger(Long id) {
        Optional<MoneyChanger> optional = moneyChangerRepository.findById(id);
        if (optional.isPresent()) {
            MoneyChanger entity = optional.get();
            entity.setIsDeleted(true);
            entity.setUpdatedAt(java.time.LocalDateTime.now());
            moneyChangerRepository.save(entity);
        }
    }

    private MoneyChangerResponseDTO mapToDto(MoneyChanger entity) {
        MoneyChangerResponseDTO dto = new MoneyChangerResponseDTO();
        dto.setId(entity.getId());
        dto.setCompanyName(entity.getCompanyName());
        dto.setEmail(entity.getEmail());
        dto.setAddress(entity.getAddress());
        dto.setPostalCode(entity.getPostalCode());
        dto.setNotes(entity.getNotes());
        dto.setDateOfIncorporation(entity.getDateOfIncorporation());
        dto.setCountry(entity.getCountry());
        dto.setUen(entity.getUen());
        dto.setSchemeId(entity.getSchemeId());

        // Load location names from repository
        List<MoneyChangerLocation> locations = locationRepository.findByMoneyChangerAndIsDeletedFalse(entity);
        List<String> locationNames = locations.stream()
                .map(MoneyChangerLocation::getLocationName)
                .collect(Collectors.toList());
        dto.setLocations(locationNames);

        return dto;
    }

}