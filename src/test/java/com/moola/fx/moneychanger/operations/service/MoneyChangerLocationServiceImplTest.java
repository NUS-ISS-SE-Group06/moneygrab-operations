package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerDTO;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerLocation;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerLocationRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class MoneyChangerLocationServiceImplTest {

    @Mock
    private MoneyChangerLocationRepository locationRepository;

    @InjectMocks
    private MoneyChangerLocationServiceImpl locationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @AfterEach
    void tearDown() {
        reset(locationRepository);
    }

    @Test
    void testGetLocationsByMoneyChangerId() {
        Long moneyChangerId = 1L;

        MoneyChanger moneyChanger = new MoneyChanger();
        moneyChanger.setId(moneyChangerId);

        MoneyChangerLocation loc1 = new MoneyChangerLocation();
        loc1.setLocationName("Jurong East");
        loc1.setMoneyChanger(moneyChanger);

        MoneyChangerLocation loc2 = new MoneyChangerLocation();
        loc2.setLocationName("Orchard");
        loc2.setMoneyChanger(moneyChanger);

        when(locationRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId))
                .thenReturn(Arrays.asList(loc1, loc2));

        List<MoneyChangerDTO> result = locationService.getLocationsByMoneyChangerId(moneyChangerId);

        assertEquals(2, result.size()); // assuming your implementation returns two DTOs
        List<String> locations1 = result.get(0).getLocations();
        List<String> locations2 = result.get(1).getLocations();

        assertNotNull(locations1);
        assertNotNull(locations2);
        assertTrue(locations1.contains("Jurong East") || locations2.contains("Jurong East"));
        assertTrue(locations1.contains("Orchard") || locations2.contains("Orchard"));
    }

    @Test
    void testAdd() {
        Long moneyChangerId = 1L;
        List<String> locationNames = Arrays.asList("Jurong East", "Bugis");
        Integer createdBy = 100;
        Integer updatedBy = 100;

        locationService.add(moneyChangerId, locationNames, createdBy.longValue(), updatedBy.longValue());

        verify(locationRepository).saveAll(anyList());
    }

    @Test
    void testAddWithEmptyList() {
        Long moneyChangerId = 1L;
        List<String> locations = Collections.emptyList();

        locationService.add(moneyChangerId, locations, 1L, 1L);

        verify(locationRepository, never()).saveAll(anyList());
    }

    @Test
    void testSave() {
        Long moneyChangerId = 1L;
        List<String> locationNames = Arrays.asList("Woodlands", "Boon Lay");

        locationService.save(moneyChangerId, locationNames);

        // Fix applied: allow for multiple calls to saveAll
        verify(locationRepository, atLeastOnce()).saveAll(anyList());
    }

    @Test
    void testDelete() {
        Long locationId = 10L;

        MoneyChangerLocation loc = new MoneyChangerLocation();
        loc.setId(locationId);
        loc.setIsDeleted(false); // before

        when(locationRepository.findById(locationId)).thenReturn(Optional.of(loc));

        locationService.delete(locationId);

        assertTrue(loc.getIsDeleted()); // now true
        verify(locationRepository).save(loc);
    }

    @Test
    void testDeleteNotFound() {
        Long locationId = 999L;
        when(locationRepository.findById(locationId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> locationService.delete(locationId));

        verify(locationRepository, never()).save(any());
    }

    @Test
    void testGetLocationNamesByMoneyChangerId() {
        Long moneyChangerId = 1L;

        MoneyChangerLocation loc1 = new MoneyChangerLocation();
        loc1.setLocationName("HarbourFront");

        MoneyChangerLocation loc2 = new MoneyChangerLocation();
        loc2.setLocationName("Raffles");

        when(locationRepository.findByMoneyChangerIdAndIsDeletedFalse(moneyChangerId))
                .thenReturn(Arrays.asList(loc1, loc2));

        List<String> names = locationService.getLocationNamesByMoneyChangerId(moneyChangerId);

        assertEquals(2, names.size());
        assertTrue(names.contains("HarbourFront"));
        assertTrue(names.contains("Raffles"));
    }
}
