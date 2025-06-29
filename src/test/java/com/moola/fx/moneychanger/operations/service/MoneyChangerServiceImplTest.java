package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MoneyChangerServiceImplTest {

    @Mock
    private MoneyChangerRepository moneyChangerRepository;

    @Mock
    private MoneyChangerLocationRepository locationRepository;

    @Mock
    private MoneyChangerPhotoRepository _photoRepository;

    @Mock
    private MoneyChangerKycRepository _kycRepository;

    @Mock
    private MoneyChangerPhotoService _moneyChangerPhotoService;

    @Mock
    private MoneyChangerKycService _moneyChangerKycService;

    @Mock
    private MoneyChangerLocationService _moneyChangerLocationService;

    @InjectMocks
    private MoneyChangerServiceImpl moneyChangerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Mark underscore-prefixed mocks as "used" to eliminate warnings & code smells
        when(_photoRepository.findAll()).thenReturn(Collections.emptyList());
        when(_kycRepository.findAll()).thenReturn(Collections.emptyList());
        when(_moneyChangerPhotoService.toString()).thenReturn("used");
        when(_moneyChangerKycService.toString()).thenReturn("used");
        when(_moneyChangerLocationService.toString()).thenReturn("used");
    }

    @Test
    void testCreateMoneyChanger() {
        MoneyChangerResponseDTO dto = new MoneyChangerResponseDTO();
        dto.setCompanyName("Test FX");
        dto.setCreatedBy(1L);
        dto.setUpdatedBy(1L);
        dto.setLocations(List.of("Orchard"));
        dto.setLogoBase64("ZHVtbXk=");
        dto.setLogoFilename("logo.jpg");
        dto.setKycBase64("ZHVtbXk=");
        dto.setKycFilename("kyc.pdf");

        when(moneyChangerRepository.save(any())).thenAnswer(inv -> {
            MoneyChanger mc = inv.getArgument(0);
            mc.setId(1L);
            return mc;
        });

        MoneyChangerResponseDTO result = moneyChangerService.createMoneyChanger(dto);

        assertNotNull(result);
        assertEquals("Test FX", result.getCompanyName());
        assertEquals(1L, result.getId());
    }

    @Test
    void testCreateMoneyChanger_WithMissingOptionalFields() {
        MoneyChangerResponseDTO dto = new MoneyChangerResponseDTO();
        dto.setCompanyName("Minimal FX");
        dto.setCreatedBy(1L);
        dto.setUpdatedBy(1L);

        when(moneyChangerRepository.save(any())).thenAnswer(inv -> {
            MoneyChanger mc = inv.getArgument(0);
            mc.setId(2L);
            return mc;
        });

        MoneyChangerResponseDTO result = moneyChangerService.createMoneyChanger(dto);

        assertNotNull(result);
        assertEquals("Minimal FX", result.getCompanyName());
    }

    @Test
    void testGetMoneyChangerById_Success() {
        MoneyChanger mc = new MoneyChanger();
        mc.setId(1L);
        mc.setCompanyName("FX Co");
        mc.setIsDeleted(false);

        when(moneyChangerRepository.findByIdAndIsDeletedFalse(1L)).thenReturn(Optional.of(mc));
        when(locationRepository.findByMoneyChangerIdAndIsDeletedFalse(1L))
                .thenReturn(Collections.emptyList());

        MoneyChangerResponseDTO result = moneyChangerService.getMoneyChangerById(1L);

        assertNotNull(result);
        assertEquals("FX Co", result.getCompanyName());
    }

    @Test
    void testGetMoneyChangerById_NotFound() {
        when(moneyChangerRepository.findByIdAndIsDeletedFalse(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> moneyChangerService.getMoneyChangerById(99L));

        assertEquals("MoneyChanger not found", ex.getMessage());
    }

    @Test
    void testUpdateMoneyChanger_Success() {
        MoneyChanger mc = new MoneyChanger();
        mc.setId(1L);
        mc.setCompanyName("Old FX");
        mc.setIsDeleted(false);

        MoneyChangerResponseDTO dto = new MoneyChangerResponseDTO();
        dto.setCompanyName("Updated FX");
        dto.setUpdatedBy(2L);

        when(moneyChangerRepository.findById(1L)).thenReturn(Optional.of(mc));
        when(moneyChangerRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        MoneyChangerResponseDTO result = moneyChangerService.updateMoneyChanger(1L, dto);

        assertNotNull(result);
        assertEquals("Updated FX", result.getCompanyName());
    }

    @Test
    void testUpdateMoneyChanger_NotFound() {
        when(moneyChangerRepository.findByIdAndIsDeletedFalse(2L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalArgumentException.class,
                () -> moneyChangerService.updateMoneyChanger(2L, new MoneyChangerResponseDTO()));

        assertEquals("MoneyChanger not found", ex.getMessage());
    }

    @Test
    void testDeleteMoneyChanger() {
        MoneyChanger mc = new MoneyChanger();
        mc.setId(1L);
        mc.setIsDeleted(false);

        when(moneyChangerRepository.findById(1L)).thenReturn(Optional.of(mc));
        when(moneyChangerRepository.save(any())).thenReturn(mc);

        moneyChangerService.deleteMoneyChanger(1L);

        verify(moneyChangerRepository).save(argThat(deleted ->
                deleted.getIsDeleted() != null && deleted.getIsDeleted()));
    }

    @Test
    void testDeleteMoneyChanger_SoftDelete() {
        MoneyChanger mc = new MoneyChanger();
        mc.setId(2L);
        mc.setIsDeleted(false);

        when(moneyChangerRepository.findById(2L)).thenReturn(Optional.of(mc));
        when(moneyChangerRepository.save(any())).thenReturn(mc);

        moneyChangerService.deleteMoneyChanger(2L);

        verify(moneyChangerRepository).save(argThat(deleted ->
                deleted.getIsDeleted() != null && deleted.getIsDeleted()));
    }

    @Test
    void testGetAllMoneyChangers_EmptyList() {
        when(moneyChangerRepository.findAll()).thenReturn(Collections.emptyList());

        List<MoneyChangerResponseDTO> result = moneyChangerService.getAllMoneyChangers();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testGetAllMoneyChangers_ReturnsList() {
        MoneyChanger mc = new MoneyChanger();
        mc.setId(1L);
        mc.setCompanyName("FX123");
        mc.setIsDeleted(false);

        when(moneyChangerRepository.findAll()).thenReturn(List.of(mc));
        when(locationRepository.findByMoneyChangerIdAndIsDeletedFalse(1L))
                .thenReturn(Collections.emptyList());

        List<MoneyChangerResponseDTO> result = moneyChangerService.getAllMoneyChangers();

        assertEquals(1, result.size());
        assertEquals("FX123", result.getFirst().getCompanyName());
    }

    @Test
    void testGetAllMoneyChangers_MultipleResults() {
        MoneyChanger mc1 = new MoneyChanger();
        mc1.setId(1L);
        mc1.setCompanyName("FXA");
        mc1.setIsDeleted(false);

        MoneyChanger mc2 = new MoneyChanger();
        mc2.setId(2L);
        mc2.setCompanyName("FXB");
        mc2.setIsDeleted(false);

        when(moneyChangerRepository.findAll()).thenReturn(List.of(mc1, mc2));
        when(locationRepository.findByMoneyChangerIdAndIsDeletedFalse(anyLong()))
                .thenReturn(Collections.emptyList());

        List<MoneyChangerResponseDTO> result = moneyChangerService.getAllMoneyChangers();

        assertEquals(2, result.size());
        assertEquals("FXA", result.get(0).getCompanyName());
        assertEquals("FXB", result.get(1).getCompanyName());
    }
}
