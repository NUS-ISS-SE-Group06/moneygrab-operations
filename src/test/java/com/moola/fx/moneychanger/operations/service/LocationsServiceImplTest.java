package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.LocationDTO;
import com.moola.fx.moneychanger.operations.model.Locations;
import com.moola.fx.moneychanger.operations.repository.LocationRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationsServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationsServiceImpl locationsService;

    @Test
    void listAll_returnsAllLocations() {
        Locations locations1 = new Locations();
        locations1.setId(1);
        locations1.setLocationName("Tampines");
        locations1.setCountryCode("SG");
        locations1.setIsDeleted(false);

        Locations locations2 = new Locations();
        locations2.setId(2);
        locations2.setLocationName("Haw Par Villa");
        locations2.setCountryCode("SG");
        locations2.setIsDeleted(false);

        Locations locations3 = new Locations();
        locations3.setId(3);
        locations3.setLocationName("Chennai");
        locations3.setCountryCode("IN");
        locations3.setIsDeleted(false);

        when(locationRepository.findAll()).thenReturn(Arrays.asList(locations1, locations2, locations3));
        List<LocationDTO> result = locationsService.listAllLocations();

        assertEquals(3, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
        assertEquals(3, result.get(2).getId());
    }

    @Test
    void listAll_returnsLocationsForACountry() {
        Locations locations3 = new Locations();
        locations3.setId(3);
        locations3.setLocationName("Chennai");
        locations3.setCountryCode("IN");
        locations3.setIsDeleted(false);

        when(locationRepository.findByCountryCodeAndIsDeletedFalse("IN")).thenReturn(Arrays.asList(locations3));
        List<LocationDTO> result = locationsService.findByCountryCode("IN");

        assertEquals(1, result.size());
        assertEquals(3, result.get(0).getId());
        assertEquals("Chennai", result.get(0).getLocationName());
    }

}
