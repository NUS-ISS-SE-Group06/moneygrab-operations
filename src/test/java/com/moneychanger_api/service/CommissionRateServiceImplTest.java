package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.model.CurrencyCode;
import com.moneychanger_api.model.Scheme;
import com.moneychanger_api.repository.CommissionRateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
class CommissionRateServiceImplTest {

    @Mock
    private CommissionRateRepository repository;

    private CommissionRateServiceImpl service;

    @BeforeEach
    public void init() {

        MockitoAnnotations.openMocks(this);
        service = new CommissionRateServiceImpl(repository);
    }

    @Test
    void testListAll() {
        CommissionRate item = new CommissionRate();
        item.setIsDeleted(false); // Simulate active record
        when(repository.findAll()).thenReturn(List.of(item));

        List<CommissionRate> result = service.listAll();
        assertEquals(1, result.size());
    }

    @Test
    void testGet_Found() {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(item));
        assertEquals(1, service.get(1).getId());
    }

    @Test
    void testGet_NotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.get(1));
    }

    @Test
    void testSave_New() {
        CommissionRate item = new CommissionRate();
        item.setRate(new BigDecimal("5.0"));

        CurrencyCode currency = new CurrencyCode();
        currency.setId(1);
        item.setCurrencyId(currency);

        Scheme scheme = new Scheme();
        scheme.setId(2);
        item.setSchemeId(scheme);

        when(repository.existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(currency, scheme, 1)).thenReturn(false);
        when(repository.save(item)).thenReturn(item);

        CommissionRate saved = service.save(item);
        assertEquals(new BigDecimal("5.0"), saved.getRate());
    }

    @Test
    void testSave_Duplicate() {
        CommissionRate item = new CommissionRate();
        item.setId(1); // Important: Set the ID for the duplicate check

        CurrencyCode currency = new CurrencyCode();
        currency.setId(1);
        item.setCurrencyId(currency);

        Scheme scheme = new Scheme();
        scheme.setId(2);
        item.setSchemeId(scheme);

        when(repository.existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(currency, scheme, 1)).thenReturn(true);

        Assertions.assertThrows(DuplicateResourceException.class, () -> service.save(item));
        verify(repository, never()).save(any());
    }


    @Test
    void testDelete_Success() {
        CommissionRate existing = new CommissionRate();
        existing.setId(1);
        existing.setIsDeleted(false);

        when(repository.findById(1)).thenReturn(Optional.of(existing));

        service.delete(1);

        Assertions.assertTrue(existing.getIsDeleted());
        verify(repository).save(existing);
    }

    @Test
    void testDelete_NotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(1));
    }

    @Test
    void testFindBySchemeId_ReturnsActiveCommissionRates() {
        int schemeId = 200;

        CommissionRate activeRate1 = new CommissionRate();
        activeRate1.setId(1);
        activeRate1.setIsDeleted(false);

        CommissionRate activeRate2 = new CommissionRate();
        activeRate2.setId(2);
        activeRate2.setIsDeleted(false);

        List<CommissionRate> mockRates = List.of(activeRate1, activeRate2);

        when(repository.findBySchemeIdIdAndIsDeletedFalse(schemeId)).thenReturn(mockRates);

        List<CommissionRate> result = service.findBySchemeId(schemeId);

        Assertions.assertEquals(2, result.size());
        Assertions.assertFalse(result.get(0).getIsDeleted());
        Assertions.assertFalse(result.get(1).getIsDeleted());
        verify(repository, times(1)).findBySchemeIdIdAndIsDeletedFalse(schemeId);
    }




}