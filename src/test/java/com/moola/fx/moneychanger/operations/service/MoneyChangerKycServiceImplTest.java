package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerKyc;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerKycRepository;
import org.apache.tika.Tika;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyChangerKycServiceImplTest {

    @Mock
    private MoneyChangerKycRepository kycRepository;

    @Mock
    private Tika tika;

    @InjectMocks
    private MoneyChangerKycServiceImpl kycService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByMoneyChangerId_WhenFound() {
        Long id = 1L;
        MoneyChangerKyc mockKyc = new MoneyChangerKyc();
        when(kycRepository.findByMoneyChangerIdAndIsDeletedFalse(id)).thenReturn(Optional.of(mockKyc));

        Optional<MoneyChangerKyc> result = kycService.getByMoneyChangerId(id);

        assertTrue(result.isPresent());
        assertEquals(mockKyc, result.get());
        verify(kycRepository).findByMoneyChangerIdAndIsDeletedFalse(id);
    }

    @Test
    void testGetByMoneyChangerId_WhenNotFound() {
        Long id = 1L;
        when(kycRepository.findByMoneyChangerIdAndIsDeletedFalse(id)).thenReturn(Optional.empty());

        Optional<MoneyChangerKyc> result = kycService.getByMoneyChangerId(id);

        assertFalse(result.isPresent());
        verify(kycRepository).findByMoneyChangerIdAndIsDeletedFalse(id);
    }

    @Test
    void testSaveOrUpdate_WithBase64Kyc() {
        Long id = 1L;
        String base64 = "ZmlsZSBjb250ZW50cw==";
        String filename = "kyc.pdf";

        // Existing record that should be soft-deleted
        MoneyChangerKyc existing = new MoneyChangerKyc();
        when(kycRepository.findByMoneyChangerIdAndIsDeletedFalse(id)).thenReturn(Optional.of(existing));
        when(tika.detect(any(byte[].class))).thenReturn("application/pdf");

        kycService.saveOrUpdate(id, base64, filename);

        // Expect 2 saves: one for soft-deletion, one for the new KYC
        verify(kycRepository, times(2)).save(any(MoneyChangerKyc.class));
    }

    @Test
    void testSaveOrUpdate_EmptyBase64() {
        kycService.saveOrUpdate(1L, "", "test.pdf");
        verify(kycRepository, never()).save(any());
    }
}
