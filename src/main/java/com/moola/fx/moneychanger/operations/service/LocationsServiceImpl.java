package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.LocationDTO;
import com.moola.fx.moneychanger.operations.model.Locations;
import com.moola.fx.moneychanger.operations.repository.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LocationsServiceImpl implements LocationsService{

    private final LocationRepository locationRepository;

    public LocationsServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public List<LocationDTO> listAllLocations() {
        // Implementation to list all locations
        return locationRepository.findAll().stream()
                .filter(location -> Boolean.FALSE.equals(location.getIsDeleted()))
                .map(this::toLocationDTO)
                .toList();
    }

    @Override
    public List<LocationDTO> findByCountryCode(String countryCode) {
        // Implementation to find locations by country code
        return locationRepository.findAll().stream()
                .filter(location -> Boolean.FALSE.equals(location.getIsDeleted()))
                .filter(location -> countryCode.equals(location.getCountryCode()))
                .map(this::toLocationDTO)
                .toList();
    }

    private LocationDTO toLocationDTO(Locations locationEntity) {
        LocationDTO locationDTO = new LocationDTO();
        locationDTO.setId(locationEntity.getId());
        locationDTO.setLocationName(locationEntity.getLocationName());
        locationDTO.setCountryCode(locationEntity.getCountryCode());
        return locationDTO;
    }
}
