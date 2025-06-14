package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.model.MoneyChanger;
import com.moola.fx.moneychanger.operations.model.MoneyChangerKyc;
import com.moola.fx.moneychanger.operations.model.MoneyChangerPhoto;
import com.moola.fx.moneychanger.operations.service.MoneyChangerKycService;
import com.moola.fx.moneychanger.operations.service.MoneyChangerPhotoService;
import com.moola.fx.moneychanger.operations.service.MoneyChangerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MoneyChangerController.class)
class MoneyChangerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MoneyChangerService moneyChangerService;

    @MockitoBean
    private MoneyChangerPhotoService moneyChangerPhotoService;

    @MockitoBean
    private MoneyChangerKycService moneyChangerKycService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testGetAllMoneyChangers() throws Exception {
        MoneyChanger mc = new MoneyChanger();
        mc.setId(1L);
        mc.setCompanyName("ABC");

        when(moneyChangerService.getAll()).thenReturn(Collections.singletonList(mc));

        mockMvc.perform(get("/v1/money-changers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].companyName").value("ABC"));
    }

    @Test
    void testGetMoneyChangerById() throws Exception {
        Long id = 1L;

        MoneyChanger mc = new MoneyChanger();
        mc.setId(id);
        mc.setCompanyName("XYZ");

        MoneyChangerPhoto photo = new MoneyChangerPhoto();
        photo.setPhotoFilename("photo.jpg");

        MoneyChangerKyc kyc = new MoneyChangerKyc();
        kyc.setDocumentFilename("doc.pdf");

        when(moneyChangerService.getById(id)).thenReturn(mc);
        when(moneyChangerPhotoService.getByMoneyChangerId(id)).thenReturn(photo);
        when(moneyChangerKycService.getByMoneyChangerId(id)).thenReturn(kyc);

        mockMvc.perform(get("/v1/money-changers/{id}", id))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("XYZ"))
                .andExpect(jsonPath("$.photoFilename").value("photo.jpg"))
                .andExpect(jsonPath("$.kycDocumentFilename").value("doc.pdf"));
    }

    @Test
    void testCreateMoneyChanger() throws Exception {
        MoneyChanger mc = new MoneyChanger();
        mc.setId(1L);
        mc.setCompanyName("Create Co");

        when(moneyChangerService.create(any(MoneyChanger.class))).thenReturn(mc);
        when(moneyChangerPhotoService.getByMoneyChangerId(anyLong())).thenReturn(null);
        when(moneyChangerKycService.getByMoneyChangerId(anyLong())).thenReturn(null);

        MockMultipartFile moneyChangerPart = new MockMultipartFile("moneyChanger", "", "application/json",
                objectMapper.writeValueAsBytes(mc));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/money-changers")
                        .file(moneyChangerPart))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Create Co"));
    }

    @Test
    void testUpdateMoneyChanger() throws Exception {
        Long id = 1L;

        MoneyChanger mc = new MoneyChanger();
        mc.setId(id);
        mc.setCompanyName("Updated Co");

        when(moneyChangerService.update(eq(id), any(MoneyChanger.class))).thenReturn(mc);
        when(moneyChangerPhotoService.getByMoneyChangerId(id)).thenReturn(null);
        when(moneyChangerKycService.getByMoneyChangerId(id)).thenReturn(null);

        MockMultipartFile moneyChangerPart = new MockMultipartFile("moneyChanger", "", "application/json",
                objectMapper.writeValueAsBytes(mc));

        mockMvc.perform(MockMvcRequestBuilders.multipart("/v1/money-changers/{id}", id)
                        .file(moneyChangerPart)
                        .with(request -> {
                            request.setMethod("PUT");
                            return request;
                        }))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Updated Co"));
    }

    @Test
    void testDeleteMoneyChanger() throws Exception {
        Long id = 1L;

        doNothing().when(moneyChangerService).delete(id);

        mockMvc.perform(delete("/v1/money-changers/{id}", id))
                .andExpect(status().isOk());
    }
}