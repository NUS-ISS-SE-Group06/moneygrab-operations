package com.moola.fx.moneychanger.operations.service; 

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.repository.CommissionRateRepository;
import com.moola.fx.moneychanger.operations.repository.CompanyCommissionSchemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CommissionRateServiceImplTest {

    @Mock
    private CommissionRateRepository repository;

    @Mock
    private CompanyCommissionSchemeRepository companyCommissionSchemeRepository;


    @InjectMocks
    private CommissionRateServiceImpl service;

    private CommissionRate makeRate(Integer id, boolean deleted) {
        CommissionRate r = new CommissionRate();
        r.setId(id);
        r.setIsDeleted(deleted);
        r.setRate(new BigDecimal("1.00"));
        return r;
    }

    @BeforeEach
    void setUp() {
        // no-op, handled by @ExtendWith
    }

    @Test
    void listAll_filtersDeleted() {
        var a = makeRate(1, false);
        var b = makeRate(2, true);
        when(repository.findAll()).thenReturn(List.of(a, b));

        var result = service.listAll();

        assertEquals(1, result.size());
        assertFalse(result.get(0).getIsDeleted());
        verify(repository).findAll();
    }

    @Test
    void get_found_returnsEntity() {
        var entity = makeRate(10, false);
        when(repository.findByIdAndIsDeletedFalse(10)).thenReturn(Optional.of(entity));

        var result = service.get(10);

        assertEquals(10, result.getId());
        verify(repository).findByIdAndIsDeletedFalse(10);
    }

    @Test
    void get_notFound_throws() {
        when(repository.findByIdAndIsDeletedFalse(999)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.get(999));
        verify(repository).findByIdAndIsDeletedFalse(999);
    }

    @Test
    void save_new_success() {
        CommissionRate rate = new CommissionRate();
        rate.setId(null);
        CurrencyCode c = new CurrencyCode(); c.setId(1);
        Scheme s = new Scheme(); s.setId(2);
        rate.setCurrencyId(c);
        rate.setSchemeId(s);

        when(repository.existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(c, s)).thenReturn(false);
        when(repository.save(rate)).thenReturn(rate);

        var result = service.save(rate);

        assertSame(rate, result);
        verify(repository).existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(c, s);
        verify(repository).save(rate);
    }

    @Test
    void save_new_duplicate_throws() {
        CommissionRate rate = new CommissionRate();
        rate.setId(null);
        CurrencyCode c = new CurrencyCode(); c.setId(1);
        Scheme s = new Scheme(); s.setId(2);
        rate.setCurrencyId(c);
        rate.setSchemeId(s);

        when(repository.existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(c, s)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.save(rate));
        verify(repository, never()).save(any());
    }

    @Test
    void save_update_success() {
        CommissionRate rate = new CommissionRate();
        rate.setId(5);
        CurrencyCode c = new CurrencyCode(); c.setId(1);
        Scheme s = new Scheme(); s.setId(2);
        rate.setCurrencyId(c);
        rate.setSchemeId(s);

        when(repository.existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(c, s, 5)).thenReturn(false);
        when(repository.save(rate)).thenReturn(rate);

        var result = service.save(rate);

        assertSame(rate, result);
        verify(repository).existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(c, s, 5);
        verify(repository).save(rate);
    }

    @Test
    void save_update_duplicate_throws() {
        CommissionRate rate = new CommissionRate();
        rate.setId(6);
        CurrencyCode c = new CurrencyCode(); c.setId(1);
        Scheme s = new Scheme(); s.setId(2);
        rate.setCurrencyId(c);
        rate.setSchemeId(s);

        when(repository.existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(c, s, 6)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.save(rate));
        verify(repository, never()).save(any());
    }

    @Test
    void delete_success() {
        var entity = makeRate(5, false);

        Scheme scheme = new Scheme();
        scheme.setId(100);
        entity.setSchemeId(scheme);

        when(repository.findById(5)).thenReturn(Optional.of(entity));
        when(companyCommissionSchemeRepository.existsBySchemeId_IdAndIsDeletedFalse(100)).thenReturn(false);

        service.delete(5, 99);

        assertTrue(entity.getIsDeleted());
        assertEquals(99, entity.getUpdatedBy());
        verify(repository).save(entity);
        verify(companyCommissionSchemeRepository).existsBySchemeId_IdAndIsDeletedFalse(100);
    }


    @Test
    void delete_notFound_throws() {
        when(repository.findById(88)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(88, 77));
        verify(repository, never()).save(any());
    }

    @Test
    void findBySchemeId_returnsResults() {
        CommissionRate r1 = makeRate(1, false);
        CommissionRate r2 = makeRate(2, false);

        when(repository.findBySchemeIdIdAndIsDeletedFalse(200)).thenReturn(List.of(r1, r2));

        var result = service.findBySchemeId(200);

        assertEquals(2, result.size());
        verify(repository).findBySchemeIdIdAndIsDeletedFalse(200);
    }
}
