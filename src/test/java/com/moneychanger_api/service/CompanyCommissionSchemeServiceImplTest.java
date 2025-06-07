package com.moneychanger_api.service;

import com.moneychanger_api.model.MoneyChanger;
import com.moneychanger_api.repository.CompanyCommissionSchemeRepository;
import com.moneychanger_api.model.CompanyCommissionScheme;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

import org.springframework.boot.test.context.SpringBootTest;


import java.util.List;
import java.util.Optional;

@SpringBootTest
public class CompanyCommissionSchemeServiceImplTest {

    @Mock
    private CompanyCommissionSchemeRepository repository;

    @InjectMocks
    private CompanyCommissionSchemeServiceImpl service;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testListAll() {
        when(repository.findAll()).thenReturn(List.of(new CompanyCommissionScheme()));
        Assertions.assertEquals(1, service.listAll().size());
    }

    @Test
    public void testGet() {
        CompanyCommissionScheme item = new CompanyCommissionScheme();
        item.setId(1);
        when(repository.findById(1)).thenReturn(Optional.of(item));
        Assertions.assertEquals(1, service.get(1).getId());
    }

    @Test
    public void testSave() {
        CompanyCommissionScheme item = new CompanyCommissionScheme();

        MoneyChanger mc = new MoneyChanger();
        mc.setId(10L);
        item.setMoneyChangerId(mc);

        when(repository.save(item)).thenReturn(item);
        Assertions.assertEquals(10L, service.save(item).getMoneyChangerId().getId());
    }

    @Test
    public void testDelete() {
        service.delete(1);
        verify(repository, times(1)).deleteById(1);
    }
}