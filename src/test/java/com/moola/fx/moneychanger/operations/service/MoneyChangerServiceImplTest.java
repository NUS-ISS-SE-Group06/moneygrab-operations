package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MoneyChangerServiceImplTest {

    @InjectMocks
    private MoneyChangerServiceImpl moneyChangerService;

    @Mock
    private MoneyChangerRepository repository;

    private MoneyChanger dummy;

    @BeforeEach
    public void setUp() {
        dummy = new MoneyChanger();
        dummy.setId(1L);
        dummy.setCompanyName("Sample Co");
        dummy.setEmail("sample@co.com");
        dummy.setIsDeleted(false);
    }

    @Test
    public void testGetAll() {
        List<MoneyChanger> list = List.of(dummy);
        when(repository.findByIsDeletedFalse()).thenReturn(list);

        List<MoneyChanger> result = moneyChangerService.getAll();
        assertEquals(1, result.size());
        assertEquals("Sample Co", result.get(0).getCompanyName());
    }

    @Test
    public void testGetOne() {
        when(repository.findById(1L)).thenReturn(Optional.of(dummy));

        MoneyChanger result = moneyChangerService.getOne(1L);
        assertNotNull(result);
        assertEquals("Sample Co", result.getCompanyName());
    }

    @Test
    public void testCreate() {
        when(repository.save(any())).thenReturn(dummy);

        MoneyChanger result = moneyChangerService.create(dummy);
        assertNotNull(result);
        assertFalse(result.getIsDeleted());
    }

    @Test
    public void testUpdate() {
        when(repository.findById(1L)).thenReturn(Optional.of(dummy));
        when(repository.save(any())).thenReturn(dummy);

        MoneyChanger updated = new MoneyChanger();
        updated.setCompanyName("Updated Co");
        updated.setEmail("updated@co.com");

        MoneyChanger result = moneyChangerService.update(1L, updated);
        assertEquals("Updated Co", result.getCompanyName());
        assertEquals("updated@co.com", result.getEmail());
    }

    @Test
    public void testDelete() {
        when(repository.findById(1L)).thenReturn(Optional.of(dummy));
        when(repository.save(any())).thenReturn(dummy);

        moneyChangerService.delete(1L);
        verify(repository).save(dummy);
        assertTrue(dummy.getIsDeleted());
    }
}
