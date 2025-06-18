package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyChangerServiceImplTest {

    @Mock
    private MoneyChangerRepository moneyChangerRepository;

    @InjectMocks
    private MoneyChangerServiceImpl moneyChangerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetMoneyChangerById_shouldThrow_ifNotFound() {
        Long mockId = 999L;
        when(moneyChangerRepository.findByIdAndIsDeletedFalse(mockId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> moneyChangerService.getMoneyChangerById(mockId)
        );

        assertEquals("MoneyChanger not found", exception.getMessage());
        verify(moneyChangerRepository, times(1)).findByIdAndIsDeletedFalse(mockId);
    }
}
