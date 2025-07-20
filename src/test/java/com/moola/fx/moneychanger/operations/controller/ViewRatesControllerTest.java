package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import com.moola.fx.moneychanger.operations.dto.RateDisplayDTO;
import com.moola.fx.moneychanger.operations.dto.RateValuesDTO;
import com.moola.fx.moneychanger.operations.service.RateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class ViewRatesControllerTest {

    @Mock
    private RateService rateService;

    @InjectMocks
    private ViewRatesController viewRatesController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Timeout(5)
    void testGetRates_shouldReturnRates() {
        // Arrange: create RateValuesDTO and embed into RateDTO
        RateValuesDTO rateValues = new RateValuesDTO();
        rateValues.setBuyRate(1.2345);
        rateValues.setSellRate(1.3456);

        RateDTO dto = new RateDTO();
        dto.setCurrencyCode("USD");
        dto.setUnit("1");
        dto.setRateValues(rateValues);

        when(rateService.getRatesByMoneyChangerId(1L))
                .thenReturn(Collections.singletonList(dto));

        // Act
        RateDisplayDTO response = viewRatesController.getRates();

        // Assert
        assertNotNull(response);
        assertEquals("Normal Monitor Style", response.getStyle());
        assertEquals(1, response.getRates().size());

        RateDTO rate = response.getRates().getFirst();
        assertEquals("USD", rate.getCurrencyCode());
        assertEquals("1", rate.getUnit());
        assertEquals(1.2345, rate.getRateValues().getBuyRate());
        assertEquals(1.3456, rate.getRateValues().getSellRate());
    }
}
