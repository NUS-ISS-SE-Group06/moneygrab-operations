package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ForeignKeyConstraintException;
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
        List<Scheme> list = List.of(new Scheme());
        when(schemeRepository.findAll()).thenReturn(list);
        Assertions.assertEquals(1, schemeService.listAll().size());
    }

    @Test
    void testGet() {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        when(schemeRepository.findById(1)).thenReturn(Optional.of(scheme));
        Assertions.assertEquals(1, schemeService.get(1).getId());
    }

    @Test
    void testSave_NewScheme_Success() {
        Scheme scheme = new Scheme();
        scheme.setNameTag("Test");
        scheme.setDescription("New scheme description");
        scheme.setIsDefault(true);

        // Simulate no existing schemes with the same name
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
        existing.setDescription("Existing scheme");
        existing.setIsDefault(false);

        Scheme newScheme = new Scheme();
        newScheme.setId(2); // New scheme has a different ID
        newScheme.setNameTag("test"); // same name, different case
        newScheme.setDescription("Duplicate scheme");
        newScheme.setIsDefault(true);

        // Simulate existing scheme with the same normalized name but different ID
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

        Scheme newScheme = new Scheme();
        newScheme.setId(2); // same ID
        newScheme.setNameTag("test"); // same name, different case

        when(schemeRepository.findAll()).thenReturn(List.of(existing));

        Assertions.assertDoesNotThrow(() -> {
            schemeService.save(newScheme);
        });
    }

    @Test
    void testSave_NewDefaultScheme_UnsetsPreviousDefaults() {
        // Existing default scheme in DB
        Scheme existingDefault = new Scheme();
        existingDefault.setId(1);
        existingDefault.setNameTag("Existing Default");
        existingDefault.setIsDefault(true);

        // New scheme to be saved as default
        Scheme newDefault = new Scheme();
        newDefault.setNameTag("New Default");
        newDefault.setIsDefault(true);

        // Mock existing schemes including the default
        when(schemeRepository.findAll()).thenReturn(List.of(existingDefault));

        // Mock save for both updated old and new scheme
        when(schemeRepository.save(any(Scheme.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Call save method
        Scheme result = schemeService.save(newDefault);

        // Verify old default was updated to false
        verify(schemeRepository, times(2)).save(any(Scheme.class)); // old + new

        Assertions.assertTrue(result.getIsDefault());
    }


    @Test
    void testDelete_Success() {
        schemeService.delete(1);
        verify(schemeRepository, times(1)).deleteById(1);
    }

    @Test
    void testDelete_ForeignKeyViolation_ThrowsForeignKeyConstraintException() {
        // Simulate FK constraint violation
        doThrow(new DataIntegrityViolationException("FK constraint")).when(schemeRepository).deleteById(1);

        ForeignKeyConstraintException exception = Assertions.assertThrows(ForeignKeyConstraintException.class, () -> {
            schemeService.delete(1);
        });

        Assertions.assertEquals("Scheme is in use and cannot be deleted.", exception.getMessage());
    }
}