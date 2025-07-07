package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.dto.ComputeRateDTO;
import com.moola.fx.moneychanger.operations.mapper.ComputeRateMapper;
import com.moola.fx.moneychanger.operations.model.ComputeRate;
import com.moola.fx.moneychanger.operations.model.ComputeRateId;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.service.ComputeRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ComputeRateController.class)
class ComputeRateControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private ComputeRateService service;
    @MockitoBean private ComputeRateMapper mapper;

    @Test
    void list_shouldReturnComputeRates() throws Exception {
        ComputeRate entity = new ComputeRate();
        ComputeRateDTO dto = new ComputeRateDTO();
        dto.setCurrencyCode("USD");

        when(service.listAll()).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/compute-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currencyCode").value("USD"));
    }

    @Test
    void get_shouldReturnComputeRateDTO() throws Exception {
        ComputeRateId id = new ComputeRateId();
        id.setCurrencyCode("USD");
        id.setMoneyChangerId(1L);

        ComputeRate entity = new ComputeRate();
        entity.setId(id);

        MoneyChanger mc = new MoneyChanger();
        mc.setId(1L);
        entity.setMoneyChanger(mc);

        ComputeRateDTO dto = new ComputeRateDTO();
        dto.setCurrencyCode("USD");
        dto.setMoneyChangerId(1L);
        dto.setProcessedAt(new Timestamp(System.currentTimeMillis()));

        when(service.get("USD", 1L)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/compute-rates/USD/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currencyCode").value("USD"))
                .andExpect(jsonPath("$.moneyChangerId").value(1));
    }

    @Test
    void batchCreate_shouldReturnCreatedDTOs() throws Exception {
        ComputeRateDTO dto = new ComputeRateDTO();
        dto.setCurrencyCode("USD");
        dto.setMoneyChangerId(1L);
        dto.setRawBid(BigDecimal.valueOf(1.2345));

        ComputeRate entity = new ComputeRate();
        ComputeRate saved = new ComputeRate();

        when(mapper.toEntity(dto)).thenReturn(entity);
        when(service.saveAll(List.of(entity))).thenReturn(List.of(saved));
        when(mapper.toDTO(saved)).thenReturn(dto);

        mockMvc.perform(post("/v1/compute-rates/batch")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(List.of(dto))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].currencyCode").value("USD"));
    }

    @Test
    void delete_shouldSucceed() throws Exception {
        mockMvc.perform(delete("/v1/compute-rates/USD/1"))
                .andExpect(status().isNoContent());

        verify(service).delete("USD", 1L);
    }
}
