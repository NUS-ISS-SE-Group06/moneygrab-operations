package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.repository.MoneyChangerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MoneyChangerServiceImplTest {

    @Mock
    private MoneyChangerRepository repository;

    @InjectMocks
    private MoneyChangerServiceImpl service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreate() {
        MoneyChanger mc = new MoneyChanger();
        mc.setCompanyName("Test Co");
        when(repository.save(any(MoneyChanger.class))).thenReturn(mc);

        MoneyChanger result = service.create(mc);
        assertEquals("Test Co", result.getCompanyName());
    }

    @Test
    void testGetAll() {
        List<MoneyChanger> list = List.of(new MoneyChanger());
        when(repository.findByIsDeletedFalse()).thenReturn(list);

        List<MoneyChanger> result = service.getAll();
        assertEquals(1, result.size());
    }

    // Additional tests can be added for get(), update(), delete()
}
