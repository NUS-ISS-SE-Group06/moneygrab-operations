package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.repository.CommissionRateRepository;
import com.moola.fx.moneychanger.operations.repository.CompanyCommissionSchemeRepository;
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
        when(repository.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(item));

        var out = service.get(1);
        assertEquals(1, out.getId());
        verify(repository).findByIdAndIsDeletedFalse(1);
    }

    @Test
    void get_deleted_throws() {
        when(repository.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.get(1));
        verify(repository).findByIdAndIsDeletedFalse(1);
    }
    

    @Test
    void save_success_create() {
        var scheme = new CompanyCommissionScheme();

        var mc = new MoneyChanger(); mc.setId(10L);
        scheme.setMoneyChangerId(mc);

        var schemeObj = new Scheme(); schemeObj.setId(20);
        scheme.setSchemeId(schemeObj);

        when(repository.existsByMoneyChangerId_idAndSchemeId_IdAndIsDeletedFalse(10L, 20)).thenReturn(false);
        when(repository.save(scheme)).thenReturn(scheme);

        var saved = service.save(scheme);
        assertSame(scheme, saved);

        verify(repository).existsByMoneyChangerId_idAndSchemeId_IdAndIsDeletedFalse(10L, 20);
        verify(repository).save(scheme);
    }

    @Test
    void save_duplicate_create_throws() {
        var scheme = new CompanyCommissionScheme();

        var mc = new MoneyChanger(); mc.setId(10L);
        scheme.setMoneyChangerId(mc);

        var schemeEntity = new Scheme(); schemeEntity.setId(20);
        scheme.setSchemeId(schemeEntity);

        when(repository.existsByMoneyChangerId_idAndSchemeId_IdAndIsDeletedFalse(10L, 20)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.save(scheme));

        verify(repository).existsByMoneyChangerId_idAndSchemeId_IdAndIsDeletedFalse(10L, 20);
        verify(repository, never()).save(any());
    }

    @Test
    void save_success_update() {
        var existing = new CompanyCommissionScheme();
        existing.setId(5);
        var mc = new MoneyChanger(); mc.setId(10L);
        var schemeEntity = new Scheme(); schemeEntity.setId(20);
        existing.setMoneyChangerId(mc);
        existing.setSchemeId(schemeEntity);

        when(repository.existsByMoneyChangerId_IdAndSchemeId_IdAndIdNotAndIsDeletedFalse(10L, 20, 5)).thenReturn(false);
        when(repository.save(existing)).thenReturn(existing);

        var out = service.save(existing);
        assertSame(existing, out);

        verify(repository).existsByMoneyChangerId_IdAndSchemeId_IdAndIdNotAndIsDeletedFalse(10L, 20, 5);
        verify(repository).save(existing);
    }

    @Test
    void save_update_duplicate_throws() {
        var existing = new CompanyCommissionScheme();
        existing.setId(5);
        var mc = new MoneyChanger(); mc.setId(10L);
        var schemeEntity = new Scheme(); schemeEntity.setId(20);
        existing.setMoneyChangerId(mc);
        existing.setSchemeId(schemeEntity);

        when(repository.existsByMoneyChangerId_IdAndSchemeId_IdAndIdNotAndIsDeletedFalse(10L, 20, 5)).thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> service.save(existing));
        verify(repository, never()).save(any());
    }

    @Test
    void delete_success() {
        int schemeId = 1;
        int userId = 99;

        var item = makeScheme(schemeId, false);
        when(repository.findById(schemeId)).thenReturn(Optional.of(item));
        when(repository.save(item)).thenReturn(item);

        service.delete(schemeId, userId);

        assertTrue(item.getIsDeleted());
        assertEquals(userId, item.getUpdatedBy());

        verify(repository).findById(schemeId);
        verify(repository).save(item);
    }

    @Test
    void delete_notFound_throws() {
        int schemeId = 1;
        int userId = 99;

        when(repository.findById(schemeId)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> service.delete(schemeId, userId));

        verify(repository).findById(schemeId);
        verify(repository, never()).save(any());
    }

    @Test
    void findBySchemeId_returnsResults() {
        var schemeId = 123;
        var a = makeScheme(1, false);
        when(repository.findBySchemeId_idAndIsDeletedFalse(schemeId)).thenReturn(List.of(a));

        var out = service.findBySchemeId(schemeId);
        assertEquals(1, out.size());
        assertEquals(1, out.get(0).getId());
        verify(repository).findBySchemeId_idAndIsDeletedFalse(schemeId);
    }

    @Test
    void findBySchemeId_empty() {
        var schemeId = 123;
        when(repository.findBySchemeId_idAndIsDeletedFalse(schemeId)).thenReturn(List.of());

        var out = service.findBySchemeId(schemeId);
        assertTrue(out.isEmpty());
        verify(repository).findBySchemeId_idAndIsDeletedFalse(schemeId);
    }
}
