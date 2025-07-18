package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import com.moola.fx.moneychanger.operations.dto.RateDisplayDTO;
import com.moola.fx.moneychanger.operations.service.RateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
    void testGetRatesByMoneyChangerId_shouldReturnRates() {
        // Arrange
        Long moneyChangerId = 1L;
        String style = "Normal Monitor Style";

        RateDTO dto = new RateDTO();
        dto.setCurrencyCode("USD");
        dto.setUnit("1");
        dto.setRtBid(1.2345);
        dto.setRtAsk(1.3456);
        dto.setBuyRate(dto.getRtBid());
        dto.setSellRate(dto.getRtAsk());

        when(rateService.getRatesByMoneyChangerId(moneyChangerId))
                .thenReturn(Collections.singletonList(dto));

        // Act
        RateDisplayDTO response = viewRatesController.getRates();

        // Assert
        assertEquals(style, response.getStyle());
        assertEquals(1, response.getRates().size());
        assertEquals("USD", response.getRates().get(0).getCurrencyCode());
        assertEquals("1", response.getRates().get(0).getUnit());
        assertEquals(1.2345, response.getRates().get(0).getBuyRate());
        assertEquals(1.3456, response.getRates().get(0).getSellRate());
    }
}
