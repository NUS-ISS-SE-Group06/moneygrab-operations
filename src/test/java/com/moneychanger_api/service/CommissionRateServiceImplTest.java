package com.moneychanger_api.service;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.repository.CommissionRateRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@SpringBootTest
public class CommissionRateServiceImplTest {

    @Mock
    private CommissionRateRepository repository;

    @InjectMocks
    private CommissionRateServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAll() {
        when(repository.findAll()).thenReturn(List.of(new CommissionRate()));
        Assertions.assertEquals(1, service.listAll().size());
    }

    @Test
    public void testGet_Found() {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(item));
        Assertions.assertEquals(1, service.get(1).getId());
    }

    @Test
    public void testGet_NotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());
        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.get(1));
    }

    @Test
    public void testSave_New() {
        CommissionRate item = new CommissionRate();
        item.setDescription("Retail");
        item.setRate(new BigDecimal("5.0"));

        when(repository.findAll()).thenReturn(List.of());
        when(repository.save(item)).thenReturn(item);

        CommissionRate saved = service.save(item);
        Assertions.assertEquals("Retail", saved.getDescription());
        Assertions.assertEquals(new BigDecimal("5.0"), saved.getRate());
    }

    @Test
    public void testSave_Duplicate() {
        CommissionRate duplicateItem = new CommissionRate();
        duplicateItem.setDescription("retail"); // Same as "Retail" (case-insensitive)

        // Mock the repository method to return true for duplicate description
        when(repository.existsByDescriptionIgnoreCase("retail")).thenReturn(true);

        // Expect the DuplicateResourceException to be thrown
        Assertions.assertThrows(DuplicateResourceException.class, () -> service.save(duplicateItem));

        // Ensure that save is never called
        verify(repository, never()).save(any());
    }

    @Test
    public void testDelete_Success() {
        CommissionRate existing = new CommissionRate();
        existing.setId(1);
        existing.setIsDeleted(false);

        when(repository.findById(1)).thenReturn(Optional.of(existing));

        service.delete(1);

        Assertions.assertTrue(existing.getIsDeleted());
        verify(repository).save(existing);
    }

    @Test
    public void testDelete_NotFound() {
        when(repository.findById(1)).thenReturn(Optional.empty());

        Assertions.assertThrows(ResourceNotFoundException.class, () -> service.delete(1));
    }



}