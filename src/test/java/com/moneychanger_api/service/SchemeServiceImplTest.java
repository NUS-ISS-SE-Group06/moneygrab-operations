package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ForeignKeyConstraintException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.Scheme;
import com.moneychanger_api.repository.SchemeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@SpringBootTest
class SchemeServiceImplTest {

    @Mock
    private SchemeRepository schemeRepository;

    private SchemeServiceImpl schemeService;

    @BeforeEach
    public void setup() {

        MockitoAnnotations.openMocks(this);
        schemeService = new SchemeServiceImpl(schemeRepository);
    }

    @Test
    void testListAll() {
        Scheme scheme = new Scheme();
        scheme.setIsDeleted(false);
        List<Scheme> list = List.of(scheme);
        when(schemeRepository.findAll()).thenReturn(list);

        List<Scheme> result = schemeService.listAll();
        Assertions.assertEquals(1, result.size());
        Assertions.assertFalse(result.get(0).getIsDeleted());
    }

    @Test
    void testGet_Success() {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setIsDeleted(false);

        when(schemeRepository.findById(1)).thenReturn(Optional.of(scheme));

        Scheme result = schemeService.get(1);

        Assertions.assertEquals(1, result.getId());
        Assertions.assertFalse(result.getIsDeleted());
    }


    @Test
    void testGet_Deleted_ThrowsException() {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setIsDeleted(true);

        when(schemeRepository.findById(1)).thenReturn(Optional.of(scheme));

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            schemeService.get(1);
        });
    }

    @Test
    void testGet_NotFound_ThrowsException() {
        when(schemeRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            schemeService.get(1);
        });
    }


    @Test
    void testSave_NewScheme_Success() {
        Scheme scheme = new Scheme();
        scheme.setNameTag("Test");
        scheme.setDescription("New scheme description");
        scheme.setIsDefault(true);

        when(schemeRepository.findAll()).thenReturn(List.of());
        when(schemeRepository.save(any(Scheme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Scheme result = schemeService.save(scheme);

        Assertions.assertEquals("Test", result.getNameTag());
        Assertions.assertEquals("New scheme description", result.getDescription());
        Assertions.assertTrue(result.getIsDefault());
    }


    @Test
    void testSave_DuplicateSchemeWithDifferentId_ThrowsException() {
        Scheme existing = new Scheme();
        existing.setId(1);
        existing.setNameTag("Test");
        existing.setIsDeleted(false);

        Scheme newScheme = new Scheme();
        newScheme.setId(2);
        newScheme.setNameTag("test"); // same name different case
        newScheme.setIsDefault(true);

        when(schemeRepository.findAll()).thenReturn(List.of(existing));

        Assertions.assertThrows(DuplicateResourceException.class, () -> {
            schemeService.save(newScheme);
        });
    }

    @Test
    void testSave_SameNameSameId_DoesNotThrowException() {
        Scheme existing = new Scheme();
        existing.setId(2);
        existing.setNameTag("Test");
        existing.setIsDeleted(false);

        Scheme newScheme = new Scheme();
        newScheme.setId(2);
        newScheme.setNameTag("test");

        when(schemeRepository.findAll()).thenReturn(List.of(existing));
        when(schemeRepository.save(any(Scheme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Assertions.assertDoesNotThrow(() -> {
            schemeService.save(newScheme);
        });
    }

    @Test
    void testSave_NewDefaultScheme_UnsetsPreviousDefaults() {
        Scheme existingDefault = new Scheme();
        existingDefault.setId(1);
        existingDefault.setNameTag("Existing Default");
        existingDefault.setIsDefault(true);
        existingDefault.setIsDeleted(false);

        Scheme newDefault = new Scheme();
        newDefault.setNameTag("New Default");
        newDefault.setIsDefault(true);

        when(schemeRepository.findAll()).thenReturn(List.of(existingDefault));
        when(schemeRepository.save(any(Scheme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Scheme result = schemeService.save(newDefault);

        verify(schemeRepository, times(2)).save(any(Scheme.class)); // once to unset old default, once to save new
        Assertions.assertTrue(result.getIsDefault());
    }

    @Test
    void testDelete_Success() {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setIsDeleted(false);

        when(schemeRepository.findById(1)).thenReturn(Optional.of(scheme));
        when(schemeRepository.save(any(Scheme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        schemeService.delete(1);

        Assertions.assertTrue(scheme.getIsDeleted());
        verify(schemeRepository, times(1)).save(scheme);
    }

    @Test
    void testDelete_NotFound_ThrowsResourceNotFound() {
        when(schemeRepository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> {
            schemeService.delete(1);
        });
    }

    @Test
    void testDelete_ForeignKeyViolation_ThrowsForeignKeyConstraintException() {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setIsDeleted(false);

        when(schemeRepository.findById(1)).thenReturn(Optional.of(scheme));
        // Simulate DataIntegrityViolationException on save
        when(schemeRepository.save(any(Scheme.class))).thenThrow(new DataIntegrityViolationException("FK violation"));

        ForeignKeyConstraintException exception = Assertions.assertThrows(ForeignKeyConstraintException.class, () -> {
            schemeService.delete(1);
        });

        Assertions.assertEquals("Scheme is in use and cannot be deleted.", exception.getMessage());
    }


}