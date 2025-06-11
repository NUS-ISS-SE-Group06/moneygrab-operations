package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.service.CompanyCommissionSchemeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CompanyCommissionSchemeControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CompanyCommissionSchemeService service;

    @InjectMocks
    private CompanyCommissionSchemeController controller;

    /**
     * Advice to convert ResourceNotFoundException into JSON { "message": "…" } with 404 status
     */
    @RestControllerAdvice
    static class TestExceptionAdvice {
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String,String> handleNotFound(ResourceNotFoundException ex) {
            return Map.of("message", ex.getMessage());
        }
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionAdvice())
                .build();
    }

    @Test
    void testListCompanyCommissionSchemes_empty() throws Exception {
        Mockito.when(service.listAll()).thenReturn(List.of());

        mockMvc.perform(get("/v1/company-commission-schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));

        Mockito.verify(service, times(1)).listAll();
    }

    @Test
    void testListCompanyCommissionSchemes_withData() throws Exception {
        CompanyCommissionScheme scheme = new CompanyCommissionScheme();
        scheme.setId(1);

        MoneyChanger mc = new MoneyChanger();
        mc.setId(1L);
        scheme.setMoneyChangerId(mc);

        CommissionRate cr = new CommissionRate();
        cr.setId(1);
        scheme.setCommissionRateId(cr);

        Mockito.when(service.listAll()).thenReturn(List.of(scheme));

        mockMvc.perform(get("/v1/company-commission-schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].moneyChangerId.id", is(1)))
                .andExpect(jsonPath("$[0].commissionRateId.id", is(1)));

        Mockito.verify(service, times(1)).listAll();
    }

    @Test
    void testGetCompanyCommissionSchemeFound() throws Exception {
        CompanyCommissionScheme item = new CompanyCommissionScheme();
        item.setId(1);

        MoneyChanger mc = new MoneyChanger();
        mc.setId(2L);
        item.setMoneyChangerId(mc);

        CommissionRate cr = new CommissionRate();
        cr.setId(3);
        item.setCommissionRateId(cr);

        Mockito.when(service.get(1)).thenReturn(item);

        mockMvc.perform(get("/v1/company-commission-schemes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.moneyChangerId.id", is(2)))
                .andExpect(jsonPath("$.commissionRateId.id", is(3)));

        Mockito.verify(service, times(1)).get(1);
    }

    @Test
    void testGetCompanyCommissionSchemeNotFound() throws Exception {
        Mockito.when(service.get(1))
                .thenThrow(new ResourceNotFoundException("CompanyCommissionScheme with ID 1 not found"));

        mockMvc.perform(get("/v1/company-commission-schemes/1"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("CompanyCommissionScheme with ID 1 not found"));

        Mockito.verify(service, times(1)).get(1);
    }

    @Test
    void testCreateCompanyCommissionScheme() throws Exception {
        CompanyCommissionScheme saved = new CompanyCommissionScheme();
        saved.setId(1);

        MoneyChanger mc = new MoneyChanger();
        mc.setId(5L);
        saved.setMoneyChangerId(mc);

        CommissionRate cr = new CommissionRate();
        cr.setId(6);
        saved.setCommissionRateId(cr);

        Mockito.when(service.save(any())).thenReturn(saved);

        mockMvc.perform(post("/v1/company-commission-schemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "moneyChangerId": { "id": 5 },
                      "commissionRateId": { "id": 6 },
                      "isDefault": true
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.moneyChangerId.id", is(5)))
                .andExpect(jsonPath("$.commissionRateId.id", is(6)));

        Mockito.verify(service, times(1)).save(any());
    }

    @Test
    void testUpdateCompanyCommissionScheme() throws Exception {
        CompanyCommissionScheme updated = new CompanyCommissionScheme();
        updated.setId(1);

        MoneyChanger mc = new MoneyChanger();
        mc.setId(8L);
        updated.setMoneyChangerId(mc);

        CommissionRate cr = new CommissionRate();
        cr.setId(9);
        updated.setCommissionRateId(cr);

        Mockito.when(service.save(any())).thenReturn(updated);

        mockMvc.perform(put("/v1/company-commission-schemes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                    {
                      "moneyChangerId": { "id": 8 },
                      "commissionRateId": { "id": 9 },
                      "isDefault": false
                    }
                    """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", is(1)))
                .andExpect(jsonPath("$.moneyChangerId.id", is(8)))
                .andExpect(jsonPath("$.commissionRateId.id", is(9)));

        Mockito.verify(service, times(1)).save(any());
    }

    @Test
    void testDeleteCompanyCommissionScheme() throws Exception {
        mockMvc.perform(delete("/v1/company-commission-schemes/1"))
                .andExpect(status().isOk());

        Mockito.verify(service, times(1)).delete(1);
    }
}
