package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChangerKyc;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerKycRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyChangerKycServiceImplTest {

    @InjectMocks
    private MoneyChangerKycServiceImpl kycService;

    @Mock
    private MoneyChangerKycRepository kycRepository;

    @Mock
    private MultipartFile mockFile;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetByMoneyChangerId() {
        MoneyChangerKyc kyc = new MoneyChangerKyc();
        kyc.setId(1L);
        kyc.setMoneyChangerId(100L);
        kyc.setDocumentFilename("test.pdf");

        when(kycRepository.findFirstByMoneyChangerIdAndIsDeletedFalse(100L)).thenReturn(Optional.of(kyc));

        MoneyChangerKyc result = kycService.getByMoneyChangerId(100L);
        assertNotNull(result);
        assertEquals("test.pdf", result.getDocumentFilename());

        verify(kycRepository, times(1)).findFirstByMoneyChangerIdAndIsDeletedFalse(100L);
    }

    @Test
    void testSaveOrUpdate_New() throws Exception {
        when(kycRepository.findFirstByMoneyChangerIdAndIsDeletedFalse(100L)).thenReturn(Optional.empty());
        when(mockFile.getBytes()).thenReturn("test data".getBytes());
        when(mockFile.getOriginalFilename()).thenReturn("test.pdf");
        when(mockFile.getContentType()).thenReturn("application/pdf");

        kycService.saveOrUpdate(100L, mockFile);

        verify(kycRepository, times(1)).save(any(MoneyChangerKyc.class));
    }

    @Test
    void testSaveOrUpdate_UpdateExisting() throws Exception {
        MoneyChangerKyc existingKyc = new MoneyChangerKyc();
        existingKyc.setId(1L);
        existingKyc.setMoneyChangerId(100L);
        existingKyc.setDocumentFilename("old.pdf");

        when(kycRepository.findFirstByMoneyChangerIdAndIsDeletedFalse(100L)).thenReturn(Optional.of(existingKyc));
        when(mockFile.getBytes()).thenReturn("new data".getBytes());
        when(mockFile.getOriginalFilename()).thenReturn("new.pdf");
        when(mockFile.getContentType()).thenReturn("application/pdf");

        kycService.saveOrUpdate(100L, mockFile);

        verify(kycRepository, times(1)).save(existingKyc);
        assertEquals("new.pdf", existingKyc.getDocumentFilename());
    }
}
