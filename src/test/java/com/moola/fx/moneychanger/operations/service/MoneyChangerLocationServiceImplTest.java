package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerLocation;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerLocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

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

    @Test
    void testAddLocation() {
        Long moneyChangerId = 1L;
        List<String> locations = Arrays.asList("Jurong", "Bishan");
        Long createdBy = 1L;
        Long updatedBy = 1L;

        locationService.addLocation(moneyChangerId, locations, createdBy, updatedBy);

        verify(locationRepository).saveAll(argThat(argument -> {
            if (argument instanceof List<?>) {
                List<?> list = (List<?>) argument;
                return list.size() == 2 &&
                        list.get(0) instanceof MoneyChangerLocation &&
                        ((MoneyChangerLocation) list.get(0)).getLocationName().equals("Jurong") &&
                        ((MoneyChangerLocation) list.get(1)).getLocationName().equals("Bishan");
            }
            return false;
        }));
    }

    @Test
    void testAddLocation_EmptyList() {
        locationService.addLocation(1L, Collections.emptyList(), 1L, 1L);
        verify(locationRepository, never()).saveAll(any());
    }

    @Test
    void testAddLocation_NullList() {
        locationService.addLocation(1L, null, 1L, 1L);
        verify(locationRepository, never()).saveAll(any());
    }

    @Test
    void testDeleteLocation() {
        Long locationId = 101L;
        MoneyChangerLocation loc = new MoneyChangerLocation();
        loc.setId(locationId);
        loc.setIsDeleted(false);

        when(locationRepository.findById(locationId)).thenReturn(java.util.Optional.of(loc));

        locationService.deleteLocation(locationId);

        verify(locationRepository).save(argThat(l -> Boolean.TRUE.equals(l.getIsDeleted())));
    }
}
