package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.dto.CommissionRateDTO;
import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.mapper.CommissionRateMapper;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.service.CommissionRateService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CommissionRateController.class)
class CommissionRateControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private CommissionRateService service;
    @MockitoBean private CommissionRateMapper mapper;

    @Test
    void list_all_returns200() throws Exception {
        CommissionRate entity = new CommissionRate();
        entity.setId(1);
        entity.setRate(new BigDecimal("1.23"));

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(1);
        dto.setRate(new BigDecimal("1.23"));

        when(service.listAll()).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rate").value(1.23));
    }

    @Test
    void list_bySchemeId_returns200() throws Exception {
        CommissionRate entity = new CommissionRate();
        entity.setId(2);
        entity.setRate(new BigDecimal("2.34"));

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(2);
        dto.setRate(new BigDecimal("2.34"));

        when(service.findBySchemeId(99)).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates").param("schemeId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].rate").value(2.34));
    }

    @Test
    void get_byId_returns200() throws Exception {
        CommissionRate entity = new CommissionRate();
        entity.setId(5);

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(5);
        dto.setRate(new BigDecimal("1.50"));

        when(service.get(5)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.rate").value(1.50));
    }

    @Test
    void create_valid_returns200() throws Exception {
        CommissionRateDTO inputDto = new CommissionRateDTO();
        inputDto.setRate(new BigDecimal("1.11"));
        inputDto.setCurrencyId(1);
        inputDto.setSchemeId(2);
        inputDto.setCreatedBy(10);

        CommissionRate entity = new CommissionRate();
        entity.setRate(new BigDecimal("1.11"));
        entity.setUpdatedBy(10);

        CommissionRate saved = new CommissionRate();
        saved.setId(123);
        saved.setRate(new BigDecimal("1.11"));

        CommissionRateDTO responseDto = new CommissionRateDTO();
        responseDto.setId(123);
        responseDto.setRate(new BigDecimal("1.11"));

        when(mapper.toEntity(any())).thenReturn(entity);
        when(service.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(123))
                .andExpect(jsonPath("$.rate").value(1.11));
    }

    @Test
    void update_valid_returns200() throws Exception {
        CommissionRateDTO inputDto = new CommissionRateDTO();
        inputDto.setRate(new BigDecimal("2.22"));
        inputDto.setUpdatedBy(20);

        CommissionRate existing = new CommissionRate();
        existing.setId(5);

        CommissionRate updated = new CommissionRate();
        updated.setId(5);
        updated.setRate(new BigDecimal("2.22"));
        updated.setUpdatedBy(20);

        CommissionRateDTO responseDto = new CommissionRateDTO();
        responseDto.setId(5);
        responseDto.setRate(new BigDecimal("2.22"));
        responseDto.setUpdatedBy(20);

        when(service.get(5)).thenReturn(existing);
        when(service.save(existing)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(responseDto);

        mockMvc.perform(put("/v1/commission-rates/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(5))
                .andExpect(jsonPath("$.rate").value(2.22))
                .andExpect(jsonPath("$.updatedBy").value(20));
    }

    @Test
    void delete_success_returns204() throws Exception {
        doNothing().when(service).delete(77, 10);

        mockMvc.perform(delete("/v1/commission-rates/77")
                        .param("userId", "10"))
                .andExpect(status().isNoContent());

        verify(service).delete(77, 10);
    }

    @Test
    void get_notFound_returns404() throws Exception {
        when(service.get(99)).thenThrow(new ResourceNotFoundException("Not found"));

        mockMvc.perform(get("/v1/commission-rates/99"))
                .andExpect(status().isNotFound());
    }

    @Test
    void create_duplicate_returns409() throws Exception {
        CommissionRateDTO inputDto = new CommissionRateDTO();
        inputDto.setRate(new BigDecimal("3.33"));

        CommissionRate entity = new CommissionRate();
        entity.setRate(new BigDecimal("3.33"));

        when(mapper.toEntity(any())).thenReturn(entity);
        when(service.save(entity)).thenThrow(new DuplicateResourceException("Already exists"));

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isConflict());
    }
}
