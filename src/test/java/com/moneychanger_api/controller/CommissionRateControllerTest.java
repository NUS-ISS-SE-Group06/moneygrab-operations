package com.moneychanger_api.controller;

import com.moneychanger_api.exception.DuplicateResourceException;
import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.model.CurrencyCode;
import com.moneychanger_api.model.Scheme;
import com.moneychanger_api.service.CommissionRateService;
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
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
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
        List<CommissionRate> list = List.of(new CommissionRate());
        Mockito.when(commissionRateService.listAll()).thenReturn(list);

        mockMvc.perform(get("/v1/commission-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testGetCommissionRateFound() throws Exception {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        item.setRate(new BigDecimal("2.50"));

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
                              "currencyId": { "id": 100 },
                              "schemeId": { "id": 200 }
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
                              "currencyId": { "id": 100 },
                              "schemeId": { "id": 200 }
                            }
                        """))
                .andExpect(status().isConflict())
                .andExpect(content().string("Commission rate for the same currency and scheme already exists."));
    }

    @Test
    void testUpdateCommissionRate() throws Exception {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        item.setRate(new BigDecimal("4.50"));

        Mockito.when(commissionRateService.save(Mockito.any())).thenReturn(item);

        mockMvc.perform(put("/v1/commission-rates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "rate": 4.50,
                              "currencyId": { "id": 100 },
                              "schemeId": { "id": 200 }
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