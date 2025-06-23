package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.MoneyChangerCurrencyDTO;
import com.moola.fx.moneychanger.operations.mapper.MoneyChangerCurrencyMapper;
import com.moola.fx.moneychanger.operations.model.MoneyChangerCurrency;
import com.moola.fx.moneychanger.operations.service.MoneyChangerCurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoneyChangerCurrencyController.class)
public class MoneyChangerCurrencyControllerTest {
    @Autowired private MockMvc mockMvc;
    @MockitoBean private MoneyChangerCurrencyService service;
    @MockitoBean private MoneyChangerCurrencyMapper mapper;

    @Test
    void testListAll() throws Exception {
        when(service.listAll()).thenReturn(List.of(new MoneyChangerCurrency()));
        when(mapper.toDTO(any())).thenReturn(new MoneyChangerCurrencyDTO());

        mockMvc.perform(get("/v1/money-changers-currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    @Test
    void testGet() throws Exception {
        MoneyChangerCurrency entity = new MoneyChangerCurrency();
        MoneyChangerCurrencyDTO dto = new MoneyChangerCurrencyDTO();
        when(service.get(1)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/money-changers-currencies/1"))
                .andExpect(status().isOk());
    }

    @Test
    void testCreate() throws Exception {
        MoneyChangerCurrency entity = new MoneyChangerCurrency();
        MoneyChangerCurrencyDTO dto = new MoneyChangerCurrencyDTO();
        when(service.save(any())).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);
        when(mapper.toEntity(dto)).thenReturn(entity);

        mockMvc.perform(post("/v1/money-changers-currencies")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void testUpdate() throws Exception {
        MoneyChangerCurrency entity = new MoneyChangerCurrency();
        MoneyChangerCurrencyDTO dto = new MoneyChangerCurrencyDTO();
        when(service.get(1)).thenReturn(entity);
        when(service.save(any())).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(put("/v1/money-changers-currencies/1")
                        .contentType("application/json")
                        .content("{}"))
                .andExpect(status().isOk());
    }

    @Test
    void testDelete() throws Exception {
        mockMvc.perform(delete("/v1/money-changers-currencies/1?userId=100"))
                .andExpect(status().isNoContent());
    }
}
