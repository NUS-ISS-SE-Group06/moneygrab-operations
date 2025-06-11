package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.dto.CommissionRateDTO;
import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.mapper.CommissionRateMapper;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.model.CurrencyCode;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.service.CommissionRateService;
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

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CommissionRateControllerTest {

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private CommissionRateService commissionRateService;

    @Mock
    private CommissionRateMapper mapper;

    @InjectMocks
    private CommissionRateController commissionRateController;

    /**
     * In-test Advice to convert exceptions into JSON {"message": "..."} bodies.
     */
    @RestControllerAdvice
    static class TestExceptionAdvice {
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String, String> handleNotFound(ResourceNotFoundException ex) {
            return Map.of("message", ex.getMessage());
        }
        @ExceptionHandler(DuplicateResourceException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public Map<String, String> handleDuplicate(DuplicateResourceException ex) {
            return Map.of("message", ex.getMessage());
        }
    }

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(commissionRateController)
                .setControllerAdvice(new TestExceptionAdvice())
                .build();
    }

    @Test
    void testListCommissionRates() throws Exception {
        CurrencyCode currency = new CurrencyCode(); currency.setId(100); currency.setCurrency("USD");
        Scheme scheme = new Scheme(); scheme.setId(200); scheme.setNameTag("Basic Plan");

        CommissionRate entity = new CommissionRate();
        entity.setId(1);
        entity.setCurrencyId(currency);
        entity.setSchemeId(scheme);
        entity.setRate(new BigDecimal("0.0250"));
        entity.setCreatedBy(1);
        entity.setUpdatedBy(1);
        entity.setIsDeleted(false);

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(1);
        dto.setCurrencyId(100);
        dto.setCurrency("USD");
        dto.setSchemeId(200);
        dto.setNameTag("Basic Plan");
        dto.setRate(new BigDecimal("0.0250"));
        dto.setCreatedBy(1);
        dto.setUpdatedBy(1);
        dto.setIsDeleted(false);

        Mockito.when(commissionRateService.listAll()).thenReturn(List.of(entity));
        Mockito.when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].currencyId").value(100))
                .andExpect(jsonPath("$[0].currency").value("USD"))
                .andExpect(jsonPath("$[0].schemeId").value(200))
                .andExpect(jsonPath("$[0].nameTag").value("Basic Plan"))
                .andExpect(jsonPath("$[0].rate").value(0.0250))
                .andExpect(jsonPath("$[0].createdBy").value(1))
                .andExpect(jsonPath("$[0].updatedBy").value(1))
                .andExpect(jsonPath("$[0].isDeleted").value(false));

        Mockito.verify(commissionRateService).listAll();
        Mockito.verify(mapper, times(1)).toDTO(entity);
    }

    @Test
    void testGetCommissionRateFound() throws Exception {
        CommissionRate item = new CommissionRate();
        item.setId(1);
        item.setRate(new BigDecimal("2.50"));

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(1);
        dto.setRate(new BigDecimal("2.50"));

        Mockito.when(commissionRateService.get(1)).thenReturn(item);
        Mockito.when(mapper.toDTO(item)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(2.50));

        Mockito.verify(commissionRateService).get(1);
        Mockito.verify(mapper).toDTO(item);
    }

    @Test
    void testGetCommissionRateNotFound() throws Exception {
        Mockito.when(commissionRateService.get(999))
                .thenThrow(new ResourceNotFoundException("Commission Rate with ID 999 not found"));

        mockMvc.perform(get("/v1/commission-rates/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message")
                        .value("Commission Rate with ID 999 not found"));
    }

    @Test
    void testCreateCommissionRate() throws Exception {
        CommissionRateDTO requestDto = new CommissionRateDTO();
        requestDto.setRate(new BigDecimal("3.00"));
        requestDto.setCurrencyId(100);
        requestDto.setSchemeId(200);
        requestDto.setCreatedBy(1);

        CommissionRate entity = new CommissionRate();
        entity.setId(1);
        entity.setRate(new BigDecimal("3.00"));
        entity.setCurrencyId(new CurrencyCode());
        entity.setSchemeId(new Scheme());
        entity.setIsDeleted(false);

        CommissionRateDTO responseDto = new CommissionRateDTO();
        responseDto.setId(1);
        responseDto.setRate(new BigDecimal("3.00"));

        Mockito.when(mapper.toEntity(any(CommissionRateDTO.class))).thenReturn(entity);
        Mockito.when(commissionRateService.save(any())).thenReturn(entity);
        Mockito.when(mapper.toDTO(entity)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(3.00));
    }

    @Test
    void testCreateDuplicateCommissionRate() throws Exception {
        CommissionRateDTO requestDto = new CommissionRateDTO();
        requestDto.setRate(new BigDecimal("3.00"));
        requestDto.setCurrencyId(100);
        requestDto.setSchemeId(200);
        requestDto.setCreatedBy(1);

        CommissionRate dummyEntity = new CommissionRate();
        dummyEntity.setRate(new BigDecimal("3.00"));
        dummyEntity.setCurrencyId(new CurrencyCode());
        dummyEntity.setSchemeId(new Scheme());

        Mockito.when(mapper.toEntity(any(CommissionRateDTO.class))).thenReturn(dummyEntity);
        Mockito.when(commissionRateService.save(any()))
                .thenThrow(new DuplicateResourceException("Commission rate for the same currency and scheme already exists."));

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message")
                        .value("Commission rate for the same currency and scheme already exists."));
    }

    @Test
    void testDeleteCommissionRate() throws Exception {
        mockMvc.perform(delete("/v1/commission-rates/1")
                        .param("userId", "1"))
                .andExpect(status().isNoContent());

        Mockito.verify(commissionRateService).delete(eq(1), eq(1));
    }

    @Test
    void testListCommissionRates_WithSchemeId() throws Exception {
        int schemeId = 200;
        CommissionRate entity = new CommissionRate();
        entity.setId(1);
        entity.setRate(new BigDecimal("1.25"));
        entity.setIsDeleted(false);

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(1);
        dto.setRate(new BigDecimal("1.25"));

        Mockito.when(commissionRateService.findBySchemeId(schemeId)).thenReturn(List.of(entity));
        Mockito.when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates")
                        .param("schemeId", String.valueOf(schemeId)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rate").value(1.25));

        Mockito.verify(commissionRateService).findBySchemeId(schemeId);
        Mockito.verify(commissionRateService, times(0)).listAll();
    }

    @Test
    void testListCommissionRates_WithoutSchemeId() throws Exception {
        CommissionRate entity = new CommissionRate();
        entity.setId(2);
        entity.setRate(new BigDecimal("2.75"));
        entity.setIsDeleted(false);

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(2);
        dto.setRate(new BigDecimal("2.75"));

        Mockito.when(commissionRateService.listAll()).thenReturn(List.of(entity));
        Mockito.when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].rate").value(2.75));

        Mockito.verify(commissionRateService).listAll();
        Mockito.verify(commissionRateService, times(0)).findBySchemeId(any());
    }

    @Test
    void testUpdateCommissionRate() throws Exception {
        CommissionRateDTO requestDto = new CommissionRateDTO();
        requestDto.setRate(new BigDecimal("4.50"));
        requestDto.setCurrencyId(100);
        requestDto.setSchemeId(200);

        CommissionRate existing = new CommissionRate();
        existing.setId(1);
        existing.setRate(new BigDecimal("2.00"));
        CurrencyCode cc = new CurrencyCode(); cc.setId(100);
        existing.setCurrencyId(cc);
        Scheme sc = new Scheme(); sc.setId(200);
        existing.setSchemeId(sc);

        CommissionRate saved = new CommissionRate();
        saved.setId(1);
        saved.setRate(new BigDecimal("4.50"));

        CommissionRateDTO responseDto = new CommissionRateDTO();
        responseDto.setId(1);
        responseDto.setRate(new BigDecimal("4.50"));

        Mockito.when(commissionRateService.get(1)).thenReturn(existing);
        Mockito.when(commissionRateService.save(any())).thenReturn(saved);
        Mockito.when(mapper.toDTO(saved)).thenReturn(responseDto);

        mockMvc.perform(put("/v1/commission-rates/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.rate").value(4.50));

        Mockito.verify(commissionRateService).get(1);
        Mockito.verify(commissionRateService).save(existing);
    }
}
