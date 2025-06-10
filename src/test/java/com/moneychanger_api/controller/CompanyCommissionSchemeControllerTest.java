package com.moneychanger_api.controller;

import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.CommissionRate;
import com.moneychanger_api.model.CompanyCommissionScheme;
import com.moneychanger_api.model.MoneyChanger;
import com.moneychanger_api.service.CompanyCommissionSchemeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.is;

@SpringBootTest
@AutoConfigureMockMvc
class CompanyCommissionSchemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CompanyCommissionSchemeService service;

    @Test
    void testListCompanyCommissionSchemes() throws Exception {
        List<CompanyCommissionScheme> list = List.of(new CompanyCommissionScheme());
        Mockito.when(service.listAll()).thenReturn(list);

        mockMvc.perform(get("/v1/company-commission-schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void testListCompanyCommissionSchemes2() throws Exception {
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
                .andExpect(jsonPath("$[0].moneyChangerId.id", is(1)))
                .andExpect(jsonPath("$[0].commissionRateId.id", is(1)));
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
                .andExpect(jsonPath("$.moneyChangerId.id", is(2)))
                .andExpect(jsonPath("$.commissionRateId.id", is(3)));
    }

    @Test
    void testGetCompanyCommissionSchemeNotFound() throws Exception {
        Mockito.when(service.get(1))
                .thenThrow(new ResourceNotFoundException("CompanyCommissionScheme with ID 1 not found"));

        mockMvc.perform(get("/v1/company-commission-schemes/1"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("CompanyCommissionScheme with ID 1 not found"));
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

        Mockito.when(service.save(Mockito.any())).thenReturn(saved);

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
                .andExpect(jsonPath("$.moneyChangerId.id", is(5)))
                .andExpect(jsonPath("$.commissionRateId.id", is(6)));
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

        Mockito.when(service.save(Mockito.any())).thenReturn(updated);

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
                .andExpect(jsonPath("$.moneyChangerId.id", is(8)))
                .andExpect(jsonPath("$.commissionRateId.id", is(9)));
    }

    @Test
    void testDeleteCompanyCommissionScheme() throws Exception {
        mockMvc.perform(delete("/v1/company-commission-schemes/1"))
                .andExpect(status().isOk());

        Mockito.verify(service, times(1)).delete(1);
    }

}