package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import com.moola.fx.moneychanger.operations.repository.ViewRatesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RateServiceImplTest {

    private RateServiceImpl rateService;
    private ViewRatesRepository viewRatesRepository;

    @BeforeEach
    void setUp() {
        viewRatesRepository = mock(ViewRatesRepository.class);
        rateService = new RateServiceImpl(viewRatesRepository);
    }

    @Test
    void getRatesByMoneyChangerId_shouldAssignBuySellRatesCorrectly() {
        Long moneyChangerId = 1L;

        RateDTO dto = new RateDTO();
        dto.setCurrencyCode("USD");
        dto.setUnit("1");
        dto.setRtBid(1.23);
        dto.setRtAsk(1.25);

        when(viewRatesRepository.findRatesByMoneyChangerId(moneyChangerId))
                .thenReturn(List.of(dto));

        List<RateDTO> result = rateService.getRatesByMoneyChangerId(moneyChangerId);

        assertNotNull(result);
        assertEquals(1, result.size());

        RateDTO actual = result.get(0);
        assertEquals("USD", actual.getCurrencyCode());
        assertEquals("1", actual.getUnit());
        assertEquals(1.23, actual.getBuyRate());
        assertEquals(1.25, actual.getSellRate());

        verify(viewRatesRepository, times(1)).findRatesByMoneyChangerId(moneyChangerId);
    }
}
