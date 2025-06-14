package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyChangerServiceImplTest {

    @Mock
    private MoneyChangerRepository repository;

    @InjectMocks
    private MoneyChangerServiceImpl service;

    private MoneyChanger dummy;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dummy = new MoneyChanger();
        dummy.setId(1L);
        dummy.setCompanyName("TestCo");
        dummy.setEmail("test@example.com");
        dummy.setIsDeleted(false);
        dummy.setCreatedAt(LocalDateTime.now());
        dummy.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    void testGetAll() {
        when(repository.findByIsDeletedFalse()).thenReturn(List.of(dummy));

        List<MoneyChanger> result = service.getAll();
        assertEquals(1, result.size());
        assertEquals("TestCo", result.get(0).getCompanyName());
    }

    @Test
    void testGetOne() {
        when(repository.findById(1L)).thenReturn(Optional.of(dummy));

        MoneyChanger result = service.getOne(1L);
        assertEquals("TestCo", result.getCompanyName());
    }

    @Test
    void testCreate() {
        when(repository.save(any(MoneyChanger.class))).thenReturn(dummy);

        MoneyChanger created = service.create(dummy);
        assertNotNull(created);
        assertFalse(created.getIsDeleted());
        verify(repository, times(1)).save(any());
    }

    @Test
    void testUpdate() {
        MoneyChanger updated = new MoneyChanger();
        updated.setCompanyName("UpdatedCo");
        updated.setEmail("updated@example.com");

        when(repository.findById(1L)).thenReturn(Optional.of(dummy));
        when(repository.save(any(MoneyChanger.class))).thenReturn(dummy);

        MoneyChanger result = service.update(1L, updated);
        assertEquals("UpdatedCo", result.getCompanyName());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    void testDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(dummy));

        service.delete(1L);

        assertTrue(dummy.getIsDeleted());
        verify(repository).save(dummy);
    }
}