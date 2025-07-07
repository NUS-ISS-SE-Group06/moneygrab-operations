package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerDTO;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerLocation;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerLocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;


@Service
public class MoneyChangerLocationServiceImpl implements MoneyChangerLocationService {

    private final MoneyChangerLocationRepository locationRepository;

    public MoneyChangerLocationServiceImpl(final MoneyChangerLocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<MoneyChangerDTO> getLocationsByMoneyChangerId(final Long id) {
        final List<MoneyChangerLocation> locations =
                locationRepository.findByMoneyChangerIdAndIsDeletedFalse(id);

        return locations.stream().map(loc -> {
            final MoneyChangerDTO dto = new MoneyChangerDTO();
            dto.setId(loc.getMoneyChanger().getId());
            dto.setLocations(
                    locations.stream().map(MoneyChangerLocation::getLocationName).toList()
            );
            return dto;
        }).toList();
    }

    @Override
    @Transactional
    public void add(
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

                    loc.setCreatedBy(0L);
                    loc.setUpdatedBy(0L);
                    return loc;
                })
                .toList();


        locationRepository.saveAll(newLocations);
    }

    @Override
    @Transactional
    public void delete(final Long locationId) {
        final MoneyChangerLocation location = locationRepository.findById(locationId)
                .orElseThrow(() -> new IllegalArgumentException("Location not found"));
        location.setIsDeleted(true);
        locationRepository.save(location);
    }

    @Override
    @Transactional
    public void save(final Long moneyChangerId, final List<String> locationNames) {
        final List<MoneyChangerLocation> existing =
                locationRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId);

        for (MoneyChangerLocation loc : existing) {
            loc.setIsDeleted(true);
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

                    // Use system default ID (e.g. service user, audit system, or null if not applicable)
                    loc.setCreatedBy(0L);
                    loc.setUpdatedBy(0L);
                    return loc;
                })
                .toList();


        locationRepository.saveAll(newLocations);
    }

    @Override
    public List<String> getLocationNamesByMoneyChangerId(final Long id) {
        return locationRepository.findByMoneyChangerIdAndIsDeletedFalse(id)
                .stream()
                .map(MoneyChangerLocation::getLocationName)
                .toList();
    }
}
