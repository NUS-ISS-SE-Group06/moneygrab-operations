package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.dto.MoneyChangerResponseDTO;
import com.moola.fx.moneychanger.operations.service.MoneyChangerService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.SerializationFeature;



@WebMvcTest(MoneyChangerController.class)
class MoneyChangerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MoneyChangerService moneyChangerService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private MoneyChangerResponseDTO getSampleDTO() {
        MoneyChangerResponseDTO dto = new MoneyChangerResponseDTO();
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
        dto.setLocations(Arrays.asList("Jurong East", "Bishan"));
        dto.setLogoBase64("base64-encoded-logo");
        dto.setKycBase64("base64-encoded-kyc");
        return dto;
    }

    @Test
    void testGetAllMoneyChangers() throws Exception {
        MoneyChangerResponseDTO dto = getSampleDTO();
        List<MoneyChangerResponseDTO> list = List.of(dto);
        Mockito.when(moneyChangerService.getAllMoneyChangers()).thenReturn(list);

        mockMvc.perform(get("/v1/money-changers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].uen").value("UEN123456X"));
    }

    @Test
    void testGetMoneyChangerById() throws Exception {
        MoneyChangerResponseDTO dto = getSampleDTO();
        Mockito.when(moneyChangerService.getMoneyChangerById(1L)).thenReturn(dto);

        mockMvc.perform(get("/v1/money-changers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("ABC FX"));
    }
    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }
    @Test
    void testCreateMoneyChanger() throws Exception {
        MoneyChangerResponseDTO dto = getSampleDTO();
        Mockito.when(moneyChangerService.createMoneyChanger(Mockito.any())).thenReturn(dto);

        mockMvc.perform(post("/v1/money-changers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.uen").value("UEN123456X"));
    }
}
