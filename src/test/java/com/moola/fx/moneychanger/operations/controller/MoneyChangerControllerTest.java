package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.moola.fx.moneychanger.operations.dto.MoneyChangerDTO;
import com.moola.fx.moneychanger.operations.service.MoneyChangerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MoneyChangerController.class)
class MoneyChangerControllerTest {

    @Autowired
    private MockMvc mockMvc;

   
    @MockitoBean
    private MoneyChangerService moneyChangerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    private MoneyChangerDTO getSampleDTO() {
        MoneyChangerDTO dto = new MoneyChangerDTO();
        dto.setId(1L);
        dto.setCompanyName("ABC FX");
        dto.setEmail("abc@email.com");
        dto.setAddress("123 Orchard Rd");
        dto.setPostalCode("123456");
        dto.setNotes("Main Branch");
        dto.setDateOfIncorporation(LocalDate.of(2023, 1, 1));
        dto.setCountry("Singapore");
        dto.setUen("UEN123456X");
        dto.setSchemeId(1);
        dto.setIsDeleted(0);
        dto.setLogoFilename("logo.jpg");
        dto.setPhotoMimetype("image/jpeg");
        dto.setKycFilename("kyc.pdf");
        dto.setDocumentMimetype("application/pdf");
        dto.setCreatedBy(1L);
        dto.setUpdatedBy(1L);
        dto.setLocations(Arrays.asList(1, 2));
        dto.setLogoBase64("base64-encoded-logo");
        dto.setKycBase64("base64-encoded-kyc");
        return dto;
    }

    @Test
    void testGetAllMoneyChangers() throws Exception {
        List<MoneyChangerDTO> list = List.of(getSampleDTO());
        when(moneyChangerService.listAll()).thenReturn(list);

        mockMvc.perform(get("/v1/money-changers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uen").value("UEN123456X"));
    }

    @Test
    void testGetMoneyChangerById() throws Exception {
        when(moneyChangerService.get(1L)).thenReturn(getSampleDTO());

        mockMvc.perform(get("/v1/money-changers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("ABC FX"));
    }

    @Test
    void testCreateMoneyChanger() throws Exception {
        MoneyChangerDTO dto = getSampleDTO();
        when(moneyChangerService.create(any())).thenReturn(dto);

        mockMvc.perform(post("/v1/money-changers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uen").value("UEN123456X"));
    }

    @Test
    void testUpdateMoneyChanger() throws Exception {
        MoneyChangerDTO dto = getSampleDTO();
        when(moneyChangerService.update(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/v1/money-changers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("ABC FX"));
    }

    @Test
    void testDeleteMoneyChanger() throws Exception {
        doNothing().when(moneyChangerService).delete(1L);

        mockMvc.perform(delete("/v1/money-changers/1"))
                .andExpect(status().isNoContent());
    }
}