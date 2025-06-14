package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.dto.CompanyCommissionSchemeDTO;
import com.moola.fx.moneychanger.operations.mapper.CompanyCommissionSchemeMapper;
import com.moola.fx.moneychanger.operations.model.CompanyCommissionScheme;
import com.moola.fx.moneychanger.operations.service.CompanyCommissionSchemeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CompanyCommissionSchemeController.class)
class CompanyCommissionSchemeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private CompanyCommissionSchemeService service;
    @MockitoBean private CompanyCommissionSchemeMapper mapper;

    @Test
    void list_withoutSchemeId_returnsAll() throws Exception {
        var entity1 = new CompanyCommissionScheme();
        var entity2 = new CompanyCommissionScheme();

        var dto1 = new CompanyCommissionSchemeDTO();
        dto1.setId(101);
        dto1.setNameTag("Scheme A");

        var dto2 = new CompanyCommissionSchemeDTO();
        dto2.setId(102);
        dto2.setNameTag("Scheme B");

        when(service.listAll()).thenReturn(List.of(entity1, entity2));
        when(mapper.toDTO(any())).thenAnswer(invocation -> {
            var arg = invocation.getArgument(0);
            if (arg == entity1) return dto1;
            if (arg == entity2) return dto2;
            return null;
        });

        mockMvc.perform(get("/v1/company-commission-schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id").value(101))
                .andExpect(jsonPath("$[0].nameTag").value("Scheme A"))
                .andExpect(jsonPath("$[1].id").value(102))
                .andExpect(jsonPath("$[1].nameTag").value("Scheme B"));
    }



    @Test
    void list_withSchemeId_returnsFiltered() throws Exception {
        var entity = new CompanyCommissionScheme();
        var dto = new CompanyCommissionSchemeDTO();
        dto.setId(42);

        when(service.findBySchemeId(42)).thenReturn(List.of(entity));
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/company-commission-schemes").param("schemeId", "42"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id").value(42));
    }

    @Test
    void get_existingId_returnsDto() throws Exception {
        var entity = new CompanyCommissionScheme();
        var dto = new CompanyCommissionSchemeDTO();
        dto.setId(1);

        when(service.get(1)).thenReturn(entity);
        when(mapper.toDTO(entity)).thenReturn(dto);

        mockMvc.perform(get("/v1/company-commission-schemes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void create_validDto_returnsSavedDto() throws Exception {
        var inputDto = new CompanyCommissionSchemeDTO();
        inputDto.setCreatedBy(7);
        inputDto.setNameTag("abc");

        var entity = new CompanyCommissionScheme();
        var saved = new CompanyCommissionScheme();
        var outputDto = new CompanyCommissionSchemeDTO();
        outputDto.setId(101);

        when(mapper.toEntity(any())).thenReturn(entity);
        when(service.save(entity)).thenReturn(saved);
        when(mapper.toDTO(saved)).thenReturn(outputDto);

        mockMvc.perform(post("/v1/company-commission-schemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(101));
    }

    @Test
    void update_existingDto_returnsUpdatedDto() throws Exception {
        int id = 3;
        var inputDto = new CompanyCommissionSchemeDTO();
        inputDto.setId(id);
        inputDto.setNameTag("NewTag");
        inputDto.setUpdatedBy(8);

        var updated = new CompanyCommissionScheme();
        var outputDto = new CompanyCommissionSchemeDTO();
        outputDto.setId(id);
        outputDto.setUpdatedBy(8);

        when(service.save(inputDto)).thenReturn(updated);
        when(mapper.toDTO(updated)).thenReturn(outputDto);

        mockMvc.perform(put("/v1/company-commission-schemes/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(inputDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id))
                .andExpect(jsonPath("$.updatedBy").value(8));
    }

    @Test
    void delete_validId_returns204() throws Exception {
        doNothing().when(service).delete(5, 9);

        mockMvc.perform(delete("/v1/company-commission-schemes/5").param("userId", "9"))
                .andExpect(status().isNoContent());

        verify(service).delete(5, 9);
    }
}
