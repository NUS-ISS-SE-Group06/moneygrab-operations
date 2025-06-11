package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.dto.CommissionRateDTO;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.service.CommissionRateService;
import com.moola.fx.moneychanger.operations.mapper.CommissionRateMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class CommissionRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommissionRateService commissionRateService;

    @MockBean
    private CommissionRateMapper mapper;

    @Test
    void testListCommissionRates() throws Exception {
        CurrencyCode currency = new CurrencyCode();
        currency.setId(100);
        currency.setCurrency("USD");

        Scheme scheme = new Scheme();
        scheme.setId(200);
        scheme.setNameTag("Basic Plan");

        CommissionRate entity = new CommissionRate();
        entity.setId(1);
        entity.setCurrencyId(currency);
        entity.setSchemeId(scheme);
        entity.setRate(new BigDecimal("0.0250"));
        entity.setCreatedBy(1);
        entity.setUpdatedBy(1);
        entity.setIsDeleted(false);

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(1);
        dto.setCurrencyId(100);
        dto.setCurrency("USD");
        dto.setSchemeId(200);
        dto.setNameTag("Basic Plan");
        dto.setRate(new BigDecimal("0.0250"));
        dto.setCreatedBy(1);
        dto.setUpdatedBy(1);
        dto.setIsDeleted(false);

        Mockito.when(commissionRateService.listAll()).thenReturn(List.of(entity));
        Mockito.when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates"))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].currencyId").value(100))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].schemeId").value(200))
                .andExpect(jsonPath("$[0].nameTag").value("Basic Plan"))
                .andExpect(jsonPath("$[0].rate").value(0.0250))
                .andExpect(jsonPath("$[0].createdBy").value(1))
                .andExpect(jsonPath("$[0].updatedBy").value(1))
                .andExpect(jsonPath("$[0].isDeleted").value(false));
    }



    @Test
    void testGetCommissionRateFound() throws Exception {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        item.setRate(new BigDecimal("2.50"));

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(1);
        dto.setRate(new BigDecimal("2.50"));

        Mockito.when(commissionRateService.get(1)).thenReturn(item);
        Mockito.when(mapper.toDTO(item)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(2.50));
    }


    @Test
    void testGetCommissionRateNotFound() throws Exception {
        Mockito.when(commissionRateService.get(999)).thenThrow(new ResourceNotFoundException("CommissionRate with ID 999 not found"));

        mockMvc.perform(get("/v1/commission-rates/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("CommissionRate with ID 999 not found"));
    }


    @Test
    void testCreateCommissionRate() throws Exception {
        // Input DTO
        CommissionRateDTO requestDto = new CommissionRateDTO();
        requestDto.setRate(new BigDecimal("3.00"));
        requestDto.setCurrencyId(100);
        requestDto.setSchemeId(200);
        requestDto.setCreatedBy(1); // needed for controller logic

        // Entity to save
        CommissionRate entity = new CommissionRate();
        entity.setId(1);
        entity.setRate(new BigDecimal("3.00"));
        entity.setCurrencyId(new CurrencyCode());
        entity.setSchemeId(new Scheme());
        entity.setIsDeleted(false);

        // Response DTO
        CommissionRateDTO responseDto = new CommissionRateDTO();
        responseDto.setId(1);
        responseDto.setRate(new BigDecimal("3.00"));

        // Mock mapper and service
        Mockito.when(mapper.toEntity(any(CommissionRateDTO.class))).thenReturn(entity);
        Mockito.when(commissionRateService.save(any())).thenReturn(entity);
        Mockito.when(mapper.toDTO(entity)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "rate": 3.00,
                      "currencyId": 100,
                      "schemeId": 200,
                      "createdBy": 1
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(3.00));
    }


    @Test
    void testCreateDuplicateCommissionRate() throws Exception {
        // Given: mock mapper.toEntity(...) to return a dummy CommissionRate
        CommissionRateDTO requestDto = new CommissionRateDTO();
        requestDto.setRate(new BigDecimal("3.00"));
        requestDto.setCurrencyId(100);
        requestDto.setSchemeId(200);
        requestDto.setCreatedBy(1);

        CommissionRate dummyEntity = new CommissionRate();
        dummyEntity.setRate(new BigDecimal("3.00"));
        dummyEntity.setCurrencyId(new CurrencyCode());
        dummyEntity.setSchemeId(new Scheme());

        Mockito.when(mapper.toEntity(any(CommissionRateDTO.class))).thenReturn(dummyEntity);
        Mockito.when(commissionRateService.save(any()))
                .thenThrow(new DuplicateResourceException("Commission rate for the same currency and scheme already exists."));

        // When + Then
        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "rate": 3.00,
                      "currencyId": 100,
                      "schemeId": 200,
                      "createdBy": 1
                    }
                    """))
                .andExpect(status().isConflict()) // HTTP 409
                .andExpect(content().string("Commission rate for the same currency and scheme already exists."));
    }







    @Test
    void testDeleteCommissionRate() throws Exception {
        mockMvc.perform(delete("/v1/commission-rates/1")
                        .param("userId", "1"))
                .andExpect(status().isNoContent());

        Mockito.verify(commissionRateService, times(1)).delete(1, 1);
    }




    @Test
    void testListCommissionRates_WithSchemeId() throws Exception {
        int schemeId = 200;

        CommissionRate entity = new CommissionRate();
        entity.setId(1);
        entity.setRate(new BigDecimal("1.25"));
        entity.setIsDeleted(false);

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(1);
        dto.setRate(new BigDecimal("1.25"));

        Mockito.when(commissionRateService.findBySchemeId(schemeId)).thenReturn(List.of(entity));
        Mockito.when(commissionRateService.listAll()).thenReturn(List.of()); // Should not be called
        Mockito.when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates")
                        .param("schemeId", String.valueOf(schemeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rate").value(1.25));

        Mockito.verify(commissionRateService).findBySchemeId(schemeId);
        Mockito.verify(commissionRateService, times(0)).listAll();
    }


    @Test
    void testListCommissionRates_WithoutSchemeId() throws Exception {
        CommissionRate entity = new CommissionRate();
        entity.setId(2);
        entity.setRate(new BigDecimal("2.75"));
        entity.setIsDeleted(false);

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(2);
        dto.setRate(new BigDecimal("2.75"));

        Mockito.when(commissionRateService.listAll()).thenReturn(List.of(entity));
        Mockito.when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].rate").value(2.75));

        Mockito.verify(commissionRateService).listAll();
        Mockito.verify(commissionRateService, times(0)).findBySchemeId(any());
    }


    @Test
    void testUpdateCommissionRate() throws Exception {
        // Arrange input DTO from request
        CommissionRateDTO requestDto = new CommissionRateDTO();
        requestDto.setRate(new BigDecimal("4.50"));
        requestDto.setCurrencyId(100);
        requestDto.setSchemeId(200);

        // Mock existing entity from service.get(id)
        CommissionRate existing = new CommissionRate();
        existing.setId(1);
        existing.setRate(new BigDecimal("2.00")); // initial value
        existing.setCurrencyId(new CurrencyCode());
        existing.getCurrencyId().setId(100);
        existing.setSchemeId(new Scheme());
        existing.getSchemeId().setId(200);

        // Mock saved result
        CommissionRate saved = new CommissionRate();
        saved.setId(1);
        saved.setRate(new BigDecimal("4.50"));

        // Mock response DTO
        CommissionRateDTO responseDto = new CommissionRateDTO();
        responseDto.setId(1);
        responseDto.setRate(new BigDecimal("4.50"));

        // Set up mocks
        Mockito.when(commissionRateService.get(1)).thenReturn(existing);
        Mockito.when(commissionRateService.save(any())).thenReturn(saved);
        Mockito.when(mapper.toDTO(saved)).thenReturn(responseDto);

        // Act + Assert
        mockMvc.perform(put("/v1/commission-rates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "rate": 4.50,
                          "currencyId": 100,
                          "schemeId": 200
                        }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(4.50));
    }



}