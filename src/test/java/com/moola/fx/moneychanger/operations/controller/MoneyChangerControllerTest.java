package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.service.MoneyChangerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;

class MoneyChangerControllerTest {

    @InjectMocks
    private MoneyChangerController controller;

    @Mock
    private MoneyChangerService service;

    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetById() {
        Long id = 1L;
        MoneyChangerResponseDTO dto = new MoneyChangerResponseDTO();
        when(service.getMoneyChangerById(id)).thenReturn(dto);

        ResponseEntity<MoneyChangerResponseDTO> response = controller.getOne(id);
        assertNotNull(response.getBody());
    }
}
