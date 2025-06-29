package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.MoneyChangerCurrency;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerCurrencyRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
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
        MoneyChangerCurrency entity = new MoneyChangerCurrency();
        when(repo.existsByMoneyChangerIdAndCurrencyIdAndIsDeletedFalse(any(), any())).thenReturn(false);
        when(repo.save(entity)).thenReturn(entity);
        assertEquals(entity, service.save(entity));
    }

    @Test
    void testDelete_Existing() {
        MoneyChangerCurrency entity = new MoneyChangerCurrency();
        when(repo.findById(1)).thenReturn(Optional.of(entity));
        service.delete(1, 100);
        assertTrue(entity.getIsDeleted());
        assertEquals(100, entity.getUpdatedBy());
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
