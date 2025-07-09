package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.*;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerCurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MoneyChangerCurrencyImplTest {

    @Mock private MoneyChangerCurrencyRepository repo;
    @Mock private ComputeRateService computeRateService;
    @Mock private CurrencyService currencyService;

    @InjectMocks private MoneyChangerCurrencyServiceImpl service;

    @Test
    void testListAll() {
        when(repo.findByIsDeletedFalse()).thenReturn(List.of(new MoneyChangerCurrency()));
        assertEquals(1, service.listAll().size());
    }

    @Test
    void testGet_Existing() {
        MoneyChangerCurrency currency = new MoneyChangerCurrency();
        when(repo.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(currency));
        assertEquals(currency, service.get(1));
    }

    @Test
    void testGet_NotFound() {
        when(repo.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.get(1));
    }

    @Test
    void testSave_Duplicate() {
        MoneyChangerCurrency entity = new MoneyChangerCurrency();
        entity.setId(1);
        when(repo.existsByMoneyChangerIdAndCurrencyIdAndIdNotAndIsDeletedFalse(any(), any(), anyInt())).thenReturn(true);
        assertThrows(DuplicateResourceException.class, () -> service.save(entity));
    }

    @Test
    void testSave_NewEntity() {
        // Arrange
        MoneyChangerCurrency entity = new MoneyChangerCurrency();

        CurrencyCode currency = new CurrencyCode();
        currency.setId(1);
        currency.setCurrency("USD");

        MoneyChanger moneyChanger = new MoneyChanger();
        moneyChanger.setId(5L);

        entity.setCurrencyId(currency);
        entity.setMoneyChangerId(moneyChanger);
        entity.setCreatedBy(99);

        when(repo.existsByMoneyChangerIdAndCurrencyIdAndIsDeletedFalse(any(), any())).thenReturn(false);
        when(repo.save(entity)).thenReturn(entity);
        when(currencyService.get(1)).thenReturn(currency); // 🔥 Mock currency lookup
        when(computeRateService.get("USD", 5L)).thenThrow(new ResourceNotFoundException("Not Found"));

        // Act
        MoneyChangerCurrency result = service.save(entity);

        // Assert
        assertEquals(entity, result);
        verify(repo).save(entity);
        verify(currencyService).get(1);

        // Verify ComputeRate creation
        ArgumentCaptor<List<ComputeRate>> captor = ArgumentCaptor.forClass(List.class);
        verify(computeRateService).saveAll(captor.capture());

        List<ComputeRate> savedRates = captor.getValue();
        assertEquals(1, savedRates.size());

        ComputeRate savedRate = savedRates.get(0);
        assertEquals("USD", savedRate.getId().getCurrencyCode());
        assertEquals(5L, savedRate.getId().getMoneyChangerId());
        assertEquals(99, savedRate.getProcessedBy());
    }

    @Test
    void testDelete_Existing() {
        // Arrange
        MoneyChangerCurrency entity = new MoneyChangerCurrency();

        CurrencyCode currency = new CurrencyCode();
        currency.setCurrency("USD");

        MoneyChanger moneyChanger = new MoneyChanger();
        moneyChanger.setId(5L);

        entity.setCurrencyId(currency);
        entity.setMoneyChangerId(moneyChanger);
        entity.setIsDeleted(false);

        when(repo.findById(1)).thenReturn(Optional.of(entity));

        ComputeRateId rateId = new ComputeRateId();
        rateId.setCurrencyCode("USD");
        rateId.setMoneyChangerId(5L);

        ComputeRate rate = new ComputeRate();
        rate.setId(rateId);

        when(computeRateService.findByMoneyChangerId(5L)).thenReturn(List.of(rate));

        // Act
        service.delete(1, 100);

        // Assert
        assertTrue(entity.getIsDeleted());
        assertEquals(100, entity.getUpdatedBy());
        verify(repo).save(entity);
        verify(computeRateService).delete("USD", 5L);
    }

    @Test
    void testDelete_NotFound() {
        when(repo.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(1, 100));
    }

    @Test
    void testFindByMoneyChangerId() {
        when(repo.findByMoneyChangerId_idAndIsDeletedFalse(1L)).thenReturn(List.of(new MoneyChangerCurrency()));
        assertEquals(1, service.findByMoneyChangerId(1).size());
    }
}
