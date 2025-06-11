package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.CompanyCommissionSchemeRepository;
import com.moola.fx.moneychanger.operations.repository.CommissionRateRepository;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import org.junit.jupiter.api.BeforeEach;
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
class CompanyCommissionSchemeServiceImplTest {

    @Mock
    private CompanyCommissionSchemeRepository repository;

    @Mock
    private MoneyChangerRepository moneyChangerRepository;

    @Mock
    private CommissionRateRepository commissionRateRepository;

    @InjectMocks
    private CompanyCommissionSchemeServiceImpl service;

    private CompanyCommissionScheme makeScheme(Integer id, boolean deleted) {
        var s = new CompanyCommissionScheme();
        s.setId(id);
        s.setIsDeleted(deleted);
        return s;
    }

    @BeforeEach
    void setUp() {
        // MockitoExtension handles init
    }

    @Test
    void listAll_filtersDeleted() {
        var a = makeScheme(1, false);
        var b = makeScheme(2, true);
        when(repository.findAll()).thenReturn(List.of(a, b));

        var out = service.listAll();
        assertEquals(1, out.size());
        assertFalse(out.get(0).getIsDeleted());
        verify(repository).findAll();
    }

    @Test
    void get_success() {
        var item = makeScheme(1, false);
        when(repository.findById(1)).thenReturn(Optional.of(item));

        var out = service.get(1);
        assertEquals(1, out.getId());
        verify(repository).findById(1);
    }

    @Test
    void get_deleted_throws() {
        var item = makeScheme(1, true);
        when(repository.findById(1)).thenReturn(Optional.of(item));

        assertThrows(ResourceNotFoundException.class, () -> service.get(1));
        verify(repository).findById(1);
    }

    @Test
    void get_notFound_throws() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.get(1));
        verify(repository).findById(1);
    }

    @Test
    void save_success() {
        var scheme = new CompanyCommissionScheme();

        var mc = new MoneyChanger();
        mc.setId(10L);
        scheme.setMoneyChangerId(mc);

        var cr = new CommissionRate();
        cr.setId(20);
        scheme.setCommissionRateId(cr);

        // both exist
        when(moneyChangerRepository.existsById(10L)).thenReturn(true);
        when(commissionRateRepository.existsById(20)).thenReturn(true);
        // no duplicate
        when(repository.existsByMoneyChangerId_IdAndCommissionRateId_Id(10L, 20)).thenReturn(false);
        when(repository.save(scheme)).thenReturn(scheme);

        var saved = service.save(scheme);
        assertSame(scheme, saved);
        verify(moneyChangerRepository).existsById(10L);
        verify(commissionRateRepository).existsById(20);
        verify(repository).existsByMoneyChangerId_IdAndCommissionRateId_Id(10L, 20);
        verify(repository).save(scheme);
    }

    @Test
    void save_nullIds_throwsIllegalArgument() {
        var scheme = new CompanyCommissionScheme();
        // moneyChangerId null
        scheme.setMoneyChangerId(null);
        scheme.setCommissionRateId(new CommissionRate());
        assertThrows(IllegalArgumentException.class, () -> service.save(scheme));

        // commissionRateId null
        scheme.setMoneyChangerId(new MoneyChanger());
        scheme.setCommissionRateId(null);
        assertThrows(IllegalArgumentException.class, () -> service.save(scheme));
    }

    @Test
    void save_moneyChangerNotFound_throws() {
        var scheme = new CompanyCommissionScheme();
        var mc = new MoneyChanger(); mc.setId(10L);
        scheme.setMoneyChangerId(mc);
        scheme.setCommissionRateId(new CommissionRate());

        when(moneyChangerRepository.existsById(10L)).thenReturn(false);
        assertThrows(ResourceNotFoundException.class, () -> service.save(scheme));
        verify(moneyChangerRepository).existsById(10L);
    }

    @Test
    void save_commissionRateNotFound_throws() {
        var scheme = new CompanyCommissionScheme();
        var mc = new MoneyChanger(); mc.setId(10L);
        var cr = new CommissionRate(); cr.setId(20);
        scheme.setMoneyChangerId(mc);
        scheme.setCommissionRateId(cr);

        when(moneyChangerRepository.existsById(10L)).thenReturn(true);
        when(commissionRateRepository.existsById(20)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> service.save(scheme));
        verify(moneyChangerRepository).existsById(10L);
        verify(commissionRateRepository).existsById(20);
    }

    @Test
    void save_duplicate_throws() {
        var scheme = new CompanyCommissionScheme();
        var mc = new MoneyChanger(); mc.setId(10L);
        var cr = new CommissionRate(); cr.setId(20);
        scheme.setMoneyChangerId(mc);
        scheme.setCommissionRateId(cr);

        when(moneyChangerRepository.existsById(10L)).thenReturn(true);
        when(commissionRateRepository.existsById(20)).thenReturn(true);
        when(repository.existsByMoneyChangerId_IdAndCommissionRateId_Id(10L, 20)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.save(scheme));
        verify(repository).existsByMoneyChangerId_IdAndCommissionRateId_Id(10L, 20);
        verify(repository, never()).save(any());
    }

    @Test
    void delete_success() {
        var item = makeScheme(1, false);
        when(repository.findById(1)).thenReturn(Optional.of(item));
        when(repository.save(item)).thenReturn(item);

        service.delete(1);

        assertTrue(item.getIsDeleted());
        verify(repository).findById(1);
        verify(repository).save(item);
    }

    @Test
    void delete_notFound_throws() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> service.delete(1));
        verify(repository).findById(1);
        verify(repository, never()).save(any());
    }
}
