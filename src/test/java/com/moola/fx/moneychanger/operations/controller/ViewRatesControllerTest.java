package com.moola.fx.moneychanger.operations.controller;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import com.moola.fx.moneychanger.operations.dto.RateDisplayDTO;
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
        RateDTO dto = new RateDTO();
        dto.setCurrencyCode("USD");
        dto.setUnit("1");
        dto.setRtBid(1.2345);
        dto.setRtAsk(1.3456);
        dto.setBuyRate(dto.getRtBid());
        dto.setSellRate(dto.getRtAsk());

        when(rateService.getRatesByMoneyChangerId(1L))
                .thenReturn(Collections.singletonList(dto));

        RateDisplayDTO response = viewRatesController.getRates();

        assertNotNull(response);
        assertEquals("Normal Monitor Style", response.getStyle());
        assertEquals(1, response.getRates().size());

        RateDTO rate = response.getRates().getFirst();
        assertEquals("USD", rate.getCurrencyCode());
        assertEquals("1", rate.getUnit());
        assertEquals(1.2345, rate.getBuyRate());
        assertEquals(1.3456, rate.getSellRate());
    }
}
