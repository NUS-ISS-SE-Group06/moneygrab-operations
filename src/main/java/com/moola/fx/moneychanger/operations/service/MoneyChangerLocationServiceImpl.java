package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerLocation;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerLocationRepository;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.time.LocalDateTime;

@Service
public class MoneyChangerLocationServiceImpl implements MoneyChangerLocationService {

    private final MoneyChangerLocationRepository locationRepository;
    private final MoneyChangerRepository moneyChangerRepository;

    public MoneyChangerLocationServiceImpl(
            MoneyChangerLocationRepository locationRepository,
            MoneyChangerRepository moneyChangerRepository
    ) {
        this.locationRepository = locationRepository;
        this.moneyChangerRepository = moneyChangerRepository;
    }

    @Override
    public List<MoneyChangerResponseDTO> getLocationsByMoneyChanger(Long moneyChangerId) {
        Optional<MoneyChanger> mcOpt = moneyChangerRepository.findById(moneyChangerId);
        if (mcOpt.isEmpty()) {
            return Collections.emptyList();
        }

        MoneyChanger moneyChanger = mcOpt.get();
        List<MoneyChangerLocation> locations = locationRepository.findByMoneyChangerAndIsDeletedFalse(moneyChanger);
        List<MoneyChangerResponseDTO> dtoList = new ArrayList<>();

        for (MoneyChangerLocation location : locations) {
            MoneyChangerResponseDTO dto = new MoneyChangerResponseDTO();
            dto.setLocation(location.getLocationName());
            dtoList.add(dto);
        }

        return dtoList;
    }

    @Override
    public void addLocation(MoneyChangerResponseDTO dto) {
        if (dto.getId() == null || dto.getLocation() == null) {
            return;
        }

        Optional<MoneyChanger> mcOpt = moneyChangerRepository.findById(dto.getId());
        if (mcOpt.isEmpty()) {
            return;
        }

        MoneyChangerLocation location = new MoneyChangerLocation();
        location.setMoneyChanger(mcOpt.get());
        location.setLocationName(dto.getLocation());
        location.setIsDeleted(false); //  always ensure explicitly set

        locationRepository.save(location);
    }

    @Override
    public void deleteLocation(Long locationId) {
        Optional<MoneyChangerLocation> locOpt = locationRepository.findById(locationId);
        if (locOpt.isPresent()) {
            MoneyChangerLocation location = locOpt.get();
            location.setIsDeleted(true);
            locationRepository.save(location);
        }
    }

    @Override
    @Transactional
    public void saveLocations(Long moneyChangerId, List<String> locations) {
        if (moneyChangerId == null || locations == null || locations.isEmpty()) {
            return;
        }

        locationRepository.softDeleteAllByMoneyChangerId(moneyChangerId);

        Optional<MoneyChanger> mcOpt = moneyChangerRepository.findById(moneyChangerId);
        if (mcOpt.isEmpty()) {
            return;
        }

        MoneyChanger moneyChanger = mcOpt.get();

        for (String locationStr : locations) {
            if (locationStr == null || locationStr.trim().isEmpty()) {
                continue;
            }

            MoneyChangerLocation location = new MoneyChangerLocation();
            location.setLocationName(locationStr.trim());
            location.setMoneyChanger(moneyChanger);
            location.setIsDeleted(false); //  this is critical
            location.setCreatedAt(LocalDateTime.now());
            location.setUpdatedAt(LocalDateTime.now());
            locationRepository.save(location);
        }
    }


    @Override
    public List<String> getLocationNamesByMoneyChanger(Long moneyChangerId) {
        Optional<MoneyChanger> mcOpt = moneyChangerRepository.findById(moneyChangerId);
        if (mcOpt.isEmpty()) {
            return Collections.emptyList();
        }

        return locationRepository.findByMoneyChangerAndIsDeletedFalse(mcOpt.get())
                .stream()
                .map(MoneyChangerLocation::getLocationName)
                .toList();
    }
}
