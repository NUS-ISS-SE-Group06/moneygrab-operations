package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.dto.CommissionRateDTO;
import com.moola.fx.moneychanger.operations.exception.DuplicateResourceException;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.mapper.CommissionRateMapper;
import com.moola.fx.moneychanger.operations.model.CommissionRate;
import com.moola.fx.moneychanger.operations.service.CommissionRateService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
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
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class CommissionRateControllerTest {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private MockMvc mockMvc;
    @Mock private CommissionRateService service;
    @Mock private CommissionRateMapper mapper;

    @InjectMocks private CommissionRateController controller;

    @BeforeEach
    void setup() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(controller)
                .setControllerAdvice(new TestExceptionAdvice())
                .build();
    }

    @Test
    void list_withoutSchemeId_returnsAll() throws Exception {
        CommissionRate entity = new CommissionRate();
        entity.setId(1);
        entity.setRate(new BigDecimal("1.23"));

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(1);
        dto.setRate(new BigDecimal("1.23"));

        when(service.listAll()).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].rate").value(1.23));

        verify(service).listAll();
        verify(service, never()).findBySchemeId(anyInt());
    }

    @Test
    void list_withSchemeId_returnsFiltered() throws Exception {
        CommissionRate entity = new CommissionRate();
        entity.setId(2);
        entity.setRate(new BigDecimal("2.34"));

        CommissionRateDTO dto = new CommissionRateDTO();
        dto.setId(2);
        dto.setRate(new BigDecimal("2.34"));

        when(service.findBySchemeId(99)).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/commission-rates")
                        .param("schemeId", "99"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[0].rate").value(2.34));

        verify(service).findBySchemeId(99);
        verify(service, never()).listAll();
    }

    @Test
    void create_validDto_returnsCreatedDto() throws Exception {
        CommissionRateDTO request = new CommissionRateDTO();
        request.setCurrencyId(1);
        request.setSchemeId(2);
        request.setRate(new BigDecimal("4.56"));
        request.setCreatedBy(10);

        CommissionRate entity = new CommissionRate();
        CommissionRate saved = new CommissionRate();
        saved.setId(100);
        saved.setRate(new BigDecimal("4.56"));

        CommissionRateDTO dtoResponse = new CommissionRateDTO();
        dtoResponse.setId(100);
        dtoResponse.setRate(new BigDecimal("4.56"));

        when(mapper.toEntity(any())).thenReturn(entity);
        when(service.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(dtoResponse);

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.rate").value(4.56));

        verify(service).save(entity);
    }

    @Test
    void create_duplicate_throws409() throws Exception {
        CommissionRateDTO request = new CommissionRateDTO();
        request.setCurrencyId(1);
        request.setSchemeId(2);
        request.setRate(new BigDecimal("5.67"));

        CommissionRate entity = new CommissionRate();

        when(mapper.toEntity(any())).thenReturn(entity);
        when(service.save(entity)).thenThrow(new DuplicateResourceException("Duplicate!"));

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Duplicate!"));

        verify(service).save(entity);
    }




    @Test
    void create_validDto_setsUpdatedByAndReturnsResponse() throws Exception {
        CommissionRateDTO requestDto = new CommissionRateDTO();
        requestDto.setCurrencyId(1);
        requestDto.setSchemeId(2);
        requestDto.setRate(new BigDecimal("1.99"));
        requestDto.setCreatedBy(88); // This should map to entity.updatedBy

        CommissionRate entity = new CommissionRate();
        entity.setRate(new BigDecimal("1.99"));
        entity.setUpdatedBy(88); // Set manually just like in controller
        // currencyId and schemeId can be mocked deeper if needed

        CommissionRate savedEntity = new CommissionRate();
        savedEntity.setId(100);
        savedEntity.setRate(new BigDecimal("1.99"));
        savedEntity.setUpdatedBy(88);

        CommissionRateDTO responseDto = new CommissionRateDTO();
        responseDto.setId(100);
        responseDto.setRate(new BigDecimal("1.99"));
        responseDto.setUpdatedBy(88);

        when(mapper.toEntity(any(CommissionRateDTO.class))).thenReturn(entity);
        when(service.save(entity)).thenReturn(savedEntity);
        when(mapper.toDTO(savedEntity)).thenReturn(responseDto);

        mockMvc.perform(post("/v1/commission-rates")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(100))
                .andExpect(jsonPath("$.rate").value(1.99))
                .andExpect(jsonPath("$.updatedBy").value(88));

        verify(mapper).toEntity(requestDto);
        verify(service).save(entity);
        verify(mapper).toDTO(savedEntity);
    }












    // Advice for handling exceptions
    @RestControllerAdvice
    static class TestExceptionAdvice {
        @ExceptionHandler(ResourceNotFoundException.class)
        @ResponseStatus(HttpStatus.NOT_FOUND)
        public Map<String,String> handleNotFound(ResourceNotFoundException ex) {
            return Map.of("message", ex.getMessage());
        }

        @ExceptionHandler(DuplicateResourceException.class)
        @ResponseStatus(HttpStatus.CONFLICT)
        public Map<String,String> handleDuplicate(DuplicateResourceException ex) {
            return Map.of("message", ex.getMessage());
        }
    }





}
