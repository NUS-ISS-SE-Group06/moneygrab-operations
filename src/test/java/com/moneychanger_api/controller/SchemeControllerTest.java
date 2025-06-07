package com.moneychanger_api.controller;

import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.service.SchemeService;
import com.moneychanger_api.model.Scheme;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.*;
import org.mockito.Mockito;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.http.MediaType;
import java.util.List;
import static org.hamcrest.Matchers.hasSize;



@SpringBootTest
@AutoConfigureMockMvc
public class SchemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchemeService schemeService;

    @Test
    public void testListSchemes() throws Exception {
        List<Scheme> schemes = List.of(new Scheme());
        Mockito.when(schemeService.listAll()).thenReturn(schemes);

        mockMvc.perform(get("/api/schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void testGetSchemeFound() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setName("Standard");

        Mockito.when(schemeService.get(1)).thenReturn(scheme);

        mockMvc.perform(get("/api/schemes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Standard"));
    }

    @Test
    public void testGetSchemeNotFound() throws Exception {
        Mockito.when(schemeService.get(999)).thenThrow(new ResourceNotFoundException("Scheme with ID 999 not found"));

        mockMvc.perform(get("/api/schemes/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Scheme with ID 999 not found"));
    }

    @Test
    public void testCreateScheme() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setName("Gold");

        Mockito.when(schemeService.save(Mockito.any(Scheme.class))).thenReturn(scheme);

        mockMvc.perform(post("/api/schemes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Gold\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Gold"));
    }

    @Test
    public void testUpdateScheme() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setName("Updated");

        Mockito.when(schemeService.save(Mockito.any(Scheme.class))).thenReturn(scheme);

        mockMvc.perform(put("/api/schemes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated"));
    }

    @Test
    public void testDeleteScheme() throws Exception {
        mockMvc.perform(delete("/api/schemes/1"))
                .andExpect(status().isOk());

        Mockito.verify(schemeService, times(1)).delete(1);
    }

}