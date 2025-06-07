package com.moneychanger_api.service;

import com.moneychanger_api.repository.CommissionRateRepository;
import com.moneychanger_api.model.CommissionRate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;


import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

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
    public void testGet() {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(item));
        Assertions.assertEquals(1, service.get(1).getId());
    }

    @Test
    public void testSave() {
        CommissionRate item = new CommissionRate();
        item.setRate(new BigDecimal("5.0"));
        when(repository.save(item)).thenReturn(item);
        Assertions.assertEquals(new BigDecimal("5.0"), service.save(item).getRate());
    }

    @Test
    public void testDelete() {
        service.delete(1);
        verify(repository, times(1)).deleteById(1);
    }
}