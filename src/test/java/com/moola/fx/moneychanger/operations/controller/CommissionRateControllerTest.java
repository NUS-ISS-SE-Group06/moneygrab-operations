package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.dto.CommissionRateDTO;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.service.CommissionRateService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
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

    @Test
    void testListCommissionRates() throws Exception {
        CurrencyCode currency = new CurrencyCode();
        currency.setId(100);
        currency.setCurrency("USD");
        currency.setDescription("US Dollar");

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
        Timestamp now = new Timestamp(System.currentTimeMillis());
        entity.setCreatedAt(now);
        entity.setUpdatedAt(now);

        List<CommissionRate> entityList = List.of(entity);

        // Mock the service to return the entity list
        Mockito.when(commissionRateService.listAll()).thenReturn(entityList);

        // Act + Assert
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

        // Mock the service to return the entity
        Mockito.when(commissionRateService.get(1)).thenReturn(item);

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
        CommissionRate item = new CommissionRate();
        item.setId(1);
        item.setRate(new BigDecimal("3.00"));

        CurrencyCode currency = new CurrencyCode();
        currency.setId(100);
        item.setCurrencyId(currency);

        Scheme scheme = new Scheme();
        scheme.setId(200);
        item.setSchemeId(scheme);

        Mockito.when(commissionRateService.save(Mockito.any())).thenReturn(item);

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "rate": 3.00,
                          "currencyId": 100,
                          "schemeId": 200
                        }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(3.00));
    }

    @Test
    void testCreateDuplicateCommissionRate() throws Exception {
        Mockito.when(commissionRateService.save(Mockito.any()))
                .thenThrow(new DuplicateResourceException("Commission rate for the same currency and scheme already exists."));

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                        {
                          "rate": 3.00,
                          "currencyId": 100,
                          "schemeId": 200
                        }
                    """))
                .andExpect(status().isConflict())
                .andExpect(content().string("Commission rate for the same currency and scheme already exists."));
    }


    @Test
    void testUpdateCommissionRate() throws Exception {
        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(1);
        dto.setRate(new BigDecimal("4.50"));
        dto.setCurrencyId(100);
        dto.setSchemeId(200);

        CommissionRate savedEntity = new CommissionRate();
        savedEntity.setId(1);
        savedEntity.setRate(new BigDecimal("4.50"));

        Mockito.when(commissionRateService.save(Mockito.any())).thenReturn(savedEntity);

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


    @Test
    void testDeleteCommissionRate() throws Exception {
        mockMvc.perform(delete("/v1/commission-rates/1"))
                .andExpect(status().isOk());

        Mockito.verify(commissionRateService, times(1)).delete(1);
    }
}