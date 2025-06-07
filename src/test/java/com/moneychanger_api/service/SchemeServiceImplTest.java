package com.moneychanger_api.service;

import com.moneychanger_api.repository.SchemeRepository;
import com.moneychanger_api.model.Scheme;

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
    public void testSave() {
        Scheme scheme = new Scheme();
        scheme.setName("Test");
        when(schemeRepository.save(scheme)).thenReturn(scheme);
        Assertions.assertEquals("Test", schemeService.save(scheme).getName());
    }

    @Test
    public void testDelete() {
        schemeService.delete(1);
        verify(schemeRepository, times(1)).deleteById(1);
    }
}