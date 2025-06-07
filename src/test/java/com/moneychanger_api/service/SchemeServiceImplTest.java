package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ForeignKeyConstraintException;
import com.moneychanger_api.model.Scheme;
import com.moneychanger_api.repository.SchemeRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;


@SpringBootTest
public class SchemeServiceImplTest {

    @Mock
    private SchemeRepository schemeRepository;

    @InjectMocks
    private SchemeServiceImpl schemeService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAll() {
        List<Scheme> list = List.of(new Scheme());
        when(schemeRepository.findAll()).thenReturn(list);
        Assertions.assertEquals(1, schemeService.listAll().size());
    }

    @Test
    public void testGet() {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        when(schemeRepository.findById(1)).thenReturn(Optional.of(scheme));
        Assertions.assertEquals(1, schemeService.get(1).getId());
    }

    @Test
    public void testSave_NewScheme_Success() {
        Scheme scheme = new Scheme();
        scheme.setName("Test");

        // Simulate no existing schemes with the same name
        when(schemeRepository.findAll()).thenReturn(List.of());
        when(schemeRepository.save(any(Scheme.class))).thenReturn(scheme);

        Scheme result = schemeService.save(scheme);
        Assertions.assertEquals("Test", result.getName());
    }

    @Test
    public void testSave_DuplicateScheme_ThrowsException() {
        Scheme existing = new Scheme();
        existing.setName("Test");

        Scheme newScheme = new Scheme();
        newScheme.setName("test"); // same name, different case

        // Simulate existing scheme with the same normalized name
        when(schemeRepository.findAll()).thenReturn(List.of(existing));

        Assertions.assertThrows(DuplicateResourceException.class, () -> {
            schemeService.save(newScheme);
        });
    }

    @Test
    public void testDelete_Success() {
        schemeService.delete(1);
        verify(schemeRepository, times(1)).deleteById(1);
    }

    @Test
    public void testDelete_ForeignKeyViolation_ThrowsForeignKeyConstraintException() {
        // Simulate FK constraint violation
        doThrow(new DataIntegrityViolationException("FK constraint")).when(schemeRepository).deleteById(1);

        ForeignKeyConstraintException exception = Assertions.assertThrows(ForeignKeyConstraintException.class, () -> {
            schemeService.delete(1);
        });

        Assertions.assertEquals("Scheme is in use and cannot be deleted.", exception.getMessage());
    }
}