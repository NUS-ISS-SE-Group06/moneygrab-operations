package com.moola.fx.moneychanger.operations.service;

import com.moola.fx.moneychanger.operations.dto.RateDTO;
import com.moola.fx.moneychanger.operations.dto.RateValuesDTO;
import com.moola.fx.moneychanger.operations.model.ViewRatesEntity;
import com.moola.fx.moneychanger.operations.repository.ViewRatesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class RateServiceImplTest {

    @Mock
    private ViewRatesRepository viewRatesRepository;

    @InjectMocks
    private RateServiceImpl rateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @Timeout(5)
    void testGetRatesByMoneyChangerId_shouldMapToDTOsCorrectly() {
        // Arrange: mock entity from DB
        ViewRatesEntity entity = new ViewRatesEntity();
        entity.setCurrencyCode("USD");
        entity.setUnit("1");
        entity.setRtBid(1.2345);
        entity.setRtAsk(1.3456);
        entity.setSpread(0.0011);
        entity.setRefBid(1.2);
        entity.setRefAsk(1.3);
        entity.setDpBid(1.21);
        entity.setDpAsk(1.31);
        entity.setMarBid(1.22);
        entity.setMarAsk(1.32);
        entity.setCfBid(1.23);
        entity.setCfAsk(1.33);
        entity.setCbBid(1.24);
        entity.setCbAsk(1.34);

        when(viewRatesRepository.findByMoneyChangerId(1L))
                .thenReturn(Collections.singletonList(entity));

        // Act
        List<RateDTO> result = rateService.getRatesByMoneyChangerId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());

        RateDTO dto = result.getFirst();
        RateValuesDTO values = dto.getRateValues();

        assertEquals("USD", dto.getCurrencyCode());
        assertEquals("1", dto.getUnit());
        assertNotNull(values);
        assertEquals(1.2345, values.getBuyRate());
        assertEquals(1.3456, values.getSellRate());
        assertEquals(0.0011, values.getSpread());
        assertEquals(1.2, values.getRefBid());
        assertEquals(1.3, values.getRefAsk());
        assertEquals(1.21, values.getDpBid());
        assertEquals(1.31, values.getDpAsk());
        assertEquals(1.22, values.getMarBid());
        assertEquals(1.32, values.getMarAsk());
        assertEquals(1.23, values.getCfBid());
        assertEquals(1.33, values.getCfAsk());
        assertEquals(1.24, values.getCbBid());
        assertEquals(1.34, values.getCbAsk());
    }
}
