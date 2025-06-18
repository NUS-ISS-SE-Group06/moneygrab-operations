package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.repository.CurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CurrencyServiceImplTest {

    @Mock
    private CurrencyRepository repo;

    @InjectMocks
    private CurrencyServiceImpl service;

    @Test
    void listAll_returnsAllCurrencies() {
        CurrencyCode code1 = new CurrencyCode();
        code1.setId(1);
        CurrencyCode code2 = new CurrencyCode();
        code2.setId(2);

        when(repo.findAll()).thenReturn(Arrays.asList(code1, code2));

        List<CurrencyCode> result = service.listAll();

        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getId());
        assertEquals(2, result.get(1).getId());
    }

    @Test
    void get_existingId_returnsCurrencyCode() {
        CurrencyCode currency = new CurrencyCode();
        currency.setId(100);

        when(repo.findById(100)).thenReturn(Optional.of(currency));

        CurrencyCode result = service.get(100);

        assertNotNull(result);
        assertEquals(100, result.getId());
    }

    @Test
    void get_nonExistingId_throwsException() {
        when(repo.findById(999)).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.get(999));
        assertTrue(ex.getMessage().contains("999"));
    }

    @Test
    void getByCurrency_existingCurrency_returnsCurrencyCode() {
        CurrencyCode usd = new CurrencyCode();
        usd.setCurrency("USD");

        when(repo.findByCurrencyIgnoreCase("usd")).thenReturn(Optional.of(usd));

        CurrencyCode result = service.getByCurrency("usd");

        assertEquals("USD", result.getCurrency());
    }

    @Test
    void getByCurrency_notFound_throwsException() {
        when(repo.findByCurrencyIgnoreCase("XYZ")).thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(ResourceNotFoundException.class, () -> service.getByCurrency("XYZ"));
        assertTrue(ex.getMessage().contains("XYZ"));
    }
}
