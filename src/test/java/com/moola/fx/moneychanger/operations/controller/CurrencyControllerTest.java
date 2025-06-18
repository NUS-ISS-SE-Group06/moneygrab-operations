package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.service.CurrencyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CurrencyController.class)
class CurrencyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CurrencyService currencyService;

    @Test
    void list_returnsAllCurrencies() throws Exception {
        CurrencyCode usd = new CurrencyCode();
        usd.setId(1);
        usd.setCurrency("USD");

        CurrencyCode sgd = new CurrencyCode();
        sgd.setId(2);
        sgd.setCurrency("SGD");

        when(currencyService.listAll()).thenReturn(Arrays.asList(usd, sgd));

        mockMvc.perform(get("/v1/currencies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[1].currency").value("SGD"));
    }

    @Test
    void get_existingId_returnsCurrency() throws Exception {
        CurrencyCode usd = new CurrencyCode();
        usd.setId(1);
        usd.setCurrency("USD");

        when(currencyService.get(1)).thenReturn(usd);

        mockMvc.perform(get("/v1/currencies/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void get_nonExistingId_returns404() throws Exception {
        when(currencyService.get(999)).thenThrow(new ResourceNotFoundException("Currency Code with ID 999 not found"));

        mockMvc.perform(get("/v1/currencies/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getByCurrency_validCode_returnsCurrency() throws Exception {
        CurrencyCode usd = new CurrencyCode();
        usd.setId(1);
        usd.setCurrency("USD");

        when(currencyService.getByCurrency("USD")).thenReturn(usd);

        mockMvc.perform(get("/v1/currencies/code/USD"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.currency").value("USD"));
    }

    @Test
    void getByCurrency_notFound_returns404() throws Exception {
        when(currencyService.getByCurrency("XYZ")).thenThrow(new ResourceNotFoundException("Currency Code with XYZ not found"));

        mockMvc.perform(get("/v1/currencies/code/XYZ"))
                .andExpect(status().isNotFound());
    }
}
