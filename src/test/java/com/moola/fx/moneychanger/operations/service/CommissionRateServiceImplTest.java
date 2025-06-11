package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.repository.CommissionRateRepository;
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
        // nothing needed—MockitoExtension does init
    }

    @Test
    void listAll_filtersDeleted() {
        CommissionRate a = makeRate(1, false);
        CommissionRate b = makeRate(2, true);
        when(repository.findAll()).thenReturn(List.of(a, b));

        List<CommissionRate> out = service.listAll();

        assertEquals(1, out.size());
        assertFalse(out.get(0).getIsDeleted());
        verify(repository, times(1)).findAll();
    }



    @Test
    void save_new_Success() {
        // id == null path
        CommissionRate r = new CommissionRate();
        r.setId(null);
        CurrencyCode c = new CurrencyCode(); c.setId(1);
        Scheme s = new Scheme(); s.setId(2);
        r.setCurrencyId(c);
        r.setSchemeId(s);

        when(repository.existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(c, s)).thenReturn(false);
        when(repository.save(r)).thenReturn(r);

        CommissionRate out = service.save(r);
        assertSame(r, out);

        verify(repository).existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(c, s);
        verify(repository).save(r);
    }

    @Test
    void save_new_Duplicate_throws() {
        CommissionRate r = new CommissionRate();
        r.setId(null);
        CurrencyCode c = new CurrencyCode(); c.setId(1);
        Scheme s = new Scheme(); s.setId(2);
        r.setCurrencyId(c);
        r.setSchemeId(s);

        when(repository.existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(c, s)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.save(r));
        verify(repository).existsByCurrencyIdAndSchemeIdAndIsDeletedFalse(c, s);
        verify(repository, never()).save(any());
    }

    @Test
    void save_update_Success() {
        // id != null path
        CommissionRate r = new CommissionRate();
        r.setId(5);
        CurrencyCode c = new CurrencyCode(); c.setId(1);
        Scheme s = new Scheme(); s.setId(2);
        r.setCurrencyId(c);
        r.setSchemeId(s);

        when(repository.existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(c, s, 5))
                .thenReturn(false);
        when(repository.save(r)).thenReturn(r);

        CommissionRate out = service.save(r);
        assertSame(r, out);

        verify(repository).existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(c, s, 5);
        verify(repository).save(r);
    }

    @Test
    void save_update_Duplicate_throws() {
        CommissionRate r = new CommissionRate();
        r.setId(7);
        CurrencyCode c = new CurrencyCode(); c.setId(1);
        Scheme s = new Scheme(); s.setId(2);
        r.setCurrencyId(c);
        r.setSchemeId(s);

        when(repository.existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(c, s, 7))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.save(r));
        verify(repository).existsByCurrencyIdAndSchemeIdAndIdNotAndIsDeletedFalse(c, s, 7);
        verify(repository, never()).save(any());
    }

    @Test
    void delete_Success() {
        CommissionRate r = makeRate(3, false);
        when(repository.findById(3)).thenReturn(Optional.of(r));

        service.delete(3, 99);

        assertTrue(r.getIsDeleted());
        assertEquals(99, r.getUpdatedBy());
        verify(repository).save(r);
    }

    @Test
    void delete_NotFound_throws() {
        when(repository.findById(4)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(4, 99));
        verify(repository, never()).save(any());
    }

    @Test
    void findBySchemeId_returnsOnlyActive() {
        int schemeId = 20;
        CommissionRate a = makeRate(1, false);
        CommissionRate b = makeRate(2, false);
        when(repository.findBySchemeIdIdAndIsDeletedFalse(schemeId))
                .thenReturn(List.of(a, b));

        List<CommissionRate> out = service.findBySchemeId(schemeId);
        assertEquals(2, out.size());
        assertFalse(out.get(0).getIsDeleted());
        assertFalse(out.get(1).getIsDeleted());
        verify(repository).findBySchemeIdIdAndIsDeletedFalse(schemeId);
    }

    @Test
    void get_notFound_throws() {
        // stub the method the service actually calls:
        when(repository.findByIdAndIsDeletedFalse(5))
                .thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.get(5));
        verify(repository).findByIdAndIsDeletedFalse(5);
    }

    @Test
    void get_found() {
        CommissionRate r = makeRate(10, false);
        // stub the correct lookup:
        when(repository.findByIdAndIsDeletedFalse(10))
                .thenReturn(Optional.of(r));

        CommissionRate out = service.get(10);
        assertEquals(10, out.getId());
        verify(repository).findByIdAndIsDeletedFalse(10);
    }

}
