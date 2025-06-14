package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MoneyChangerServiceImplTest {

    @InjectMocks
    private MoneyChangerServiceImpl moneyChangerService;

    @Mock
    private MoneyChangerRepository repository;

    private MoneyChanger dummyMoneyChanger;

    @BeforeEach
    public void setup() {
        dummyMoneyChanger = new MoneyChanger();
        dummyMoneyChanger.setId(1L);
        dummyMoneyChanger.setCompanyName("Test Company");
        dummyMoneyChanger.setEmail("test@example.com");
        dummyMoneyChanger.setIsDeleted(false);
    }

    @Test
    public void testGetAll() {
        List<MoneyChanger> list = Arrays.asList(dummyMoneyChanger);
        when(repository.findByIsDeletedFalse()).thenReturn(list);

        List<MoneyChanger> result = moneyChangerService.getAll();
        assertEquals(1, result.size());
        assertEquals("Test Company", result.get(0).getCompanyName());
    }

    @Test
    public void testGetById_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(dummyMoneyChanger));

        MoneyChanger result = moneyChangerService.getById(1L);
        assertEquals("Test Company", result.getCompanyName());
    }

    @Test
    public void testGetById_Deleted_ThrowsException() {
        dummyMoneyChanger.setIsDeleted(true);
        when(repository.findById(1L)).thenReturn(Optional.of(dummyMoneyChanger));

        assertThrows(EntityNotFoundException.class, () -> {
            moneyChangerService.getById(1L);
        });
    }

    @Test
    public void testCreate() {
        when(repository.save(any(MoneyChanger.class))).thenReturn(dummyMoneyChanger);

        MoneyChanger result = moneyChangerService.create(dummyMoneyChanger);
        assertFalse(result.getIsDeleted());
        assertEquals("Test Company", result.getCompanyName());
    }

    @Test
    public void testUpdate_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(dummyMoneyChanger));
        when(repository.save(any(MoneyChanger.class))).thenReturn(dummyMoneyChanger);

        MoneyChanger update = new MoneyChanger();
        update.setCompanyName("Updated Company");
        update.setEmail("updated@example.com");

        MoneyChanger result = moneyChangerService.update(1L, update);
        assertEquals("Updated Company", result.getCompanyName());
        assertEquals("updated@example.com", result.getEmail());
    }

    @Test
    public void testDelete_Success() {
        when(repository.findById(1L)).thenReturn(Optional.of(dummyMoneyChanger));
        when(repository.save(any(MoneyChanger.class))).thenReturn(dummyMoneyChanger);

        moneyChangerService.delete(1L);

        verify(repository, times(1)).save(dummyMoneyChanger);
        assertTrue(dummyMoneyChanger.getIsDeleted());
    }

    @Test
    public void testGetOne() {
        when(repository.findById(1L)).thenReturn(Optional.of(dummyMoneyChanger));

        MoneyChanger result = moneyChangerService.getOne(1L);
        assertEquals("Test Company", result.getCompanyName());
    }
}
