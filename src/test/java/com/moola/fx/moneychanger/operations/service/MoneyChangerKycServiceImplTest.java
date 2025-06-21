package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.repository.MoneyChangerKycRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;


import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoneyChangerKycServiceImplTest {

    @Mock
    private MoneyChangerKycRepository kycRepository;

    @InjectMocks
    private MoneyChangerKycServiceImpl kycService;

    private static final Long TEST_ID = 1L;

    @Test
    void testSaveKyc_WithEmptyBase64_ShouldReturn() {
        kycService.saveOrUpdate(TEST_ID, "", "test.pdf");
        verify(kycRepository, never()).save(any());
    }
}
