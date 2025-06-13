package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.repository.SchemeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.moola.fx.moneychanger.operations.repository.CompanyCommissionSchemeRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SchemeServiceImplTest {

    @Mock
    private SchemeRepository schemeRepository;

    @Mock
    private CompanyCommissionSchemeRepository companyCommissionSchemeRepository;

    @InjectMocks
    private SchemeServiceImpl schemeService;

    private Scheme makeScheme(Integer id, boolean deleted) {
        Scheme s = new Scheme();
        s.setId(id);
        s.setIsDeleted(deleted);
        return s;
    }

    @BeforeEach
    void setUp() {
        // MockitoExtension does the initialization
    }

    @Test
    void listAll_filtersDeleted() {
        Scheme a = makeScheme(1, false);
        Scheme b = makeScheme(2, true);
        when(schemeRepository.findAll()).thenReturn(List.of(a, b));

        var out = schemeService.listAll();
        assertEquals(1, out.size());
        assertFalse(out.get(0).getIsDeleted());
        verify(schemeRepository).findAll();
    }

    @Test
    void get_success() {
        Scheme s = new Scheme();
        s.setId(1);
        s.setIsDeleted(false);
        when(schemeRepository.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.of(s));

        var out = schemeService.get(1);
        assertEquals(1, out.getId());
        verify(schemeRepository).findByIdAndIsDeletedFalse(1);
    }

    @Test
    void get_deletedOrNotFound_throws() {
        // Deleted case
        when(schemeRepository.findByIdAndIsDeletedFalse(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> schemeService.get(1));
        verify(schemeRepository).findByIdAndIsDeletedFalse(1);
    }

    @Test
    void save_newScheme_success() {
        Scheme newScheme = new Scheme();
        newScheme.setId(null);
        newScheme.setNameTag("Test");
        newScheme.setDescription("Desc");
        newScheme.setIsDefault(true);

        when(schemeRepository.findAll()).thenReturn(List.of());
        when(schemeRepository.save(newScheme)).thenAnswer(i -> i.getArgument(0));

        var out = schemeService.save(newScheme);
        assertEquals("Test", out.getNameTag());
        assertTrue(out.getIsDefault());
        verify(schemeRepository).findAll();
        verify(schemeRepository).save(newScheme);
    }


    @Test
    void save_newDefault_unsetsPreviousDefault() {
        Scheme existingDefault = makeScheme(1, false);
        existingDefault.setNameTag("Old");
        existingDefault.setIsDefault(true);

        Scheme newDefault = new Scheme();
        newDefault.setNameTag("New");
        newDefault.setIsDefault(true);

        when(schemeRepository.findAll()).thenReturn(List.of(existingDefault));
        // .save(...) called twice: once to unset, once to save new
        when(schemeRepository.save(any(Scheme.class))).thenAnswer(i -> i.getArgument(0));

        var out = schemeService.save(newDefault);
        assertTrue(out.getIsDefault());
        verify(schemeRepository, times(2)).save(any(Scheme.class));
    }

    @Test
    void delete_success() {
        int id = 1, userId = 99;
        Scheme s = makeScheme(id, false);
        when(schemeRepository.findById(id)).thenReturn(Optional.of(s));

        // 👇 New: stub scheme usage check
        when(companyCommissionSchemeRepository.existsBySchemeId_IdAndIsDeletedFalse(id)).thenReturn(false);

        when(schemeRepository.save(s)).thenReturn(s);

        assertDoesNotThrow(() -> schemeService.delete(id, userId));
        assertTrue(s.getIsDeleted());
        assertEquals(userId, s.getUpdatedBy());

        verify(schemeRepository).findById(id);
        verify(companyCommissionSchemeRepository).existsBySchemeId_IdAndIsDeletedFalse(id); // 👈 added
        verify(schemeRepository).save(s);
    }


    @Test
    void delete_notFound_throws() {
        when(schemeRepository.findById(1)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> schemeService.delete(1, 99));
        verify(schemeRepository).findById(1);
        verify(schemeRepository, never()).save(any());
    }

    @Test
    void save_duplicateWithDifferentId_throws() {
        Scheme duplicate = new Scheme();
        duplicate.setId(2);
        duplicate.setNameTag(" SameName ");   // any casing/whitespace
        duplicate.setIsDefault(false);

        // Service will normalize to "samename"
        when(schemeRepository.existsByNameTagIgnoreCaseAndIdNotAndIsDeletedFalse("samename", 2))
                .thenReturn(true);

        assertThrows(DuplicateResourceException.class, () -> schemeService.save(duplicate));

        verify(schemeRepository).existsByNameTagIgnoreCaseAndIdNotAndIsDeletedFalse("samename", 2);
        verify(schemeRepository, never()).save(any());
    }

    @Test
    void save_sameNameSameId_doesNotThrow() {
        // Given an existing, non-deleted scheme
        Scheme existing = makeScheme(2, false);
        existing.setNameTag("Match");

        // And an "update" DTO with the same ID and name (with whitespace/case variation)
        Scheme update = new Scheme();
        update.setId(2);
        update.setNameTag("  match  ");

        // The service normalizes to "match", so stub the exists-check to return false
        when(schemeRepository.existsByNameTagIgnoreCaseAndIdNotAndIsDeletedFalse("match", 2))
                .thenReturn(false);

        // Stub save(...) to simply return the passed entity
        when(schemeRepository.save(update)).thenAnswer(invocation -> invocation.getArgument(0));

        // When & Then: no exception should be thrown
        assertDoesNotThrow(() -> schemeService.save(update));

        // Verify only the methods actually called in this path:
        verify(schemeRepository).existsByNameTagIgnoreCaseAndIdNotAndIsDeletedFalse("match", 2);
        verify(schemeRepository).save(update);
        // Note: we do NOT verify findAll(), since isDefault != true, so it's never called.
    }


}
