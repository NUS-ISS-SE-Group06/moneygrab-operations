package com.moneychanger_api.controller;

import com.moneychanger_api.exception.ResourceNotFoundException;
import com.moneychanger_api.model.Scheme;
import com.moneychanger_api.service.SchemeService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@SpringBootTest
@AutoConfigureMockMvc
class SchemeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SchemeService schemeService;

    @Test
    void testListSchemes() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Basic");
        scheme.setDescription("Basic plan");
        scheme.setIsDefault(false);

        List<Scheme> schemes = List.of(scheme);
        Mockito.when(schemeService.listAll()).thenReturn(schemes);

        mockMvc.perform(get("/v1/schemes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].nameTag", is("Basic")))
                .andExpect(jsonPath("$[0].description", is("Basic plan")))
                .andExpect(jsonPath("$[0].isDefault", is(false)));
    }

    @Test
    void testGetSchemeFound() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Standard");
        scheme.setDescription("Standard scheme");
        scheme.setIsDefault(true);

        Mockito.when(schemeService.get(1)).thenReturn(scheme);

        mockMvc.perform(get("/v1/schemes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTag").value("Standard"))
                .andExpect(jsonPath("$.description").value("Standard scheme"))
                .andExpect(jsonPath("$.isDefault").value(true));
    }

    @Test
    void testGetSchemeNotFound() throws Exception {
        Mockito.when(schemeService.get(999)).thenThrow(new ResourceNotFoundException("Scheme with ID 999 not found"));

        mockMvc.perform(get("/v1/schemes/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Scheme with ID 999 not found"));
    }

    @Test
    void testCreateScheme() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Gold");
        scheme.setDescription("Gold plan");
        scheme.setIsDefault(true);

        Mockito.when(schemeService.save(Mockito.any(Scheme.class))).thenReturn(scheme);

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

    }

    @Test
    void testUpdateScheme() throws Exception {
        Scheme scheme = new Scheme();
        scheme.setId(1);
        scheme.setNameTag("Updated");
        scheme.setDescription("Updated description");
        scheme.setIsDefault(false);

        Mockito.when(schemeService.save(Mockito.any(Scheme.class))).thenReturn(scheme);

        mockMvc.perform(put("/v1/schemes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "nameTag": "Updated",
                                    "description": "Updated description",
                                    "isDefault": false
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameTag").value("Updated"))
                .andExpect(jsonPath("$.description").value("Updated description"))
                .andExpect(jsonPath("$.isDefault").value(false));

    }

    @Test
    void testDeleteScheme() throws Exception {
        mockMvc.perform(delete("/v1/schemes/1"))
                .andExpect(status().isOk());

        Mockito.verify(schemeService, times(1)).delete(1);
    }

}