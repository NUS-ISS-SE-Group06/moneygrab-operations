package com.moneychanger_api.controller;

import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.service.CommissionRateService;
import com.moneychanger_api.model.CommissionRate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;

@SpringBootTest
@AutoConfigureMockMvc
public class CommissionRateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommissionRateService commissionRateService;

    @Test
    public void testListCommissionRates() throws Exception {
        List<CommissionRate> list = List.of(new CommissionRate());
        Mockito.when(commissionRateService.listAll()).thenReturn(list);

        mockMvc.perform(get("/api/commission-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetCommissionRateFound() throws Exception {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        item.setRate(new BigDecimal("2.50"));

        Mockito.when(commissionRateService.get(1)).thenReturn(item);

        mockMvc.perform(get("/api/commission-rates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(2.50));
    }

    @Test
    public void testGetCommissionRateNotFound() throws Exception {
        Mockito.when(commissionRateService.get(999)).thenThrow(new ResourceNotFoundException("CommissionRate with ID 999 not found"));

        mockMvc.perform(get("/api/commission-rates/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("CommissionRate with ID 999 not found"));

    }

    @Test
    public void testCreateCommissionRate() throws Exception {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        item.setRate(new BigDecimal("3.00"));

        Mockito.when(commissionRateService.save(Mockito.any())).thenReturn(item);

        mockMvc.perform(post("/api/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rate\":3.00}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(3.00));
    }

    @Test
    public void testUpdateCommissionRate() throws Exception {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        item.setRate(new BigDecimal("4.50"));

        Mockito.when(commissionRateService.save(Mockito.any())).thenReturn(item);

        mockMvc.perform(put("/api/commission-rates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"rate\":4.50}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(4.50));
    }

    @Test
    public void testDeleteCommissionRate() throws Exception {
        mockMvc.perform(delete("/api/commission-rates/1"))
                .andExpect(status().isOk());

        Mockito.verify(commissionRateService, times(1)).delete(1);
    }
}