package com.moola.fx.moneychanger.operations.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.moola.fx.moneychanger.operations.exception.ResourceNotFoundException;
import com.moola.fx.moneychanger.operations.model.Scheme;
import com.moola.fx.moneychanger.operations.service.SchemeService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SchemeController.class)
class SchemeControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;

    @MockitoBean private SchemeService schemeService;

    @Test
    void testListSchemes() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Basic");
        scheme.setDescription("Basic plan");
        scheme.setIsDefault(false);

        when(schemeService.listAll()).thenReturn(List.of(scheme));

        mockMvc.perform(get("/v1/schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nameTag").value("Basic"))
                .andExpect(jsonPath("$[0].description").value("Basic plan"))
                .andExpect(jsonPath("$[0].isDefault").value(false));

        verify(schemeService).listAll();
    }

    @Test
    void testCreateScheme() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Gold");
        scheme.setDescription("Gold plan");
        scheme.setIsDefault(true);

        when(schemeService.save(any(Scheme.class))).thenReturn(scheme);

        mockMvc.perform(post("/v1/schemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                            {
                              "nameTag": "Gold",
                              "description": "Gold plan",
                              "isDefault": true
                            }
                        """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTag").value("Gold"))
                .andExpect(jsonPath("$.description").value("Gold plan"))
                .andExpect(jsonPath("$.isDefault").value(true));

        verify(schemeService).save(any(Scheme.class));
    }

    @Test
    void testGetById_success() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(2);
        scheme.setNameTag("Silver");
        scheme.setDescription("Silver plan");
        scheme.setIsDefault(false);

        when(schemeService.get(2)).thenReturn(scheme);

        mockMvc.perform(get("/v1/schemes/2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(2))
                .andExpect(jsonPath("$.nameTag").value("Silver"))
                .andExpect(jsonPath("$.description").value("Silver plan"))
                .andExpect(jsonPath("$.isDefault").value(false));
    }

    @Test
    void testGetById_notFound_returns404() throws Exception {
        int id = 99;
        String errorMessage = "Commission Rate with ID " + id + " not found or has been deleted";

        when(schemeService.get(id)).thenThrow(new ResourceNotFoundException(errorMessage));

        mockMvc.perform(get("/v1/schemes/{id}", id))
                .andExpect(status().isNotFound());

        verify(schemeService).get(id);
    }





}
