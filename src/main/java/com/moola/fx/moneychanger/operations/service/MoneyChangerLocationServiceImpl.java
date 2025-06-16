package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerLocation;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerLocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class MoneyChangerLocationServiceImpl implements MoneyChangerLocationService {

    private final MoneyChangerLocationRepository locationRepository;

    public MoneyChangerLocationServiceImpl(final MoneyChangerLocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<MoneyChangerResponseDTO> getLocationsByMoneyChanger(final Long moneyChangerId) {
        final List<MoneyChangerLocation> locations =
                locationRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId);

        return locations.stream().map(loc -> {
            final MoneyChangerResponseDTO dto = new MoneyChangerResponseDTO();
            dto.setId(loc.getMoneyChanger().getId());
            dto.setLocations(
                    locations.stream().map(MoneyChangerLocation::getLocationName).toList()
            );
            return dto;
        }).toList();
    }

    @Override
    @Transactional
    public void addLocation(
            final Long moneyChangerId,
            final List<String> locationNames,
            final Long createdBy,
            final Long updatedBy
    ) {
        if (locationNames == null || locationNames.isEmpty()) {
            return;
        }

        final List<MoneyChangerLocation> newLocations = locationNames.stream()
                .filter(Objects::nonNull)
                .map(name -> {
                    final MoneyChangerLocation loc = new MoneyChangerLocation();
                    loc.setLocationName(name);

                    final MoneyChanger moneyChanger = new MoneyChanger();
                    moneyChanger.setId(moneyChangerId);
                    loc.setMoneyChanger(moneyChanger);

                    loc.setIsDeleted(false);
                    loc.setCreatedAt(LocalDateTime.now());
                    loc.setUpdatedAt(LocalDateTime.now());
                    loc.setCreatedBy(0L);
                    loc.setUpdatedBy(0L);
                    return loc;
                })
                .collect(Collectors.toList());

        locationRepository.saveAll(newLocations);
    }

    @Override
    @Transactional
    public void deleteLocation(final Long locationId) {
        final MoneyChangerLocation location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        location.setIsDeleted(true);
        location.setUpdatedAt(LocalDateTime.now());
        locationRepository.save(location);
    }

    @Override
    @Transactional
    public void saveLocations(final Long moneyChangerId, final List<String> locationNames) {
        final List<MoneyChangerLocation> existing =
                locationRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId);

        for (MoneyChangerLocation loc : existing) {
            loc.setIsDeleted(true);
            loc.setUpdatedAt(LocalDateTime.now());
        }

        locationRepository.saveAll(existing);

        final List<MoneyChangerLocation> newLocations = locationNames.stream()
                .filter(Objects::nonNull)
                .map(name -> {
                    final MoneyChangerLocation loc = new MoneyChangerLocation();
                    loc.setLocationName(name);

                    final MoneyChanger mc = new MoneyChanger();
                    mc.setId(moneyChangerId);
                    loc.setMoneyChanger(mc);

                    loc.setIsDeleted(false);
                    loc.setCreatedAt(LocalDateTime.now());
                    loc.setUpdatedAt(LocalDateTime.now());

                    // Use system default ID (e.g. service user, audit system, or null if not applicable)
                    loc.setCreatedBy(0L);
                    loc.setUpdatedBy(0L);
                    return loc;
                })
                .collect(Collectors.toList());

        locationRepository.saveAll(newLocations);
    }

    @Override
    public List<String> getLocationNamesByMoneyChanger(final Long moneyChangerId) {
        return locationRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId)
                .stream()
                .map(MoneyChangerLocation::getLocationName)
                .collect(Collectors.toList());
    }
}
